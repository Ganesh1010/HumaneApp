package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import org.apache.http.client.HttpClient;
import java.util.ArrayList;

public class NeedDonationFragment extends Fragment {
    View v;
    
    public ImageButton backButton;
    public ArrayList<NeedItemDetails> needItemDetailsArrayList;
    private RecyclerView displayParticularNeedRecyclerView;
    public NeedItemDetails needItemDetails;
    public ImageView donationCart;
    public String needId;
    public ArrayList<NeedDetails> needDetailsArrayList;
    public LandingPage landingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (v == null) 
            v = inflater.inflate(R.layout.fragment_donation_for_need, container, false);
        landingPage = (LandingPage) getActivity();
        Bundle bundle = getArguments();
        needId = bundle.getString("NEED_ID");

        backButton = (ImageButton) v.findViewById(R.id.back_home);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landingPage.onBackPressed();
            }
        });

        donationCart = (ImageView) v.findViewById(R.id.donation_cart);

        needItemDetails = new NeedItemDetails();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(landingPage);
        needDetailsArrayList = landingPage.getNeedItemFromActivity();
        if(needDetailsArrayList!=null)
        {
            for (NeedDetails needDetails : needDetailsArrayList)
                if (needDetails!=null && needDetails.getNeed_id() == Integer.parseInt(needId))
                {
                    needItemDetailsArrayList = (ArrayList<NeedItemDetails>) needDetails.getItems();
                    ItemDetailsAdapter adapter = new ItemDetailsAdapter(needItemDetailsArrayList, landingPage, landingPage, needId);
                    displayParticularNeedRecyclerView = (RecyclerView) v.findViewById(R.id.display_particular_need_recycler_view);
                    displayParticularNeedRecyclerView.setHasFixedSize(true);
                    displayParticularNeedRecyclerView.setLayoutManager(layoutManager);
                    displayParticularNeedRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    displayParticularNeedRecyclerView.setAdapter(adapter);
                }
        }
        return v;
    }
}
