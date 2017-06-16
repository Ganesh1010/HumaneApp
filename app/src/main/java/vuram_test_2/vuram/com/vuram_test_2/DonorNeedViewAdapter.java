package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
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

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorNeedViewAdapter extends RecyclerView.Adapter {

    public Context context;
    public ArrayList<NeedDetails> needDetailsList;
    public final String TAG = "DonorNeedViewAdapter";
    public final int VIEW_ITEM = 1;
    public final int VIEW_PROGRESS = 0;
    public int visibleThreshold = 1;
    public int lastVisibleItem, totalItemCount;
    public boolean loading;
    Activity activity;
    public OnLoadMoreListener onLoadMoreListener;
    public Fragment fragment=null;
    public android.app.FragmentManager fragmentManager;

    public DonorNeedViewAdapter(Context context, ArrayList<NeedDetails> needDetailsList, RecyclerView donorNeedDisplayRecyclerView,Activity activity) {
        this.context = context;
        this.needDetailsList = needDetailsList;
        this.activity=activity;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) donorNeedDisplayRecyclerView.getLayoutManager();
        donorNeedDisplayRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView donorNeedDisplayRecyclerView, int dx, int dy) {
                super.onScrolled(donorNeedDisplayRecyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.d(TAG, "onScrolled: total Count"+totalItemCount);
                Log.d(TAG, "onScrolled: last visible "+lastVisibleItem);
                Log.d(TAG, "onScrolled: loading"+loading);
                if (!loading && (totalItemCount <= (lastVisibleItem + visibleThreshold)) && lastVisibleItem!=-1) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return needDetailsList.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
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
        Log.d(TAG, "onBindViewHolder: Start @Need " + (position + 1));
        if (holder instanceof ViewHolder)
        {
            if(needDetailsList!=null) 
            {
                final NeedDetails needDetails = needDetailsList.get(position);
                if (needDetails != null) {
                    ((ViewHolder) holder).donorNeedView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragment = new NeedDonationFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("NEED_ID", String.valueOf(needDetails.getNeed_id()));
                            fragment.setArguments(bundle);
                            fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).addToBackStack("tag").commit();
                        }
                    });

                    ((ViewHolder) holder).orgName.setText(needDetails.org.org_name);
                    ((ViewHolder) holder).orgAddress.setText(needDetails.org.getAddress());
                    ((ViewHolder) holder).orgContactNo.setText(needDetails.org.getPhone());
                    ((ViewHolder) holder).orgLogo.setImageResource(R.drawable.ngo);
                    int animationDuration = 1000;

                    int totalQuantity = 0;
                    int totalDonatedReceived = 0;

                    for (NeedItemDetails needItemDetails : needDetails.items) {
                        totalQuantity += needItemDetails.getQuantity();
                        totalDonatedReceived += needItemDetails.getDonated_and_received_amount();
                    }
                    if (totalQuantity != 0 || totalDonatedReceived != 0) {
                        ((ViewHolder) holder).overallSatisfiedBar.setProgressWithAnimation(totalDonatedReceived * 100 / totalQuantity, animationDuration);
                        ((ViewHolder) holder).percentage.setText(totalDonatedReceived * 100 / totalQuantity + "%");
                    } else {
                        ((ViewHolder) holder).overallSatisfiedBar.setProgressWithAnimation(50, animationDuration);
                        ((ViewHolder) holder).percentage.setText(50 + "%");
                    }
                    ((ViewHolder) holder).needItems.removeAllViews();
                    View itemView;
                    for (NeedItemDetails needItemDetails : needDetails.items) {
                        itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_view, null);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("NEED_ID", String.valueOf(needDetails.getNeed_id()));
                                fragment = new NeedDonationFragment();
                                fragment.setArguments(bundle);
                                fragmentManager = activity.getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).addToBackStack("tag").commit();
                            }
                        });
                        ImageView itemIcon = (ImageView) itemView.findViewById(R.id.item_image_item_view);
                        TextView itemName = (TextView) itemView.findViewById(R.id.item_name_item_view);
                        ProgressBar satisfactionBar = (ProgressBar) itemView.findViewById(R.id.item_status_item_view);
                        switch (needItemDetails.getItem_type_id()) {
                            case 1:
                                itemIcon.setImageResource(R.drawable.ic_food_black);
                                itemName.setText(getMainItemName(needItemDetails.getItem_type_id()));
                                satisfactionBar.setProgress(needItemDetails.getDonated_and_received_amount());
                                //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                                break;
                            case 2:
                                itemIcon.setImageResource(R.drawable.ic_cloth_black);
                                itemName.setText(getMainItemName(needItemDetails.getItem_type_id()));
                                satisfactionBar.setProgress(needItemDetails.getDonated_and_received_amount());
                                //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);

                                break;
                            case 3:
                                itemIcon.setImageResource(R.drawable.ic_blood_black);
                                itemName.setText(getMainItemName(needItemDetails.getItem_type_id()));
                                satisfactionBar.setProgress(needItemDetails.getDonated_and_received_amount());
                                //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                                break;
                            case 4:
                                itemIcon.setImageResource(R.drawable.ic_grocery_cart_black);
                                itemName.setText(getMainItemName(needItemDetails.getItem_type_id()));
                                satisfactionBar.setProgress(needItemDetails.getDonated_and_received_amount());
                                //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                                break;
                            case 5:
                                itemIcon.setImageResource(R.drawable.ic_stationery_black);
                                itemName.setText(getMainItemName(needItemDetails.getItem_type_id()));
                                satisfactionBar.setProgress(needItemDetails.getDonated_and_received_amount());
                                //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
                                break;
                        }
                        ((ViewHolder) holder).needItems.addView(itemView);
                    }
                }
                else
                    Log.e(TAG,"each needDetail is null",new Throwable());
            }
            else
                Log.e(TAG,"needDetailsList is null",new Throwable());
            Log.d(TAG, "onBindViewHolder: End");
        }
        else
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
    }

    @Override
    public int getItemCount() {
        return needDetailsList==null?0:needDetailsList.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
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

    public String getMainItemName(int mainItemCode)
    {
        DatabaseHelper db=new DatabaseHelper(activity);
        String mainItemName = "Main Item Name";
        ArrayList<MainItemDetails> mainItemDetailsList=db.getAllMainItemDetails();
        if(mainItemDetailsList!=null) {
            for (MainItemDetails mainItemDetails : mainItemDetailsList)
                if (mainItemCode == mainItemDetails.getMainItemCode())
                    mainItemName = mainItemDetails.getMainItemName();
        }
        return mainItemName;
    }
}