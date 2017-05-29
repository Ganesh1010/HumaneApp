package vuram_test_2.vuram.com.vuram_test_2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DonationConfirmationItemDislayAdapter extends RecyclerView.Adapter<DonationConfirmationItemDislayAdapter.ViewHolder> {
    @Override
    public DonationConfirmationItemDislayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name_and_quantity_layout, parent, false);
        return new DonationConfirmationItemDislayAdapter.ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView itemName;
        TextView quantity;

        public ViewHolder(View v) {
            super(v);

            itemName = (TextView) v.findViewById(R.id.itemNameDonationConfirmation);
            quantity = (TextView) v.findViewById(R.id.itemQuantityDonationConfirmation);

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
