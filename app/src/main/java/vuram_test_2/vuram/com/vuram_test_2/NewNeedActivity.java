package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class NewNeedActivity extends AppCompatActivity {

    public Button submit;
    public FloatingActionButton fabAdd;
    public EditText itemQuantity;
    public RadioGroup gender;
    public RadioButton radioSexButton;
    public EditText age;
    public Spinner mainItemSpinner,subItemSpinner;
    public LinearLayout clothesLayout;
    public Button post;
    public Button cancel;
    public ImageView dateImage;
    public TextView textView;
    public RecyclerView recyclerView;
    public String mainItem;
    public String subItem;
    public ArrayList<NeedItemDetails> needDetails;
    public boolean dataFilled=false;
    public static int id=-1;
    public Date datetime;
    public View hiddenPanel;
    public RelativeLayout toolbarSubmit;
    public ArrayList<MainItemDetails> mainItemDetails;
    public ArrayList<SubItemDetails> subItemDetails;
    public DatabaseHelper db;

    Gson gson;

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";
    private SwitchDateTimeDialogFragment dateTimeFragment;

    public Animation bottomUp,bottomDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_need);
        DetailsPopulator detailsPopulator =new DetailsPopulator(this);
        detailsPopulator.getCountryDetailsFromAPI();

        db=new DatabaseHelper(NewNeedActivity.this);
        mainItemDetails=db.getAllMainItemDetails();
        subItemDetails=db.getAllSubItemDetails();

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
        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),now.get(Calendar.HOUR) ,now.get(Calendar.MINUTE),now.get(Calendar.HOUR_OF_DAY)>12?(now.get(Calendar.PM)):(now.get(Calendar.AM))).getTime());
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

        mainItemSpinner = (Spinner)findViewById(R.id.main_item_spinner_needForm);
        subItemSpinner= (Spinner) findViewById(R.id.sub_item_spinner_needForm);
        itemQuantity= (EditText) findViewById(R.id.itemQuantity_needForm);
        gender= (RadioGroup) findViewById(R.id.gender_needForm);
        age= (EditText) findViewById(R.id.age_needForm);
        clothesLayout= (LinearLayout) findViewById(R.id.clothesLayout_needForm);
        post= (Button)findViewById(R.id.post_needForm);
        cancel= (Button) findViewById(R.id.cancel_needForm);

        ArrayList<ItemSpinnerData> mainItemList=new ArrayList<>();
        for (int i=0;i<mainItemDetails.size();i++) {
            switch (mainItemDetails.get(i).getMainItemCode())
            {
                case 1:
                    mainItemList.add(new ItemSpinnerData(mainItemDetails.get(i).getMainItemName(),R.drawable.ic_food_black));
                    break;
                case 2:
                    mainItemList.add(new ItemSpinnerData(mainItemDetails.get(i).getMainItemName(),R.drawable.ic_cloth_black));
                    break;
                case 3:
                    mainItemList.add(new ItemSpinnerData(mainItemDetails.get(i).getMainItemName(),R.drawable.ic_blood_black));
                    break;
                case 4:
                    mainItemList.add(new ItemSpinnerData(mainItemDetails.get(i).getMainItemName(),R.drawable.ic_grocery_cart_black));
                    break;
                case 5:
                    mainItemList.add(new ItemSpinnerData(mainItemDetails.get(i).getMainItemName(),R.drawable.ic_stationery_black));
                    break;
            }
        }

        MainItemSpinnerAdapter mainItemSpinnerAdapter=new MainItemSpinnerAdapter(NewNeedActivity.this, R.layout.main_item_spinner_layout,R.id.main_item_txt_spinner_needForm,mainItemList);
        mainItemSpinner.setAdapter(mainItemSpinnerAdapter);
        
        final RadioButton lastGenderRadioBtn = (RadioButton) findViewById(R.id.female);

        mainItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item  = ((TextView) findViewById(R.id.main_item_txt_spinner_needForm)).getText().toString();
                mainItem=item;

                ArrayList<ItemSpinnerData> subItemList=new ArrayList<>();
                itemQuantity.setText("");
                itemQuantity.setError(null);
                gender.clearCheck();
                lastGenderRadioBtn.setError(null);
                age.setText("");
                age.setError(null);

                for(int i=0;i<subItemDetails.size();i++)
                    if(subItemDetails.get(i).getMainItemCode()==(id+1))
                        subItemList.add(new ItemSpinnerData(subItemDetails.get(i).getSubItemName()));

                SubItemSpinnerAdapter subItemSpinnerAdapter=new SubItemSpinnerAdapter(NewNeedActivity.this, R.layout.sub_item_spinner_layout,R.id.sub_item_txt_spinner_needForm,subItemList);
                subItemSpinner.setAdapter(subItemSpinnerAdapter);

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
                NeedItemDetails needItemDetails=new NeedItemDetails();

                int selectedIdGender = gender.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedIdGender);

                if(itemQuantity.getText().toString().isEmpty()) {
                    dataFilled=false;
                    itemQuantity.setError("enter the item Quantity");
                }
                else {
                    needItemDetails.setNeed_item_id(++NewNeedActivity.id);
                    for(int i=1;i<=subItemDetails.size();i++)
                        if(mainItem.equals(mainItemDetails.get(subItemDetails.get(i).getMainItemCode()).getMainItemName()))
                        {
                            needItemDetails.setItem_type_id(subItemDetails.get(i).getMainItemCode());
                            needItemDetails.setSub_item_type_id(subItemDetails.get(i).getSubItemCode());
                        }
                    needItemDetails.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                    needItemDetails.setDeadline(sdf.format(datetime));
                    dataFilled=true;
                }

                if(mainItem.equals("Clothes")) {

                    if(gender.getCheckedRadioButtonId()<=0) {
                        dataFilled=false;
                        lastGenderRadioBtn.setError("select gender");
                    }
                    if(age.getText().toString().isEmpty()) {
                        dataFilled=false;
                        age.setError("enter the age");
                    }

                    else {
                        needItemDetails.setGender(radioSexButton.getText().toString());
                        needItemDetails.setAge(Integer.parseInt(age.getText().toString()));
                        dataFilled=true;
                    }
                }

                if(dataFilled) {
                    needDetails.add(needItemDetails);
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

                itemQuantity.setText("");
                itemQuantity.setError(null);
                gender.clearCheck();
                lastGenderRadioBtn.setError(null);
                age.setText("");
                age.setError(null);
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

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    class CreateUserAccount extends AsyncTask {

        HttpResponse response;
        HttpClient client;

        @Override
        protected Object doInBackground(Object[] params) {

            client = new DefaultHttpClient();

            NeedDetails need_details=new NeedDetails();
            need_details.setLatitude("1234");
            need_details.setLongitude("12345");
            need_details.items=needDetails;

                String coordinator_token = Connectivity.getAuthToken(NewNeedActivity.this, Connectivity.Coordinator_Token);
                String donor_token = Connectivity.getAuthToken(NewNeedActivity.this, Connectivity.Donor_Token);
                response = Connectivity.makePostRequest(RestAPIURL.postNeedURL, gson.toJson(need_details,NeedDetails.class), client, donor_token);
                Log.d("Request JSON", gson.toJson(need_details,NeedDetails.class));
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
                    Toast.makeText(NewNeedActivity.this,"needItemDetails successfully posted...",Toast.LENGTH_LONG).show();
                    NewNeedActivity.this.finish();
                    HomeActivity.countNeedDetails=0;
                    startActivity(new Intent(NewNeedActivity.this,HomeActivity.class));
                }

            super.onPostExecute(o);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
