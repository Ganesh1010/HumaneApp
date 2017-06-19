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

    ArrayList<NeedDetails> needDetailsArrayList;
    int needId;
    RecyclerView needRecyclerView, receivalCardView;
    NeedListViewAdapter needListViewAdapter;
    NeedReceivalCard needReceivalCard;
    Button showNeedListButton;
    LinearLayout needListLayout, headingLayout;
    CircularProgressBar progressBar;
    TextView percentage;
    View divider1;
    Toolbar toolbar;
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

        needDetailsArrayList = new ArrayList<>();

        progressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar_ReceivalPage);
        percentage = (TextView) findViewById(R.id.circularProgressPercentageTextVIew_ReceivalPage);

        showNeedListButton = (Button) findViewById(R.id.showListButton_ReceivalPage);
        needRecyclerView = (RecyclerView) findViewById(R.id.needListRecyclerView_ReceivalPage);
        receivalCardView = (RecyclerView) findViewById(R.id.donorListRecyclerView_ReceivalPage);
        needListLayout = (LinearLayout) findViewById(R.id.needListDivider_ReceivalPage);
        headingLayout = (LinearLayout) findViewById(R.id.listHeadingLinearLayout_ReceivalPage);

        new NeedAndDonatedDetails().execute();
        needId =getIntent().getExtras().getInt(USER_KEY_TYPE);

    }
    class NeedAndDonatedDetails extends AsyncTask {
        HttpResponse response;
        HttpClient client;
        NeedDetails needDetails;
        List<DonationDetails> donationDetailsArrayList=new ArrayList<>();

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
                    try
                    {
                        JSONObject jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                        JSONArray results = jsonObject.getJSONArray("results");
                        Gson gson = new Gson();
                        Log.d("Result ", "doInBackground: "+results.toString());
                        needDetailsArrayList = gson.fromJson(results.toString(),new TypeToken<List<NeedDetails>>() {}.getType());

                        for(NeedDetails needDetails:needDetailsArrayList)
                            if(needDetails.getNeed_id()==needId) {
                                this.needDetails = needDetails;
                                break;
                            }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {

            System.out.println("need from need activity: "+needDetails.getNeed_id());

            needListViewAdapter = new NeedListViewAdapter(NeedDetailsActivity.this,needDetails);
            needRecyclerView.setAdapter(needListViewAdapter);
            needRecyclerView.setLayoutManager(new LinearLayoutManager(NeedDetailsActivity.this));
            needListLayout.setVisibility(View.GONE);
            headingLayout.setVisibility(View.GONE);

            int animationDuration = 1000; // 2500ms = 2,5s
            int totalQuantity=0;
            int totalDonatedReceived=0;
            for(int i=0;i<needDetails.getItems().size();i++) {
                totalQuantity += needDetails.getItems().get(i).getQuantity();
                totalDonatedReceived += needDetails.getItems().get(i).getDonated_and_received_amount();
            }
            if(totalQuantity!=0 || totalDonatedReceived!=0) {
                progressBar.setProgressWithAnimation(totalDonatedReceived * 100 / totalQuantity, animationDuration);
                percentage.setText(totalDonatedReceived * 100 / totalQuantity + "%");
            }

            donationDetailsArrayList=needDetails.getDonations();

            needReceivalCard = new NeedReceivalCard(NeedDetailsActivity.this,donationDetailsArrayList);
            receivalCardView.setAdapter(needReceivalCard);
            receivalCardView.setLayoutManager(new LinearLayoutManager(NeedDetailsActivity.this));

            divider1 = findViewById(R.id.divider1);

            showNeedListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (needListLayout.getVisibility() == View.GONE) {
                        needListLayout.setVisibility(View.VISIBLE);
                        headingLayout.setVisibility(View.VISIBLE);
                        divider1.setVisibility(View.VISIBLE);
                        showNeedListButton.setText("Hide Need List");
                    } else {
                        needListLayout.setVisibility(View.GONE);
                        headingLayout.setVisibility(View.GONE);
                        divider1.setVisibility(View.GONE);
                        showNeedListButton.setText("Show Need List");
                    }
                }
            });
            Toast.makeText(NeedDetailsActivity.this, "Displayed Succesfully", Toast.LENGTH_SHORT).show();
            super.onPostExecute(o);
        }
    }
}

