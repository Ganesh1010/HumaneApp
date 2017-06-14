package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommonUI.TAG;

/**
 * Created by sujithv on 6/9/2017.
 */

public class DonationListAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    Activity activity;
    static List<DonatedItemDetails> donatedItemDetailsList = null;
    ArrayList<DonationDetailsReadOnly> donationDetailsReadOnlyList = null;
    DonationDetailsReadOnly donationDetailsReadOnly;
    DonationDetailViewHolder holder;
    boolean isDisplayingDonatedItemsList;

    private final int DONATION_ID = 0;

    public DonationListAdapter(Activity activity, ArrayList<DonationDetailsReadOnly> donationDetailsReadOnlyList) {
        Log.d(TAG, "DonationListAdapter: ");
        this.activity = activity;
        this.donationDetailsReadOnlyList = donationDetailsReadOnlyList;
    }

    public DonationListAdapter(Context context, List<DonatedItemDetails> donatedItemDetailsList) {
        Log.d(TAG, "DonationListAdapter: ");
        this.activity = activity;
        this.donatedItemDetailsList = donatedItemDetailsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_donation_details, parent, false);
        return new DonationDetailViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        holder = (DonationDetailViewHolder) viewHolder;
        Log.d(TAG, "onBindViewHolder: position: " + position);
        donationDetailsReadOnly = donationDetailsReadOnlyList.get(position);

        String donorName = donationDetailsReadOnly.getUser_name();
        String donorMobileNo = donationDetailsReadOnly.getMobile();
        Date donatedDate = donationDetailsReadOnly.getDonated_at();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        String dateFormat = simpleDateFormat.format(donatedDate);
        int donorId = donationDetailsReadOnly.getUserId();
        int donationId = donationDetailsReadOnly.getDonation_id();
        donatedItemDetailsList = donationDetailsReadOnly.getDonateditems();

        holder.donorNameTextView.setText(donorName);
        holder.donorMobileNoTextView.setText(donorMobileNo);
        holder.donatedDateTextView.setText(dateFormat);
        loadDonatedItemListFragment();

        holder.viewItemsButton.setOnClickListener(DonationListAdapter.this);
        holder.receivedButton.setTag(position);
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
                if (isDisplayingDonatedItemsList) {
                    Log.d(TAG, "onClick: Hiding the item list");
                    holder.donatedListFragmentHolder.setVisibility(View.GONE);
                    isDisplayingDonatedItemsList = false;
                } else {
                    Log.d(TAG, "onClick: Displaying the item list");
                    holder.donatedListFragmentHolder.setVisibility(View.VISIBLE);
                    isDisplayingDonatedItemsList = true;
                }
                break;
            case R.id.received_button_donation_details:
                int position = (int) v.getTag();
                int donationId = donationDetailsReadOnlyList.get(position).getDonation_id();
                new MarkAsReceived().execute(donationId);
                donationDetailsReadOnlyList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, donationDetailsReadOnlyList.size());
                /* remove this card */
                break;
        }
    }

    private void loadDonatedItemListFragment() {
        Log.d(TAG, "loadDonatedItemListFragment: ");
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DonatedItemListFragment donatedItemListFragment = new DonatedItemListFragment();
        fragmentTransaction.add(R.id.donated_list_fragment_holder_donation_details, donatedItemListFragment);
        fragmentTransaction.commit();
    }

    class DonationDetailViewHolder extends RecyclerView.ViewHolder {

        public ImageView donorImageView;
        public TextView donorNameTextView;
        public TextView donorMobileNoTextView;
        public TextView donatedDateTextView;
        public LinearLayout donatedListFragmentHolder;
        public Button viewItemsButton;
        public Button receivedButton;

        public DonationDetailViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "DonationDetailViewHolder: ");
            donorImageView = (ImageView) itemView.findViewById(R.id.donor_image_donation_details);
            donorNameTextView = (TextView) itemView.findViewById(R.id.donor_name_donation_details);
            donorMobileNoTextView = (TextView) itemView.findViewById(R.id.donor_mobile_donation_details);
            donatedDateTextView = (TextView) itemView.findViewById(R.id.donated_date_donation_details);
            donatedListFragmentHolder = (LinearLayout) itemView.findViewById(R.id.donated_list_fragment_holder_donation_details);
            donatedListFragmentHolder.setVisibility(View.GONE);
            isDisplayingDonatedItemsList = false;
            viewItemsButton = (Button) itemView.findViewById(R.id.view_items_button_donation_details);
            receivedButton = (Button) itemView.findViewById(R.id.received_button_donation_details);
        }
    }

    class MarkAsReceived extends AsyncTask {

        HttpResponse httpResponse;
        HttpClient httpClient;
        int code;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                int donationId = (int) objects[0];

                httpClient = new DefaultHttpClient();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", donationId);
                httpResponse = Connectivity.makePostRequest(RestAPIURL.receivedURL, jsonObject.toString(), httpClient, null);
                if (httpResponse != null) {
                    code = httpResponse.getStatusLine().getStatusCode();
                    Log.d("DonationListAdapter: ", "doInBackground:" + httpResponse.getStatusLine().getStatusCode());
                    Log.d("DonationListAdapter: ", "doInBackground:" + jsonObject.toString());
                    Log.d("DonationListAdapter: ", "doInBackground: " + httpResponse.getStatusLine().getReasonPhrase());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

    }

}
