package vuram_test_2.vuram.com.vuram_test_2;

public class  NeedItemDetails {

    int need_item_id;
    int donated_amount;
    int donated_and_received_amount;
    boolean is_fulfilled;

    int item_type_id;
    int sub_item_type_id;
    int quantity;
    String gender;
    String age;
    String deadline;

    public NeedItemDetails() {
    }

    public NeedItemDetails(int need_item_id, int quantity) {

        this.need_item_id = need_item_id;
        this.quantity = quantity;
    }

    public int getNeed_item_id() {
        return need_item_id;
    }

    public void setNeed_item_id(int need_item_id) {
        this.need_item_id = need_item_id;
    }

    public int getDonated_amount() {
        return donated_amount;
    }

    public void setDonated_amount(int donated_amount) {
        this.donated_amount = donated_amount;
    }

    public int getDonated_and_received_amount() {
        return donated_and_received_amount;
    }

    public void setDonated_and_received_amount(int donated_and_received_amount) {
        this.donated_and_received_amount = donated_and_received_amount;
    }

    public boolean is_fulfilled() {
        return is_fulfilled;
    }

    public void setIs_fulfilled(boolean is_fulfilled) {
        this.is_fulfilled = is_fulfilled;
    }

    public int getItem_type_id() {
        return item_type_id;
    }

    public void setItem_type_id(int item_type_id) {
        this.item_type_id = item_type_id;
    }

    public int getSub_item_type_id() {
        return sub_item_type_id;
    }

    public void setSub_item_type_id(int sub_item_type_id) {
        this.sub_item_type_id = sub_item_type_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String print()
    {
        return ("Item Type : "+item_type_id+ "gender : "+gender);
    }
}
