package vuram_test_2.vuram.com.vuram_test_2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,GoogleMap.OnMyLocationButtonClickListener {

    public Geocoder geocoder;
    public static double latitude;
    public static double longitude;
    public double cur_latitude;
    public double cur_longitude;
    public List<Address> addresses = null;
    public GoogleMap mGoogleMap;
    public SupportMapFragment mapFrag;
    public LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    public LatLng location;
    public Marker mCurrLocationMarker;
    public Button setLocation;
    public ProgressDialog progressDialog;
    public LocationAddress locationAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog=new ProgressDialog(this);

        geocoder = new Geocoder(this, Locale.getDefault());
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        setLocation= (Button) findViewById(R.id.setLocation);
        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getLocation().execute();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMyLocationButtonClickListener(this);
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                location = new LatLng(latitude, longitude);
                mCurrLocationMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, latLng.longitude)).draggable(true).visible(true).title(getAddress(latitude, longitude).getAddress()));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
            }
        });

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location locations) {
        System.out.println("location changed");
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(locations.getLatitude(), locations.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        cur_latitude = locations.getLatitude();
        cur_longitude = locations.getLongitude();

        latitude=cur_latitude;
        longitude=cur_longitude;

        location = new LatLng(cur_latitude, cur_longitude);
        mCurrLocationMarker = mGoogleMap.addMarker(new MarkerOptions().position(location).title(getAddress(cur_latitude, cur_longitude).getAddress()));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this).setTitle("Location Permission Needed").setMessage("This app needs the Location permission, please accept to use location functionality").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(MapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                }).create().show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public LocationAddress getAddress(double latitude, double longitude) {
        StringBuffer Address = new StringBuffer();
        locationAddress=new LocationAddress();
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String area = addresses.get(0).getSubLocality();
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();// Only if available else return NULL

                if (address != null)
                    Address.append(address + ",");
                if (area != null)
                    Address.append(area + ",");
                if (city != null)
                    Address.append(city + ",");
                if (state != null)
                    Address.append(state + ",");
                if (postalCode != null)
                    Address.append(postalCode + ",");
                if (country != null)
                    Address.append(country + ",");
                if (knownName != null && !knownName.equals(address))
                    Address.append(knownName);

                locationAddress.setAddress(Address.toString());
                locationAddress.setLatitude(latitude);
                locationAddress.setLongitude(longitude);

                //Address = address + "," + area + "," + city + "," + state + "," + postalCode + "," + country;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationAddress;
    }

    public void confirmAddress() throws IOException {
        Intent intent = new Intent();
        intent.putExtra("ADDRESS", getAddress(latitude, longitude));
        setResult(2, intent);
        finish();//finishing activity
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_map, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search for your Location...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);// Do not iconify the widget; expand it by defaul

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                onMapSearch(query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onMapSearch(String locations) {
        System.out.println("location searched");
        if(addresses.size()>0)
            addresses.clear();
        //String locations = locationSearch.getText().toString();
        try {
            addresses = geocoder.getFromLocationName(locations, 1);
            Address address = addresses.get(0);
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            latitude=address.getLatitude();
            longitude=address.getLongitude();
            location=new LatLng(latitude,longitude);
            mCurrLocationMarker=mGoogleMap.addMarker(new MarkerOptions().position(location).title(getAddress(latitude,longitude).getAddress()));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,17));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        System.out.println("current location");
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        location = new LatLng(cur_latitude,cur_longitude);
        location = new LatLng(cur_latitude, cur_longitude);
        latitude=cur_latitude;
        longitude=cur_longitude;
        mCurrLocationMarker=mGoogleMap.addMarker(new MarkerOptions().position(location).title(getAddress(cur_latitude,cur_longitude).getAddress()));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,17));

        return true;
    }

    class getLocation extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                confirmAddress();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog= new ProgressDialog(MapActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Getting Address...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            super.onPostExecute(o);
        }
    }
}