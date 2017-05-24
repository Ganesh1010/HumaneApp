package vuram_test_2.vuram.com.vuram_test_2;

/**
 * Created by akshayagr on 12-05-2017.
 */

public class DonatedItemDetails {
    public int getDonated_item_id() {
        return donated_item_id;
    }

    public DonatedItemDetails() {
    }

    public DonatedItemDetails(int donated_item_id, int quantity) {
        this.donated_item_id = donated_item_id;
        this.quantity = quantity;
    }

    public void setDonated_item_id(int donated_item_id) {
        this.donated_item_id = donated_item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getNeeditem() {
        return needitem;
    }

    public void setNeeditem(int needitem) {
        this.needitem = needitem;
    }

    public int getDonation_id() {
        return donation_id;
    }

    public void setDonation_id(int donation_id) {
        this.donation_id = donation_id;
    }

    int donated_item_id,quantity,needitem,donation_id;
}
