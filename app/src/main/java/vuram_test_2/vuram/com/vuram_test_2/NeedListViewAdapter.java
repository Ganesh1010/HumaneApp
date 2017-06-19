package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzt.TAG;


public class NeedListViewAdapter extends  RecyclerView.Adapter< NeedListViewAdapter.MyViewHolder>{

    Context context;
    NeedDetails needDetails;
    ArrayList<SubItemDetails> subItemDetailsArrayList;
    
    public NeedListViewAdapter(Context context,NeedDetails needDetails){

        this.context = context;
        this.needDetails=needDetails;

        DatabaseHelper db = new DatabaseHelper(context);
        if(db!=null) {
            subItemDetailsArrayList = db.getAllSubItemDetails();
        }
        else{
            Log.e(TAG, "Database  is null", new NullPointerException());
        }
    }

    @Override
    public  NeedListViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.layout_need_list_view,parent,false);
        return new  NeedListViewAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NeedListViewAdapter.MyViewHolder holder, int position) {

        System.out.println(needDetails.getNeed_id());
        for(SubItemDetails subItemDetails:subItemDetailsArrayList)
            if(subItemDetails.getSubItemCode()==needDetails.getItems().get(position).getSub_item_type_id())
            {
                holder.itemNameView.setText(subItemDetails.getSubItemName());
                holder.quantityView.setText(needDetails.getItems().get(position).getQuantity()+"");
            }
    }

    @Override
    public int getItemCount() {
        return needDetails!=null?needDetails.getItems().size():0;
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView,quantityView;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemNameView = (TextView)itemView.findViewById(R.id.itemNameTextView_ReceivalPage);
            quantityView = (TextView)itemView.findViewById(R.id.quantityTextView_ReceivalPage);
        }
    }
}
