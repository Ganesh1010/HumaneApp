package vuram_test_2.vuram.com.vuram_test_2;

/**
 * Created by akshayagr on 12-05-2017.
 */

public class OrganisationDetails
{
    String org_reg_no,org_name,address,email,description,phone;
    int people_count;
    int org_type;
    int countryCode,org_country;
    double latitude,longitude;

    public String getOrg_reg_no() {
        return org_reg_no;
    }

    public void setOrg_reg_no(String org_reg_no) {
        this.org_reg_no = org_reg_no;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getOrg_country() {
        return org_country;
    }

    public void setOrg_country(int org_country) {
        this.org_country = org_country;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String mobile) {
        this.phone = mobile;
    }

    public int getPeople_count() {
        return people_count;
    }

    public void setPeople_count(int people_count) {
        this.people_count = people_count;
    }

    public int getOrg_type() {
        return org_type;
    }

    public void setOrg_type(int org_type) {
        this.org_type = org_type;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

}
