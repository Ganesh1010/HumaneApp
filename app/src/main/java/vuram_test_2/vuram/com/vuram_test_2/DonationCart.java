package vuram_test_2.vuram.com.vuram_test_2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

public class DonationCart extends Fragment {
    public RecyclerView recyclerView;
    public Context context;
    public Button confirmDonation;
    public EditText chooseDeliveryLocation;
    public NeedDetails needDetails;
    public NeedItemDetails needItemDetails;
    public List<NeedItemDetails>neededItems;
    public DonationDetails donationDetails;
    public DonatedItemDetails donatedItemDetails;
    public List<DonatedItemDetails> donatedItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=OrgDetailsActivity.context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_donation_cart, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerViewDonationCart);
        confirmDonation= (Button) view.findViewById(R.id.confirm_donate);

        confirmDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker gps=new GPSTracker(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialoglayout = inflater.inflate(R.layout.layout_dialog_donation_cart, null);
                chooseDeliveryLocation= (EditText) dialoglayout.findViewById(R.id.chooseDeliverLocation);
                chooseDeliveryLocation.setText(gps.getLatitude()+" "+gps.getLongitude());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delivery Location");
                builder.setView(dialoglayout);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

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
        needItemDetails.setSub_item_type_id(23);
        neededItems.add(needItemDetails);

        needItemDetails=new NeedItemDetails();
        needItemDetails.setNeed_item_id(2);
        needItemDetails.setItem_type_id(4);
        needItemDetails.setSub_item_type_id(7);
        neededItems.add(needItemDetails);

        needItemDetails=new NeedItemDetails();
        needItemDetails.setNeed_item_id(3);
        needItemDetails.setItem_type_id(1);
        needItemDetails.setSub_item_type_id(22);
        neededItems.add(needItemDetails);

        needItemDetails=new NeedItemDetails();
        needItemDetails.setNeed_item_id(4);
        needItemDetails.setItem_type_id(4);
        needItemDetails.setSub_item_type_id(9);
        neededItems.add(needItemDetails);

        donatedItemDetails=new DonatedItemDetails();
        donatedItemDetails.setDonation_id(1);
        donatedItemDetails.setDonated_item_id(1);
        donatedItemDetails.setNeeditem(3);
        donatedItemDetails.setQuantity(10);
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
        donatedItemDetails.setNeeditem(4);
        donatedItemDetails.setQuantity(30);
        donatedItems.add(donatedItemDetails);

        donatedItemDetails=new DonatedItemDetails();
        donatedItemDetails.setDonation_id(1);
        donatedItemDetails.setDonated_item_id(4);
        donatedItemDetails.setNeeditem(2);
        donatedItemDetails.setQuantity(40);
        donatedItems.add(donatedItemDetails);

        donationDetails=new DonationDetails();
        donationDetails.setDonateditems(donatedItems);

        needDetails=new NeedDetails();
        needDetails.setItems(neededItems);
    }

}
