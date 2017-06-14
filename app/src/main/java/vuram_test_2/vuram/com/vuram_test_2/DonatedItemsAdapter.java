package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommonUI.TAG;

/**
 * Created by sujithv on 6/13/2017.
 */

public class DonatedItemsAdapter extends RecyclerView.Adapter {

    Context context;
    List<DonatedItemDetails> donatedItemDetailsList;
    int itemViewType;

    DonatedItemDetails donatedItemDetails;

    public static final int MAIN_ITEM = 0;
    public static final int SUB_ITEM = 1;

    public DonatedItemsAdapter(Context context, List<DonatedItemDetails> donatedItemDetailsList, int itemViewType) {
        this.context = context;
        this.donatedItemDetailsList = donatedItemDetailsList;
        this.itemViewType = itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflatedView;
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case MAIN_ITEM:
                Log.d(TAG, "onCreateViewHolder: Main");
                inflatedView = inflater.inflate(R.layout.layout_donated_main_item_details, parent, false);
                viewHolder = new MainItemViewHolder(inflatedView);
                break;
            case SUB_ITEM:
                Log.d(TAG, "onCreateViewHolder: Sub");
                inflatedView = inflater.inflate(R.layout.layout_donated_sub_item_details, parent, false);
                viewHolder = new SubItemViewHolder(inflatedView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case MAIN_ITEM:
                Log.d(TAG, "onBindViewHolder: Main");
                MainItemViewHolder mainItemViewHolder = (MainItemViewHolder) holder;
                configureMainItemViewHolder(mainItemViewHolder, position);
                break;
            case SUB_ITEM:
                Log.d(TAG, "onBindViewHolder: Sub");
                SubItemViewHolder subItemViewHolder = (SubItemViewHolder) holder;
                configureSubItemViewHolder(subItemViewHolder, position);
                break;
        }
    }

    private void configureMainItemViewHolder(MainItemViewHolder mainItemViewHolder, int position) {
        Log.d(TAG, "configureMainItemViewHolder: ");

        mainItemViewHolder.mainItemNameTextView.setText("Groceries");
        
        DonatedItemsAdapter donatedItemsAdapter = new DonatedItemsAdapter(context, donatedItemDetailsList, SUB_ITEM);
        mainItemViewHolder.subItemsRecyclerView.setHasFixedSize(true);
        mainItemViewHolder.subItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mainItemViewHolder.subItemsRecyclerView.setAdapter(donatedItemsAdapter);
    }

    private void configureSubItemViewHolder(SubItemViewHolder subItemViewHolder, int position) {
        Log.d(TAG, "configureSubItemViewHolder: ");

        donatedItemDetails = (DonatedItemDetails) donatedItemDetailsList.get(position);
        int needItemId = donatedItemDetails.getNeed_item_id();
        int quantity = donatedItemDetails.getQuantity();

        subItemViewHolder.subItemNameTextView.setText("Pencil");
        subItemViewHolder.subItemQuantityTextView.setText(Integer.toString(quantity));
    }

    @Override
    public int getItemViewType(int position) {
        return this.itemViewType;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MainItemViewHolder extends RecyclerView.ViewHolder {

        TextView mainItemNameTextView;
        RecyclerView subItemsRecyclerView;

        public MainItemViewHolder(View itemView) {
            super(itemView);
            mainItemNameTextView = (TextView) itemView.findViewById(R.id.main_item_name_main_item_details);
            subItemsRecyclerView = (RecyclerView) itemView.findViewById(R.id.sub_items_recyclerview_donated_items);
        }
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {

        TextView subItemNameTextView;
        TextView subItemQuantityTextView;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            subItemNameTextView = (TextView) itemView.findViewById(R.id.sub_item_name_sub_item_details);
            subItemQuantityTextView = (TextView) itemView.findViewById(R.id.sub_item_quantity_sub_item_details);
        }
    }

}
