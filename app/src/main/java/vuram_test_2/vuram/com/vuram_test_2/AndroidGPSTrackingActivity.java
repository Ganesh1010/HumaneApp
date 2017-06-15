package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AndroidGPSTrackingActivity {

    public String TAG = "AndroidGPSTrackingActivity";

    GPSTrackerSample gps;
    LocationAddress locationAddress, location;
    public List<Address> fetchedAddress = new ArrayList<>();
    public Geocoder geocoder;
    double latitude;
    double longitude;
    String obtainedAddress;
    Intent intent;
    Activity activity;

    public AndroidGPSTrackingActivity(Activity activity)
    {
        this.activity=activity;
    }

    public LocationAddress getAddress() {
        gps = new GPSTrackerSample(activity);

        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Toast.makeText(activity, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

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
                    String knownName = fetchedAddress.get(0).getFeatureName();// Only if available else return NULL

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

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return locationAddress;


        } else
            gps.showSettingsAlert();
        return null;
    }
}
