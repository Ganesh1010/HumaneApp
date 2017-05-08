package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by akshayagr on 21-03-2017.
 */

public class NeedReceivalCard extends RecyclerView.Adapter<NeedReceivalCard.NeedCardHolder> {

    Context context;
    ArrayList donatedCardDetails;
    NeedListViewAdapter adapter;

    public NeedReceivalCard(Context context, ArrayList donatedCardDetails) {
        this.context = context;
        this.donatedCardDetails = donatedCardDetails;
    }

    @Override
    public NeedCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Akshaya Changed..
        View v = LayoutInflater.from(context).inflate(R.layout.receival_cardview,parent,false);
        return new NeedCardHolder(v);
    }

    @Override
    public void onBindViewHolder(final NeedCardHolder holder, int position) {


        NeedListViewItems items = (NeedListViewItems) donatedCardDetails.get(position);
        holder.donorName.setText(items.getDonorName());
       // Toast.makeText(context,"donor NAme"+items.getDonorName(),Toast.LENGTH_LONG).show();
        holder.itemName.setText(items.getItemName());
        //Toast.makeText(context,"Item NAme"+items.getItemName(),Toast.LENGTH_LONG).show();
        holder.gender.setText(items.getGender());
        //Toast.makeText(context,"Gender"+items.getGender(),Toast.LENGTH_LONG).show();
        holder.quantity.setText(items.getQuantity());
        //Toast.makeText(context,"Quantity"+items.getQuantity(),Toast.LENGTH_LONG).show();


        holder.donatedItemDetails.setAdapter(adapter);
        //Toast.makeText(context,"after adapter",Toast.LENGTH_LONG).show();
        holder.donatedItemDetails.setLayoutManager(new LinearLayoutManager(context));

        //Toast.makeText(context,"donor NAme"+items.getItemName(),Toast.LENGTH_LONG).show();

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
            adapter = new NeedListViewAdapter(context,donatedCardDetails);

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
            gender = (TextView)itemView.findViewById(R.id.genderTextView_ReceivalPage);
            quantity = (TextView)itemView.findViewById(R.id.quantityTextView_ReceivalPage);
        }
    }
}
