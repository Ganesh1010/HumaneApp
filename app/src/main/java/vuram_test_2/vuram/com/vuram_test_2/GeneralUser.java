package vuram_test_2.vuram.com.vuram_test_2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GeneralUser extends AppCompatActivity implements View.OnClickListener {

    Button getLocation;
    TextView address;
    GPSTracker gps;
    String mapAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && !(resultCode==RESULT_CANCELED))
        {
            mapAddress = data.getStringExtra("ADDRESS");
            address.setText(mapAddress);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GeneralUser.this);
        builder.setTitle("Confirm Donation");
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.activity_general_user, null);

        address= (TextView) dialogView.findViewById(R.id.address_general_user);
        getLocation= (Button) dialogView.findViewById(R.id.btn_map);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(GeneralUser.this);

                if(gps.getIsGPSTrackingEnabled()){
                    if(isNetworkAvailable()) {

                        Intent intent = new Intent(GeneralUser.this, MapActivity.class);
                        startActivityForResult(intent, 2);
                    }
                    else
                    {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GeneralUser.this);
                        alertDialog.setTitle("Internet settings");
                        alertDialog.setMessage("Mobile data is not enabled. Do you want to go to settings menu?");

                        // On pressing Settings button
                        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                            }
                        });
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }
                }else{
                    gps.showSettingsAlert();
                }
            }
        });

        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
