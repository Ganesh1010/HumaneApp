package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class HomeActivity extends AppCompatActivity {

    private final int MENU_ITEM_ONE = 1;
    private final int MENU_ITEM_TWO = 2;
    private final int MENU_ITEM_THREE = 3;
    private final int MENU_ITEM_FOUR = 4;
    public static Set<String> appliedFilter;
    Gson gson;
    HttpClient client;
    private final String TAG = "HomeActivity.java";
    private RecyclerView recyclerView;
    private ImageButton filterImageButton;
    private FloatingActionButton newNeedFloatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        appliedFilter=new TreeSet<>();
        /* Spinner */
        SpinnerListener spinnerListener = new SpinnerListener();
        Spinner spinner = (Spinner) findViewById(R.id.author_spinner_donor_home);
        spinner.setOnItemSelectedListener(spinnerListener);
        gson= new Gson();
        List<String> categories = new ArrayList<String>();
        categories.add("Donor");
        categories.add("Organization");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(dataAdapter);

        /* Menu Button */
        final ImageButton menuButton = (ImageButton) findViewById(R.id.menu_imagebutton_donor_home);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(HomeActivity.this, menuButton);
                Menu menu = popupMenu.getMenu();
                menu.add(Menu.NONE, MENU_ITEM_ONE, Menu.NONE, "My Profile");
                menu.add(Menu.NONE, MENU_ITEM_TWO, Menu.NONE, "Settings");
                menu.add(Menu.NONE, MENU_ITEM_THREE, Menu.NONE, "About Us");
                if(Connectivity.getAuthToken(HomeActivity.this,Connectivity.Donor_Token)!=null)
                    menu.add(Menu.NONE, MENU_ITEM_FOUR, Menu.NONE, "Logout");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(HomeActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().toString().equals("My Profile"))
                            startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
                        else if(item.getTitle().toString().equals("Logout"))
                        {
                            Connectivity.deleteAuthToken(HomeActivity.this,Connectivity.Donor_Token);
                            startActivity(new Intent(HomeActivity.this,LoginPage.class));
                            HomeActivity.this.finish();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        /* SearchView */
        final SearchView searchView = (SearchView) findViewById(R.id.searchView_donor_home);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Dynamic",Toast.LENGTH_SHORT).show();
                searchView.setIconified(false);
            }
        });

        new GetNeedItemDetails().execute();
        filterImageButton = (ImageButton) findViewById(R.id.filter_imagebutton_donor_home);
        filterImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,filter.class);
                startActivityForResult(intent, 2);
            }
        });

        getWidgets();
        newNeedFloatingActionButton = (FloatingActionButton) findViewById(R.id.new_need_home_page);
        newNeedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NewNeedActivity.class));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            Iterator i=appliedFilter.iterator();
            while (i.hasNext())
                System.out.println(i.next());
        }
    }

    private void getWidgets() {
        recyclerView = (RecyclerView) findViewById(R.id.needs_recyclerview_home_page);
    }

    class GetNeedItemDetails extends AsyncTask {
        HttpResponse response;
        ArrayList<NeedDetails> needitem;
        @Override
        protected Object doInBackground(Object[] params) {
            response = Connectivity.makeGetRequest(RestAPIURL.needList,client,Connectivity.getAuthToken(HomeActivity.this,Connectivity.Donor_Token));
            if (response != null)
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                try {
                    JSONObject jsonObject=new JSONObject(Connectivity.getJosnFromResponse(response));
                    JSONArray results=jsonObject.getJSONArray("results");
                    Gson gson=new Gson();
                    needitem=gson.fromJson(results.toString(),new TypeToken<List<NeedDetails>>(){}.getType());
                    Log.d("Results",needitem.size()+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }
            else
                    Log.d("CAll ","Response null");
            return null;
        }
        @Override
        protected void onPreExecute() {
            client= new DefaultHttpClient();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if(response!=null)
                if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201)
                {
                     recyclerView.setHasFixedSize(true);
                     recyclerView.setAdapter(new DonorNeedViewAdapter(HomeActivity.this,needitem));
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                    recyclerView.addItemDecoration(dividerItemDecoration);
                   // Toast.makeText(RegistrationPage.this,"Registration Successful.Kindly Login to continue",Toast.LENGTH_LONG).show();
                   // RegistrationPage.this.startActivity(new Intent(RegistrationPage.this,LoginPage.class));
                   // RegistrationPage.this.finish();
                }

            super.onPostExecute(o);
        }
    }
    class SpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            TextView userType = (TextView) parent.getChildAt(0);
            if (userType != null) {
                userType.setTextColor(Color.WHITE);
            }

            String authorType = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), "Selected: " + authorType, Toast.LENGTH_LONG).show();

            if (authorType.equals("Donor")) {
                newNeedFloatingActionButton.setVisibility(View.INVISIBLE);
              //  recyclerView.setAdapter(new DonorNeedViewAdapter(HomeActivity.this, needs));
            } else {
                newNeedFloatingActionButton.setVisibility(View.VISIBLE);
              //  recyclerView.setAdapter(new OrgNeedViewAdapter(HomeActivity.this, needs));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
