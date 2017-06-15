package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;

public class HomeActivity extends AppCompatActivity implements LoadNextDetails, View.OnClickListener, AdapterView.OnItemSelectedListener {

    public final String TAG = "HomeActivity.java";
    public final String myProfile = "My Profile";
    public final String aboutUs = "About Us";
    public final String logout = "Logout";

    public final int MENU_ITEM_ONE = 1;
    public final int MENU_ITEM_TWO = 2;
    public final int MENU_ITEM_THREE = 3;

    public static final int FILTER_REQUEST = 5;
    public static final int LOCATION_REQUEST = 6;
    public String compareValue;
    boolean noMoredatatoload;
    LinearLayoutManager mLinearLayoutManager;
    public static Map<Integer,ArrayList<Integer>> filterItems=new HashMap<>();
    public static String locationName = "Location";
    public Handler handler;
    public HttpResponse response;
    public DonorNeedViewAdapter donorAdapter;
    public OrgNeedViewAdapter orgAdapter;
    public Gson gson;
    public HttpClient client;
    public String nextUrl;
    public ProgressDialog progressDialog=null;
    public ArrayList<NeedDetails> needitem=new ArrayList<>();
    public ArrayList<NeedDetails> orgNeeds = new ArrayList<>();
    public ArrayList<NeedDetails> tempneeditem,tempOrgNeeds;
    public Intent intent;
    public RecyclerView recyclerView;
    public ImageButton filterImageButton;
    public TextView  tvEmptyView;
    public GetNeedItemDetails getNeedItemDetails;
    public GetOrganisationNeedDetails getOrganisationNeedDetails;
    public FloatingActionButton newNeedFloatingActionButton;
    public JSONObject jsonObject;
    public Spinner spinner;
    public ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //appliedFilter = new TreeSet<>();
        //filterItems=new HashMap<>();

        /* Spinner */
        spinner = (Spinner) findViewById(R.id.author_spinner_donor_home);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        //spinner.setOnItemSelectedListener(HomeActivity.this);
        gson = new Gson();
        List<String> categories = new ArrayList<>();
        categories.add("Donor");
        categories.add("Organization");
        mLinearLayoutManager=new LinearLayoutManager(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(dataAdapter);
        intent = getIntent();
        Log.d("hai", intent.getStringExtra(USER_KEY_TYPE));

        if (intent.getStringExtra(USER_KEY_TYPE).equals("DONOR"))
        {
            if (compareValue != null) {
                compareValue = "Donor";
                int spinnerPosition = dataAdapter.getPosition(compareValue);
                spinner.setSelection(spinnerPosition);
                Log.d("hai", spinnerPosition + "");
                nextUrl=RestAPIURL.needList;
            }
        }

        if (intent.getStringExtra(USER_KEY_TYPE).equals("ORGANISATION")) {
            if (compareValue != null) {
                compareValue = "Organization";
                int spinnerPosition = dataAdapter.getPosition(compareValue);
                spinner.setSelection(spinnerPosition);
                Log.d("hai", spinnerPosition + "");
                nextUrl=RestAPIURL.orgDetails;
            }
        }

        handler = new Handler();
       // nextUrl=RestAPIURL.needList;
       // startNeedAsyncTask(true);
        /* Menu Button */
        menuButton = (ImageButton) findViewById(R.id.menu_imagebutton_donor_home);
        menuButton.setOnClickListener(HomeActivity.this);

        /* Choose Location */
        ImageButton currentLocationImageButton = (ImageButton) findViewById(R.id.current_location_imagebutton_home);
        currentLocationImageButton.setOnClickListener(HomeActivity.this);

        filterImageButton = (ImageButton) findViewById(R.id.filter_imagebutton_donor_home);
        filterImageButton.setOnClickListener(HomeActivity.this);

        newNeedFloatingActionButton = (FloatingActionButton) findViewById(R.id.new_need_home_page);
        newNeedFloatingActionButton.setOnClickListener(HomeActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.needs_recyclerview_home_page);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILTER_REQUEST) {

//            Iterator i=appliedFilter.iterator();
//            while (i.hasNext()) {
//                System.out.println(i.next());
//            }
        }
        else if (requestCode == LOCATION_REQUEST) {

        }
    }

    public void startNeedAsyncTask(boolean isFirstTime) {
        getNeedItemDetails = new GetNeedItemDetails(isFirstTime);
        getNeedItemDetails.nextNeedDetails=this;
        getNeedItemDetails.execute();
    }

    public void startOrgAsyncTask(boolean isFirstTime) {
        getOrganisationNeedDetails = new GetOrganisationNeedDetails(isFirstTime);
        getOrganisationNeedDetails.nextOrgDetails=this;
        getOrganisationNeedDetails.execute();
    }

    @Override
    public void nextURL(String result) {
        if(result.equals("nextNeedDetails"))
            startNeedAsyncTask(false);
        else if(result.equals("nextOrgDetails"))
            startOrgAsyncTask(false);
        else if(result.equals("finishedNeedDetails"))
            getNeedItemDetails.cancel(true);
        else
            getOrganisationNeedDetails.cancel(true);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.menu_imagebutton_donor_home:
                PopupMenu popupMenu = new PopupMenu(HomeActivity.this, menuButton);
                Menu menu = popupMenu.getMenu();
                menu.add(Menu.NONE, MENU_ITEM_TWO, Menu.NONE, aboutUs);

                if(Connectivity.getAuthToken(HomeActivity.this,Connectivity.Donor_Token) != null) {
                    menu.add(Menu.NONE, MENU_ITEM_ONE, Menu.NONE, myProfile);
                    menu.add(Menu.NONE, MENU_ITEM_THREE, Menu.NONE, logout);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(HomeActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        String itemSelected = item.getTitle().toString();
                        if (itemSelected.equals(myProfile)) {
                            startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
                            //HomeActivity.this.finish();
                        }
                        else if(itemSelected.equals(logout)) {
                            Connectivity.deleteAuthToken(HomeActivity.this,Connectivity.Donor_Token);
                            startActivity(new Intent(HomeActivity.this,LoginPageFragment.class));
                            //HomeActivity.this.finish();
                        } else if (itemSelected.equals(aboutUs)) {

                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;

            case R.id.current_location_imagebutton_home:
                Intent intent = new Intent(HomeActivity.this, ChooseLocationActivity.class);
                startActivityForResult(intent, LOCATION_REQUEST);
                //HomeActivity.this.finish();
                break;

            case R.id.filter_imagebutton_donor_home:
                intent = new Intent(HomeActivity.this, FilterActivity.class);
                startActivityForResult(intent, FILTER_REQUEST);
                //HomeActivity.this.finish();
                break;

            case R.id.new_need_home_page:
                startActivity(new Intent(HomeActivity.this, NewNeedActivity.class));
                //HomeActivity.this.finish();
                break;
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
            nextUrl=RestAPIURL.needList;
            if(needitem.size()>0)
                needitem.clear();
            startNeedAsyncTask(true);
        }
        else if(authorType.equals("Organization")) {
            newNeedFloatingActionButton.setVisibility(View.VISIBLE);
            nextUrl=RestAPIURL.orgDetails;
            if(orgNeeds.size()>0)
                orgNeeds.clear();
            startOrgAsyncTask(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    class GetOrganisationNeedDetails extends AsyncTask {
        public LoadNextDetails nextOrgDetails;
        public boolean isFirstTime;
        public GetOrganisationNeedDetails(boolean isFirstTime)
        {
            this.isFirstTime=isFirstTime;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(isFirstTime) {
                client = new DefaultHttpClient();
                progressDialog = new ProgressDialog(HomeActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            response = Connectivity.makeGetRequest(nextUrl, client, Connectivity.getAuthToken(HomeActivity.this, Connectivity.Donor_Token));
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                try {
                    jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                    JSONArray results = jsonObject.getJSONArray("results");
                    if (!jsonObject.isNull("next"))
                        nextUrl = jsonObject.getString("next");
                    Gson gson = new Gson();
                    tempOrgNeeds= gson.fromJson(results.toString(), new TypeToken<List<NeedDetails>>() {}.getType());
                    if(isFirstTime)
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
        protected void onPostExecute(final Object o) {
            super.onPostExecute(o);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(response!=null)
                if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201)
                {
                    recyclerView.setHasFixedSize(true);
                    //recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    orgAdapter=new OrgNeedViewAdapter(HomeActivity.this,orgNeeds,recyclerView);
                    recyclerView.setAdapter(orgAdapter);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    if (orgNeeds.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        tvEmptyView.setVisibility(View.VISIBLE);

                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvEmptyView.setVisibility(View.GONE);
                    }

                    if(!isFirstTime) {
                        orgNeeds.addAll(tempOrgNeeds);
                        orgAdapter.notifyDataSetChanged();
                        orgAdapter.setLoaded();
                    }

                    orgAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            //add null , so the adapter will check view_type and show progress bar at bottom
                            orgNeeds.add(null);

                            recyclerView.post(new Runnable() {
                                public void run() {
                                    // There is no need to use notifyDataSetChanged()
                                    orgAdapter.notifyItemInserted(orgNeeds.size() - 1);
                                }
                            });

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //   remove progress item
                                    orgNeeds.remove(orgNeeds.size() - 1);
                                    orgAdapter.notifyItemRemoved(orgNeeds.size());
                                    if (!jsonObject.isNull("next")) {
                                        nextOrgDetails.nextURL("nextOrgDetails");
                                    } else {
                                        nextOrgDetails.nextURL("finishedOrgDetails");
                                        Toast.makeText(getApplicationContext(), "No more needs to load..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, 2000);
                        }
                    });
                }
        }
    }

    class GetNeedItemDetails extends AsyncTask {
        public LoadNextDetails nextNeedDetails;
        public boolean isFirstTime;
        public GetNeedItemDetails(boolean isFirstTime)
        {
            this.isFirstTime=isFirstTime;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(tempneeditem!=null)
                tempneeditem.clear();
            if(isFirstTime) {
                client = new DefaultHttpClient();
                progressDialog = new ProgressDialog(HomeActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] params)
        {
            if(nextUrl!=null) {
                response = Connectivity.makeGetRequest(nextUrl, client, Connectivity.getAuthToken(HomeActivity.this, Connectivity.Donor_Token));
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                    try {
                        jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                        JSONArray results = jsonObject.getJSONArray("results");
                        if (!jsonObject.isNull("next"))
                            nextUrl = jsonObject.getString("next");
                        else {
                            nextUrl = null;
                            noMoredatatoload=true;
                        }
                        Gson gson = new Gson();
                        tempneeditem = gson.fromJson(results.toString(), new TypeToken<List<NeedDetails>>() {
                        }.getType());
                        Log.d(TAG, "doInBackground: Temp Item" + tempneeditem.size());
                        if (isFirstTime)
                            needitem.addAll(tempneeditem);
                        Log.d("Results", needitem.size() + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    Log.d("CAll ", "Response null");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if(response!=null)
            {
                if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201) {

                    if(isFirstTime)
                    {
                        recyclerView.setLayoutManager(mLinearLayoutManager);
                        donorAdapter = new DonorNeedViewAdapter(HomeActivity.this, needitem, recyclerView,HomeActivity.this);
                        donorAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                Handler handler = new Handler();
                                Log.d(TAG, "onLoadMore: Callled");
                                final Runnable r = new Runnable() {
                                    public void run() {
                                        needitem.add(null);
                                        donorAdapter.notifyItemInserted(needitem.size() - 1);
                                        startNeedAsyncTask(false);
                                        Log.d(TAG, "run: Loaded Data");
                                    }
                                };
                                handler.post(r);
                            }
                        });
                        recyclerView.setAdapter(donorAdapter);
                    }
                    else
                    {
                        needitem.remove(needitem.size()-1);
                        needitem.addAll(tempneeditem);
                        donorAdapter.notifyItemInserted(needitem.size());
                        if(!noMoredatatoload)
                            donorAdapter.setLoaded();
                    }
                }
            }
        }
    }
}