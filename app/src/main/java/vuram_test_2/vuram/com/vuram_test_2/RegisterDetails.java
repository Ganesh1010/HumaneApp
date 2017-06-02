package vuram_test_2.vuram.com.vuram_test_2;


/**
 * Created by ganeshrajam on 17-04-2017.
 */

public class RegisterDetails {
    String mobile;
    int country;
    OrganisationDetails org;
    boolean isCoordinator;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public void setCoordinator(boolean coordinator) {
        isCoordinator = coordinator;
    }

}