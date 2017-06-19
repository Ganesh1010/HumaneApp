package vuram_test_2.vuram.com.vuram_test_2;

import java.util.ArrayList;
import java.util.List;

public class DonationDetails {

    public List<DonatedItemDetails> getDonateditems() {
        return donateditems;
    }

    public void setDonateditems(List<DonatedItemDetails> donateditems) {
        this.donateditems = donateditems;
    }

    List<DonatedItemDetails> donateditems;
    boolean is_registereduser;
    boolean is_received;
    String donated_at;
    int need_id;
    String user;


    public void setIs_registereduser(boolean is_registereduser) {
        this.is_registereduser = is_registereduser;
    }

    public String getDonated_at() {
        return donated_at;
    }

    public void setDonated_at(String donated_at) {
        this.donated_at = donated_at;
    }

    public void setIs_received(boolean is_received) {
        this.is_received = is_received;
    }

    DonatingUserDetails donatingUserDetails;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getNeed_id() {
        return need_id;
    }

    public void setNeed_id(int need_id) {
        this.need_id = need_id;
    }

    public String getdonated_at() {
        return donated_at;
    }

    public void setdonated_at(String donated_at) {
        this.donated_at = donated_at;
    }

    public boolean is_received() {
        return is_received;
    }

    public void setReceived(boolean received) {
        is_received = received;
    }

    public boolean is_registereduser() {
        return is_registereduser;
    }

    public void setRegisteredUser(boolean registeredUser) {
        is_registereduser = registeredUser;
    }

    public DonatingUserDetails getDonatingUserDetails() {
        return donatingUserDetails;
    }

    public void setDonatingUserDetails(DonatingUserDetails donatingUserDetails) {
        this.donatingUserDetails = donatingUserDetails;
    }


}
