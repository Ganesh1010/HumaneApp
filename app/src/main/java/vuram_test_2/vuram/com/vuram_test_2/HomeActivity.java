package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;

public class HomeActivity extends AppCompatActivity implements LoadNextNeedDetails, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final int MENU_ITEM_ONE = 1;
    private final int MENU_ITEM_TWO = 2;
    private final int MENU_ITEM_THREE = 3;
    private final int MENU_ITEM_FOUR = 4;
    public static final int FILTER_REQUEST = 5;
    public static final int LOCATION_REQUEST = 6;
    String compareValue;
    public static Set<String> appliedFilter;
    public static String locationName = "Chennai";
    protected Handler handler;
    HttpResponse response;
    private DonorNeedViewAdapter mAdapter;
    Gson gson;
    HttpClient client;
    String nextUrl=RestAPIURL.needList;
    ProgressDialog progressDialog=null;
    ArrayList<NeedDetails> needitem=new ArrayList<>();
    ArrayList<NeedDetails> orgNeeds = new ArrayList<>();
    ArrayList<NeedDetails> tempneeditem,tempOrgNeeds;
    private final String TAG = "HomeActivity.java";
    public int count=0;
    Intent intent;
    private RecyclerView recyclerView;
    private ImageButton filterImageButton;
    private TextView  tvEmptyView;
    private GetNeedItemDetails getNeedItemDetails;
    private FloatingActionButton newNeedFloatingActionButton;
    JSONObject jsonObject;
    Spinner spinner;

    ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        appliedFilter = new TreeSet<>();

        /* Spinner */
        spinner = (Spinner) findViewById(R.id.author_spinner_donor_home);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        spinner.setOnItemSelectedListener(HomeActivity.this);
        gson = new Gson();
        List<String> categories = new ArrayList<String>();
        categories.add("Donor");
        categories.add("Organization");
        progressDialog = new ProgressDialog(HomeActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(dataAdapter);
        intent = getIntent();
        Log.d("hai", intent.getStringExtra(USER_KEY_TYPE));
      if (intent.getStringExtra(USER_KEY_TYPE).equals("DONOR")) {
          if (compareValue != null) {
              compareValue = "Donor";
              int spinnerPosition = dataAdapter.getPosition(compareValue);
              spinner.setSelection(spinnerPosition);
              Log.d("hai", spinnerPosition + "");
          }
      }
        if (intent.getStringExtra(USER_KEY_TYPE).equals("ORGANISATION"))

        compareValue = "Organization";
        if (compareValue != null) {
            int spinnerPosition = dataAdapter.getPosition(compareValue);
            spinner.setSelection(spinnerPosition);
            Log.d("hai", spinnerPosition + "");
        }

        handler = new Handler();

        /* Menu Button */
        menuButton = (ImageButton) findViewById(R.id.menu_imagebutton_donor_home);
        menuButton.setOnClickListener(HomeActivity.this);

        /* Choose Location */
        ImageButton currentLocationImageButton = (ImageButton) findViewById(R.id.current_location_imagebutton_home);
        currentLocationImageButton.setOnClickListener(HomeActivity.this);
        TextView currentLocationTextView = (TextView) findViewById(R.id.current_location_textview_home);
        currentLocationTextView.setOnClickListener(HomeActivity.this);

       // startAsyncTask();
        filterImageButton = (ImageButton) findViewById(R.id.filter_imagebutton_donor_home);
        filterImageButton.setOnClickListener(HomeActivity.this);

        getWidgets();
        newNeedFloatingActionButton = (FloatingActionButton) findViewById(R.id.new_need_home_page);
        newNeedFloatingActionButton.setOnClickListener(HomeActivity.this);
    }

    private void getWidgets() {
        recyclerView = (RecyclerView) findViewById(R.id.needs_recyclerview_home_page);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILTER_REQUEST) {
            Iterator i=appliedFilter.iterator();
            while (i.hasNext()) {
                System.out.println(i.next());
            }
        } else if (requestCode == LOCATION_REQUEST) {
            TextView currentLocationTextView = (TextView) findViewById(R.id.current_location_textview_home);
            currentLocationTextView.setText(locationName);
        }
    }

    private void startAsyncTask(){
        getNeedItemDetails = new GetNeedItemDetails();
        getNeedItemDetails.nextNeedDetails=this;
        getNeedItemDetails.execute();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    };

    @Override
    public void nextURL(String result) {
        if(result.equals("next"))
            startAsyncTask();
        else
            getNeedItemDetails.cancel(true);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.menu_imagebutton_donor_home:
                PopupMenu popupMenu = new PopupMenu(HomeActivity.this, menuButton);
                Menu menu = popupMenu.getMenu();
                menu.add(Menu.NONE, MENU_ITEM_ONE, Menu.NONE, "My Profile");
                menu.add(Menu.NONE, MENU_ITEM_TWO, Menu.NONE, "Settings");
                menu.add(Menu.NONE, MENU_ITEM_THREE, Menu.NONE, "About Us");
                if(Connectivity.getAuthToken(HomeActivity.this,Connectivity.Donor_Token)!=null)
                    menu.add(Menu.NONE, MENU_ITEM_FOUR, Menu.NONE, "Logout");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(HomeActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().toString().equals("My Profile"))
                            startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
                        else if(item.getTitle().toString().equals("Logout"))
                        {
                            Connectivity.deleteAuthToken(HomeActivity.this,Connectivity.Donor_Token);
                            startActivity(new Intent(HomeActivity.this,LoginPageFragment.class));
                            HomeActivity.this.finish();
                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;

            case R.id.current_location_imagebutton_home:
            case R.id.current_location_textview_home:
                Intent intent = new Intent(HomeActivity.this, ChooseLocationActivity.class);
                startActivityForResult(intent, LOCATION_REQUEST);
                break;

            case R.id.filter_imagebutton_donor_home:
                intent = new Intent(HomeActivity.this, FilterActivity.class);
                startActivityForResult(intent, 2);
                break;

            case R.id.new_need_home_page:
                startActivity(new Intent(HomeActivity.this, NewNeedActivity.class));
                break;
        }
    }

    class GetNeedItemDetails extends AsyncTask {

        public LoadNextNeedDetails nextNeedDetails;
        @Override
        protected Object doInBackground(Object[] params)
        {
            response = Connectivity.makeGetRequest(nextUrl, client, Connectivity.getAuthToken(HomeActivity.this, Connectivity.Donor_Token));
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                try {
                    jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                    JSONArray results = jsonObject.getJSONArray("results");
                    nextUrl = jsonObject.getString("next");
                    Gson gson = new Gson();
                    tempneeditem = gson.fromJson(results.toString(), new TypeToken<List<NeedDetails>>() {}.getType());
                    if(count==0)
                        needitem.addAll(tempneeditem);

                    Log.d("Results", needitem.size() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                Log.d("CAll ", "Response null");
            return null;
        }
        @Override
        protected void onPreExecute() {
            client= new DefaultHttpClient();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Object o) {
            //mHandler.sendMessageDelayed(new Message(), 3000);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(response!=null)
                if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201)
                {
                    if(count==0) {
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                        mAdapter = new DonorNeedViewAdapter(HomeActivity.this, needitem, recyclerView);
                        recyclerView.setAdapter(mAdapter);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                        recyclerView.addItemDecoration(dividerItemDecoration);
                    }
                    if (needitem.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        tvEmptyView.setVisibility(View.VISIBLE);

                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvEmptyView.setVisibility(View.GONE);
                    }

                    mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            //add null , so the adapter will check view_type and show progress bar at bottom
                            needitem.add(null);
                            mAdapter.notifyItemInserted(needitem.size() - 1);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //   remove progress item
                                    needitem.remove(needitem.size() - 1);
                                    mAdapter.notifyItemRemoved(needitem.size());


                                    if(!jsonObject.isNull("next")) {
                                        nextNeedDetails.nextURL("next");
                                        count++;
                                        if(count>0) {
                                            needitem.addAll(tempneeditem);
                                            mAdapter.notifyDataSetChanged();
                                            mAdapter.setLoaded();
                                        }
                                    }

                                    else {
                                        nextNeedDetails.nextURL("finished");
                                        Toast.makeText(getApplicationContext(),"No more needs to load..",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, 1000);
                        }
                    });


                }

            super.onPostExecute(o);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView userType = (TextView) parent.getChildAt(0);
        if (userType != null) {
            userType.setTextColor(Color.WHITE);
        }

        String authorType = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + authorType, Toast.LENGTH_LONG).show();

        if (authorType.equals("Donor")) {
            newNeedFloatingActionButton.setVisibility(View.INVISIBLE);
            startAsyncTask();
            //new GetNeedItemDetails().execute();
            //  recyclerView.setAdapter(new DonorNeedViewAdapter(HomeActivity.this, needs));
        } else {
            newNeedFloatingActionButton.setVisibility(View.VISIBLE);
            //  recyclerView.setAdapter(new OrgNeedViewAdapter(HomeActivity.this, needs));
        }

        if(authorType.equals("Organization")) {
            Toast.makeText(parent.getContext(), " Testing Selected: " + authorType, Toast.LENGTH_LONG).show();

            new GetOrganisationNeedDetails().execute();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    class GetOrganisationNeedDetails extends AsyncTask{

        @Override
        protected void onPreExecute() {
            client = new DefaultHttpClient();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            response = Connectivity.makeGetRequest(RestAPIURL.orgDetails, client, Connectivity.getAuthToken(HomeActivity.this, Connectivity.Donor_Token));
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                try {
                    jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                    JSONArray results = jsonObject.getJSONArray("results");
                    nextUrl = jsonObject.getString("next");
                    Gson gson = new Gson();
                    tempOrgNeeds= gson.fromJson(results.toString(), new TypeToken<List<NeedDetails>>() {}.getType());
                    orgNeeds.addAll(tempOrgNeeds);

                    Log.d("Results", orgNeeds.size() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                Log.d("CAll ", "Response null");
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            mHandler.sendMessageDelayed(new Message(), 3000);
            if(response!=null)
                if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201)
                {
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    mAdapter=new DonorNeedViewAdapter(HomeActivity.this,orgNeeds,recyclerView);
                    recyclerView.setAdapter(mAdapter);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    if (needitem.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        tvEmptyView.setVisibility(View.VISIBLE);

                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvEmptyView.setVisibility(View.GONE);
                    }
                    mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            //add null , so the adapter will check view_type and show progress bar at bottom
                            needitem.add(null);
                            mAdapter.notifyItemInserted(needitem.size() - 1);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //   remove progress item
                                    needitem.remove(needitem.size() - 1);
                                    mAdapter.notifyItemRemoved(needitem.size());

                                    /*if(!jsonObject.isNull("next"))
                                       nextNeedDetails.nextURL("next");

                                    else
                                      nextNeedDetails.nextURL("finished");*/
                                    needitem.addAll(tempneeditem);
                                    mAdapter.notifyDataSetChanged();
                                    mAdapter.setLoaded();
                                }
                            }, 2000);
                        }
                    });
                }

            super.onPostExecute(o);
        }

    }
}
