package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DonatedItemListFragment extends Fragment {

    RecyclerView donatedItemListRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_donated_item_list, container, false);
        donatedItemListRecyclerView = (RecyclerView) inflatedView.findViewById(R.id.donated_items_recyclerview_donated_item_list);
        DonatedItemsAdapter donatedItemsAdapter = new DonatedItemsAdapter(inflatedView.getContext(), DonationListAdapter.donatedItemDetailsList, DonatedItemsAdapter.MAIN_ITEM);
        donatedItemListRecyclerView.setHasFixedSize(true);
        donatedItemListRecyclerView.setLayoutManager(new LinearLayoutManager(inflatedView.getContext()));
        donatedItemListRecyclerView.setAdapter(donatedItemsAdapter);
        return inflatedView;
    }

}
