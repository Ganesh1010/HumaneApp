package vuram_test_2.vuram.com.vuram_test_2;


import java.util.Date;
import java.util.List;

public class DonationDetailsReadOnly {
    int donation_id;
    List<DonatedItemDetails> donateditems;
    String user,user_name,mobile;
    boolean is_registereduser;
    Date donated_at;

    public int getDonation_id() {
        return donation_id;
    }

    public void setDonation_id(int donation_id) {
        this.donation_id = donation_id;
    }

    public List<DonatedItemDetails> getDonateditems() {
        return donateditems;
    }

    public void setDonateditems(List<DonatedItemDetails> donateditems) {
        this.donateditems = donateditems;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean is_registereduser() {
        return is_registereduser;
    }

    public void setIs_registereduser(boolean is_registereduser) {
        this.is_registereduser = is_registereduser;
    }

    public Date getDonated_at() {
        return donated_at;
    }

    public void setDonated_at(Date donated_at) {
        this.donated_at = donated_at;
    }

    public DonatedItemDetails getDonatedItemDetails() {
        return donatedItemDetails;
    }

    public void setDonatedItemDetails(DonatedItemDetails donatedItemDetails) {
        this.donatedItemDetails = donatedItemDetails;
    }

    DonatedItemDetails donatedItemDetails;

    public DonationDetailsReadOnly(DonatedItemDetails donatedItemDetails, String user) {
        this.donatedItemDetails = donatedItemDetails;
        this.user = user;
    }

}
