package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GetCurrentLocation {

    public String TAG = "GetCurrentLocation";
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    GPSTracker gps;
    LocationAddress locationAddress, location;
    public List<Address> fetchedAddress = new ArrayList<>();
    public Geocoder geocoder;
    double latitude;
    double longitude;
    Intent intent;
    Activity activity;
    int locationMode;

    public GetCurrentLocation(Activity activity)
    {
        this.activity=activity;
    }

    public LocationAddress getAddress() {
        gps = new GPSTracker(activity);
        // check if GPS enabled
        if (gps.canGetLocation())
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                try {
                    locationMode = Settings.Secure.getInt(activity.getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                if (locationMode != 3) {
                    Toast.makeText(activity, "Set the Location to High Accuracy", Toast.LENGTH_SHORT).show();
                    gps.showSettingsAlert();
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= 23)
                    {
                        if (ContextCompat.checkSelfPermission(activity,
                                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        }
                        else
                        {
                            locationAddress=getCurrentAddress();
                            return locationAddress;
                        }

                    } else
                    {
                        locationAddress=getCurrentAddress();
                        return locationAddress;
                    }
                }
            }
        }
        else
            gps.showSettingsAlert();
        return null;
    }

    public LocationAddress getCurrentAddress()
    {
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        StringBuffer Address = new StringBuffer();
        locationAddress = new LocationAddress();

        try {
            geocoder = new Geocoder(activity, Locale.getDefault());
            fetchedAddress = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (fetchedAddress != null) {
                String address = fetchedAddress.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String area = fetchedAddress.get(0).getSubLocality();
                String city = fetchedAddress.get(0).getLocality();
                String state = fetchedAddress.get(0).getAdminArea();
                String country = fetchedAddress.get(0).getCountryName();
                String postalCode = fetchedAddress.get(0).getPostalCode();

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
                    Address.append(country);

                locationAddress.setAddress(Address.toString());
                locationAddress.setLatitude(latitude);
                locationAddress.setLongitude(longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationAddress;
    }
}
