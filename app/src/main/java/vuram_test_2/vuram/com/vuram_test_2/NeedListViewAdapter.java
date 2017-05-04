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
    TextView itemNameView,quantityView,genderView;
    ArrayList needListViewItems;

    public NeedListViewAdapter(Context context, ArrayList<NeedListViewItems> needListViewItems){

        this.context = context;
        this.needListViewItems = needListViewItems;

    }
    @Override
    public NeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(context).inflate(R.layout.layout_need_list_view,parent,false);
        return new NeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NeedViewHolder holder, int position) {

        NeedListViewItems items = (NeedListViewItems) needListViewItems.get(position);
        itemNameView.setText(items.getItemName());

        genderView.setText(items.getGender());
        quantityView.setText(items.getQuantity());



    }

    @Override
    public int getItemCount() {
        return needListViewItems.size() ;
    }

    public  class NeedViewHolder extends RecyclerView.ViewHolder {
        public NeedViewHolder(View itemView) {
            super(itemView);
            itemNameView = (TextView)itemView.findViewById(R.id.itemNameTextView_ReceivalPage);
            genderView = (TextView)itemView.findViewById(R.id.genderTextView_ReceivalPage);
            quantityView = (TextView)itemView.findViewById(R.id.quantityTextView_ReceivalPage);
        }
    }
}
