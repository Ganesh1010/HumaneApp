package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    ArrayList<FilterItemCategoryList> filterItems;
    ArrayList<String>itemName;
    FilterItemCategoryList item;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        /*
        item=new FilterItemCategoryList();
        itemName=new ArrayList();
        itemName.add("Breakfast");
        itemName.add("Lunch");
        itemName.add("Dinner");
        itemName.add("Snacks");
        item.setItemCategory("Food");
        item.setItemName(itemName);
        filterItems.add(item);

        item=new FilterItemCategoryList();
        itemName=new ArrayList();
        itemName.add("Male");
        itemName.add("Female");
        item.setItemCategory("Clothes");
        item.setItemName(itemName);
        filterItems.add(item);

        item=new FilterItemCategoryList();
        itemName=new ArrayList();
        itemName.add("wheat");
        itemName.add("rice");
        itemName.add("cereals");
        itemName.add("sugar");
        item.setItemCategory("Groceries");
        item.setItemName(itemName);
        filterItems.add(item);

        item=new FilterItemCategoryList();
        itemName=new ArrayList();
        itemName.add("Pen");
        itemName.add("Pencil");
        itemName.add("Eraser");
        itemName.add("Books");
        item.setItemCategory("Stationeries");
        item.setItemName(itemName);
        filterItems.add(item);
        */

        //
        new FilterPreparationTask().execute();
        //

    }
    class FilterPreparationTask extends AsyncTask {

        ProgressDialog progressDialog;
        DatabaseHelper db;
        ArrayList<MainItemDetails> mainItemDetailsList;
        ArrayList<SubItemDetails> subItemDetailsList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FilterActivity.this);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            db = new DatabaseHelper(FilterActivity.this);
            mainItemDetailsList = db.getAllMainItemDetails();
            subItemDetailsList = db.getAllSubItemDetails();
            filterItems = new ArrayList<>();
            for (int i = 0; i < mainItemDetailsList.size(); i++) {
                MainItemDetails mainItemDetails = mainItemDetailsList.get(i);
                int mainItemCode = mainItemDetails.getMainItemCode();
                String mainItemName = mainItemDetails.getMainItemName();
                item=new FilterItemCategoryList();
                item.setItemCategory(mainItemName);
                itemName=new ArrayList();
                for (int j = 0; j < subItemDetailsList.size(); j++) {
                    SubItemDetails subItemDetails = subItemDetailsList.get(j);
                    if (subItemDetails.getMainItemCode() == mainItemCode) {
                        String subItemName = subItemDetails.getSubItemName();
                        itemName.add(subItemName);
                    }
                }
                item.setItemName(itemName);
                filterItems.add(item);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setAdapter(new FilterRecycleAdapter(FilterActivity.this,filterItems));
            recyclerView.setLayoutManager(new GridLayoutManager(FilterActivity.this,1));

            findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    setResult(HomeActivity.FILTER_REQUEST,intent);
                    finish();
                }
            });

            findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i = 0; i< FilterRecycleAdapter.allCheckBoxes.size(); i++)
                        FilterRecycleAdapter.allCheckBoxes.get(i).setChecked(false);
                    HomeActivity.appliedFilter.clear();
                }
            });
            progressDialog.cancel();
        }
    }
}
