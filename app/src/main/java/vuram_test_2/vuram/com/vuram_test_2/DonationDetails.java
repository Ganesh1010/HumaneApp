package vuram_test_2.vuram.com.vuram_test_2;

import java.util.ArrayList;
import java.util.List;

public class DonationDetails {

    int donation_id;
    List<DonatedItemDetails> donated_items;
    String user;


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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
