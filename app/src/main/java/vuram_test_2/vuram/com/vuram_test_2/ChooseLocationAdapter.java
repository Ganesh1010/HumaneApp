package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by sujithv on 5/24/2017.
 */

public class ChooseLocationAdapter extends RecyclerView.Adapter {

    Activity activity;
    ArrayList<String> citiesList;

    public ChooseLocationAdapter(Activity activity, ArrayList<String> citiesList) {
        this.activity = activity;
        this.citiesList = citiesList;
        Log.d(TAG, "ChooseLocationAdapter: received a list of cities (" + citiesList.size() + ")");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Inflating the city name view");
        View cityNameView = LayoutInflater.from(activity).inflate(R.layout.layout_choose_location_city_view, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(cityNameView);

        if (viewHolder != null) {
            Log.i(TAG, "onCreateViewHolder: Inflation Successful");
        } else {
            Log.i(TAG, "onCreateViewHolder: Inflation Unsuccessful");
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String cityName = citiesList.get(position);
        Log.d(TAG, "onBindViewHolder: Current City: " + cityName);
        ((ViewHolder)holder).cityNameTextView.setText(cityName);
        Log.d(TAG, "onBindViewHolder: Textview name is set as " + ((ViewHolder)holder).cityNameTextView.getText().toString());
        ((ViewHolder)holder).cityNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: City name(" + ((TextView)v.findViewById(R.id.city_name_textview)).getText().toString() + ") is selected");
                HomeActivity.locationName = cityName;
                Log.d(TAG, "onClick: Finishing the ChooseLocation Activity");
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() { return citiesList.size();}

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cityNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            cityNameTextView = (TextView) itemView.findViewById(R.id.city_name_textview);
            if (cityNameTextView != null) {
                Log.d(TAG, "ViewHolder: Holder is ready");
            } else {
                Log.d(TAG, "ViewHolder: Cannot create holder");
            }
        }
    }
}
