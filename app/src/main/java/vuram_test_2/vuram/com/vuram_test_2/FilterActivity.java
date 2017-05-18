package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recyclerView);

        filterItems= new ArrayList<>();

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


        recyclerView.setAdapter(new FilterRecycleAdapter(this,filterItems));
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                setResult(2,intent);
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

    }
}
