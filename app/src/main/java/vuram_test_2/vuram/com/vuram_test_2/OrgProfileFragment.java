package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
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

public class OrgProfileFragment extends Fragment implements OnMapReadyCallback {

    Activity activity;
    private static final String TAG = "OrgProfileFragment";
    TextView orgName, orgCityName, orgMobileNo, orgEmail, orgPeopleCount, orgType;
    Button editButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.activity = getActivity();
        if (this.activity != null) {
            View inflatedView = inflater.inflate(R.layout.fragment_org_profile, container, false);

            // Embedding Map fragment
            MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment_org_profile_fragment);
            if (mapFragment == null) {
                Log.e(TAG, "onCreateView: ", new NullPointerException());
            }
            mapFragment.getMapAsync(this);

            // Defining the objects to the views
            orgName = (TextView) inflatedView.findViewById(R.id.org_name_textview_org_profile_fragment);
            orgCityName = (TextView) inflatedView.findViewById(R.id.org_city_name_testview_org_profile_fragment);
            orgMobileNo = (TextView) inflatedView.findViewById(R.id.org_mobile_no_textview_org_profile_fragment);
            orgEmail = (TextView) inflatedView.findViewById(R.id.org_email_textview_org_profile_fragment);
            orgPeopleCount = (TextView) inflatedView.findViewById(R.id.org_people_count_textview_org_profile_fragment);
            orgType = (TextView) inflatedView.findViewById(R.id.org_type_textview_org_profile_fragment);
            MapView mapView;
//        mapView = (MapView) findViewById(R.id.mapImageView);
            Log.d(TAG, "onMapReady : On Create");
            // Gets to GoogleMap from the MapView and does initialization stuff
            //      mapView.getMapAsync(this);

            new PopulatingTask().execute();

            return inflatedView;
        } else {
            Log.e(TAG, "onCreateView: ", new NullPointerException());
            return null;
        }
    }

    @Override
    public void onMapReady(GoogleMap Map) {
        if (ActivityCompat.checkSelfPermission(this.activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
    }

    class PopulatingTask extends AsyncTask {

        ProgressDialog progressDialog;
        HttpClient httpClient;
        HttpResponse httpResponse;
        OrganisationDetails organisationDetails;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading Organisation details");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            httpClient = new DefaultHttpClient();
            String coordinatorToken = Connectivity.getAuthToken(activity, Connectivity.Donor_Token);
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
            OrgTypeLookUpDetails orgTypeLookUpDetails = new DatabaseHelper(activity).getOrgTypeLookUpDetails(orgTypeNo);
            if (orgTypeLookUpDetails != null) {
                orgType.setText(orgTypeLookUpDetails.getOrgTypeName());
            } else {
                orgType.setText("Unknown type");
            }

            orgPeopleCount.setText(Integer.toString(organisationDetails.getPeople_count()));

            editButton = (Button) activity.findViewById(R.id.edit_button_org_profile_fragment);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, EditOrgProfileActivity.class);
                    startActivity(intent);
                }
            });

            progressDialog.cancel();
        }
    }
}