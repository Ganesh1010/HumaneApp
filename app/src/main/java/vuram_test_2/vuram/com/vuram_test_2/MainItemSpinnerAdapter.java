package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

class MainItemSpinnerAdapter extends ArrayAdapter<NewNeedItemSpinnerData> {
    int groupID;
    ArrayList<NewNeedItemSpinnerData> newNeedItemSpinnerDataArrayList;
    LayoutInflater inflater;

    public MainItemSpinnerAdapter(Activity context, int groupID, int id, ArrayList<NewNeedItemSpinnerData> newNeedItemSpinnerDataArrayList){
        super(context,id,newNeedItemSpinnerDataArrayList);
        this.newNeedItemSpinnerDataArrayList=newNeedItemSpinnerDataArrayList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupID=groupID;
    }

    public View getView(int position, View convertView, ViewGroup parent )
    {
        View itemView=inflater.inflate(groupID,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.main_item_img_spinner_needForm);
        imageView.setImageResource(newNeedItemSpinnerDataArrayList.get(position).getMainItemImageId());
        TextView textView=(TextView)itemView.findViewById(R.id.main_item_txt_spinner_needForm);
        textView.setText(newNeedItemSpinnerDataArrayList.get(position).getMainItemName());
        return itemView;
    }
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);
    }
}
