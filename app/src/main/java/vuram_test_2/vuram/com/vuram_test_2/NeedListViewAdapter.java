package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by akshayagr on 21-03-2017.
 */

public class NeedListViewAdapter extends RecyclerView.Adapter<NeedListViewAdapter.NeedViewHolder> {

    Context context;
    TextView itemNameView,quantityView;
    ArrayList needDetailsList;

    public NeedListViewAdapter(Context context, ArrayList needListViewItems){

        this.context = context;
        this.needDetailsList = needListViewItems;
    }

    @Override
    public NeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(context).inflate(R.layout.layout_need_list_view,parent,false);
        return new NeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NeedViewHolder holder, int position) {

        NeedItemDetails items = (NeedItemDetails) needDetailsList.get(position);
        // itemNameView.setText(items.getNeed_item_id());
         //quantityView.setText(items.getQuantity());

         itemNameView.setText("1");
         quantityView.setText("2");




    }

    @Override
    public int getItemCount() {
        return needDetailsList.size();
    }

    public  class NeedViewHolder extends RecyclerView.ViewHolder {
        public NeedViewHolder(View itemView) {
            super(itemView);
            itemNameView = (TextView)itemView.findViewById(R.id.itemNameTextView_ReceivalPage);
            quantityView = (TextView)itemView.findViewById(R.id.quantityTextView_ReceivalPage);
        }
    }
}
