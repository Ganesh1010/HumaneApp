package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DonationCart extends Fragment {
    public RecyclerView recyclerView;
    public Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=new NewNeedActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_donation_cart, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerViewDonationCart);

        //recyclerView.setAdapter(new DonationCartAdapter(DonationCart.this, needDetails));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }
}
