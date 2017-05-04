package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class HomeActivity extends AppCompatActivity {

    private final int MENU_ITEM_ONE = 1;
    private final int MENU_ITEM_TWO = 2;
    private final int MENU_ITEM_THREE = 3;
    private final int MENU_ITEM_FOUR = 4;
    public static Set<String> appliedFilter;
    Gson gson;
    HttpClient client;
    private final String TAG = "HomeActivity.java";
    ArrayList<TestNeedDetails> needs;
    NeedItemDetails[] itemDetailses;
    private RecyclerView recyclerView;
    private ImageButton filterImageButton;
    private FloatingActionButton newNeedFloatingActionButton;
    NeedItemDetails needItemDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        needs = new ArrayList<>();
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
               // menu.add(Menu.NONE, MENU_ITEM_FOUR, Menu.NONE, "Login");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(HomeActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().toString().equals("My Profile"))
                            startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
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

        /* RecyclerView */
        getWidgets();
        initializeNeeds();

        Log.d("Size",needs.size()+"");
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new DonorNeedViewAdapter(this, needs));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);

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

    private void initializeNeeds() {
        Random randomNumberGenerator = new Random();
        int needsCount = randomNumberGenerator.nextInt(6) + 15;
        Log.d(TAG, "initializeNeeds: Needs : " + needsCount);
        for (int i = 0; i < needsCount; i++) {
            int sumOfDatisfiedPercent = 0;
            TestNeedDetails needDetails = new TestNeedDetails();
            needDetails.orgName =   "Vuram Technologies Solutions\n";
            needDetails.orgAddress = "Chennai - 600093\n";
            needDetails.orgContactNo = "123456789";
            needDetails.orgLogo = "";

            int itemsCount = randomNumberGenerator.nextInt(6) + 1;
            Log.d(TAG, "initializeNeeds: Items : " + itemsCount);
            for (int j = 0; j < itemsCount; j++) {
                ItemDetails itemDetails = new ItemDetails();
                itemDetails.itemName = "Food";
                itemDetails.itemIcon = "";
                int itemStatus = randomNumberGenerator.nextInt(71) + 30;
                itemDetails.satisfiedPercentage = itemStatus;

                needDetails.itemDetailsList.add(itemDetails);

                sumOfDatisfiedPercent += itemStatus;
            }
            needDetails.overallSatisfiedPercentage = sumOfDatisfiedPercent / itemsCount;
            // Adding one more Need to the Needs list
            needs.add(needDetails);
        }
    }
    class GetNeedItemDetails extends AsyncTask {
        String mobile,username,password,email;
        HttpResponse response;
        @Override
        protected Object doInBackground(Object[] params) {
            HttpGet httpGet=new HttpGet(RestAPIURL.login);
            try {
                response = client.execute(httpGet);
            }
            catch (Exception e)
            {

            }
                if(response!=null)
                {
                    Log.d("Response Code",response.getStatusLine().getStatusCode()+"");

                    try {
                        InputStream ips = response.getEntity().getContent();
                        BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
                        StringBuilder sb = new StringBuilder();
                        String s;
                        while(true )
                        {
                            s = buf.readLine();
                            if(s==null || s.length()==0)
                                break;
                            sb.append(s);

                        }

                        buf.close();
                        ips.close();
                        Log.d("Response body",sb.toString());
                        NeedDetails[] need=gson.fromJson(sb.toString(),NeedDetails[].class);
                        System.out.print(need[0].print());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Log.d("Response","Null");
                }

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
                   // Toast.makeText(RegistrationPage.this,"Registration Successful.Kindly Login to continue",Toast.LENGTH_LONG).show();
                   // RegistrationPage.this.startActivity(new Intent(RegistrationPage.this,LoginPage.class));
                   // RegistrationPage.this.finish();
                }
           // Log.d("GSON",gson.toJson(registerDetails).toString());

            super.onPostExecute(o);
        }
    }
    public  HttpResponse makeRequest(String uri, String json) {
        try {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            return client.execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
                recyclerView.setAdapter(new DonorNeedViewAdapter(HomeActivity.this, needs));
            } else {
                newNeedFloatingActionButton.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new OrgNeedViewAdapter(HomeActivity.this, needs));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
