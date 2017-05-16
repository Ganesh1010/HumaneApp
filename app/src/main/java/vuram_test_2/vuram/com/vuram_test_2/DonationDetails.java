package vuram_test_2.vuram.com.vuram_test_2;


import java.util.ArrayList;
import java.util.List;

public class DonationDetails {

    int donation_id;
    List<DonatedItemDetails> donateditems;
    String user;

    public DonationDetails() {

        donateditems = new ArrayList<>();
    }
}
