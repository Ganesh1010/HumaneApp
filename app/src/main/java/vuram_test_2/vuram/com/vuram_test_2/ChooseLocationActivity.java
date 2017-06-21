package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;
import static vuram_test_2.vuram.com.vuram_test_2.R.id.detect_my_location_imagebutton;

public class ChooseLocationActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    final String TAG = "ChooseLocationActivity";
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    TextView detectMyLocationTextView;
    ImageButton detectMyLocationImageButton;
    SearchView searchView;
    ArrayList<String> citiesList;
    ChooseLocationAdapter chooseLocationAdapter;
    RecyclerView citiesNameRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        /* Detect My Location */
        detectMyLocationImageButton = (ImageButton) findViewById(R.id.detect_my_location_imagebutton);
        detectMyLocationImageButton.setOnClickListener(ChooseLocationActivity.this);
        detectMyLocationTextView = (TextView) findViewById(R.id.detect_my_location_textview);
        detectMyLocationTextView.setOnClickListener(ChooseLocationActivity.this);

        /* Recycler View */
        citiesNameRecyclerView = (RecyclerView) findViewById(R.id.cities_name_recyclerview_choose_locaation);
        citiesNameRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /* Cities List */
        citiesList = new ArrayList<>();
        citiesList.add("Chennai");
        citiesList.add("Coimbatore");
        citiesList.add("Coin");
        citiesList.add("Bangalore");

        /* Displaying the cities */
        displayCities(citiesList);

        /* searchView */
        searchView = (SearchView) findViewById(R.id.city_search_view_choose_location);
        searchView.setOnClickListener(ChooseLocationActivity.this);
        searchView.setOnQueryTextListener(ChooseLocationActivity.this);
    }

    private void displayCities(ArrayList<String> citiesList) {
        /* Adapting the complete cities list to the Recycler View */
        chooseLocationAdapter = new ChooseLocationAdapter(this, citiesList);
        citiesNameRecyclerView.setAdapter(chooseLocationAdapter);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case detect_my_location_imagebutton:
            case R.id.detect_my_location_textview:
                if (new GPSTracker(ChooseLocationActivity.this).canGetLocation()) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(ChooseLocationActivity.this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ChooseLocationActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        } else {
                            setCurrentLocation();
                            //new FetchLocationDetailsTask().execute();
                        }
                    } else {
                        setCurrentLocation();
                        //new FetchLocationDetailsTask().execute();
                    }
                } else {
                    new GPSTracker(ChooseLocationActivity.this).showSettingsAlert();
                }
                break;

            case R.id.city_search_view_choose_location:
                searchView.setIconified(false);
                break;
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<String> queriedCitiesList = new ArrayList<String>();
        for (int i=0; i<citiesList.size(); i++) {
            String cityName = citiesList.get(i);
            if (cityName.toLowerCase().startsWith(newText.toLowerCase())) {
                queriedCitiesList.add(cityName);
                Log.d(TAG, "populateAdapter: City Added: " + cityName);
            }
        }
        Log.d(TAG, "onQueryTextChange: Total City Count:" + queriedCitiesList.size());

        /* Displaying new queried list */
        displayCities(queriedCitiesList);
        return true;
    }

    private void setCurrentLocation() {
        GPSTracker gpsTracker = new GPSTracker(ChooseLocationActivity.this);
        String cityName = gpsTracker.getLocality(ChooseLocationActivity.this);
        if (cityName != null) {
            Toast.makeText(ChooseLocationActivity.this,"cities : "+cityName,Toast.LENGTH_SHORT).show();
            HomeActivity.locationName = cityName;
            OrganisationLandingPage.locationName = cityName;
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLocationActivity.this);
            builder.setTitle("Alert !");
            builder.setMessage("Unable to fetch location details");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    class FetchLocationDetailsTask extends AsyncTask {

        String cityName;
        ProgressDialog progressDialog;
        GPSTracker gpsTracker;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ChooseLocationActivity.this);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            gpsTracker = new GPSTracker(ChooseLocationActivity.this);
            cityName = gpsTracker.getLocality(ChooseLocationActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.cancel();
            if (cityName != null) {
                HomeActivity.locationName = cityName;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLocationActivity.this);
                builder.setTitle("Alert !");
                builder.setMessage("Unable to fetch location details");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            finish();
        }
    }
}