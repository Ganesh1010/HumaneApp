package vuram_test_2.vuram.com.vuram_test_2;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button editButton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_org_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        // Back Arrow
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorTextIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Embedding Map fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
       // fragmentTransaction.add(R.id.map_holder_linear_layout_org_profile, myMapFragment).commit();

        // Defining the objects to the views
        orgName = (TextView) findViewById(R.id.org_name_textview_org_profile);
        orgCityName = (TextView) findViewById(R.id.org_city_name_testview_org_profile);
        orgMobileNo = (TextView) findViewById(R.id.org_mobile_no_textview_org_profile);
        orgEmail = (TextView) findViewById(R.id.org_email_textview_org_profile);
        orgPeopleCount = (TextView) findViewById(R.id.org_people_count_textview_org_profile);
        orgType = (TextView) findViewById(R.id.org_type_textview_org_profile);

        new PopulatingTask().execute();
    }

    class PopulatingTask extends AsyncTask {

        ProgressDialog progressDialog;
        HttpClient httpClient;
        HttpResponse httpResponse;
        OrganisationDetails organisationDetails;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrgProfileActivity.this);
            progressDialog.setMessage("Loading Organisation details");
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

            orgName.setText(organisationDetails.getOrg_name());
            orgCityName.setText(organisationDetails.getAddress());
            orgMobileNo.setText(organisationDetails.getPhone());
            orgEmail.setText(organisationDetails.getEmail());

            /* loading org type details */
            int orgTypeNo = organisationDetails.getOrg_type();
            Log.d(TAG, "onPostExecute: Received Org Type No: " + orgTypeNo);
            OrgTypeLookUpDetails orgTypeLookUpDetails = new DatabaseHelper(OrgProfileActivity.this).getOrgTypeLookUpDetails(orgTypeNo);
            if (orgTypeLookUpDetails != null) {
                orgType.setText(orgTypeLookUpDetails.getOrgTypeName());
            } else {
                orgType.setText("Unknown type");
            }

            orgPeopleCount.setText(Integer.toString(organisationDetails.getPeople_count()));

            editButton = (Button) findViewById(R.id.edit_button_org_profile);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrgProfileActivity.this, EditOrgProfileActivity.class);
                    startActivity(intent);
                }
            });

            progressDialog.cancel();
        }
    }
}
