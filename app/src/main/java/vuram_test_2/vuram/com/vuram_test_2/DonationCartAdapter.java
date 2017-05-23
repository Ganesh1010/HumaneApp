package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DonationCartAdapter extends RecyclerView.Adapter<DonationCartAdapter.ViewHolder> {

    DonationDetails donationDetails;
    NeedDetails needDetails;
    Context context;
    List<Map<NeedItemDetails,DonatedItemDetails>> itemsDonated=new ArrayList<>();

    public DonationCartAdapter(Context context, DonationDetails donationDetails,NeedDetails needDetails)
    {
        this.needDetails=needDetails;
        this.donationDetails=donationDetails;
        this.context=context;
    }
    @Override
    public DonationCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_donation_cart_item, parent, false);

        int need_item_id;
        for(int i=0;i<donationDetails.getDonateditems().size();i++) {
            Map<NeedItemDetails,DonatedItemDetails> donatedDetails=new HashMap<>();
            need_item_id = donationDetails.getDonateditems().get(i).getNeeditem();
            for(int j=0;j<needDetails.getItems().size();j++)
                if (need_item_id==needDetails.getItems().get(j).getNeed_item_id())
                    donatedDetails.put(needDetails.getItems().get(j), donationDetails.getDonateditems().get(i));
            itemsDonated.add(donatedDetails);
        }

        return new DonationCartAdapter.ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(DonationCartAdapter.ViewHolder holder, int position) {
        for(int i=0;i<needDetails.getItems().size();i++) {
            if(donationDetails.getDonateditems().get(position).getNeeditem()==needDetails.getItems().get(i).getNeed_item_id())
            holder.category.setText(needDetails.getItems().get(i).getItem_type_id()+"");
        }

        holder.itemRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.itemRecyclerView.setAdapter(new DonationCartItemListAdapter(itemsDonated));
    }

    @Override
    public int getItemCount() {
        return donationDetails.getDonateditems().size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView category;
        RecyclerView itemRecyclerView;
        public ViewHolder(View v) {
            super(v);

            category= (TextView) v.findViewById(R.id.category_DonationCart);
            itemRecyclerView= (RecyclerView) v.findViewById(R.id.itemRecyclerView);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
