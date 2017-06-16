package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommonUI.TAG;

public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerAdapter.ViewHolder> {

    public List<FilterItemCategoryList>filterItemCategoryLists;
    public Context context;
    public ArrayList<MainItemDetails> mainItemDetails;
    public ArrayList<SubItemDetails> subItemDetails;
    public static ArrayList<CheckBox> filterItemCheckBoxes=new ArrayList<>();
    public int selectedMainItemCode,selectedSubItemCode;
    public ArrayList<Integer> subItemCodeArrayList;

    public FilterRecyclerAdapter(Context context, ArrayList<FilterItemCategoryList> filterItemCategoryLists,ArrayList<MainItemDetails> mainItemDetails,ArrayList<SubItemDetails> subItemDetails) {
        this.filterItemCategoryLists=filterItemCategoryLists;
        this.context=context;
        this.mainItemDetails=mainItemDetails;
        this.subItemDetails=subItemDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_row_view_item, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mainItemName.setText(filterItemCategoryLists.get(position).getMainItemName());
        for(int i=0;i<mainItemDetails.size();i++) {
            if (mainItemDetails.get(i).getMainItemName() ==filterItemCategoryLists.get(position).getMainItemName())
                switch (mainItemDetails.get(i).getMainItemCode()) {
                    case 1:
                        holder.filterMainItemIcon.setImageResource(R.drawable.ic_food_black);
                        break;
                    case 2:
                        holder.filterMainItemIcon.setImageResource(R.drawable.ic_cloth_black);
                        break;
                    case 3:
                        holder.filterMainItemIcon.setImageResource(R.drawable.ic_blood_black);
                        break;
                    case 4:
                        holder.filterMainItemIcon.setImageResource(R.drawable.ic_grocery_cart_black);
                        break;
                    case 5:
                        holder.filterMainItemIcon.setImageResource(R.drawable.ic_stationery_black);
                        break;
                }
        }

//        final LinearLayout genderMale= new LinearLayout(context);
//        final LinearLayout genderFemale= new LinearLayout(context);
//        LayoutInflater inflater;
//        View view;
        
        for (int i = 0; i < filterItemCategoryLists.get(position).getSubItemNameList().size(); i++)
        {
            final CheckBox filterItemName = new CheckBox(context);
            filterItemName.setText(filterItemCategoryLists.get(position).getSubItemNameList().get(i) + "");

            Iterator it = HomeActivity.filterItems.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry<Integer,ArrayList<Integer>> filter = (Map.Entry)it.next();

                subItemCodeArrayList=filter.getValue();
                for(int j=0;j<subItemCodeArrayList.size();j++)
                    for (int k = 0; k < subItemDetails.size(); k++)
                        if (subItemCodeArrayList.get(j) == subItemDetails.get(k).getSubItemCode())
                            if (subItemDetails.get(k).getSubItemName().equals(filterItemName.getText().toString())) {
                                filterItemName.setChecked(true);
                                break;
                            }
            }

//            if(filterItemName.getText().toString().equals("Male"))
//            {
//                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                view = inflater.inflate(R.layout.filter_clothes_gender, null);
//                genderMale.addView(view);
//                holder.filterLayout.addView(filterItemName);
//                holder.filterLayout.addView(genderMale);
//
//                genderMale.setVisibility(View.GONE);
//            }
//            else if(filterItemName.getText().toString().equals("Female"))
//            {
//                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                view = inflater.inflate(R.layout.filter_clothes_gender, null);
//                genderFemale.addView(view);
//                holder.filterLayout.addView(filterItemName);
//                holder.filterLayout.addView(genderFemale);
//
//                genderFemale.setVisibility(View.GONE);
//            }
            
            holder.filterLayout.addView(filterItemName);
            filterItemCheckBoxes.add(filterItemName);

            filterItemName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String selectedSubItemName = filterItemName.getText().toString();
                    if (isChecked) {
                        // Finding subItemCodeArrayList & mainItemCode of this checked item
                        for (SubItemDetails singleSubItemDetails : subItemDetails) {
                            if (singleSubItemDetails != null) {
                                if (singleSubItemDetails.getSubItemName().equals(selectedSubItemName)) {
                                    selectedMainItemCode = singleSubItemDetails.getMainItemCode();
                                    selectedSubItemCode = singleSubItemDetails.getSubItemCode();
                                    if (HomeActivity.filterItems.size() == 0) {
                                        System.out.println("new entry in filter list");
                                        subItemCodeArrayList = new ArrayList<>();
                                    } else {
                                        boolean isNewItem = true;
                                        for (Iterator<Map.Entry<Integer, ArrayList<Integer>>> it = HomeActivity.filterItems.entrySet().iterator(); it.hasNext(); ) {
                                            Map.Entry<Integer, ArrayList<Integer>> filter = (Map.Entry) it.next();
                                            if (filter.getKey() == selectedMainItemCode) {
                                                isNewItem = false;
                                                System.out.println("update entry in filter list");
                                                subItemCodeArrayList = filter.getValue();
                                            }
                                        }
                                        if (isNewItem) {
                                            System.out.println("new entryin filter list");
                                            subItemCodeArrayList = new ArrayList<>();
                                        }
                                    }
                                    subItemCodeArrayList.add(selectedSubItemCode);
                                    HomeActivity.filterItems.put(selectedMainItemCode, subItemCodeArrayList);
                                }
                            }
                        }
                    }
                    else
                    {
                        for (SubItemDetails singleSubItemDetails : subItemDetails) {
                            if(singleSubItemDetails!=null) {
                                if (singleSubItemDetails.getSubItemName().equals(selectedSubItemName)) {
                                    selectedMainItemCode = singleSubItemDetails.getMainItemCode();
                                    selectedSubItemCode = singleSubItemDetails.getSubItemCode();
                                    for (Iterator<Map.Entry<Integer, ArrayList<Integer>>> it = HomeActivity.filterItems.entrySet().iterator(); it.hasNext(); ) {
                                        Map.Entry<Integer, ArrayList<Integer>> filter = (Map.Entry) it.next();
                                        if (filter.getKey() == selectedMainItemCode) {
                                            subItemCodeArrayList = filter.getValue();
                                            subItemCodeArrayList.remove(new Integer(selectedSubItemCode));
                                            HomeActivity.filterItems.put(selectedMainItemCode, subItemCodeArrayList);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return filterItemCategoryLists.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mainItemName;
        public LinearLayout filterLayout;
        public ImageView filterMainItemIcon;
        public ImageView indicationIcon;
        public View view;

        public ViewHolder(View v) {
            super(v);
            mainItemName= (TextView) v.findViewById(R.id.filter_main_item_name);
            filterLayout= (LinearLayout) v.findViewById(R.id.filterLayout);
            filterMainItemIcon= (ImageView) v.findViewById(R.id.filter_main_item_icon);
            indicationIcon= (ImageView) v.findViewById(R.id.filter_item_indication_icon);
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
