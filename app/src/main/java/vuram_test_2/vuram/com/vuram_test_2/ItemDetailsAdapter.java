package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.readystatesoftware.viewbadger.BadgeView;
import java.util.ArrayList;
import vuram_test_2.vuram.com.vuram_test_2.util.CommonUI;

public class ItemDetailsAdapter extends RecyclerView.Adapter<ItemDetailsAdapter.MyViewHolder> {
    int quantityEachNeed=0,needId;
    int countDonatedItems=0;
    Context context;
    Activity activity;
    ArrayList<NeedItemDetails>needItemDetails;
    ArrayList<DonatedItemDetails> donatedItemDetailsList;
    public ArrayList<MainItemDetails> mainItemDetails;
    public ArrayList<SubItemDetails> subItemDetails;
    public DonationDetails donationDetails=new DonationDetails();
    public DonatedItemDetails donatedItemDetails;
    public DatabaseHelper db;
    View target;
    BadgeView badge;
    CommonUI commonUI=new CommonUI();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView requested;
        ImageView imageView;
        ImageView increment;
        ImageView decrement;
        TextView value;
        TextView requestedQuantity;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageView=(ImageView)itemView.findViewById(R.id.imageView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.requested=(TextView) itemView.findViewById(R.id.requested);
            this.increment=(ImageView) itemView.findViewById(R.id.increment_custom);
            this.decrement=(ImageView) itemView.findViewById(R.id.decrement_custom);
            this.value=(TextView) itemView.findViewById(R.id.number_custom);
            this.requestedQuantity= (TextView) itemView.findViewById(R.id.requested_amount);

        }
    }

    public ItemDetailsAdapter(ArrayList<NeedItemDetails>needItemDetails,Context context,Activity activity,String needId) {
        this.needItemDetails=needItemDetails;
        this.context=context;
        this.activity=activity;
        this.needId=Integer.parseInt(needId);
        db=new DatabaseHelper(this.activity);
        mainItemDetails=db.getAllMainItemDetails();
        subItemDetails=db.getAllSubItemDetails();
        donatedItemDetailsList = new ArrayList<>();
        target = activity.findViewById(R.id.cart);
        badge = new BadgeView(context, target);
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_details, parent, false);
        view.setOnClickListener(OrgDetailsActivity.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.setIsRecyclable(false);
        final NeedItemDetails particularNeedItemDetails=needItemDetails.get(listPosition);

        holder.imageView.setImageResource(getItemImages(particularNeedItemDetails.getItem_type_id() - 1));

        for(int i=0;i<mainItemDetails.size();i++)
            if(mainItemDetails.get(i).getMainItemCode()==particularNeedItemDetails.getItem_type_id())
                holder.title.setText(mainItemDetails.get(i).getMainItemName());
        for(int i=0;i<subItemDetails.size();i++)
            if(subItemDetails.get(i).getSubItemCode()==particularNeedItemDetails.getSub_item_type_id())
                holder.requested.setText(subItemDetails.get(i).getSubItemName());

        holder.requestedQuantity.setText("Requested : "+particularNeedItemDetails.getQuantity()+"");

        if(quantityEachNeed==0)
            holder.decrement.setVisibility(View.INVISIBLE);
        holder.increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ++quantityEachNeed;
                    if(quantityEachNeed>0)
                        holder.decrement.setVisibility(View.VISIBLE);
                    boolean isExists = false;
                    for (int i = 0; i < donatedItemDetailsList.size(); i++) {
                        donatedItemDetails = donatedItemDetailsList.get(i);
                        if (donatedItemDetails.getNeed_item_id() == particularNeedItemDetails.getNeed_item_id())
                        {
                            donatedItemDetails.setQuantity(donatedItemDetails.getQuantity() + 1);
                            if(donatedItemDetails.getQuantity()==particularNeedItemDetails.getQuantity())
                                holder.increment.setVisibility(View.INVISIBLE);
                            donatedItemDetailsList.set(i, donatedItemDetails);
                            isExists = true;
                            break;
                        }
                    }
                    if (!isExists) {
                        donatedItemDetails=new DonatedItemDetails();
                        donatedItemDetails.setNeed_item_id(particularNeedItemDetails.getNeed_item_id());
                        donatedItemDetails.setQuantity(1);
                        donatedItemDetailsList.add(donatedItemDetails);
                        ++countDonatedItems;
                    }

                    holder.value.setText((donatedItemDetails.getQuantity()+""));
                    donationDetails.setDonateditems(donatedItemDetailsList);
                    donationDetails.setNeed_id(needId);
                    commonUI.displayCheckoutUI(activity.findViewById(R.id.activity_donor_org_details),quantityEachNeed,activity,donationDetails);
                    badge.setText(countDonatedItems+"");
                    if(countDonatedItems>0)
                        badge.setVisibility(View.VISIBLE);
                    badge.show();
                }
            });

            holder.decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    --quantityEachNeed;
                    if(quantityEachNeed<particularNeedItemDetails.quantity)
                        holder.increment.setVisibility(View.VISIBLE);
                    for (int i = 0; i < donatedItemDetailsList.size(); i++) {
                        donatedItemDetails = donatedItemDetailsList.get(i);
                        if (donatedItemDetails.getNeed_item_id() == particularNeedItemDetails.getNeed_item_id())
                        {
                            donatedItemDetails.setQuantity(donatedItemDetails.getQuantity() - 1);
                            if(donatedItemDetails.getQuantity()==0)
                            {
                                holder.decrement.setVisibility(View.INVISIBLE);
                                donatedItemDetailsList.remove(i);
                                --countDonatedItems;
                            }
                            else
                                donatedItemDetailsList.set(i, donatedItemDetails);
                            break;
                        }
                    }

                    holder.value.setText((donatedItemDetails.getQuantity()+""));

                    commonUI.displayCheckoutUI(activity.findViewById(R.id.activity_donor_org_details),quantityEachNeed,activity,donationDetails);
                    badge.setText(countDonatedItems+"");
                    if(countDonatedItems==0)
                        badge.setVisibility(View.INVISIBLE);
                    badge.show();
                }
            });
        //Log.d("value", dataSet.get(listPosition).getRequested() + "");

    }
    private int getItemImages(int listPosition) {
        Integer[] drawableArray = {R.drawable.ic_food_black,R.drawable.ic_cloth_black,R.drawable.ic_blood_black,R.drawable.ic_grocery_cart_black,R.drawable.ic_stationery_black};
        return drawableArray[listPosition];
    }

    @Override
    public int getItemCount() {
        return needItemDetails!=null?needItemDetails.size():0;
    }
    }


