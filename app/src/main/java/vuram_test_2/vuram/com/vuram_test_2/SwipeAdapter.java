package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeCardHolder> {

    Context context;
    ArrayList donorList;

    public SwipeAdapter(Context context, ArrayList donorList) {
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
        holder.donorName.setText("akshaya");
        holder.donorMob.setText("9566343945");
        holder.date.setText("18 jan 2017");

    }


    @Override
    public int getItemCount() {
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
