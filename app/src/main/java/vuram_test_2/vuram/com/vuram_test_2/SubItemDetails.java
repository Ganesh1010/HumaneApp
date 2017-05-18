package vuram_test_2.vuram.com.vuram_test_2;

/**
 * Created by sujithv on 5/17/2017.
 */

public class SubItemDetails {
    int item_id;
    String item_name;
    int main_item_id;

    public int getSubItemCode() {
        return item_id;
    }

    public void setSubItemCode(int subItemCode) {
        this.item_id = subItemCode;
    }

    public String getSubItemName() {
        return item_name;
    }

    public void setSubItemName(String subItemName) {
        this.item_name = subItemName;
    }

    public int getMainItemCode() {
        return main_item_id;
    }

    public void setMainItemCode(int mainItemCode) {
        this.main_item_id = mainItemCode;
    }
}
