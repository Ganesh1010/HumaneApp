package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {

    ArrayList<FilterItemCategoryList>filterItemCategoryLists;
    ArrayList<String>subItemNameList;
    FilterItemCategoryList filterItemCategoryList;
    RecyclerView filterItemRecyclerView;

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
            if(db.getAllMainItemDetails()!=null)
                mainItemDetailsList = db.getAllMainItemDetails();
            if(db.getAllSubItemDetails()!=null)
                subItemDetailsList = db.getAllSubItemDetails();
            filterItemCategoryLists = new ArrayList<>();

            for (MainItemDetails mainItemDetails: mainItemDetailsList)
            {
                int mainItemCode = mainItemDetails.getMainItemCode();
                String mainItemName = mainItemDetails.getMainItemName();
                filterItemCategoryList=new FilterItemCategoryList();
                filterItemCategoryList.setMainItemName(mainItemName);
                subItemNameList=new ArrayList();

                for (SubItemDetails subItemDetails:subItemDetailsList)
                {
                    if (subItemDetails.getMainItemCode() == mainItemCode) {
                        String subItemName = subItemDetails.getSubItemName();
                        subItemNameList.add(subItemName);
                    }
                }
                filterItemCategoryList.setSubItemNameList(subItemNameList);
                filterItemCategoryLists.add(filterItemCategoryList);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(progressDialog.isShowing())
                progressDialog.cancel();

            filterItemRecyclerView = (RecyclerView) findViewById(R.id.filter_recycler_view);
            filterItemRecyclerView.setAdapter(new FilterRecyclerAdapter(FilterActivity.this,filterItemCategoryLists,mainItemDetailsList,subItemDetailsList));
            filterItemRecyclerView.setLayoutManager(new LinearLayoutManager(FilterActivity.this));

            findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    for(int i = 0; i< FilterRecyclerAdapter.filterItemCheckBoxes.size(); i++)
                        FilterRecyclerAdapter.filterItemCheckBoxes.get(i).setChecked(false);
                    HomeActivity.filterItems.clear();
                }
            });
        }
    }
}
