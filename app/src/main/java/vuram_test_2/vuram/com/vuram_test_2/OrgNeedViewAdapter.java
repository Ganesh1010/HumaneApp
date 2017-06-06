package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Random;

public class OrgNeedViewAdapter extends RecyclerView.Adapter<OrgNeedViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TestNeedDetails> needs;
    private int screenWidth;

    private final String TAG = "OrgNeedViewAdapter.java";

    public OrgNeedViewAdapter(Context context, ArrayList<TestNeedDetails> needs) {
        this.context = context;
        this.needs = needs;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(context).inflate(R.layout.layout_need_view_to_org, parent, false);
        ViewHolder holder = new ViewHolder(inflatedView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Start @Need " + (position + 1));

        TestNeedDetails needDetails = needs.get(position);

        holder.orgNeedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Need Details Page will be opened", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, NeedDetailsActivity.class));
            }
        });

        holder.donorCount.setText("+" + (new Random().nextInt(20) + 1));

        int animationDuration = 1000; // 2500ms = 2,5s
        holder.overallSatisfiedPercentageBar.setProgressWithAnimation(needDetails.overallSatisfiedPercentage, animationDuration);
        holder.satisfiedPercentage.setText(needDetails.overallSatisfiedPercentage + "%");

        holder.needItems.removeAllViews();

        View itemView = null;
        for (int i = 0; i < needDetails.itemDetailsList.size(); i++) {
            ItemDetails itemDetails = needDetails.itemDetailsList.get(i);
            // Inflating a new Item View
            itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_view, null);
            ImageView itemIcon = (ImageView) itemView.findViewById(R.id.item_image_item_view);
            TextView itemName = (TextView) itemView.findViewById(R.id.item_name_item_view);
            ProgressBar satisfactionBar = (ProgressBar) itemView.findViewById(R.id.item_status_item_view);

            // Defining Item Details
            switch (new Random().nextInt(4)) {
                case 0:
                    itemIcon.setImageResource(R.drawable.ic_food_black);
                    //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                    itemName.setText("Food");
                    satisfactionBar.setProgress(itemDetails.satisfiedPercentage);
                    break;
                case 1:
                    itemIcon.setImageResource(R.drawable.ic_cloth_black);
                    //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                    itemName.setText("Cloth");
                    satisfactionBar.setProgress(itemDetails.satisfiedPercentage);
                    break;
                case 2:
                    itemIcon.setImageResource(R.drawable.ic_grocery_cart_black);
                    //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                    itemName.setText("Grocery");
                    satisfactionBar.setProgress(itemDetails.satisfiedPercentage);
                    break;
                case 3:
                    itemIcon.setImageResource(R.drawable.ic_stationery_black);
                    //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                    itemName.setText("Stationery");
                    satisfactionBar.setProgress(itemDetails.satisfiedPercentage);
                    break;
            }


            // Adding the item to the items layout(Horizontal Scolling Linear Layout)
            holder.needItems.addView(itemView);
            Log.d("Child count+ postiton",holder.needItems.getChildCount()+"");
            Log.d(TAG, "onBindViewHolder: " + (i + 1) + " item(s) added");
        }

        Log.d(TAG, "onBindViewHolder: End");
    }

    @Override
    public int getItemCount() {
        return needs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView orgNeedView;
        public CircularProgressBar overallSatisfiedPercentageBar;
        public TextView satisfiedPercentage;
        public TextView donorCount;
        public LinearLayout needItems;

        public ViewHolder(View itemView) {
            super(itemView);
            orgNeedView = (CardView) itemView.findViewById(R.id.need_cardview_to_org);
            overallSatisfiedPercentageBar = (CircularProgressBar) itemView.findViewById(R.id.circularProgressBar);
            satisfiedPercentage = (TextView) itemView.findViewById(R.id.circularProgressPercentage);
            donorCount = (TextView) itemView.findViewById(R.id.donor_count_org_need_view);
            if (donorCount == null) {
                Log.d(TAG, "ViewHolder: Donor Count TextView returns null");
            }
            needItems = (LinearLayout) itemView.findViewById(R.id.need_items_org_need_view);
        }
    }
}