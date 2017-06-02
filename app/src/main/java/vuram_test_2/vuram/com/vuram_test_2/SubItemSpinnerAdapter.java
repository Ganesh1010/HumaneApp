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

class SubItemSpinnerAdapter extends ArrayAdapter<ItemSpinnerData> {
    int groupid;
    ArrayList<ItemSpinnerData> list;
    LayoutInflater inflater;

    public SubItemSpinnerAdapter(Activity context, int groupid, int id, ArrayList<ItemSpinnerData> list){
        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent )
    {
        View itemView=inflater.inflate(groupid,parent,false);
        TextView textView=(TextView)itemView.findViewById(R.id.sub_item_txt_spinner_needForm);
        textView.setText(list.get(position).getText());
        return itemView;
    }
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);
    }
}
