package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class DonationCartAdapter extends RecyclerView.Adapter<DonationCartAdapter.ViewHolder> {

    DonationDetails donationDetails;
    NeedDetails needDetails;
    Context context;
    ArrayList<MainItemDetails> mainItemDetailsList = new ArrayList<>();
    ArrayList<SubItemDetails> subItemDetailsList = new ArrayList<>();
    DatabaseHelper dbHelper;

    public DonationCartAdapter(Context context, DonationDetails donationDetails,NeedDetails needDetails)
    {
        this.needDetails=needDetails;
        this.donationDetails=donationDetails;
        this.context=context;
    }
    @Override
    public DonationCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_donation_cart_item, parent, false);
        this.dbHelper=new DatabaseHelper(context);
        mainItemDetailsList=dbHelper.getAllMainItemDetails();
        subItemDetailsList=dbHelper.getAllSubItemDetails();
        return new DonationCartAdapter.ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(DonationCartAdapter.ViewHolder holder, int position) {
        for(int i=0;i<needDetails.getItems().size();i++) {

            if(donationDetails.getDonateditems().get(position).getNeed_item_id()==needDetails.getItems().get(i).getNeed_item_id()) {

                for(int j=0;j<mainItemDetailsList.size();j++)
                    if(mainItemDetailsList.get(j).getMainItemCode()==needDetails.getItems().get(i).getItem_type_id())
                        holder.category.setText(mainItemDetailsList.get(j).getMainItemName());

                for(int j=0;j<subItemDetailsList.size();j++)
                    if(subItemDetailsList.get(j).getSubItemCode()==needDetails.getItems().get(i).getSub_item_type_id())
                        holder.itemName.setText(subItemDetailsList.get(j).getSubItemName());

                holder.quantity.setText(donationDetails.getDonateditems().get(position).getQuantity()+"");

                switch (needDetails.getItems().get(i).getItem_type_id())
                {
                    case 1:
                        holder.categoryIcon.setImageResource(R.drawable.ic_food_black);
                        break;
                    case 2:
                        holder.categoryIcon.setImageResource(R.drawable.ic_cloth_black);
                        break;
                    case 4:
                        holder.categoryIcon.setImageResource(R.drawable.ic_grocery_cart_black);
                        break;
                    case 5:
                        holder.categoryIcon.setImageResource(R.drawable.ic_stationery_black);
                        break;
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return donationDetails.getDonateditems().size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView category;
        TextView itemName;
        TextView quantity;
        ImageView categoryIcon;

        public ViewHolder(View v) {
            super(v);

            category= (TextView) v.findViewById(R.id.category_DonationCart);
            itemName= (TextView) v.findViewById(R.id.itemViewName_DonationCart);
            quantity= (TextView) v.findViewById(R.id.itemViewQuantity_DonationCart);
            categoryIcon= (ImageView) v.findViewById(R.id.categoryIcon_DonationCart);

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
