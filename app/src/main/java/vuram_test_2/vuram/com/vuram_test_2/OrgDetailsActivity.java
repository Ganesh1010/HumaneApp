package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import eu.fiskur.simpleviewpager.ImageResourceLoader;
import eu.fiskur.simpleviewpager.SimpleViewPager;

public class OrgDetailsActivity extends AppCompatActivity  {
    Button bt1;
    ImageButton imageButton;
    static int count=0;
    static Context context;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_details);
        imageButton=(ImageButton)findViewById(R.id.back_home);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        context = getBaseContext();

        bt1 = (Button) findViewById(R.id.donate_donor_org);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == bt1)

                {
                    Intent intent = new Intent(OrgDetailsActivity.this, DetailsOfQuantitySelected.class);
                    startActivity(intent);
                }
            }
        });
        final SimpleViewPager simpleViewPager = (SimpleViewPager) findViewById(R.id.simple_view_pager_donor_org);
        int[] resourceIds = new int[]{
                R.drawable.ngo,
                R.drawable.ngo,
                R.drawable.ngo,
                R.drawable.ngo };

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_donor_org);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel>();
        for (int i = 0; i < MyData.nameArray.length; i++) {
            data.add(new DataModel(
                    MyData.drawableArray[i],
                    MyData.nameArray[i],
                    MyData.requestarray[i],
                    MyData.donatedarray[i],
                    MyData.id_[i]

            ));
        }

        removedItems = new ArrayList<Integer>();
        myOnClickListener = new MyOnClickListener(this);
        adapter = new ItemDetailsAdapter(data);
        recyclerView.setAdapter(adapter);


        simpleViewPager.setImageIds(resourceIds, new ImageResourceLoader() {
            @Override
            public void loadImageResource(ImageView imageView, int i) {
                imageView.setImageResource(i);
            }
        });

        int indicatorColor = Color.parseColor("#ffffff");
        int selectedIndicatorColor = Color.parseColor("#8BC34A");
        simpleViewPager.showIndicator(indicatorColor, selectedIndicatorColor);
    }



    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;
        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.layout_select_quantity);
            dialog.setTitle("Enter The Quantity");


            final TextView value=(TextView)dialog.findViewById(R.id.number_custom);

            Button increment=(Button) dialog.findViewById(R.id.increment_custom);
            Button decrement=(Button) dialog.findViewById(R.id.decrement_custom);
            increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count++;
                    value.setText(String.valueOf(count));

                }
            });
            decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count--;
                    value.setText(String.valueOf(count));

                }
            });

            Button dialogButton = (Button) dialog.findViewById(R.id.cancel_custom);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            final Button yesButton=(Button)dialog.findViewById(R.id.submit);
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     //quantity.setText("Your donation is: "+count);
                     dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}




