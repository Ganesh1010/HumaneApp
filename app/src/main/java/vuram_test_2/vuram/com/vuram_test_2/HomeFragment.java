package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_DONOR;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_ORG;

public class HomeFragment extends Fragment implements LoadNextDetails{

    View v;
    public final String TAG = "HomeFragment.java";
    public final String myProfile = "My Profile";
    public final String aboutUs = "About Us";
    public final String logout = "Logout";
    public final int MENU_ITEM_ONE = 1;
    public final int MENU_ITEM_TWO = 2;
    public final int MENU_ITEM_THREE = 3;
    public static final int FILTER_REQUEST = 5;
    public static final int LOCATION_REQUEST = 6;
    public static Set<String> appliedFilter;
    public HttpResponse response;
    public DonorNeedViewAdapter donorAdapter;
    public OrgNeedViewAdapter orgAdapter;
    public Gson gson;
    public HttpClient client;
    public String nextUrl;
    public ProgressDialog progressDialog=null;
    public ArrayList<NeedDetails> donorNeeds=new ArrayList<>();
    public ArrayList<NeedDetails> orgNeeds = new ArrayList<>();
    public ArrayList<NeedDetails> tempDonorNeeds,tempOrgNeeds;
    public Intent intent;
    public RecyclerView recyclerView;
    public ImageButton filterImageButton;
    public TextView tvEmptyView;
    public GetDonorNeedDetails getDonorNeedDetails;
    public GetOrganisationNeedDetails getOrganisationNeedDetails;
    public FloatingActionButton newNeedFloatingActionButton;
    public JSONObject jsonObject;
    public Spinner spinner;
    public ImageButton menuButton;
    public String userType;
    public Fragment fragment = null;
    public boolean noMoreDataToLoad=false;
    FragmentManager fragmentManager;
    public LinearLayoutManager mLinearLayoutManager;
    String authorType;
    int spinnerPosition;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {

        if(v==null)
            v = inflater.inflate(R.layout.activity_home_page,container,false);

        recyclerView = (RecyclerView)v.findViewById(R.id.needs_recyclerview_home_page);
        appliedFilter = new TreeSet<>();
        gson = new Gson();
        recyclerView.getRecycledViewPool().clear();
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        List<String> categories = new ArrayList<>();
        categories.add(USER_TYPE_SELECTION_DONOR);
        categories.add(USER_TYPE_SELECTION_ORG);

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

                authorType = adapterView.getItemAtPosition(i).toString();
             //   Toast.makeText(adapterView.getContext(), "Selected: " + authorType, Toast.LENGTH_LONG).show();
                Log.d(TAG, "onCreateView:" +authorType);

                if (authorType.equals(USER_TYPE_SELECTION_DONOR)) {
                    newNeedFloatingActionButton.setVisibility(View.INVISIBLE);
                    nextUrl=RestAPIURL.needList;
                    if(donorNeeds.size()>0)
                        donorNeeds.clear();
                    noMoreDataToLoad=false;
                    startNeedAsyncTask(true);
                }
                else if(authorType.equals(USER_TYPE_SELECTION_ORG)) {
                    Log.d("onItemSelected: ","inside organisation");
                    newNeedFloatingActionButton.setVisibility(View.VISIBLE);
                    nextUrl = RestAPIURL.orgDetails;
                    if (orgNeeds.size() > 0)
                        orgNeeds.clear();
                    noMoreDataToLoad=false;
                    startOrgAsyncTask(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(dataAdapter);

         Bundle bundle = getArguments();
        if(bundle!=null) {
            userType = bundle.getString(USER_KEY_TYPE);
            Log.d(" Home Fragment", userType);
        }
        else
            Log.e(TAG, "onCreateView: user Type inside bundle is empty",new NullPointerException());

        if (userType.equals(USER_TYPE_SELECTION_DONOR)) {
                spinnerPosition = dataAdapter.getPosition(USER_TYPE_SELECTION_DONOR);
                spinner.setSelection(spinnerPosition);
                Log.d("User Type Spinner Position", spinnerPosition + "");
                nextUrl = RestAPIURL.needList;
        }

        if(userType.equals(USER_TYPE_SELECTION_ORG)){
                spinnerPosition = dataAdapter.getPosition(USER_TYPE_SELECTION_ORG);
                spinner.setSelection(spinnerPosition);
                Log.d("hai", spinnerPosition + "");
                nextUrl=RestAPIURL.orgDetails;
        }



        /* Menu Button */
        menuButton = (ImageButton)v.findViewById(R.id.menu_imagebutton_donor_home);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), menuButton);
                Menu menu = popupMenu.getMenu();
                menu.add(Menu.NONE, MENU_ITEM_TWO, Menu.NONE, aboutUs);

                if(Connectivity.getAuthToken(getActivity(),Connectivity.Donor_Token) != null) {
                    menu.add(Menu.NONE, MENU_ITEM_ONE, Menu.NONE, myProfile);
                    menu.add(Menu.NONE, MENU_ITEM_THREE, Menu.NONE, logout);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        String itemSelected = item.getTitle().toString();
                        if (itemSelected.equals(myProfile)) {
                            startActivity(new Intent(getActivity(), UserProfileActivity.class));
                        }
                        else if(itemSelected.equals(logout)) {
                            Connectivity.deleteAuthToken(getActivity(),Connectivity.Donor_Token);
                            Bundle bundle = new Bundle();
                            bundle.putString(USER_KEY_TYPE,USER_TYPE_SELECTION_DONOR);
                            fragment = new LoginPageFragment();
                            fragmentManager = getFragmentManager();
                            fragment.setArguments(bundle);
                            fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();

                        } else if (itemSelected.equals(aboutUs)) {

                        }
                        return true;
                    }
                });
                popupMenu.show();


            }
        });

        /* Choose Location */
        ImageButton currentLocationImageButton = (ImageButton)v.findViewById(R.id.current_location_imagebutton_home);
        currentLocationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChooseLocationActivity.class);
                startActivityForResult(intent, LOCATION_REQUEST);

            }
        });

        /* filter */
        filterImageButton = (ImageButton)v.findViewById(R.id.filter_imagebutton_donor_home);
        filterImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), FilterActivity.class);
                startActivityForResult(intent, FILTER_REQUEST);
            }
        });

        /* New need button*/

        newNeedFloatingActionButton = (FloatingActionButton)v.findViewById(R.id.new_need_home_page);
        newNeedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new NewNeedActivityFragment();
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();
            }
        });

        return v;
    }

    public void startNeedAsyncTask(boolean isFirstTime) {

        getDonorNeedDetails = new GetDonorNeedDetails(isFirstTime);
        getDonorNeedDetails.nextNeedDetails = this;
        getDonorNeedDetails.execute();
    }

    public void startOrgAsyncTask(boolean isFirstTime) {
        getOrganisationNeedDetails = new GetOrganisationNeedDetails(isFirstTime);
        getOrganisationNeedDetails.nextOrgDetails= this;
        getOrganisationNeedDetails.execute();
    }

    @Override
    public void nextURL(String result) {
        if(result.equals("nextNeedDetails"))
            startNeedAsyncTask(false);
        else if(result.equals("nextOrgDetails"))
            startOrgAsyncTask(false);
        else if(result.equals("finishedNeedDetails"))
            getDonorNeedDetails.cancel(true);
        else
            getOrganisationNeedDetails.cancel(true);
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
            if(tempOrgNeeds!=null)
                tempOrgNeeds.clear();
            if(isFirstTime) {
                client = new DefaultHttpClient();
                progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if(nextUrl!=null) {
                response = Connectivity.makeGetRequest(nextUrl, client, Connectivity.getAuthToken(getActivity(), Connectivity.Donor_Token));
                if (response != null) {
                    if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                        try {
                            jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                            JSONArray results = jsonObject.getJSONArray("results");
                            if (!jsonObject.isNull("next"))
                                nextUrl = jsonObject.getString("next");
                            else {
                                nextUrl = null;
                                noMoreDataToLoad = true;
                            }
                            Gson gson = new Gson();
                            tempOrgNeeds = gson.fromJson(results.toString(), new TypeToken<List<NeedDetails>>() {
                            }.getType());
                            if (isFirstTime)
                                orgNeeds.addAll(tempOrgNeeds);

                            Log.d("Results", orgNeeds.size() + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else
                        Log.d("CAll ", "Response null");
                }
            }
            Log.e(TAG, "doInBackground: Http Response is null",new NullPointerException() );
            return null;
        }

        @Override
        protected void onPostExecute(final Object o) {
            super.onPostExecute(o);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(response!=null) {
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                    if (isFirstTime)
                    {
                        recyclerView.setLayoutManager(mLinearLayoutManager);
                        orgAdapter = new OrgNeedViewAdapter(getActivity(), orgNeeds, recyclerView);

                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                        recyclerView.addItemDecoration(dividerItemDecoration);

                        orgAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                //add null , so the adapter will check view_type and show progress bar at bottom
                                Handler handler = new Handler();
                                final Runnable r = new Runnable() {
                                    public void run() {
                                        int size=orgNeeds.size();
                                        orgNeeds.add(null);
                                        orgAdapter.notifyItemRangeInserted(size, orgNeeds.size()-size);
                                        startOrgAsyncTask(false);
                                        Log.d(TAG, "run: Loaded Data");
                                    }
                                };
                                handler.post(r);
                            }
                        });
                        recyclerView.setAdapter(orgAdapter);
                    } else {
                        orgNeeds.remove(orgNeeds.size() - 1);
                        orgNeeds.addAll(tempOrgNeeds);
                        orgAdapter.notifyItemInserted(orgNeeds.size());
                        if (!noMoreDataToLoad)
                            orgAdapter.setLoaded();
                    }
                }
            }

        }
    }

    class GetDonorNeedDetails extends AsyncTask {
         public LoadNextDetails nextNeedDetails;
         public boolean isFirstTime;
         public GetDonorNeedDetails(boolean isFirstTime)
        {
            this.isFirstTime=isFirstTime;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(tempDonorNeeds!=null)
                tempDonorNeeds.clear();
            if(isFirstTime) {
                client = new DefaultHttpClient();
                progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] params)
        {
            Log.d(TAG, "doInBackground: "+(isFirstTime?"Firstime":"Secondtme")+nextUrl);
            if(nextUrl!=null) {
                response = Connectivity.makeGetRequest(nextUrl, client, Connectivity.getAuthToken(getActivity(), Connectivity.Donor_Token));
                if (response != null) {
                    if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                        try {
                            jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                            JSONArray results = jsonObject.getJSONArray("results");
                            if (!jsonObject.isNull("next"))
                                nextUrl = jsonObject.getString("next");
                            else {
                                nextUrl = null;
                                noMoreDataToLoad = true;
                            }
                            Gson gson = new Gson();
                            tempDonorNeeds = gson.fromJson(results.toString(), new TypeToken<List<NeedDetails>>() {
                            }.getType());
                            Log.d(TAG, "doInBackground: Temp Item" + tempDonorNeeds.size());
                            if (isFirstTime)
                                donorNeeds.addAll(tempDonorNeeds);
                            Log.d("Results", donorNeeds.size() + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else
                        Log.d("CAll ", "Response null");
                }
                Log.e(TAG,"HTTP response is null ",new NullPointerException());
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
                        donorAdapter = new DonorNeedViewAdapter(getActivity(), donorNeeds, recyclerView,HomeFragment.this.getActivity());
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                        recyclerView.addItemDecoration(dividerItemDecoration);
                        recyclerView.setAdapter(donorAdapter);
                        donorAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                Handler handler = new Handler();
                                Log.d(TAG, "onLoadMore: Called");
                                final Runnable r = new Runnable() {
                                    public void run() {
                                        donorNeeds.add(null);
                                        donorAdapter.notifyItemInserted(donorNeeds.size() - 1);
                                        startNeedAsyncTask(false);
                                        Log.d(TAG, "run: Loaded Data");
                                    }
                                };
                                handler.post(r);
                            }
                        });
                    }
                    else
                    {
                        donorNeeds.remove(donorNeeds.size()-1);
                        donorNeeds.addAll(tempDonorNeeds);
                        donorAdapter.notifyItemInserted(donorNeeds.size());
                        if(!noMoreDataToLoad)
                            donorAdapter.setLoaded();
                    }
                    LandingPage landingPage= (LandingPage) getActivity();
                    if(landingPage!=null)
                        landingPage.setNeedDetailsinActivity(donorNeeds);
                }
            }

            else
                Log.e(TAG, "onPostExecute: Http response is null", new NullPointerException());
        }
    }

}
