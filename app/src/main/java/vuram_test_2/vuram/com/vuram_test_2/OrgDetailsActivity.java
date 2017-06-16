package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static com.google.android.gms.internal.zzt.TAG;

public class OrgDetailsActivity extends AppCompatActivity  {
    public ImageButton imageButton;
    public static Context context;
    public ArrayList<NeedItemDetails> itemList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private  RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    public HttpClient client;
    public NeedItemDetails needItemDetails;
    public NeedDetails need;
    public ImageView donationCart;
    public String needId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_details);
        Intent homeactivity = getIntent();
        if (homeactivity  != null) {
            needId = homeactivity.getStringExtra("Need");
            imageButton = (ImageButton) findViewById(R.id.back_home);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            donationCart = (ImageView) findViewById(R.id.cart);
            donationCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OrgDetailsActivity.this, test.class));
                }
            });
            context = getBaseContext();
            new GetParticularNeedDetails().execute();
            needItemDetails = new NeedItemDetails();
            layoutManager = new LinearLayoutManager(this);
        }
        else
        {
            Log.e(TAG, "Intent  is null", new NullPointerException());
        }
    }


    public class GetParticularNeedDetails extends AsyncTask {
        HttpResponse response;
        ArrayList<NeedDetails> needDetails;
        ProgressDialog progressDialog;

        @Override
        protected Object doInBackground(Object[] objects) {
            client = new DefaultHttpClient();
            response = Connectivity.makeGetRequest(RestAPIURL.needList, client, Connectivity.getAuthToken(OrgDetailsActivity.this, Connectivity.Donor_Token));
            if (response != null)
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                    try {
                        JSONObject jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                        JSONArray results = jsonObject.getJSONArray("results");
                        Gson gson = new Gson();
                        needDetails = gson.fromJson(results.toString(), new TypeToken<List<NeedDetails>>() {}.getType());
                        Log.d("Result", needDetails.size() + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("CAllS ", "Response null");
                }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrgDetailsActivity.this);
            progressDialog.setMessage("Loading the need details");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            if(progressDialog.isShowing())
                progressDialog.cancel();
            for (int j = 0; j < needDetails.size(); j++)
                if (needDetails.get(j).getNeed_id() == Integer.parseInt(needId))
                {
                    need = needDetails.get(j);
                    Log.d("out", need + "");
                    itemList = (ArrayList<NeedItemDetails>) need.getItems();
                    adapter = new ItemDetailsAdapter(itemList, context, OrgDetailsActivity.this,needId);
                    recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_donor_org);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                }
        }
    }
}