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

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorNeedViewAdapter extends RecyclerView.Adapter<DonorNeedViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TestNeedDetails> needs;
    private int screenWidth;
    private NeedDetails[] needDetails;
    private final String TAG = "DonorNeedViewAdapter";
    public DonorNeedViewAdapter(Context context, ArrayList<TestNeedDetails> needs) {
        this.context = context;
        this.needs = needs;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public DonorNeedViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(context).inflate(R.layout.layout_need_view_to_donor, parent, false);
        ViewHolder holder = new ViewHolder(inflatedView);
        return holder;
    }

    @Override
    public void onBindViewHolder(DonorNeedViewAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Start @Need " + (position + 1));
       // TestNeedDetails needDetails = needs.get(position);
        NeedDetails need=needDetails[position];
        holder.donorNeedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Org Details Page will be opened", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, OrgDetailsActivity.class));
            }
        });
        holder.orgName.setText("Org Name Test");
        holder.orgAddress.setText("Address");
        holder.orgContactNo.setText("957851907");
        holder.orgLogo.setImageResource(R.drawable.ngo);
        //holder.roundedOrgLogo.setImageResource(R.drawable.ngo);
        int animationDuration = 1000; // 2500ms = 2,5s
        holder.overallSatisfiedBar.setProgressWithAnimation(80, animationDuration);
        holder.percentage.setText("80" + "%");
        //Glide.with(context).load(needDetails.orgLogo).into(holder.orgLogo);
        holder.needItems.removeAllViews();
        View itemView = null;
        for (int i = 0; i < need.items.size(); i++) {
            NeedItemDetails itemDetails = need.items.get(i);
            // Inflating a new Item View
            itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_view, null);
            ImageView itemIcon = (ImageView) itemView.findViewById(R.id.item_image_item_view);
            TextView itemName = (TextView) itemView.findViewById(R.id.item_name_item_view);
            ProgressBar satisfactionBar = (ProgressBar) itemView.findViewById(R.id.item_status_item_view);
            switch (new Random().nextInt(4)) {
                case 0:
                    itemIcon.setImageResource(R.drawable.ic_food_black);
                    //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                    itemName.setText("Food");
                    satisfactionBar.setProgress(40);
                    break;
                case 1:
                    itemIcon.setImageResource(R.drawable.ic_cloth_black);
                    //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                    itemName.setText("Cloth");
                    satisfactionBar.setProgress(40);
                    break;
                case 2:
                    itemIcon.setImageResource(R.drawable.ic_grocery_cart_black);
                    //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                    itemName.setText("Grocery");
                    satisfactionBar.setProgress(40);
                    break;
                case 3:
                    itemIcon.setImageResource(R.drawable.ic_stationery_black);
                    //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                    itemName.setText("Stationery");
                    satisfactionBar.setProgress(40);
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

        public CardView donorNeedView;
        public CircleImageView orgLogo;
        public TextView orgName;
        public TextView orgAddress;
        public TextView orgContactNo;
        public CircularProgressBar overallSatisfiedBar;
        public TextView percentage;
        public LinearLayout needItems;

        public ViewHolder(View itemView) {
            super(itemView);
            donorNeedView = (CardView) itemView.findViewById(R.id.need_cardview_to_donor);
            orgLogo = (CircleImageView) itemView.findViewById(R.id.org_logo_donor_need_view);
            orgName = (TextView) itemView.findViewById(R.id.orgName);
            orgAddress = (TextView) itemView.findViewById(R.id.orgAddress);
            orgContactNo = (TextView) itemView.findViewById(R.id.orgContactNo);
            overallSatisfiedBar = (CircularProgressBar) itemView.findViewById(R.id.circularProgressBar);
            percentage = (TextView) itemView.findViewById(R.id.circularProgressPercentage);
            needItems = (LinearLayout) itemView.findViewById(R.id.need_items_donor_need_view);
        }
    }
}