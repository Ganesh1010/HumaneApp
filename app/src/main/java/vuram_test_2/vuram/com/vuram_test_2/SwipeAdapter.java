package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeCardHolder> {

    Context context;
    ArrayList<DonationDetailsReadOnly> donorList;
    SimpleDateFormat date;
    String donatedDate;

    public SwipeAdapter(Context context, ArrayList<DonationDetailsReadOnly> donorList) {
        this.context = context;
        this.donorList = donorList;

    }

    @Override
    public SwipeCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.swipe_card_receival_page, parent, false);
        return new SwipeCardHolder(v);

    }

    @Override
    public void onBindViewHolder(final SwipeCardHolder holder, int position) {

        DonationDetailsReadOnly donationDetailsReadOnly = donorList.get(position);
        date =new SimpleDateFormat("dd MMM yyyy");
        donatedDate = date.format(donationDetailsReadOnly.getDonated_at());

        holder.donorName.setText(donationDetailsReadOnly.getUser_name());
        holder.donorMob.setText(donationDetailsReadOnly.getMobile());
        holder.date.setText(donatedDate.toString());

    }


    @Override
    public int getItemCount() {
       // if(donorList.size()!= 0)
          return donorList.size();
    }

    public void removeItem(int position) {
        donorList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, donorList.size());
    }

    public class SwipeCardHolder extends RecyclerView.ViewHolder{

        TextView  donorName,donorMob,date;



        public SwipeCardHolder(View itemView) {
            super(itemView);
            donorName = (TextView)itemView.findViewById(R.id.donor_name_swipe_card_textView);
            donorMob = (TextView)itemView.findViewById(R.id.donor_mob_swipe_card_text_view);
            date = (TextView)itemView.findViewById(R.id.date_swipe_Card_textView);
                }

    }
}
