package vuram_test_2.vuram.com.vuram_test_2;

import java.util.Date;

/**
 * Created by ganeshrajam on 19-04-2017.
 */

public class  NeedItemDetails {
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

    public int getItem_type_id() {
        return item_type_id;
    }

    public void setItem_type_id(int item_type_id) {
        this.item_type_id = item_type_id;
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

    public boolean is_fulfilled() {
        return is_fulfilled;
    }

    public void setIs_fulfilled(boolean is_fulfilled) {
        this.is_fulfilled = is_fulfilled;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    int need_item_id,donated_amount,donated_and_received_amount, item_type_id, quantity;
    String gender,age;
    boolean is_fulfilled;
    Date deadline;
    public String print()
    {
        return ("Item Type : "+item_type_id+ "gender : "+gender);
    }
}
