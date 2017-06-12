package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;

public class NeedDetailsActivity extends AppCompatActivity {

    ArrayList<NeedItemDetails> needListData;
    ArrayList<DonationDetails> needCardData;
    ArrayList<DonationDetails> donatedDetailsList;
    ArrayList<NeedDetails> needDetailsArrayList;
    List  donatedItemList,itemslist;
    int donatedItemId,needItemId,needQuantity,donatedQuantity,needId;
    String donorName,mainItemName;
    RecyclerView needrecyclerView, receivalCardView;
    NeedListViewAdapter needListViewAdapter;
    NeedReceivalCard needReceivalCard;
    Button showlistButton;
    LinearLayout needListLayout, headingLayout;
    CircularProgressBar progressBar;
    TextView percentage;
    View divider1;
    Toolbar toolbar;
    NeedItemDetails needItemsToDisplay;
    NeedDetails needDetails,need;
    NeedItemDetails needItemDetails;
    //NeedDetails need;
     DonationDetails donationDetailsToDisplay;
     DonatedItemDetails donatedItemDetailsTodisplay;
     DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar_need_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        needDetails = new NeedDetails();
        needItemDetails = new NeedItemDetails();
        needDetailsArrayList = new ArrayList<NeedDetails>();
        donatedDetailsList = new ArrayList<>();
        need = new NeedDetails();
        itemslist = new ArrayList();

        //   needDetails.setNeed_id(8);

        needListData = new ArrayList();
        needCardData = new ArrayList();
        //needViewData();
        //needCardViewData();

        progressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar_ReceivalPage);
        percentage = (TextView) findViewById(R.id.circularProgressPercentageTextVIew_ReceivalPage);

        int animationDuration = 1000; // 2500ms = 2,5s
        progressBar.setProgressWithAnimation(80, animationDuration);
        percentage.setText("80%");

        showlistButton = (Button) findViewById(R.id.showListButton_ReceivalPage);
        needrecyclerView = (RecyclerView) findViewById(R.id.needListRecyclerView_ReceivalPage);
        receivalCardView = (RecyclerView) findViewById(R.id.donorListRecyclerView_ReceivalPage);
        needListLayout = (LinearLayout) findViewById(R.id.needListDivider_ReceivalPage);
        headingLayout = (LinearLayout) findViewById(R.id.listHeadingLinearLayout_ReceivalPage);

        new NeedAndDonatedDetails().execute();

    }
    class NeedAndDonatedDetails extends AsyncTask {
        HttpResponse response;
        HttpClient client;
        NeedListViewItems needListViewItems;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            client = new DefaultHttpClient();
            databaseHelper = new DatabaseHelper(NeedDetailsActivity.this);

            response = Connectivity.makeGetRequest(RestAPIURL.orgDetails, client, Connectivity.getAuthToken(NeedDetailsActivity.this, Connectivity.Donor_Token));
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                    try {
                        JSONObject jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                        JSONArray results = jsonObject.getJSONArray("results");
                        Gson gson = new Gson();
                        Log.d("Result ", "doInBackground: "+results.toString());
                        needDetailsArrayList = gson.fromJson(results.toString(),new TypeToken<List<NeedDetails>>() {}.getType());
                        needId =getIntent().getExtras().getInt(USER_KEY_TYPE);
                        Log.d("ju",needId+"");
                        Log.d("output for need id 3", need + "");


                        if (need != null)
                        {
                            Log.d("Need Id", "doInBackground: "+need.getNeed_id());
                            itemslist = need.getItems();
                            Log.d("ItemsList", "doInBackground: " + itemslist);

                            if (itemslist != null)
                            {


                                donatedDetailsList = (ArrayList<DonationDetails>) need.getDonations();

                                for (int i = 0; i < donatedDetailsList.size(); i++) {
                                    DonationDetails donationDetails = donatedDetailsList.get(i);

                                    donatedItemList = donationDetails.getDonateditems();
                                    donorName = donationDetails.getUser();
                                    Log.d("donor Name", "doInBackground: " + donorName);


                                    for (int j = 0; j < donatedItemList.size(); j++) {
                                        DonatedItemDetails donatedItemDetails = (DonatedItemDetails) donatedItemList.get(j);

                                        donatedItemId = donatedItemDetails.getDonated_item_id();
                                        donatedQuantity = donatedItemDetails.getQuantity();

                                        Log.d("donated Item", "doInBackground: " + donatedItemId);
                                        Log.d("donated Quantity", "doInBackground: " + donatedQuantity);
                                    }

                                    donatedItemDetailsTodisplay = new DonatedItemDetails(donatedItemId, donatedQuantity);

                                    needCardData.add(donationDetailsToDisplay);
                                }
                            } else
                                Toast.makeText(NeedDetailsActivity.this, "Json Object retreival failed", Toast.LENGTH_SHORT).show();
                                donatedItemDetailsTodisplay = new DonatedItemDetails(donatedItemId,donatedQuantity);
                                //donationDetailsToDisplay = new DonationDetails(donatedItemList.get(0),donorName);
                              //  needListViewItems = new NeedListViewItems(donatedItemId, donorName, donatedQuantity);

                                needCardData.add(donationDetailsToDisplay);

                    }
                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {

            needListViewAdapter = new NeedListViewAdapter(NeedDetailsActivity.this,needDetailsArrayList,needId);
            needrecyclerView.setAdapter(needListViewAdapter);
            needrecyclerView.setLayoutManager(new LinearLayoutManager(NeedDetailsActivity.this));
            needListLayout.setVisibility(View.GONE);
            headingLayout.setVisibility(View.GONE);



            // Toast.makeText(this,"after list adapter",Toast.LENGTH_LONG).show();
            needReceivalCard = new NeedReceivalCard(NeedDetailsActivity.this,needCardData,needId,needDetailsArrayList);
            System.out.println("Hai");
            receivalCardView.setAdapter(needReceivalCard);
            System.out.println("Hai");
            receivalCardView.setLayoutManager(new LinearLayoutManager(NeedDetailsActivity.this));

            divider1 = findViewById(R.id.divider1);

            showlistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(NeedDetailsActivity.this, DonationListActivity.class));
                    /*
                    if (needListLayout.getVisibility() == View.GONE) {
                        needListLayout.setVisibility(View.VISIBLE);
                        headingLayout.setVisibility(View.VISIBLE);
                        divider1.setVisibility(View.VISIBLE);
                        showlistButton.setText("Hide Need List");
                    } else {
                        needListLayout.setVisibility(View.GONE);
                        headingLayout.setVisibility(View.GONE);
                        divider1.setVisibility(View.GONE);
                        showlistButton.setText("Show Need List");
                    }
                    */
                }
            });


            Toast.makeText(NeedDetailsActivity.this, "Displayed Succesfully", Toast.LENGTH_SHORT).show();
            super.onPostExecute(o);
        }

    }
}

