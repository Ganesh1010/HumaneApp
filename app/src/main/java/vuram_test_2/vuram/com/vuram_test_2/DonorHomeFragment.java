package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import vuram_test_2.vuram.com.vuram_test_2.util.CommonUI;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_ORG;

/**
 * Created by akshayagr on 08-06-2017.
 */

public class DonorHomeFragment extends Fragment implements LoadNextDetails, View.OnClickListener, AdapterView.OnItemSelectedListener {

    View v;
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
    public static Set<String> appliedFilter;
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
    public TextView tvEmptyView;
    public GetNeedItemDetails getNeedItemDetails;
    public GetOrganisationNeedDetails getOrganisationNeedDetails;
    public FloatingActionButton newNeedFloatingActionButton;
    public JSONObject jsonObject;
    public Spinner spinner;
    public ImageButton menuButton;
    LandingPage landingPage;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILTER_REQUEST) {
            Iterator i=appliedFilter.iterator();
            while (i.hasNext()) {
                System.out.println(i.next());
            }
        }
        else if (requestCode == LOCATION_REQUEST) {

        }
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(v==null)
            v = inflater.inflate(R.layout.activity_home_page,container,false);

        landingPage = (LandingPage)getActivity();
        appliedFilter = new TreeSet<>();

        /* Spinner */
        spinner = (Spinner) v.findViewById(R.id.author_spinner_donor_home);
        tvEmptyView = (TextView) v.findViewById(R.id.empty_view);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView userType = (TextView) adapterView.getChildAt(0);
                if (userType != null) {
                    userType.setTextColor(Color.WHITE);
                }

                String authorType = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), "Selected: " + authorType, Toast.LENGTH_LONG).show();

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
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        gson = new Gson();
        List<String> categories = new ArrayList<>();
        categories.add("Donor");
        categories.add("Organization");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(landingPage, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(dataAdapter);
        intent = landingPage.getIntent();
        String user = getArguments().getString(USER_KEY_TYPE);
     //   Log.d("hai", intent.getStringExtra(USER_KEY_TYPE));

       // if (intent.getStringExtra(USER_KEY_TYPE).equals("DONOR"))
        if(user.equals("DONOR"))
        {
            if (compareValue != null) {
                compareValue = "Donor";
                int spinnerPosition = dataAdapter.getPosition(compareValue);
                spinner.setSelection(spinnerPosition);
                Log.d("hai", spinnerPosition + "");
                nextUrl=RestAPIURL.needList;
            }
        }

     //   if (intent.getStringExtra(USER_KEY_TYPE).equals("ORGANISATION")) {
        if(user.equals("ORGANISATION")){
            if (compareValue != null) {
                compareValue = "Organization";
                int spinnerPosition = dataAdapter.getPosition(compareValue);
                spinner.setSelection(spinnerPosition);
                Log.d("hai", spinnerPosition + "");
                nextUrl=RestAPIURL.orgDetails;
            }
        }

        handler = new Handler();

        /* Menu Button */
        menuButton = (ImageButton)v. findViewById(R.id.menu_imagebutton_donor_home);
        menuButton.setOnClickListener((View.OnClickListener) this);

        /* Choose Location */
        ImageButton currentLocationImageButton = (ImageButton)v.findViewById(R.id.current_location_imagebutton_home);
        currentLocationImageButton.setOnClickListener((View.OnClickListener)this);

        filterImageButton = (ImageButton)v.findViewById(R.id.filter_imagebutton_donor_home);
        filterImageButton.setOnClickListener((View.OnClickListener)this);

        newNeedFloatingActionButton = (FloatingActionButton)v.findViewById(R.id.new_need_home_page);
        newNeedFloatingActionButton.setOnClickListener((View.OnClickListener)this);

        recyclerView = (RecyclerView)v.findViewById(R.id.needs_recyclerview_home_page);


        return v;
    }
    public void startNeedAsyncTask(boolean isFirstTime) {
        getNeedItemDetails = new GetNeedItemDetails(isFirstTime);
        getNeedItemDetails.nextNeedDetails= (LoadNextDetails) this;
        getNeedItemDetails.execute();
    }

    public void startOrgAsyncTask(boolean isFirstTime) {
        getOrganisationNeedDetails = new GetOrganisationNeedDetails(isFirstTime);
        getOrganisationNeedDetails.nextOrgDetails= (LoadNextDetails) this;
        getOrganisationNeedDetails.execute();
    }




    @Override
    public void onClick(View view) {

    }

    @Override
    public void nextURL(String result) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


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
                progressDialog = new ProgressDialog(landingPage, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            response = Connectivity.makeGetRequest(nextUrl, client, Connectivity.getAuthToken(landingPage, Connectivity.Donor_Token));
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
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(response!=null)
                if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201)
                {
                    recyclerView.setHasFixedSize(true);
                    //recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    recyclerView.setLayoutManager(new GridLayoutManager(landingPage,2));
                    orgAdapter=new OrgNeedViewAdapter(landingPage,orgNeeds,recyclerView);
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
                                        Toast.makeText(landingPage, "No more needs to load..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, 2000);
                        }
                    });
                }
            super.onPostExecute(o);
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
            if(isFirstTime) {
                client = new DefaultHttpClient();
                progressDialog = new ProgressDialog(landingPage, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] params)
        {
            response = Connectivity.makeGetRequest(nextUrl, client, Connectivity.getAuthToken(landingPage, Connectivity.Donor_Token));
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                try {
                    jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                    JSONArray results = jsonObject.getJSONArray("results");
                    if (!jsonObject.isNull("next"))
                        nextUrl = jsonObject.getString("next");
                    Gson gson = new Gson();
                    tempneeditem = gson.fromJson(results.toString(), new TypeToken<List<NeedDetails>>() {}.getType());
                    if(isFirstTime)
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
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if(response!=null)
            {
                if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201) {
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(landingPage));
                    donorAdapter = new DonorNeedViewAdapter(landingPage, needitem, recyclerView);
                    recyclerView.setAdapter(donorAdapter);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                    recyclerView.addItemDecoration(dividerItemDecoration);

                    if (needitem.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        tvEmptyView.setVisibility(View.VISIBLE);

                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvEmptyView.setVisibility(View.GONE);
                    }

                    if(!isFirstTime) {
                        needitem.addAll(tempneeditem);
                        donorAdapter.notifyDataSetChanged();
                        donorAdapter.setLoaded();
                    }

                    donorAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            //add null , so the adapter will check view_type and show progress bar at bottom
                            needitem.add(null);
                            recyclerView.post(new Runnable() {
                                public void run() {
                                    // There is no need to use notifyDataSetChanged()
                                    donorAdapter.notifyItemInserted(needitem.size() - 1);
                                }
                            });

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //   remove progress item
                                    needitem.remove(needitem.size() - 1);
                                    donorAdapter.notifyItemRemoved(needitem.size());
                                    if (!jsonObject.isNull("next")) {
                                        nextNeedDetails.nextURL("nextNeedDetails");
                                    } else {
                                        nextNeedDetails.nextURL("finishedNeedDetails");
                                        Toast.makeText(landingPage, "No more needs to load..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },2000);
                        }
                    });
                }
            }
        }
    }

}
