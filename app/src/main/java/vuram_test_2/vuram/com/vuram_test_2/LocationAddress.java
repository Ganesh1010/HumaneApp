package vuram_test_2.vuram.com.vuram_test_2;

import java.io.Serializable;

/**
 * Created by rahulk on 6/8/2017.
 */

public class LocationAddress implements Serializable {
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String address;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    double latitude;
    double longitude;

}
