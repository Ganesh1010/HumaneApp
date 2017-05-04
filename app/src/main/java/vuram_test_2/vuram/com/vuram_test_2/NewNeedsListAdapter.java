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
import java.util.ArrayList;
import java.util.List;

public class NewNeedsListAdapter extends RecyclerView.Adapter<NewNeedsListAdapter.ViewHolder> {
    List<NeedClass> list;
    Context context;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    public NewNeedsListAdapter(Context context, ArrayList<NeedClass> list)
    {
        this.list=list;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_item, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.clothesViewLayout.setVisibility(View.GONE);
        holder.category.setText(holder.category.getText() + list.get(position).getCategory());
        holder.itemViewName.setText(holder.itemViewName.getText() + list.get(position).getItemName());
        holder.itemViewQuantity.setText(holder.itemViewQuantity.getText() + "" + list.get(position).getItemQuantity() + "");
        holder.dateView.setText(holder.dateView.getText()+ list.get(position).getDate()+"  "+list.get(position).getTime());

        if (list.get(position).getCategory().equals("Clothes")) {
            holder.clothesViewLayout.setVisibility(View.VISIBLE);
            holder.genderView.setText(holder.genderView.getText() + list.get(position).getGender());
            holder.ageView.setText(holder.ageView.getText() + list.get(position).getAge());
        }
        switch (list.get(position).getCategory())
        {
            case "Food":
                holder.categoryIcon.setImageResource(R.drawable.ic_food_black);
                break;
            case "Clothes":
                holder.categoryIcon.setImageResource(R.drawable.ic_cloth_black);
                break;
            case "Groceries":
                holder.categoryIcon.setImageResource(R.drawable.ic_grocery_cart_black);
                break;
            case "Stationeries":
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
        ImageView categoryIcon,edit,delete;

        public ViewHolder(View v) {
            super(v);

            category= (TextView) v.findViewById(R.id.category_needForm);
            categoryIcon= (ImageView) v.findViewById(R.id.categoryIcon_needForm);
            itemViewName= (TextView) v.findViewById(R.id.itemViewName_needForm);
            itemViewQuantity= (TextView) v.findViewById(R.id.itemViewQuantity_needForm);
            genderView= (TextView) v.findViewById(R.id.genderView_needForm);
            ageView= (TextView) v.findViewById(R.id.ageView_needForm);
            edit= (ImageView) v.findViewById(R.id.edit_needForm);
            delete= (ImageView) v.findViewById(R.id.delete_needForm);
            dateView= (TextView) v.findViewById(R.id.dateView_needForm);
            clothesViewLayout= (LinearLayout) v.findViewById(R.id.clothesViewLayout_needForm);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"edit",Toast.LENGTH_SHORT).show();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(list.get(getAdapterPosition()).getItemNumber());
                }
            });

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            removeAt(list.get(getAdapterPosition()).getItemNumber());
            return true;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context,list.get(getAdapterPosition()).getItemNumber()+"",Toast.LENGTH_SHORT).show();
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