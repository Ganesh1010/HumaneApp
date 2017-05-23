package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonationCartItemListAdapter extends RecyclerView.Adapter<DonationCartItemListAdapter.ViewHolder> {

    List<Map<NeedItemDetails,DonatedItemDetails>> itemsDonated=new ArrayList<>();

    public DonationCartItemListAdapter(List<Map<NeedItemDetails,DonatedItemDetails>> itemsDonated)
    {
        this.itemsDonated=itemsDonated;
    }
    @Override
    public DonationCartItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_donation_cart_row_view_item, parent, false);

        System.out.println("item size "+itemsDonated.size());
        return new DonationCartItemListAdapter.ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(DonationCartItemListAdapter.ViewHolder holder, int position) {

        for (Map.Entry<NeedItemDetails, DonatedItemDetails> entry : itemsDonated.get(position).entrySet()) {
            NeedItemDetails key = entry.getKey();
            DonatedItemDetails value = entry.getValue();
            holder.itemName.setText(key.getItem_type_id()+"");
            holder.quantity.setText(value.getQuantity()+"");
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView itemName;
        TextView quantity;

        public ViewHolder(View v) {
            super(v);

            itemName= (TextView) v.findViewById(R.id.itemViewName_DonationCart);
            quantity= (TextView) v.findViewById(R.id.itemViewQuantity_DonationCart);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context,list.get(getAdapterPosition()).getNeed_item_id()+"",Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
