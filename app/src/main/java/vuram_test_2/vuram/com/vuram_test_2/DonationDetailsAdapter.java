package vuram_test_2.vuram.com.vuram_test_2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommonUI.TAG;

/**
 * Created by gokulrajk on 6/15/2017.
 */

public class DonationDetailsAdapter extends RecyclerView.Adapter {
    ArrayList<DonatedItemDetails> donatedItemDetailses;
    DonatedItemDetails donation;
    TextView quantity;
    TextView name;

    public DonationDetailsAdapter(ArrayList<DonatedItemDetails> donatedItemDetailses) {
        this.donatedItemDetailses=donatedItemDetailses;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_need_list_view, parent, false);
        return new DonationDetailsAdapter.ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      donation=donatedItemDetailses.get(position);
        int itemquantity=donation.getQuantity();
        int itemname=donation.getNeeditem();
        quantity.setText(itemquantity);
        name.setText(itemname);
    }

    @Override
    public int getItemCount() {
        return donatedItemDetailses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            quantity=(TextView) itemView.findViewById(R.id.itemNameTextView_ReceivalPage);
            name=(TextView) itemView.findViewById(R.id.quantityTextView_ReceivalPage);
        }
    }
}
