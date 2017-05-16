package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class NewNeedActivity extends AppCompatActivity {

    public Button submit;
    public FloatingActionButton fabAdd;
    public EditText itemName;
    public EditText itemQuantity;
    public RadioGroup gender;
    public RadioButton radioSexButton;
    public RadioButton radioAgeButton;
    public RadioGroup age;
    public Spinner spinner;
    public LinearLayout clothesLayout;
    public Button post;
    public Button cancel;
    public ImageView dateImage;
    public TextView textView;
    public RecyclerView recyclerView;
    public String category;
    public ArrayList<NeedClass> needDetails;
    boolean dataFilled=false;
    static int id=-1;
    public Date datetime;
    public View hiddenPanel;
    public RelativeLayout toolbarSubmit;
    Gson gson;

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";
    private SwitchDateTimeDialogFragment dateTimeFragment;

    public Animation bottomUp,bottomDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_need);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(R.string.positive_button_datetime_picker),
                    getString(R.string.negative_button_datetime_picker)
            );
        }
        Calendar now = Calendar.getInstance();
        hiddenPanel= findViewById(R.id.hiddenPanel_needForm);
        toolbarSubmit= (RelativeLayout) findViewById(R.id.toolbarSubmit);

        final SimpleDateFormat myDateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        final SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        dateTimeFragment.startAtCalendarView();
        dateTimeFragment.set24HoursMode(false);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),now.get(Calendar.HOUR) ,now.get(Calendar.AM_PM), now.get(Calendar.MINUTE)).getTime());
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
        }

        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                datetime=date;
                textView.setText(myDateFormat.format(date)+ "  "+myTimeFormat.format(date));
            }
            @Override
            public void onNegativeButtonClick(Date date) {
                textView.setText("");
            }
        });

        dateImage = (ImageView) findViewById(R.id.date_needForm);
        dateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            }
        });

        textView = (TextView) findViewById(R.id.textView);
        if (savedInstanceState != null)
            textView.setText(savedInstanceState.getCharSequence(STATE_TEXTVIEW));

        recyclerView= (RecyclerView) findViewById(R.id.recyclerView_needForm);
        submit= (Button) findViewById(R.id.submit_needForm);
        fabAdd= (FloatingActionButton)findViewById(R.id.fabAdd_needForm);
        needDetails= new ArrayList<>();
        bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        bottomDown = AnimationUtils.loadAnimation(this, R.anim.bottom_down);

        spinner = (Spinner)findViewById(R.id.spinner_needForm);
        itemName= (EditText) findViewById(R.id.itemName_needForm);
        itemQuantity= (EditText) findViewById(R.id.itemQuantity_needForm);
        gender= (RadioGroup) findViewById(R.id.gender_needForm);
        age= (RadioGroup) findViewById(R.id.age_needForm);
        clothesLayout= (LinearLayout) findViewById(R.id.clothesLayout_needForm);
        post= (Button)findViewById(R.id.post_needForm);
        cancel= (Button) findViewById(R.id.cancel_needForm);

        ArrayList<SpinnerItemData> list=new ArrayList<>();
        list.add(new SpinnerItemData("Food",R.drawable.ic_food_black));
        list.add(new SpinnerItemData("Clothes",R.drawable.ic_cloth_black));
        list.add(new SpinnerItemData("Groceries",R.drawable.ic_grocery_cart_black));
        list.add(new SpinnerItemData("Stationeries",R.drawable.ic_stationery_black));

        CategorySpinnerAdapter adapter=new CategorySpinnerAdapter(this, R.layout.spinner_layout,R.id.txt_spinner_needForm,list);
        spinner.setAdapter(adapter);

        final RadioButton lastGenderRadioBtn = (RadioButton) findViewById(R.id.female);
        final RadioButton lastAgeRadioBtn = (RadioButton) findViewById(R.id.old);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item  = ((TextView) findViewById(R.id.txt_spinner_needForm)).getText().toString();
                category=item;

                itemName.setText("");
                itemName.setError(null);
                itemQuantity.setText("");
                itemQuantity.setError(null);
                gender.clearCheck();
                lastGenderRadioBtn.setError(null);
                age.clearCheck();
                lastAgeRadioBtn.setError(null);

                if(item.equals("Clothes"))
                    clothesLayout.setVisibility(View.VISIBLE);
                else
                    clothesLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NeedClass need = new NeedClass();

                int selectedIdGender = gender.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedIdGender);

                int selectedIdAge = age.getCheckedRadioButtonId();
                radioAgeButton = (RadioButton) findViewById(selectedIdAge);

                if(itemName.getText().toString().equals("")) {
                    dataFilled=false;
                    itemName.setError("enter the item name");
                }

                else if(itemQuantity.getText().toString().isEmpty()) {
                    dataFilled=false;
                    itemQuantity.setError("enter the item Quantity");
                }
                else {
                    need.setItemNumber(++NewNeedActivity.id);
                    need.setCategory(category);
                    need.setItemName(itemName.getText().toString());
                    need.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                    need.setDate(myDateFormat.format(datetime)+"");
                    need.setTime(myTimeFormat.format(datetime)+"");
                    dataFilled=true;
                }

                if(category.equals("Clothes")) {

                    if(gender.getCheckedRadioButtonId()<=0) {
                        dataFilled=false;
                        lastGenderRadioBtn.setError("select gender");
                    }
                    if(age.getCheckedRadioButtonId()<=0) {
                        dataFilled=false;
                        lastAgeRadioBtn.setError("select age");
                    }

                    else {
                        need.setGender(radioSexButton.getText().toString());
                        need.setAge(radioAgeButton.getText().toString());
                        dataFilled=true;
                    }
                }

                if(dataFilled) {
                    needDetails.add(need);
                    recyclerView.setAdapter(new NewNeedsListAdapter(NewNeedActivity.this, needDetails));
                    recyclerView.setLayoutManager(new LinearLayoutManager(NewNeedActivity.this));
                    hiddenPanel.startAnimation(bottomDown);
                    hiddenPanel.setVisibility(View.GONE);
                    toolbarSubmit.setVisibility(View.VISIBLE);
                    fabAdd.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenPanel.startAnimation(bottomDown);
                hiddenPanel.setVisibility(View.GONE);
                toolbarSubmit.setVisibility(View.VISIBLE);
                fabAdd.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPanelShown()) {
                    hiddenPanel.startAnimation(bottomUp);
                    hiddenPanel.setVisibility(View.VISIBLE);
                    toolbarSubmit.setVisibility(View.GONE);
                    fabAdd.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    hiddenPanel.startAnimation(bottomDown);
                    hiddenPanel.setVisibility(View.GONE);
                    toolbarSubmit.setVisibility(View.VISIBLE);
                    fabAdd.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                itemName.setText("");
                itemName.setError(null);
                itemQuantity.setText("");
                itemQuantity.setError(null);
                gender.clearCheck();
                lastGenderRadioBtn.setError(null);
                age.clearCheck();
                lastAgeRadioBtn.setError(null);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gson= new Gson();
                new CreateUserAccount().execute();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putCharSequence(STATE_TEXTVIEW, textView.getText());
        super.onSaveInstanceState(savedInstanceState);
    }

    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }

    class CreateUserAccount extends AsyncTask {

        HttpResponse response;
        HttpClient client;

        @Override
        protected Object doInBackground(Object[] params) {

            client = new DefaultHttpClient();
            for(int i=0;i<needDetails.size();i++) {
                response = Connectivity.makePostRequest(RestAPIURL.needList, gson.toJson(needDetails.get(i)).toString(), client,Connectivity.getAuthToken(NewNeedActivity.this,Connectivity.Donor_Token));
                Log.d("Request JSON", gson.toJson(needDetails.get(i)).toString());
                if (response != null) {
                    Log.d("Response Code", response.getStatusLine().getStatusCode() + "");

                    try {
                        Connectivity.getJosnFromResponse(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response", "Null");
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if(response!=null)
                if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201)
                {
                    Toast.makeText(NewNeedActivity.this,"Need successfully posted...",Toast.LENGTH_LONG).show();
                    //NewNeedActivity.this.startActivity(new Intent(NewNeedActivity.this,LoginPage.class));
                    NewNeedActivity.this.finish();
                }
            //  Log.d("GSON",gson.toJson(registerDetails).toString());

            super.onPostExecute(o);
        }
    }

}
