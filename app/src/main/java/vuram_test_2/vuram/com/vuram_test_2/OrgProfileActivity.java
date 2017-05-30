package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class OrgProfileActivity extends AppCompatActivity {

    private static final String TAG = "OrgProfileActivity.java";
    TextView orgName, orgCityName, orgMobileNo, orgEmail, orgPeopleCount, orgType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_profile);

        // Embedding Map fragment
        Fragment myMapFragment = new MyMapFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_holder_linear_layout_org_profile, myMapFragment).commit();

        // Defining the objects to the views
        orgName = (TextView) findViewById(R.id.org_name_textview_org_profile);
        orgCityName = (TextView) findViewById(R.id.org_city_name_testview_org_profile);
        orgMobileNo = (TextView) findViewById(R.id.org_mobile_no_textview_org_profile);
        orgEmail = (TextView) findViewById(R.id.org_email_textview_org_profile);
        orgPeopleCount = (TextView) findViewById(R.id.org_people_count_textview_org_profile);
        orgType = (TextView) findViewById(R.id.org_type_textview_org_profile);

        new FetchOrgDetailsTask().execute();
    }

    class FetchOrgDetailsTask extends AsyncTask {

        ProgressDialog progressDialog;
        HttpClient httpClient;
        HttpResponse httpResponse;
        OrganisationDetails organisationDetails;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrgProfileActivity.this);
            progressDialog.show();

        }

        @Override
        protected Object doInBackground(Object[] params) {
            httpClient = new DefaultHttpClient();
            String coordinatorToken = Connectivity.getAuthToken(OrgProfileActivity.this, Connectivity.Donor_Token);
            httpResponse = Connectivity.makeGetRequest(RestAPIURL.getOrgDetails,httpClient,coordinatorToken);
            String JSONString = Connectivity.getJosnFromResponse(httpResponse);
            Log.d(TAG, "doInBackground: " + JSONString);
            Gson gson = new Gson();
            ArrayList<OrganisationDetails> organisationDetailsList = gson.fromJson(JSONString, new TypeToken<List<OrganisationDetails>>() {}.getType());
            if (organisationDetailsList != null) {
                if (organisationDetailsList.size() > 0) {
                    organisationDetails = organisationDetailsList.get(0);
                } else {
                    Log.e(TAG, "doInBackground: Organisation list is empty", new ArrayIndexOutOfBoundsException());
                }
            } else {
                Log.e(TAG, "doInBackground: Organisation data not found", new NullPointerException());
            }
            return organisationDetails;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.cancel();

            orgName.setText(organisationDetails.getOrg_name());
            orgCityName.setText(organisationDetails.getAddress());
            orgMobileNo.setText(organisationDetails.getMobile());
            orgEmail.setText(organisationDetails.getEmail());
            orgType.setText(organisationDetails.getOrg_type());
            orgPeopleCount.setText(100 + "");
        }
    }
}
