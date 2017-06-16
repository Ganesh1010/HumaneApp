package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class DonationListFragment extends Fragment {

    private static final String TAG = "DonationListFragment";
    Activity activity;
    RecyclerView donationListRecyclerView;
    ArrayList<DonationDetailsReadOnly> donationDetailsReadOnlyList;
    DonationDetailsReadOnly donationDetailsReadOnly;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.activity = getActivity();
        if (this.activity != null) {
            View inflatedView = inflater.inflate(R.layout.fragment_donation_list, container, false);
            donationListRecyclerView = (RecyclerView) inflatedView.findViewById(R.id.donation_details_recycler_view_donation_list_fragment);
            new ShowDonationsList().execute();
            return inflatedView;
        } else {
            Log.e(TAG, "onCreateView: ", new NullPointerException());
            return null;
        }
    }

    class ShowDonationsList extends AsyncTask {

        ProgressDialog progressDialog;
        HttpClient httpClient;
        HttpResponse httpResponse;
        DonationListAdapter donationListAdapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Donation details");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            httpClient = new DefaultHttpClient();
            String coordinatorToken = Connectivity.getAuthToken(activity, Connectivity.Donor_Token);
            httpResponse = Connectivity.makeGetRequest(RestAPIURL.donationList, httpClient, coordinatorToken);
            String JSONString = Connectivity.getJosnFromResponse(httpResponse);
            Log.d(TAG, "doInBackground: " + JSONString);
            Gson gson = new Gson();
            donationDetailsReadOnlyList = gson.fromJson(JSONString, new TypeToken<List<DonationDetailsReadOnly>>() {}.getType());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d(TAG, "onPostExecute: List size: " + donationDetailsReadOnlyList.size());
            donationListAdapter = new DonationListAdapter(activity, donationDetailsReadOnlyList);
            donationListRecyclerView.setHasFixedSize(true);
            donationListRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            donationListRecyclerView.setItemAnimator(new DefaultItemAnimator());

            donationListRecyclerView.setAdapter(donationListAdapter);
            progressDialog.cancel();
        }
    }

}
