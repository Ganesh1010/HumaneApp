package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class DonationCart extends Fragment {
    public RecyclerView recyclerView;
    public Context context;

    public NeedDetails needDetails;
    public NeedItemDetails needItemDetails;
    public List<NeedItemDetails>neededItems;

    public DonationDetails donationDetails;
    public DonatedItemDetails donatedItemDetails;
    public List<DonatedItemDetails> donatedItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=test.context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_donation_cart, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerViewDonationCart);

        loadData();
        System.out.println("after loading data");
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new DonationCartAdapter(context, donationDetails,needDetails));

        return view;
    }

    public void loadData()
    {
        donatedItems=new ArrayList<>();
        neededItems=new ArrayList<>();

        needItemDetails=new NeedItemDetails();
        needItemDetails.setNeed_item_id(1);
        needItemDetails.setItem_type_id(1);
        needItemDetails.setSub_item_type_id(2);
        neededItems.add(needItemDetails);

        needItemDetails=new NeedItemDetails();
        needItemDetails.setNeed_item_id(2);
        needItemDetails.setItem_type_id(2);
        needItemDetails.setSub_item_type_id(2);
        neededItems.add(needItemDetails);

        needItemDetails=new NeedItemDetails();
        needItemDetails.setNeed_item_id(3);
        needItemDetails.setItem_type_id(1);
        needItemDetails.setSub_item_type_id(2);
        neededItems.add(needItemDetails);

        needItemDetails=new NeedItemDetails();
        needItemDetails.setNeed_item_id(4);
        needItemDetails.setItem_type_id(2);
        needItemDetails.setSub_item_type_id(2);
        neededItems.add(needItemDetails);

        donatedItemDetails=new DonatedItemDetails();
        donatedItemDetails.setDonation_id(1);
        donatedItemDetails.setDonated_item_id(1);
        donatedItemDetails.setNeeditem(1);
        donatedItemDetails.setQuantity(20);
        donatedItems.add(donatedItemDetails);

        donatedItemDetails=new DonatedItemDetails();
        donatedItemDetails.setDonation_id(1);
        donatedItemDetails.setDonated_item_id(2);
        donatedItemDetails.setNeeditem(1);
        donatedItemDetails.setQuantity(20);
        donatedItems.add(donatedItemDetails);

        donatedItemDetails=new DonatedItemDetails();
        donatedItemDetails.setDonation_id(1);
        donatedItemDetails.setDonated_item_id(3);
        donatedItemDetails.setNeeditem(2);
        donatedItemDetails.setQuantity(20);
        donatedItems.add(donatedItemDetails);

        donatedItemDetails=new DonatedItemDetails();
        donatedItemDetails.setDonation_id(1);
        donatedItemDetails.setDonated_item_id(4);
        donatedItemDetails.setNeeditem(2);
        donatedItemDetails.setQuantity(20);
        donatedItems.add(donatedItemDetails);

        donationDetails=new DonationDetails();
        donationDetails.setDonateditems(donatedItems);

        needDetails=new NeedDetails();
        needDetails.setItems(neededItems);
    }
}
