package vuram_test_2.vuram.com.vuram_test_2;

public class DonatedItemDetails {

    int donated_item_id;
    int quantity;
    int donation_id;

    public int getNeed_item_id() {
        return need_item_id;
    }

    public void setNeed_item_id(int need_item_id) {
        this.need_item_id = need_item_id;
    }

    int need_item_id;

    public DonatedItemDetails() {}

    public int getDonated_item_id() {
        return donated_item_id;
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

    public int getDonation_id() {
        return donation_id;
    }

    public void setDonation_id(int donation_id) {
        this.donation_id = donation_id;
    }
}
