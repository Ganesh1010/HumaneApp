package vuram_test_2.vuram.com.vuram_test_2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommonUI.TAG;

public class DonationDetailsAdapter extends RecyclerView.Adapter {
    List<DonatedItemDetails> donatedItemDetailsArrayList;
    DonatedItemDetails donatedItemDetails;
    TextView quantity;
    TextView name;

    public DonationDetailsAdapter(List<DonatedItemDetails> donatedItemDetailsArrayList) {
        this.donatedItemDetailsArrayList=donatedItemDetailsArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_need_list_view, parent, false);
        return new DonationDetailsAdapter.ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        donatedItemDetails=donatedItemDetailsArrayList.get(position);
        int itemQuantity=donatedItemDetails.getQuantity();
        int itemName=donatedItemDetails.getNeeditem();
        quantity.setText(itemQuantity+"");
        name.setText(itemName+"");
    }

    @Override
    public int getItemCount() {
        return donatedItemDetailsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.itemNameTextView_ReceivalPage);
            quantity=(TextView) itemView.findViewById(R.id.quantityTextView_ReceivalPage);
        }
    }
}
