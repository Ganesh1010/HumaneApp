package vuram_test_2.vuram.com.vuram_test_2.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ganeshrajam on 23-05-2017.
 */

public class CommonUI {
    public static void displayCheckoutUI(View v, int itemsCount, final Context context)
    {
        Snackbar.make(v, itemsCount+" Item(s) added", Snackbar.LENGTH_LONG)
                .setAction("Donate", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Perform anything for the action selected
                        Toast.makeText(context,"Donate Clicked",Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }
}
