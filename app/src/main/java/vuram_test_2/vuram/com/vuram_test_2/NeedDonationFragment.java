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

import java.util.ArrayList;

public class NeedDonationFragment extends Fragment {
    View v;
    public  final String TAG="Need Donation Fragment";
    public ImageButton backButton;
    public ArrayList<NeedItemDetails> needItemDetailsArrayList;
    private RecyclerView displayParticularNeedRecyclerView;
    public NeedItemDetails needItemDetails;
    public ImageView donationCart;
    public String needId;
    public ArrayList<NeedDetails> needDetailsArrayList;
    public LandingPage landingPage; public Fragment fragment=null;
    public android.app.FragmentManager fragmentManager;

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
        donationCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new DonationCart();
                fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).addToBackStack("tag").commit();
            }
        });


        needItemDetails = new NeedItemDetails();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(landingPage);
        needDetailsArrayList = landingPage.getNeedItemFromActivity();
        if(needDetailsArrayList!=null)
        {
            for (NeedDetails needDetails : needDetailsArrayList)
                if (needDetails!=null && needDetails.getNeed_id() == Integer.parseInt(needId))
                {
                    needItemDetailsArrayList = (ArrayList<NeedItemDetails>) needDetails.getItems();
                    DonationItemDetailsAdapter adapter = new DonationItemDetailsAdapter(needItemDetailsArrayList, landingPage, needId);
                    displayParticularNeedRecyclerView = (RecyclerView) v.findViewById(R.id.display_particular_need_recycler_view);
                    displayParticularNeedRecyclerView.setHasFixedSize(true);
                    displayParticularNeedRecyclerView.setLayoutManager(layoutManager);
                    displayParticularNeedRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    displayParticularNeedRecyclerView.setAdapter(adapter);
                }
        }
        else
            Log.e(TAG, "onCreateView: need details Arraylist is null",new NullPointerException() );
        return v;
    }
}
