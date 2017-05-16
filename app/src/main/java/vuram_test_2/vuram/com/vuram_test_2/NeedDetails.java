package vuram_test_2.vuram.com.vuram_test_2;

import java.util.ArrayList;
import java.util.List;

public class NeedDetails {
    int need_id;
    boolean is_fulfilled;
    String latitude, longitude;

    public NeedItemDetails getItems() {
        return items;
    }

    //List<NeedItemDetails> items;
    NeedItemDetails items;

    public void setItems(NeedItemDetails items) {
        this.items = items;
    }

    List<DonationDetails> donations;
    OrganisationDetails org;

    public int getNeed_id() {
        return need_id;
    }

    public void setNeed_id(int need_id) {
        this.need_id = need_id;
    }

    public boolean is_fulfilled() {
        return is_fulfilled;
    }

    public void setIs_fulfilled(boolean is_fulfilled) {
        this.is_fulfilled = is_fulfilled;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public List<DonationDetails> getDonations() {
        return donations;
    }

    public void setDonations(List<DonationDetails> donations) {
        this.donations = donations;
    }

    public OrganisationDetails getOrg() {
        return org;
    }

    public void setOrg(OrganisationDetails org) {
        this.org = org;
    }

    NeedDetails() {
        //items = new ArrayList<>();
        items=new NeedItemDetails();
        donations = new ArrayList<>();
    }

    public OrganisationDetails getOrganisationDetails(OrganisationDetails organisationDetails) {
        return organisationDetails;
    }

    public void setOrganisationDetails(OrganisationDetails organisationDetails)
    {
        this.org=organisationDetails;
    }

    /*public  String  print()
    {
        return(items.get(0).print());
    }*/
}