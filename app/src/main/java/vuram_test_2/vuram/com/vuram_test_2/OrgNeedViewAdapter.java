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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;

public class OrgNeedViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<NeedDetails> needs;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    int need_id;
    Intent intent;

    private final String TAG = "OrgNeedViewAdapter.java";

    public OrgNeedViewAdapter(Context context, ArrayList<NeedDetails> needs,RecyclerView recyclerView) {
        this.context = context;
        this.needs = needs;
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
                if (!loading && (totalItemCount <= (lastVisibleItem + visibleThreshold))) {
                    if (onLoadMoreListener != null)
                        onLoadMoreListener.onLoadMore();
                    loading = true;
                }
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        return needs.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_need_view_to_org, parent, false);

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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Start @Need " + (position + 1));
        Log.d("position",position+"");

        if (holder instanceof ViewHolder)
        {
            final NeedDetails needDetails = needs.get(position);
            Log.d("need org",need_id+"");
            ((ViewHolder)holder).orgNeedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    need_id=needDetails.getNeed_id();
                    Toast.makeText(context, "Need Details Page will be opened", Toast.LENGTH_SHORT).show();
                    intent=new Intent(context, NeedDetailsActivity.class);
                    intent.putExtra(USER_KEY_TYPE,need_id);
                    Log.d("need org view adapter",need_id+"");
                    context.startActivity(intent);
                }
            });

            ((ViewHolder)holder).donorCount.setText(needDetails.getDonations().size()+"");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
            sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
            Date d = null;
            try {
                d = sdf1.parse(needDetails.getNeed_posted_date());
            } catch (ParseException e) {
                try {
                    d= sdf2.parse(needDetails.getNeed_posted_date());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
            String formattedTime = output.format(d);
            ((ViewHolder)holder).requestedOn.setText(formattedTime);

            int animationDuration = 1000; // 2500ms = 2,5s
            int totalQuantity=0;
            int totalDonatedReceived=0;
            for(NeedItemDetails needItemDetails:needDetails.getItems()) {
                totalQuantity += needItemDetails.getQuantity();
                totalDonatedReceived += needItemDetails.getDonated_and_received_amount();
            }
            if(totalQuantity!=0 || totalDonatedReceived!=0) {
                ((ViewHolder) holder).overallSatisfiedPercentageBar.setProgressWithAnimation(totalDonatedReceived * 100 / totalQuantity, animationDuration);
                ((ViewHolder) holder).satisfiedPercentage.setText(totalDonatedReceived * 100 / totalQuantity + "%");
            }
            else
            {
                ((ViewHolder) holder).overallSatisfiedPercentageBar.setProgressWithAnimation(50, animationDuration);
                ((ViewHolder) holder).satisfiedPercentage.setText(50 + "%");
            }
            ((ViewHolder)holder).needItems.removeAllViews();
            View itemView = null;
            for (NeedItemDetails needItemDetails:needDetails.getItems()) {
                // Inflating a new Item View
                itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_view, null);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Need Details Page will be opened", Toast.LENGTH_SHORT).show();
                        need_id=needDetails.getNeed_id();
                        intent=new Intent(context, NeedDetailsActivity.class);
                        intent.putExtra(USER_KEY_TYPE,need_id);
                        Log.d("need org view adapter",need_id+"");
                        context.startActivity(intent);
                    }
                });
                ImageView itemIcon = (ImageView) itemView.findViewById(R.id.item_image_item_view);
                TextView itemName = (TextView) itemView.findViewById(R.id.item_name_item_view);
                ProgressBar satisfactionBar = (ProgressBar) itemView.findViewById(R.id.item_status_item_view);

                // Defining Item Details
                switch (needItemDetails.getItem_type_id()) {
                    case 1:
                        itemIcon.setImageResource(R.drawable.ic_food_black);
                        //Glide.with(context).load(needItemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Food");
                        satisfactionBar.setProgress((needItemDetails.getDonated_and_received_amount()*100)/needItemDetails.getQuantity());
                        break;
                    case 2:
                        itemIcon.setImageResource(R.drawable.ic_cloth_black);
                        //Glide.with(context).load(needItemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Cloth");
                        satisfactionBar.setProgress((needItemDetails.getDonated_and_received_amount()*100)/needItemDetails.getQuantity());
                        break;
                    case 3:
                        itemIcon.setImageResource(R.drawable.ic_blood_black);
                        //Glide.with(context).load(needItemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Blood");
                        satisfactionBar.setProgress((needItemDetails.getDonated_and_received_amount()*100)/needItemDetails.getQuantity());
                        break;
                    case 4:
                        itemIcon.setImageResource(R.drawable.ic_grocery_cart_black);
                        //Glide.with(context).load(needItemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Groceries");
                        satisfactionBar.setProgress((needItemDetails.getDonated_and_received_amount()*100)/needItemDetails.getQuantity());
                        break;
                    case 5:
                        itemIcon.setImageResource(R.drawable.ic_stationery_black);
                        //Glide.with(context).load(needItemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Stationeries");
                        satisfactionBar.setProgress((needItemDetails.getDonated_and_received_amount()*100)/needItemDetails.getQuantity());
                        break;
                    default:
                        itemIcon.setImageResource(R.drawable.ic_stationery_black);
                        //Glide.with(context).load(needItemDetails.itemIcon).into(itemIcon);
                        itemName.setText("Others");
                        satisfactionBar.setProgress((needItemDetails.getDonated_and_received_amount()*100)/needItemDetails.getQuantity());
                        break;
                }
                
                // Adding the item to the items layout(Horizontal Scolling Linear Layout)
                ((ViewHolder)holder).needItems.addView(itemView);
                Log.d("Child count+ postiton", ((ViewHolder) holder).needItems.getChildCount() + "");
                //Log.d(TAG, "onBindViewHolder: " + (i + 1) + " item(s) added");
            }

            Log.d(TAG, "onBindViewHolder: End");
        }
        else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
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
        public TextView requestedOn;
        public LinearLayout needItems;

        public ViewHolder(View itemView) {
            super(itemView);
            orgNeedView = (CardView) itemView.findViewById(R.id.need_cardview_to_org);
            overallSatisfiedPercentageBar = (CircularProgressBar) itemView.findViewById(R.id.circularProgressBar);
            satisfiedPercentage = (TextView) itemView.findViewById(R.id.circularProgressPercentage);
            donorCount = (TextView) itemView.findViewById(R.id.donor_count_org_need_view);
            requestedOn= (TextView) itemView.findViewById(R.id.requested_on);
            if (donorCount == null) {
                Log.d(TAG, "ViewHolder: Donor Count TextView returns null");
            }
            needItems = (LinearLayout) itemView.findViewById(R.id.need_items_org_need_view);
        }
    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }
}