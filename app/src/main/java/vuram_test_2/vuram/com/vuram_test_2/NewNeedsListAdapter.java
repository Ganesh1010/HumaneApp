package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NewNeedsListAdapter extends RecyclerView.Adapter<NewNeedsListAdapter.ViewHolder> {
    List<NeedItemDetails> list;
    ArrayList<SubItemDetails> subItemDetails;
    ArrayList<MainItemDetails> mainItemDetails;
    Context context;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    public NewNeedsListAdapter(Context context, ArrayList<NeedItemDetails> list,ArrayList<MainItemDetails>mainItemDetails,ArrayList<SubItemDetails>subItemDetails)
    {
        this.list=list;
        this.context=context;
        this.subItemDetails=subItemDetails;
        this.mainItemDetails=mainItemDetails;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_item, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.clothesViewLayout.setVisibility(View.GONE);
        
        for(int i=0;i<subItemDetails.size();i++)
            if(list.get(position).getSub_item_type_id()==subItemDetails.get(i).getSubItemCode())
            {
                holder.category.setText(holder.category.getText() + mainItemDetails.get(subItemDetails.get(i).getMainItemCode()-1).getMainItemName());
                holder.itemViewName.setText(holder.itemViewName.getText() + subItemDetails.get(i).getSubItemName());
            }

        holder.itemViewQuantity.setText(holder.itemViewQuantity.getText() + "" + list.get(position).getQuantity() + "");

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
        sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
        sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy hh:mm a",Locale.getDefault());
        Date d = null;
        try {
            d = sdf1.parse(list.get(position).getDeadline());
        } catch (ParseException e) {
            try {
                d= sdf2.parse(list.get(position).getDeadline());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        String formattedTime = output.format(d);
        holder.dateView.setText(holder.dateView.getText()+ formattedTime);

        if (list.get(position).getItem_type_id()==2) {
            holder.clothesViewLayout.setVisibility(View.VISIBLE);
            holder.genderView.setText(holder.genderView.getText() + list.get(position).getGender());
            holder.ageView.setText(holder.ageView.getText() + "" + list.get(position).getAge() + "");
        }
        switch (list.get(position).getItem_type_id())
        {
            case 1:
                holder.categoryIcon.setImageResource(R.drawable.ic_food_black);
                break;
            case 2:
                holder.categoryIcon.setImageResource(R.drawable.ic_cloth_black);
                break;
            case 3:
                holder.categoryIcon.setImageResource(R.drawable.ic_blood_black);
                break;
            case 4:
                holder.categoryIcon.setImageResource(R.drawable.ic_grocery_cart_black);
                break;
            case 5:
                holder.categoryIcon.setImageResource(R.drawable.ic_stationery_black);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView category,itemViewName,itemViewQuantity,genderView,ageView,dateView;
        LinearLayout clothesViewLayout;
        ImageView categoryIcon,delete;

        public ViewHolder(View v) {
            super(v);

            category= (TextView) v.findViewById(R.id.category_needForm);
            categoryIcon= (ImageView) v.findViewById(R.id.categoryIcon_needForm);
            itemViewName= (TextView) v.findViewById(R.id.itemViewName_needForm);
            itemViewQuantity= (TextView) v.findViewById(R.id.itemViewQuantity_needForm);
            genderView= (TextView) v.findViewById(R.id.genderView_needForm);
            ageView= (TextView) v.findViewById(R.id.ageView_needForm);
            delete= (ImageView) v.findViewById(R.id.delete_needForm);
            dateView= (TextView) v.findViewById(R.id.dateView_needForm);
            clothesViewLayout= (LinearLayout) v.findViewById(R.id.clothesViewLayout_needForm);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(list.get(getAdapterPosition()).getNeed_item_id());
                }
            });

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            removeAt(list.get(getAdapterPosition()).getNeed_item_id());
            return true;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context,list.get(getAdapterPosition()).getNeed_item_id()+"",Toast.LENGTH_SHORT).show();
        }
    }

    public void removeAt(final int position) {

        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Want to remove item?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}