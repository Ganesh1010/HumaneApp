package vuram_test_2.vuram.com.vuram_test_2;


public class NeedListViewItems {

    String itemName,donorName;
    int quantity,itemId,donatedId;


    public NeedListViewItems(int itemId, int quantity) {
     //   this.itemName = itemName;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public NeedListViewItems(int donatedId, String donorName,int quantity) {
      //  this.itemName = itemName;
        this.donatedId = donatedId;
        this.quantity = quantity;
        this.donorName = donorName;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

}

