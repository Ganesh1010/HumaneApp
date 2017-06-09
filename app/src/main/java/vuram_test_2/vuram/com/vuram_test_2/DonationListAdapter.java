package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sujithv on 6/9/2017.
 */

public class DonationListAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    Context context;
    ArrayList<DonationDetailsReadOnly> donationDetailsReadOnlyList;
    DonationDetailsReadOnly donationDetailsReadOnly;
    DonationDetailViewHolder holder;
    SimpleDateFormat simpleDateFormat;
    boolean displayDonatedItemsList = false;

    public DonationListAdapter(Context context, ArrayList<DonationDetailsReadOnly> donationDetailsReadOnlyList) {
        this.context = context;
        this.donationDetailsReadOnlyList = donationDetailsReadOnlyList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_donation_details, parent, false);
        return new DonationDetailViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        holder = (DonationDetailViewHolder) viewHolder;
        donationDetailsReadOnly = donationDetailsReadOnlyList.get(position);

        String donorName = donationDetailsReadOnly.getUser_name();
        String donorMobileNo = donationDetailsReadOnly.getMobile();
        Date donatedDate = donationDetailsReadOnly.getDonated_at();
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        String dateFormat = simpleDateFormat.format(donatedDate);
        int donorId = donationDetailsReadOnly.getUserId();

        holder.donorNameTextView.setText(donorName);
        holder.donorMobileNoTextView.setText(donorMobileNo);
        holder.donatedDateTextView.setText(dateFormat);

        holder.viewItemsButton.setOnClickListener(DonationListAdapter.this);
        holder.receivedButton.setOnClickListener(DonationListAdapter.this);
    }

    @Override
    public int getItemCount() {
        return donationDetailsReadOnlyList.size();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.view_items_button_donation_details:
                if (displayDonatedItemsList) {
                    holder.donatedListRecyclerView.setVisibility(View.GONE);
                    displayDonatedItemsList = false;
                } else {
                    holder.donatedListRecyclerView.setVisibility(View.VISIBLE);
                    displayDonatedItemsList = true;
                }
                break;
            case R.id.received_button_donation_details:
                break;
        }
    }

    class DonationDetailViewHolder extends RecyclerView.ViewHolder {

        public ImageView donorImageView;
        public TextView donorNameTextView;
        public TextView donorMobileNoTextView;
        public TextView donatedDateTextView;
        public RecyclerView donatedListRecyclerView;
        public Button viewItemsButton;
        public Button receivedButton;

        public DonationDetailViewHolder(View itemView) {
            super(itemView);
            donorImageView = (ImageView) itemView.findViewById(R.id.donor_image_donation_details);
            donorNameTextView = (TextView) itemView.findViewById(R.id.donor_name_donation_details);
            donorMobileNoTextView = (TextView) itemView.findViewById(R.id.donor_mobile_donation_details);
            donatedDateTextView = (TextView) itemView.findViewById(R.id.donated_date_donation_details);
            donatedListRecyclerView = (RecyclerView) itemView.findViewById(R.id.donated_list_recycler_view);
            viewItemsButton = (Button) itemView.findViewById(R.id.view_items_button_donation_details);
            receivedButton = (Button) itemView.findViewById(R.id.received_button_donation_details);
        }
    }
}
