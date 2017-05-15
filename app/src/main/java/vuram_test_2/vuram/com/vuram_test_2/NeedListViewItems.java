package vuram_test_2.vuram.com.vuram_test_2;

/**
 * Created by akshayagr on 21-03-2017.
 */

public class NeedListViewItems {

    String itemName,gender,quantity,donorName;


    public NeedListViewItems(String donorName) {
        this.donorName = donorName;

    }

    public NeedListViewItems(String itemName, String gender, String quantity) {
        this.itemName = itemName;
        this.gender = gender;
        this.quantity = quantity;
    }

    public NeedListViewItems(String itemName, String gender, String quantity, String donorName) {
        this.itemName = itemName;
        this.gender = gender;
        this.quantity = quantity;
        this.donorName = donorName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }



    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

}

