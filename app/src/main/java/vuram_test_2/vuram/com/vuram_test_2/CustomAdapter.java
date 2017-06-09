package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter{

    String [] result;
    Intent intent;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomAdapter(OrganisationLandingPage mainActivity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.layout_adapter, null);
        holder.tv=(TextView) rowView.findViewById(R.id.iconsname);
        holder.img=(ImageView) rowView.findViewById(R.id.icons);
        holder.tv.setText(result[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==0) {
                    intent=new Intent(context,UserProfileActivity.class);
                    context.startActivity(intent);
                }
                if(position==2) {
                    intent=new Intent(context,OrgProfileActivity.class);
                    context.startActivity(intent);
                }
                if (position == 4) {
                    Intent intent = new Intent(context, DonationListActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        holder.img.setImageResource(imageId[position]);

        return rowView;
    }

}