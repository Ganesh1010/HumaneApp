package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.internal.zzt.TAG;


public class NeedReceivalCard extends RecyclerView.Adapter<NeedReceivalCard.NeedCardHolder> {

    Context context;
    ArrayList<DonationDetails> donatedDetailsList;
    ArrayList donatedCardDetails;
    NeedListViewAdapter adapter;
    int needId;
    ArrayList<DonatedItemDetails> donatedItemList;
    NeedDetails need;
    int donatedItemId,donatedQuantity;
    DonatedItemDetails donatedItemDetailsTodisplay;
    ArrayList<DonatedItemDetails> needCardData;
    String donorName;
    ArrayList<NeedDetails> needDetailsArrayList;


    public NeedReceivalCard(Context context, ArrayList donatedCardDetails,int needId,ArrayList<NeedDetails> needDetailsArrayList) {
        this.context = context;
        this.donatedCardDetails = donatedCardDetails;
        this.needId=needId;
        this.needDetailsArrayList=needDetailsArrayList;
    }

    @Override
    public NeedCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.receival_cardview, parent, false);
        return new NeedCardHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    public void onBindViewHolder(final NeedCardHolder holder, int position) {


        for (NeedDetails needDetails : needDetailsArrayList) {
            Log.d("All need Id in donate", "doInBackground: " + needDetails.getNeed_id());
            Log.d(TAG, "NeedListViewAdapter: Received Need Id" + needId);
            if (needId != 52) {
                needId = 52;
            }
            if (needDetails.getNeed_id() == needId) {
                need = needDetails;
                break;
            }
        }
            donatedDetailsList = (ArrayList<DonationDetails>) need.getDonations();
            for (int i = 0; i < donatedDetailsList.size(); i++) {
                DonationDetails donationDetails = donatedDetailsList.get(i);

                donatedItemList = (ArrayList<DonatedItemDetails>) donationDetails.getDonated_items();
                donorName = donationDetails.getUser();
                Log.d("donor Name", "doInBackground: " + donorName);
                Log.d("size", "doInBackground: " + donatedItemList.size());

                holder.donorName.setText("abc");
                holder.itemName.setText("1" + "");
                holder.quantity.setText("123" + "");

                for (int j = 0; j < donatedItemList.size(); j++) {
                    DonatedItemDetails donatedItemDetails = (DonatedItemDetails) donatedItemList.get(j);

                    donatedItemId = donatedItemDetails.getDonated_item_id();
                    donatedQuantity = donatedItemDetails.getQuantity();

                    Log.d("donated Item", "doInBackground: " + donatedItemId);
                    Log.d("donated Quantity", "doInBackground: " + donatedQuantity);

                }

                donatedItemDetailsTodisplay = new DonatedItemDetails(donatedItemId, donatedQuantity);
                needCardData =new ArrayList<>();
                needCardData.add(donatedItemDetailsTodisplay);
                DonationDetailsAdapter donationDetailsAdapter=new DonationDetailsAdapter(needCardData);
                holder.donatedItemDetails.setAdapter(donationDetailsAdapter);

        }
    }
    @Override
    public int getItemCount() {
        return donatedCardDetails.size();
    }

    public class NeedCardHolder extends RecyclerView.ViewHolder{

        public ImageView listImg;
        public CircleImageView donorImg,receivedImg;
        public TextView donorName,itemName,quantity,gender;
        public LinearLayout layout;
        public RecyclerView donatedItemDetails;
        public LinearLayout cardListHeading;

        public NeedCardHolder(View itemView) {
            super(itemView);

           // Toast.makeText(context,"receival card holdder",Toast.LENGTH_LONG).show();
             //adapter = new NeedListViewAdapter(context,donatedCardDetails);
           // adapter = new NeedListViewAdapter(context,needDetailsArrayList,needId);

          layout = (LinearLayout) itemView.findViewById(R.id.cardrecyclerViewLinearLayout_ReceivalPage);
            //cardListHeading = (LinearLayout)itemView.findViewById(R.id.cardDonatedListHeadingLinearLayout_ReceivalPage);
            //layout.setVisibility(View.GONE);
           // cardListHeading.setVisibility(View.GONE);

            donatedItemDetails = (RecyclerView)itemView.findViewById(R.id.donatedItemListRecyclerView_ReceivalPage);

            donorImg = (CircleImageView)itemView.findViewById(R.id.donorImgView_ReceivalPage);
            listImg = (ImageView)itemView.findViewById(R.id.listViewImgView_ReceivalPage);
            receivedImg = (CircleImageView) itemView.findViewById(R.id.receivedImgView_ReceivalPage);
            donorName = (TextView)itemView.findViewById(R.id.donorNameTextView_ReceivalPage);
            itemName =(TextView)itemView.findViewById(R.id.itemNameTextView_ReceivalPage);
            quantity = (TextView)itemView.findViewById(R.id.quantityTextView_ReceivalPage);
        }
    }
}
