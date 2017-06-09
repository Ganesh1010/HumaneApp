package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;



public class NeedReceivalCard extends RecyclerView.Adapter<NeedReceivalCard.NeedCardHolder> {

    Context context;
    ArrayList donatedCardDetails;
    ArrayList<NeedItemDetails> needItems;
    NeedListViewAdapter adapter;
    DonatedItemDetails donatedItem;
    int needId,needQuantity;
    NeedItemDetails needItemDetails;


    public NeedReceivalCard(Context context, ArrayList donatedCardDetails) {
        this.context = context;
        this.donatedCardDetails = donatedCardDetails;
    }

    @Override
    public NeedCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.receival_cardview, parent, false);
        return new NeedCardHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    public void onBindViewHolder(final NeedCardHolder holder, int position) {

        /*DonationDetails items = (DonationDetails) donatedCardDetails.get(position);
        holder.itemName.setText(items.getItemName());
        holder.donorName.setText(items.getDonorName());
        holder.quantity.setText(items.getQuantity());
        */

        DonationDetails items = (DonationDetails) donatedCardDetails.get(position);
        holder.donorName.setText(items.getUser());
        Toast.makeText(context,"donor Name"+items.getUser(),Toast.LENGTH_LONG).show();
        donatedItem = items.getDonateditems().get(0);

          needId = donatedItem.getDonated_item_id();
          needQuantity = donatedItem.getQuantity();

          needItemDetails = new NeedItemDetails(needId,needQuantity);
          needItems = new ArrayList<>();
          needItems.add(needItemDetails);
         Toast.makeText(context,"size"+needItems.size(),Toast.LENGTH_LONG).show();
         // needItemDetails.setNeed_item_id(needId);
          //needItemDetails.setQuantity(needQuantity);
          ///needItemDetails = new DonatedItemDetails(2,100);
          //needItems = new ArrayList();
         //  needItems.add(needItemDetails);

//        holder.itemName.setText(donatedItem.getDonated_item_id());
//        holder.quantity.setText(donatedItem.getQuantity());

        holder.itemName.setText("1");
        holder.quantity.setText("123");

       // Toast.makeText(context,"Quantity"+donatedItem.getQuantity(),Toast.LENGTH_LONG).show();


        holder.donatedItemDetails.setAdapter(adapter);
        //Toast.makeText(context,"after adapter",Toast.LENGTH_LONG).show();
        holder.donatedItemDetails.setLayoutManager(new LinearLayoutManager(context));

      //  Toast.makeText(context,"donor NAme"+items.getItemName(),Toast.LENGTH_LONG).show();*/

        holder.listImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(context," list image",Toast.LENGTH_LONG).show();
                if (holder.layout.getVisibility() == View.GONE) {
                    holder.layout.setVisibility(View.VISIBLE);
                    holder.cardListHeading.setVisibility(View.VISIBLE);
                } else {
                    holder.layout.setVisibility(View.GONE);
                    holder.cardListHeading.setVisibility(View.GONE);
                }
            }
        });
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
            adapter = new NeedListViewAdapter(context,needItems);

            layout = (LinearLayout) itemView.findViewById(R.id.cardrecyclerViewLinearLayout_ReceivalPage);
            cardListHeading = (LinearLayout)itemView.findViewById(R.id.cardDonatedListHeadingLinearLayout_ReceivalPage);
            layout.setVisibility(View.GONE);
            cardListHeading.setVisibility(View.GONE);

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
