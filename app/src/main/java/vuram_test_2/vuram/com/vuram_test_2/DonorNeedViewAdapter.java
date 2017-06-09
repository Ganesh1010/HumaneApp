package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class DonorNeedViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<NeedDetails> needDetails;
    private final String TAG = "DonorNeedViewAdapter";
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public DonorNeedViewAdapter(Context context, ArrayList<NeedDetails> needs, RecyclerView recyclerView) {
        this.context = context;
        this.needDetails = needs;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null)
                        onLoadMoreListener.onLoadMore();
                    loading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return needDetails.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_need_view_to_donor, parent, false);

            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        System.out.println("position : "+position);
        Log.d(TAG, "onBindViewHolder: Start @Need " + (position + 1));
        if (holder instanceof ViewHolder)
        {
            NeedDetails need = needDetails.get(position);
            ((ViewHolder)holder).donorNeedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Org Details Page will be opened", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, OrgDetailsActivity.class));
                }
            });
            ((ViewHolder)holder).orgName.setText(need.org.org_name);
            ((ViewHolder)holder).orgAddress.setText(need.org.getAddress());
            ((ViewHolder)holder).orgContactNo.setText(need.org.getPhone());
            ((ViewHolder)holder).orgLogo.setImageResource(R.drawable.ngo);
            int animationDuration = 1000; // 2500ms = 2,5s
            int totalQuantity=0;
            int totalDonatedReceived=0;
            for(int i=0;i<need.getItems().size();i++) {
                totalQuantity += need.getItems().get(i).getQuantity();
                totalDonatedReceived += need.getItems().get(i).getDonated_and_received_amount();
            }
            if(totalQuantity!=0 || totalDonatedReceived!=0) {
                ((ViewHolder) holder).overallSatisfiedBar.setProgressWithAnimation(totalDonatedReceived * 100 / totalQuantity, animationDuration);
                ((ViewHolder) holder).percentage.setText(totalDonatedReceived * 100 / totalQuantity + "%");
            }
            else
            {
                ((ViewHolder) holder).overallSatisfiedBar.setProgressWithAnimation(50, animationDuration);
                ((ViewHolder) holder).percentage.setText(50 + "%");
            }
            //Glide.with(context).load(needDetails.orgLogo).into(((ViewHolder)holder).orgLogo);
            ((ViewHolder)holder).needItems.removeAllViews();
            View itemView = null;
            for (int i = 0; i < need.items.size(); i++) {
                NeedItemDetails itemDetails = need.items.get(i);
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_view, null);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Org Details Page will be opened", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, OrgDetailsActivity.class));
                    }
                });
                ImageView itemIcon = (ImageView) itemView.findViewById(R.id.item_image_item_view);
                TextView itemName = (TextView) itemView.findViewById(R.id.item_name_item_view);
                ProgressBar satisfactionBar = (ProgressBar) itemView.findViewById(R.id.item_status_item_view);
                switch (itemDetails.getItem_type_id()) {
                    case 1:
                        itemIcon.setImageResource(R.drawable.ic_food_black);
                        //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Food");
                        satisfactionBar.setProgress(need.getItems().get(i).getDonated_and_received_amount());
                        break;
                    case 2:
                        itemIcon.setImageResource(R.drawable.ic_cloth_black);
                        //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Cloth");
                        satisfactionBar.setProgress(need.getItems().get(i).getDonated_and_received_amount());
                        break;
                    case 3:
                        itemIcon.setImageResource(R.drawable.ic_blood_black);
                        //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Blood");
                        satisfactionBar.setProgress(need.getItems().get(i).getDonated_and_received_amount());
                        break;
                    case 4:
                        itemIcon.setImageResource(R.drawable.ic_grocery_cart_black);
                        //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Groceries");
                        satisfactionBar.setProgress(need.getItems().get(i).getDonated_and_received_amount());
                        break;
                    case 5:
                        itemIcon.setImageResource(R.drawable.ic_stationery_black);
                        //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Stationeries");
                        satisfactionBar.setProgress(need.getItems().get(i).getDonated_and_received_amount());
                        break;
                    default:
                        itemIcon.setImageResource(R.drawable.ic_stationery_black);
                        //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Others");
                        satisfactionBar.setProgress(need.getItems().get(i).getDonated_and_received_amount());
                        break;
                }
                // Adding the item to the items layout(Horizontal Scolling Linear Layout)
                ((ViewHolder)holder).needItems.addView(itemView);
                Log.d("Child count+ position", ((ViewHolder)holder).needItems.getChildCount() + "");
                Log.d(TAG, "onBindViewHolder: " + (i + 1) + " item(s) added");
            }
            Log.d(TAG, "onBindViewHolder: End");
        }
        else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return needDetails.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
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