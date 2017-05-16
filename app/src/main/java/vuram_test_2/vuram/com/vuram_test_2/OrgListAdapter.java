package vuram_test_2.vuram.com.vuram_test_2;


import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrgListAdapter extends RecyclerView.Adapter<OrgListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrgDetails> needs;
    private int screenWidth;

    private final String TAG = "OrgListAdapter.java";

    public OrgListAdapter(Context context, ArrayList<OrgDetails> needs) {
        this.context = context;
        this.needs = needs;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public OrgListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(context).inflate(R.layout.layout_org_list, parent, false);
        ViewHolder holder = new ViewHolder(inflatedView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final OrgListAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Start @Org " + (position + 1));

        holder.orgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Org Details Page will be opened", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, OrgDetailsActivity.class));
            }
        });
        holder.galleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Gallery Icon", Toast.LENGTH_SHORT).show();
                if (holder.layout.getVisibility() == View.GONE) {
                    holder.divider.setVisibility(View.VISIBLE);
                    holder.layout.setVisibility(View.VISIBLE);
                    //holder.orgPhotosRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    holder.divider.setVisibility(View.GONE);
                    holder.layout.setVisibility(View.GONE);
                    //holder.orgPhotosRecyclerView.setVisibility(View.GONE);
                }
            }
        });
        holder.orgName.setText("Vuram Technology Solutions");
        /*
        OrgDetails needDetails = needs.get(position);
        holder.orgName.setText(needDetails.orgName);
        holder.orgAddress.setText(needDetails.orgAddress);
        holder.orgContactNo.setText(needDetails.orgContactNo);
        holder.orgLogo.setImageResource(R.drawable.ngo);
        //holder.roundedOrgLogo.setImageResource(R.drawable.ngo);
        holder.overallSatisfiedBar.setProgress(needDetails.overallSatisfiedPercentage);

        //Glide.with(context).load(needDetails.orgLogo).into(holder.orgLogo);
        holder.needItems.removeAllViews();

        View itemView = null;
        for (int i = 0; i < needDetails.itemDetailsList.size(); i++) {
            ItemDetails itemDetails = needDetails.itemDetailsList.get(i);
            // Inflating a new Item View
            itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_view, null);
            ImageView itemIcon = (ImageView) itemView.findViewById(R.id.itemImage);
            TextView itemName = (TextView) itemView.findViewById(itemName);
            ProgressBar satisfactionBar = (ProgressBar) itemView.findViewById(R.id.itemStatus);

            // Defining Item Details
            itemIcon.setImageResource(R.drawable.ic_food_black);
            //Glide.with(context).load(itemDetails.itemIcon).into(itemIcon);
            itemName.setText(itemDetails.itemName);
            satisfactionBar.setProgress(itemDetails.satisfiedPercentage);

            // Adding the item to the items layout(Horizontal Scolling Linear Layout)
            holder.needItems.addView(itemView);
            Log.d("Child count+ postiton",holder.needItems.getChildCount()+"");
            Log.d(TAG, "onBindViewHolder: " + (i + 1) + " item(s) added");
        }
        */
        //setAnimation(itemView,position);
        Log.d(TAG, "onBindViewHolder: End");
    }

    @Override
    public int getItemCount() {
        return needs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView orgCard;
        public ImageView orgLogo;
        public TextView orgName;
        public ImageButton addImageButton;
        public ImageButton deleteImageButton;
        public ImageButton galleryImageButton;
        public ImageButton infoImageButton;
        public RecyclerView orgPhotosRecyclerView;
        public View divider;
        public LinearLayout layout;

        private boolean headerClicked = false;
        
        public ViewHolder(View orgPhotosView) {
            super(orgPhotosView);

            orgCard = (CardView) orgPhotosView.findViewById(R.id.org_card_list);
            orgLogo = (ImageView) orgPhotosView.findViewById(R.id.org_logo_user_profile);
            orgName = (TextView) orgPhotosView.findViewById(R.id.org_name_user_profile);
            addImageButton = (ImageButton) orgPhotosView.findViewById(R.id.add_imagebutton_user_profile);
            deleteImageButton = (ImageButton) orgPhotosView.findViewById(R.id.delete_imagebutton_user_profile);
            galleryImageButton = (ImageButton) orgPhotosView.findViewById(R.id.gallery_imagebutton_user_profile);
            divider = orgPhotosView.findViewById(R.id.divider1_user_profile);
            layout = (LinearLayout) orgPhotosView.findViewById(R.id.layout_to_hide_user_profile);

            orgPhotosRecyclerView = (RecyclerView) orgPhotosView.findViewById(R.id.org_photos_recyclerview_user_profile);
        }
    }
}