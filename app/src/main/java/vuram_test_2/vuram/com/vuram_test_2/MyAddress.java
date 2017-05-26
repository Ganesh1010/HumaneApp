package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by sujithv on 5/24/2017.
 */

public class MyAddress {
    double latitude;
    double longitude;
    String cityName;
    String stateName;
    String countryName;
    Geocoder geocoder;
    Context context;

    public MyAddress(Context context, double latitude, double longitude) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCityName() {
        fetchAddress();
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        fetchAddress();
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryName() {
        fetchAddress();
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void fetchAddress() {
        geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.cityName = addresses.get(0).getAddressLine(0);
        this.stateName = addresses.get(0).getAddressLine(1);
        this.countryName = addresses.get(0).getAddressLine(2);
    }
}
