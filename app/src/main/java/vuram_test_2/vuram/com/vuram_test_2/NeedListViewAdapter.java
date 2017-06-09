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
    TextView itemNameView,quantityView;
    ArrayList needDetailsList;
    String[] needName;
    List itemslist;
    int[] needQuantities;
    NeedDetails need;
    ArrayList<MainItemDetails> mainItemDetailsList;
    int needItemId,needQuantity,needid;
    ArrayList<DonatedItemDetails> donatedItemDetails;

    public NeedListViewAdapter(Context context,ArrayList<NeedDetails> needDetailsArrayList,int needid){

        this.context = context;
        this.needDetailsList= needDetailsArrayList;
        this.needid =needid;
        this.context=context;
        for(NeedDetails needDetails:needDetailsArrayList)
        {
            Log.d("All need Id", "doInBackground: "+needDetails.getNeed_id());
            Log.d(TAG, "NeedListViewAdapter: Received Need Id"+needid);
            if(needid!=52)
            {
                needid=52;
            }
            if(needDetails.getNeed_id()==needid)
            {
                need = needDetails;
                break;
            }
        }
        itemslist=need.getItems();
        Log.d(TAG, "NeedListViewAdapter: "+need.getNeed_id());
        Log.d(TAG, "NeedListViewAdapter:items "+need.getItems().size());
        needQuantities = new int[itemslist.size()];
        needName = new String[itemslist.size()];

        Log.d("ItemListsize", String.valueOf(itemslist.size()));

        DatabaseHelper db = new DatabaseHelper(context);
        mainItemDetailsList = db.getAllMainItemDetails();

        donatedItemDetails = new ArrayList<>();
    }

    @Override
    public  NeedListViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(context).inflate(R.layout.layout_need_list_view,parent,false);
        return new  NeedListViewAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NeedListViewAdapter.MyViewHolder holder, int position) {

        for (int i = 0; i < itemslist.size(); i++) {
            NeedItemDetails needItemDetails = (NeedItemDetails) itemslist.get(i);
            needItemId = needItemDetails.getItem_type_id();
            needQuantity = needItemDetails.getQuantity();
            Log.d("needItemId", needItemId + "");
            needQuantities[i] = needQuantity;
            //subItemId = needItemDetails.getSub_item_type_id();
            for (int j = 0; j < mainItemDetailsList.size(); j++) {
                MainItemDetails mainItemDetails = mainItemDetailsList.get(j);
                if (needItemId == mainItemDetails.getMainItemCode()) {
                    String mainItemName = mainItemDetails.getMainItemName();
                    needName[i] = mainItemName;
                }
            }
            itemNameView.setText(needName[position]);
            quantityView.setText(needQuantities[position]+"");
        }
    }

    @Override
    public int getItemCount() {
        return need!=null?need.getItems().size():0;
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
            itemNameView = (TextView)itemView.findViewById(R.id.itemNameTextView_ReceivalPage);
            quantityView = (TextView)itemView.findViewById(R.id.quantityTextView_ReceivalPage);
        }
    }
}
