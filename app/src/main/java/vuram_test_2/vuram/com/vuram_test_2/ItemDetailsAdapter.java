package vuram_test_2.vuram.com.vuram_test_2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ItemDetailsAdapter extends RecyclerView.Adapter<ItemDetailsAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView requested;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageView=(ImageView)itemView.findViewById(R.id.imageView);
            this.title = (TextView) itemView.findViewById(R.id.title1);
            this.requested=(TextView) itemView.findViewById(R.id.requested);


        }
    }

    public ItemDetailsAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_details, parent, false);

        view.setOnClickListener(OrgDetailsActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.setIsRecyclable(false);
        holder.title.setText(dataSet.get(listPosition).getName());
        holder.imageView.setImageResource(dataSet.get(listPosition).getImage());
        holder.requested.setText("Quantity "+dataSet.get(listPosition).getRequested()+"");
        Log.d("value",dataSet.get(listPosition).getRequested()+"");
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}