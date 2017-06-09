package vuram_test_2.vuram.com.vuram_test_2;

import java.util.ArrayList;
import java.util.List;

public class DonationDetails {


    boolean is_registereduser;
    boolean is_received;
    String donated_at;
    int donation_id;
    List<DonatedItemDetails> donated_items;

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

    int need_id;

    public String getdonated_at() {
        return donated_at;
    }

    public void setdonated_at(String donated_at) {
        this.donated_at = donated_at;
    }

    String user;

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

    public UnregisteredUser getUnregisteredUser() {
        return unregisteredUser;
    }

    public void setUnregisteredUser(UnregisteredUser unregisteredUser) {
        this.unregisteredUser = unregisteredUser;
    }

    UnregisteredUser unregisteredUser;


    public DonationDetails() {

        donated_items = new ArrayList<>();
    }

    public int getDonation_id() {
        return donation_id;
    }

    public void setDonation_id(int donation_id) {
        this.donation_id = donation_id;
    }

    public List<DonatedItemDetails> getDonateditems() {
        return donated_items;
    }

    public void setDonateditems(List<DonatedItemDetails> donateditems) {
        this.donated_items = donateditems;

    }

}
