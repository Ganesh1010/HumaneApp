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

public class filterRecycleAdapter extends RecyclerView.Adapter<filterRecycleAdapter.ViewHolder> {
    List<filterItemCategoryList> list;
    Context context;
    public static ArrayList<CheckBox>allCheckBoxes=new ArrayList<>();
    public filterRecycleAdapter(Context context, ArrayList<filterItemCategoryList> list)
    {
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
                    if (isChecked)
                    {
                        if (itemName.getText().toString().equals("Male")) {
                            genderMale.setVisibility(View.VISIBLE);
                            final CheckBox children= (CheckBox) genderMale.findViewById(R.id.children);
                            final CheckBox adult= (CheckBox) genderMale.findViewById(R.id.adult);
                            final CheckBox elder= (CheckBox) genderMale.findViewById(R.id.elder);
                            children.setChecked(true);
                            adult.setChecked(true);
                            elder.setChecked(true);
                            allCheckBoxes.add(children);
                            allCheckBoxes.add(adult);
                            allCheckBoxes.add(elder);
                            HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+children.getText().toString());
                            HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+adult.getText().toString());
                            HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+elder.getText().toString());

                            children.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked)
                                        HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+children.getText().toString());
                                    else
                                        HomeActivity.appliedFilter.remove(itemName.getText().toString()+" "+children.getText().toString());
                                }
                            });
                            adult.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked)
                                        HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+adult.getText().toString());
                                    else
                                        HomeActivity.appliedFilter.remove(itemName.getText().toString()+" "+adult.getText().toString());
                                }
                            });
                            elder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked)
                                        HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+elder.getText().toString());
                                    else
                                        HomeActivity.appliedFilter.remove(itemName.getText().toString()+" "+elder.getText().toString());
                                }
                            });
                        }

                        else if (itemName.getText().toString().equals("Female")) {
                            genderFemale.setVisibility(View.VISIBLE);

                            final CheckBox children= (CheckBox) genderFemale.findViewById(R.id.children);
                            final CheckBox adult= (CheckBox) genderFemale.findViewById(R.id.adult);
                            final CheckBox elder= (CheckBox) genderFemale.findViewById(R.id.elder);
                            children.setChecked(true);
                            adult.setChecked(true);
                            elder.setChecked(true);
                            allCheckBoxes.add(children);
                            allCheckBoxes.add(adult);
                            allCheckBoxes.add(elder);
                            HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+children.getText().toString());
                            HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+adult.getText().toString());
                            HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+elder.getText().toString());

                            children.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked)
                                        HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+children.getText().toString());
                                }
                            });
                            adult.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked)
                                        HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+adult.getText().toString());
                                }
                            });
                            elder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked)
                                        HomeActivity.appliedFilter.add(itemName.getText().toString()+" "+elder.getText().toString());
                                }
                            });
                        }
                            HomeActivity.appliedFilter.add(itemName.getText().toString());
                    }

                    else {
                        if (itemName.getText().toString().equals("Male")) {
                            HomeActivity.appliedFilter.remove("Male Children");
                            HomeActivity.appliedFilter.remove("Male Adult");
                            HomeActivity.appliedFilter.remove("Male Elder");

                            genderMale.setVisibility(View.GONE);
                        }
                        else if (itemName.getText().toString().equals("Female")) {

                            HomeActivity.appliedFilter.remove("Female Children");
                            HomeActivity.appliedFilter.remove("Female Adult");
                            HomeActivity.appliedFilter.remove("Female Elder");
                            genderFemale.setVisibility(View.GONE);
                        }

                        HomeActivity.appliedFilter.remove(itemName.getText().toString());
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
