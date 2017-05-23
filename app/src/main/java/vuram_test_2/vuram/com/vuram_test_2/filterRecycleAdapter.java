package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FilterRecycleAdapter extends RecyclerView.Adapter<FilterRecycleAdapter.ViewHolder> {

    List<FilterItemCategoryList> list;
    Context context;
    public static ArrayList<CheckBox> allCheckBoxes=new ArrayList<>();
    public static ArrayList<SubItemDetails> subItemDetailsArrayList;

    public FilterRecycleAdapter(Context context, ArrayList<FilterItemCategoryList> list) {
        this.list=list;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_row_view_item, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.itemCategory.setText(list.get(position).getItemCategory());
        switch (list.get(position).getItemCategory())
        {
            case "Food":
                holder.filterItemIcon.setImageResource(R.drawable.ic_food_black);
                break;
            case "Clothes":
                holder.filterItemIcon.setImageResource(R.drawable.ic_cloth_black);
                break;
            case "Groceries":
                holder.filterItemIcon.setImageResource(R.drawable.ic_grocery_cart_black);
                break;
            case "Stationeries":
                holder.filterItemIcon.setImageResource(R.drawable.ic_stationery_black);
                break;
        }
        final LinearLayout genderMale= new LinearLayout(context);
        final LinearLayout genderFemale= new LinearLayout(context);
        LayoutInflater inflater;
        View view;
        for (int i = 0; i < list.get(position).getItemName().size(); i++) {
            final CheckBox itemName = new CheckBox(context);
            itemName.setText(list.get(position).getItemName().get(i) + "");

            if(HomeActivity.appliedFilter.contains(itemName.getText().toString()))
                itemName.setChecked(true);

            if(itemName.getText().toString().equals("Male"))
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.filter_clothes_gender, null);
                genderMale.addView(view);
                holder.filterLayout.addView(itemName);
                holder.filterLayout.addView(genderMale);

                genderMale.setVisibility(View.GONE);
            }
            else if(itemName.getText().toString().equals("Female"))
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.filter_clothes_gender, null);
                genderFemale.addView(view);
                holder.filterLayout.addView(itemName);
                holder.filterLayout.addView(genderFemale);

                genderFemale.setVisibility(View.GONE);
            }
            else
                holder.filterLayout.addView(itemName);

            allCheckBoxes.add(itemName);

            itemName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String subItemName = itemName.getText().toString();
                    if (isChecked) {
                        // Finding subItemCode & mainItemCode of this checked item
                        DatabaseHelper db = new DatabaseHelper(context);
                        ArrayList<SubItemDetails> subItemDetailsArrayList = db.getAllSubItemDetails();
                        for (int i = 0; i < subItemDetailsArrayList.size(); i++) {
                            SubItemDetails subItemDetails = subItemDetailsArrayList.get(i);
                            if (subItemDetails.getSubItemName().equals(subItemName)) {
                                subItemDetailsArrayList.add(subItemDetails);
                                break;
                            }
                        }
                        // following line is created by Rahul
                        HomeActivity.appliedFilter.add(subItemName);
                    } else {
                        // Searching for this subItem in checked list
                        for (int i = 0; i < subItemDetailsArrayList.size(); i++) {
                            SubItemDetails subItemDetails = subItemDetailsArrayList.get(i);
                            if (subItemDetails.getSubItemName().equals(subItemName)) {
                                subItemDetailsArrayList.remove(i);
                                break;
                            }
                        }

                        // following line is created by Rahul
                        HomeActivity.appliedFilter.remove(subItemName);
                    }
                    //Toast.makeText(context, itemName.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemCategory;
        public LinearLayout filterLayout;
        public ImageView filterItemIcon;
        public ImageView indicationIcon;
        public View view;

        public ViewHolder(View v) {
            super(v);
            //System.out.println(v.getTag().toString());
            itemCategory= (TextView) v.findViewById(R.id.itemCategory);
            if (itemCategory == null) {
                System.out.println("here");
            }
            filterLayout= (LinearLayout) v.findViewById(R.id.filterLayout);
            filterItemIcon= (ImageView) v.findViewById(R.id.itemIcon);
            indicationIcon= (ImageView) v.findViewById(R.id.indicationIcon);
            view=v.findViewById(R.id.view);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(filterLayout.getVisibility() == View.VISIBLE) {
                filterLayout.setVisibility(View.GONE);
                indicationIcon.setImageResource(R.drawable.ic_arrow_down_black);
                view.setVisibility(View.INVISIBLE);
            }
            else {
                filterLayout.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                indicationIcon.setImageResource(R.drawable.ic_arrow_up_black);
            }
        }
    }
}
