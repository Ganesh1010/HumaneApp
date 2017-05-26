package vuram_test_2.vuram.com.vuram_test_2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class ChooseLocationActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    ImageButton detectLocationImageButton;
    SearchView searchView;
    ArrayList<String> citiesList;
    ChooseLocationAdapter chooseLocationAdapter;
    RecyclerView citiesNameRecyclerView;

    private static final String[] SUGGESTIONS = { "Chennai", "Coimbatore", "Bangalore" };
    SimpleCursorAdapter simpleCursorAdapter;

    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        detectLocationImageButton = (ImageButton) findViewById(R.id.detectLocationImageButton);
        detectLocationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new GPSTracker(ChooseLocationActivity.this).getIsGPSTrackingEnabled()) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(ChooseLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ChooseLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        } else {
                            HomeActivity.locationName = new GPSTracker(ChooseLocationActivity.this).getLocality(ChooseLocationActivity.this);
                            finish();
                        }
                    } else {
                        HomeActivity.locationName = new GPSTracker(ChooseLocationActivity.this).getLocality(ChooseLocationActivity.this);
                        finish();
                    }
                } else {
                    new GPSTracker(ChooseLocationActivity.this).showSettingsAlert();
                }
            }
        });

        /* searchView */
        searchView = (SearchView) findViewById(R.id.city_search_view_choose_location);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] { R.id.city_search_view_choose_location };
        simpleCursorAdapter = new SimpleCursorAdapter(ChooseLocationActivity.this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        searchView.setSuggestionsAdapter(simpleCursorAdapter);
        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });

        citiesList =  new ArrayList<String>();
        citiesList.add("Chennai");
        citiesList.add("Coimbatore");
        citiesList.add("Bangalore");

        chooseLocationAdapter = new ChooseLocationAdapter(this, citiesList);
        citiesNameRecyclerView = (RecyclerView) findViewById(R.id.cities_name_recyclerview_choose_locaation);
        citiesNameRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        citiesNameRecyclerView.setAdapter(chooseLocationAdapter);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS[i]});
        }
        simpleCursorAdapter.changeCursor(c);
        simpleCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                MyAddress myAddress = new MyAddress(ChooseLocationActivity.this, latitude, longitude);
                HomeActivity.locationName = myAddress.getCityName();
                finish();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
