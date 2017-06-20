package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class OrganisationNeedViewFragment extends Fragment implements LoadNextDetails {
    private static final String TAG ="Organisation Need View Fragment" ;
    public Activity activity;
    public RecyclerView organisationNeedsRecyclerView;
    public GetOrganisationNeedDetails getOrganisationNeedDetails;   public ArrayList<NeedDetails> orgNeeds = new ArrayList<>();
    public ArrayList<NeedDetails> tempOrgNeeds;
    public String nextUrl=RestAPIURL.orgDetails;public Gson gson;
    public HttpClient client;public ProgressDialog progressDialog=null;
    public JSONObject jsonObject;
    public HttpResponse response;
    public LinearLayoutManager mLinearLayoutManager;
    public boolean noMoreDataToLoad=false;
    public OrgNeedViewAdapter orgAdapter;
    public FloatingActionButton newNeedFloatingActionButton;
    public Fragment fragment = null;FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.activity = getActivity();
        if (this.activity != null) {
            View inflatedView = inflater.inflate(R.layout.fragment_org_need_view, container, false);
            organisationNeedsRecyclerView = (RecyclerView) inflatedView.findViewById(R.id.organisation_needs_recycler_view);
            newNeedFloatingActionButton = (FloatingActionButton)inflatedView.findViewById(R.id.new_need_home_page);
            newNeedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment = new NewNeedActivityFragment();
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_holder_org_landing_page,fragment).commit();
                }
            });
            mLinearLayoutManager=new LinearLayoutManager(getActivity());
            startOrgAsyncTask(true);
            //new GetOrganisationNeedDetails().execute();
            return inflatedView;
        } else {
            Log.e(TAG, "onCreateView: ", new NullPointerException());
            return null;
        }
    }

    @Override
    public void nextURL(String result) {
        if(result.equals("nextOrgDetails"))
            startOrgAsyncTask(false);
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
                        organisationNeedsRecyclerView.setLayoutManager(mLinearLayoutManager);
                        orgAdapter = new OrgNeedViewAdapter(getActivity(), orgNeeds, organisationNeedsRecyclerView);

                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(organisationNeedsRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
                        organisationNeedsRecyclerView.addItemDecoration(dividerItemDecoration);

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
                        organisationNeedsRecyclerView.setAdapter(orgAdapter);
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
    public void startOrgAsyncTask(boolean isFirstTime) {
        getOrganisationNeedDetails = new GetOrganisationNeedDetails(isFirstTime);
        getOrganisationNeedDetails.nextOrgDetails= this;
        getOrganisationNeedDetails.execute();
    }
}
