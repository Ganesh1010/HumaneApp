package vuram_test_2.vuram.com.vuram_test_2;


import java.util.Date;

/**
 * Created by ganeshrajam on 17-04-2017.
 */

public class RegisterDetails {
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    String mobile;
    int country;
    Date dob;
    OrganisationDetails org;

    public OrganisationDetails getOrg() {
        return org;
    }

    public void setOrg(OrganisationDetails org) {
        this.org = org;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }


}
