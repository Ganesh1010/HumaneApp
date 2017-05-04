package vuram_test_2.vuram.com.vuram_test_2;

import java.util.Date;

/**
 * Created by ganeshrajam on 19-04-2017.
 */

public class NeedItemDetails {
    int id, item_type_id, quantity;
    String gender;
    boolean is_fulfilled;
    Date deadline;
    public String print()
    {
        return ("Item Type : "+item_type_id+
        "gender : "+gender);
    }
}
