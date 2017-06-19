package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.internal.zzt.TAG;

public class NeedReceivalCard extends RecyclerView.Adapter<NeedReceivalCard.NeedCardHolder> {

    Context context;
    List<DonationDetails> donationDetailsArrayList;
    NeedListViewAdapter adapter;
    List<DonatedItemDetails> donatedItemDetailsArrayList;

    public NeedReceivalCard(Context context, List<DonationDetails>donationDetailsArrayList) {
        this.context = context;
        this.donationDetailsArrayList = donationDetailsArrayList;
    }

    @Override
    public NeedCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.receival_cardview, parent, false);
        return new NeedCardHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    public void onBindViewHolder(final NeedCardHolder holder, int position) {
        donatedItemDetailsArrayList=donationDetailsArrayList.get(position).getDonateditems();
        System.out.println("size of donated item details"+donatedItemDetailsArrayList.size()+"");

        DonationDetailsAdapter donationDetailsAdapter = new DonationDetailsAdapter(donatedItemDetailsArrayList);
        holder.donatedItemDetailsRecyclerView.setAdapter(donationDetailsAdapter);
        holder.donatedItemDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

    }
    @Override
    public int getItemCount() {
        return donationDetailsArrayList.size();
    }

    public class NeedCardHolder extends RecyclerView.ViewHolder{

        public ImageView listImg;
        public CircleImageView donorImg,receivedImg;
        public TextView donorName,itemName,quantity;
        public LinearLayout layout;
        public RecyclerView donatedItemDetailsRecyclerView;
        public LinearLayout cardListHeading;

        public NeedCardHolder(View itemView) {
            super(itemView);

           // Toast.makeText(context,"receival card holdder",Toast.LENGTH_LONG).show();
             //adapter = new NeedListViewAdapter(context,donatedCardDetails);
           // adapter = new NeedListViewAdapter(context,needDetailsArrayList,needId);

          layout = (LinearLayout) itemView.findViewById(R.id.cardrecyclerViewLinearLayout_ReceivalPage);


            donatedItemDetailsRecyclerView = (RecyclerView)itemView.findViewById(R.id.donatedItemListRecyclerView_ReceivalPage);
            donorImg = (CircleImageView)itemView.findViewById(R.id.donorImgView_ReceivalPage);
            listImg = (ImageView)itemView.findViewById(R.id.listViewImgView_ReceivalPage);
            receivedImg = (CircleImageView) itemView.findViewById(R.id.receivedImgView_ReceivalPage);
            donorName = (TextView)itemView.findViewById(R.id.donorNameTextView_ReceivalPage);
            itemName =(TextView)itemView.findViewById(R.id.itemNameTextView_ReceivalPage);
            quantity = (TextView)itemView.findViewById(R.id.quantityTextView_ReceivalPage);
        }
    }
}
