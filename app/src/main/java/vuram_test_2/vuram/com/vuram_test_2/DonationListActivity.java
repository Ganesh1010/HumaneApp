package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class DonationListActivity extends AppCompatActivity {

    private static final String TAG = "DonationListActivity";
    RecyclerView donationListRecyclerView;
    ArrayList<DonationDetailsReadOnly> donationDetailsReadOnlyList;
    DonationDetailsReadOnly donationDetailsReadOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_list);

        new ShowDonationsList().execute();
    }

    class ShowDonationsList extends AsyncTask {

        ProgressDialog progressDialog;
        HttpClient httpClient;
        HttpResponse httpResponse;
        DonationListAdapter donationListAdapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DonationListActivity.this);
            progressDialog.setMessage("Loading Donation details");
            progressDialog.show();
            donationListRecyclerView = (RecyclerView) findViewById(R.id.donation_details_recycler_view_donation_list);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            httpClient = new DefaultHttpClient();
            String coordinatorToken = Connectivity.getAuthToken(DonationListActivity.this, Connectivity.Donor_Token);
            httpResponse = Connectivity.makeGetRequest(RestAPIURL.donationList,httpClient,coordinatorToken);
            String JSONString = Connectivity.getJosnFromResponse(httpResponse);
            Log.d(TAG, "doInBackground: " + JSONString);
            Gson gson = new Gson();
            donationDetailsReadOnlyList = gson.fromJson(JSONString, new TypeToken<List<DonationDetailsReadOnly>>() {}.getType());
            if (donationDetailsReadOnlyList != null) {
                if (donationDetailsReadOnlyList.size() > 0) {
                    donationDetailsReadOnly = donationDetailsReadOnlyList.get(0);
                } else {
                    Log.e(TAG, "doInBackground: Organisation list is empty", new ArrayIndexOutOfBoundsException());
                }
            } else {
                Log.e(TAG, "doInBackground: Organisation data not found", new NullPointerException());
            }
            return donationDetailsReadOnlyList;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            donationListAdapter = new DonationListAdapter(DonationListActivity.this, donationDetailsReadOnlyList);
            donationListRecyclerView.setAdapter(donationListAdapter);
            progressDialog.cancel();
        }
    }
}
