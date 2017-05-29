package vuram_test_2.vuram.com.vuram_test_2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DonationConfirmationActivity extends AppCompatActivity {

    public LinearLayout mainItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_confirmation);

        mainItemList= (LinearLayout) findViewById(R.id.mainItemNeedList);

        for(int i=0;i<2;i++)
        {
            ImageView mainItem=new ImageView(getApplicationContext());
            mainItem.setBackgroundResource(R.drawable.ic_cloth_black);
            mainItemList.addView(mainItem);
            LinearLayout.LayoutParams margins = new LinearLayout.LayoutParams(mainItem.getLayoutParams());
            margins.rightMargin = 20;
            margins.leftMargin = 20;
            mainItem.setLayoutParams(margins);
            mainItem.setMinimumHeight(100);
            mainItem.setMaxHeight(200);
            mainItem.setMinimumWidth(100);
            mainItem.setMaxWidth(200);

            mainItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DonationConfirmationActivity.this);
                    builder.setTitle("Item Details");
                    LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.donation_confirmation_item_display, null);
                    RecyclerView itemToDisplay= (RecyclerView) dialogView.findViewById(R.id.itemToDisplay);
                    itemToDisplay.setAdapter(new DonationConfirmationItemDisplayAdapter());
                    builder.setView(dialogView);
                    itemToDisplay.setAdapter(new DonationConfirmationItemDisplayAdapter());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }
}
