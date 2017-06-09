package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static vuram_test_2.vuram.com.vuram_test_2.MapActivity.latitude;
import static vuram_test_2.vuram.com.vuram_test_2.MapActivity.longitude;

public class OrgProfileActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "OrgProfileActivity.java";
    TextView orgName, orgCityName, orgMobileNo, orgEmail, orgPeopleCount, orgType;
    Button editButton;
    Toolbar toolbar;
    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_org_profile);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("");

        // Back Arrow
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorTextIcons), PorterDuff.Mode.SRC_ATOP);
        //getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Embedding Map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        // Defining the objects to the views
        orgName = (TextView) findViewById(R.id.org_name_textview_org_profile);
        orgCityName = (TextView) findViewById(R.id.org_city_name_testview_org_profile);
        orgMobileNo = (TextView) findViewById(R.id.org_mobile_no_textview_org_profile);
        orgEmail = (TextView) findViewById(R.id.org_email_textview_org_profile);
        orgPeopleCount = (TextView) findViewById(R.id.org_people_count_textview_org_profile);
        orgType = (TextView) findViewById(R.id.org_type_textview_org_profile);
        MapView mapView;
//        mapView = (MapView) findViewById(R.id.mapImageView);
        Log.d(TAG, "onMapReady : On Create");
        // Gets to GoogleMap from the MapView and does initialization stuff
        //      mapView.getMapAsync(this);

        new PopulatingTask().execute();
    }

    @Override
    public void onMapReady(GoogleMap Map) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Map.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        latitude = 26.78;
        longitude = 72.56;
        Log.d(TAG, "onMapReady: "+latitude);
        Map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude)).title("My Home")
                .snippet("Home Address"));
        // For zooming automatically to the Dropped PIN Location
        Map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                latitude, longitude), 12.0f));
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
