package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

/**
 * Created by rahulk on 6/15/2017.
 */

public class DonationForNeedFragment extends Fragment {
    View v;
    public ImageButton imageButton;
    public ArrayList<NeedItemDetails> itemList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    public HttpClient client;
    public NeedItemDetails needItemDetails;
    public NeedDetails need;
    public ImageView donationCart;
    public String needId;
    ArrayList<NeedDetails> needDetails;
    LandingPage landingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (v == null)
            v = inflater.inflate(R.layout.activity_org_details, container, false);
        Toast.makeText(getActivity(), "Fragmanet", Toast.LENGTH_SHORT).show();
        landingPage = (LandingPage) getActivity();
        Bundle bundle = getArguments();
        needId = bundle.getString("NEED");
        Toast.makeText(landingPage, "NEED ID" + needId, Toast.LENGTH_LONG).show();

        imageButton = (ImageButton) v.findViewById(R.id.back_home);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landingPage.onBackPressed();
            }
        });
        donationCart = (ImageView) v.findViewById(R.id.cart);
        needItemDetails = new NeedItemDetails();
        layoutManager = new LinearLayoutManager(landingPage);
        needDetails = landingPage.getNeedItemFromActivity();
        for (int j = 0; j < needDetails.size(); j++)
            if (needDetails.get(j).getNeed_id() == Integer.parseInt(needId)) {
                need = needDetails.get(j);
                Log.d("out", need + "");
                itemList = (ArrayList<NeedItemDetails>) need.getItems();
                adapter = new ItemDetailsAdapter(itemList, landingPage, landingPage, needId);
                recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view_donor_org);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
        return v;
    }
}
