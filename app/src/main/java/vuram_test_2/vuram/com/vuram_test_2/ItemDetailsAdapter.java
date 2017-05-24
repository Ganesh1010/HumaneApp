package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ItemDetailsAdapter extends RecyclerView.Adapter<ItemDetailsAdapter.MyViewHolder> {

    NeedDetails need;
    List itemslist;
    String[] needName;
    int[] needQuantities;
    Context context;
    OrganisationDetails orgdetails;

    ArrayList<NeedItemDetails> needItemDetailses;
    ArrayList<DonatedItemDetails> donatedItemDetails;
    ArrayList<MainItemDetails> mainItemDetailsList;
    int needItemId,needQuantity,subItemId,needid;
    DonatedItemDetails item;
    public interface OnRecyclerItemClickListener {

        void onRecyclerItemClick(String data);
    }
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView requested;
        ImageView imageView;
        ImageView increment;
        ImageView decrement;
        TextView value;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageView=(ImageView)itemView.findViewById(R.id.imageView);
            this.title = (TextView) itemView.findViewById(R.id.title1);
            this.requested=(TextView) itemView.findViewById(R.id.requested);
            this.increment=(ImageView) itemView.findViewById(R.id.increment_custom);
            this.decrement=(ImageView) itemView.findViewById(R.id.decrement_custom);
            this.value=(TextView) itemView.findViewById(R.id.number_custom);

        }
    }

    public ItemDetailsAdapter(ArrayList<NeedDetails> needdetails,Context context) {
        needid = 0;
        this.context=context;
        need = needdetails.get(needid);
        itemslist=need.getItems();
        needQuantities = new int[itemslist.size()];
        needName = new String[itemslist.size()];

        Log.d("ItemListsize", String.valueOf(itemslist.size()));

        DatabaseHelper db = new DatabaseHelper(context);
        mainItemDetailsList = db.getAllMainItemDetails();



            donatedItemDetails = new ArrayList<DonatedItemDetails>();
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_details, parent, false);

        view.setOnClickListener(OrgDetailsActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.setIsRecyclable(false);
        for (int i = 0; i < itemslist.size(); i++) {
            NeedItemDetails needItemDetails = (NeedItemDetails) itemslist.get(i);
            needItemId = needItemDetails.getItem_type_id();
            needQuantity = needItemDetails.getQuantity();
            Log.d("needItemId", needItemId + "");
            needQuantities[i] = needQuantity;
            //subItemId = needItemDetails.getSub_item_type_id();
            for (int j = 0; j < mainItemDetailsList.size(); j++) {
                MainItemDetails mainItemDetails = mainItemDetailsList.get(j);
                if (needItemId == mainItemDetails.getMainItemCode()) {
                    String mainItemName = mainItemDetails.getMainItemName();
                    needName[i] = mainItemName;
                }
            }
            // holder.imageView.setImageResource(dataSet.get(listPosition).getImage());
        }
        holder.imageView.setImageResource(getimages(listPosition));
        holder.title.setText(needName[listPosition]);
        holder.requested.setText(needQuantities[listPosition]+"");
        holder.increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isExists = false;
                    for (int i = 0; i < donatedItemDetails.size(); i++) {
                        item = donatedItemDetails.get(i);
                            if (item.getNeeditem() == need.getItems().get(listPosition).getNeed_item_id()) {
                                item.setQuantity(item.getQuantity() + 1);
                                donatedItemDetails.set(i, item);
                                isExists = true;
                            }

                    }
                    if (!isExists) {
                        item = new DonatedItemDetails();
                        item.setNeeditem(need.getItems().get(listPosition).getNeed_item_id());
                        item.setQuantity(1);
                        donatedItemDetails.add(item);
                    }
                    holder.value.setText(("Requested: "+item.getQuantity()+""));

                }
            });
            holder.decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isExists = false;
                    for (int i = 0; i < donatedItemDetails.size(); i++) {
                        item = donatedItemDetails.get(i);
                        if (item.getNeeditem() == need.getItems().get(listPosition).getNeed_item_id()) {
                            item.setQuantity(item.getQuantity() - 1);
                            donatedItemDetails.set(i, item);
                            isExists = true;
                        }

                    }
                    if (!isExists) {
                        item = new DonatedItemDetails();
                        item.setNeeditem(need.getItems().get(listPosition).getNeed_item_id());
                        item.setQuantity(1);
                        donatedItemDetails.add(item);
                    }
                    holder.value.setText((item.getQuantity()+""));


                }
            });

            //Log.d("value", dataSet.get(listPosition).getRequested() + "");

    }
    private int getimages(int listPosition) {
        Integer[] drawableArray = {R.drawable.ic_food_black,R.drawable.ic_food_black,R.drawable.ic_food_black,R.drawable.ic_cloth_black,R.drawable.ic_grocery_cart_black,R.drawable.ic_stationery_black};
        return drawableArray[listPosition];
    }

    @Override
    public int getItemCount() {
        return need!=null?need.getItems().size():0;
    }

    }


