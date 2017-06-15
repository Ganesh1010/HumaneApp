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
import java.util.Map;

public class FilterActivity extends AppCompatActivity {

    ArrayList<FilterItemCategoryList> filterItems;
    ArrayList<String>itemName;
    FilterItemCategoryList item;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        new FilterPreparationTask().execute();

    }
    class FilterPreparationTask extends AsyncTask {

        ProgressDialog progressDialog;
        DatabaseHelper db;
        ArrayList<MainItemDetails> mainItemDetailsList;
        ArrayList<SubItemDetails> subItemDetailsList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FilterActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading filter details...");
            progressDialog.setCanceledOnTouchOutside(false);
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
            recyclerView.setAdapter(new FilterRecycleAdapter(FilterActivity.this,filterItems,mainItemDetailsList,subItemDetailsList));
            recyclerView.setLayoutManager(new GridLayoutManager(FilterActivity.this,1));

            findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("here in apply");
                    Map<Integer,ArrayList<Integer>> map = HomeActivity.filterItems;
                    for (Map.Entry<Integer,ArrayList<Integer>> entry : map.entrySet())
                    {
                        for(int i=0;i<entry.getValue().size();i++)
                            System.out.println(entry.getKey() + "/" + entry.getValue().get(i));
                    }

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
                    HomeActivity.filterItems.clear();
                }
            });
            progressDialog.cancel();
        }
    }
}
