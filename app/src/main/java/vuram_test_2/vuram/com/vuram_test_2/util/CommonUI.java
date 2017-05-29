package vuram_test_2.vuram.com.vuram_test_2.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import vuram_test_2.vuram.com.vuram_test_2.GPSTracker;
import vuram_test_2.vuram.com.vuram_test_2.GeneralUser;
import vuram_test_2.vuram.com.vuram_test_2.MapActivity;
import vuram_test_2.vuram.com.vuram_test_2.R;

import static android.app.Activity.RESULT_CANCELED;
import static vuram_test_2.vuram.com.vuram_test_2.R.string.address;

/**
 * Created by ganeshrajam on 23-05-2017.
 */

public class CommonUI implements PreferenceManager.OnActivityResultListener {
    public static Activity context;
    static Button getLocation;
    static TextView address;
    static GPSTracker gps;
    String mapAddress;
    public static void displayCheckoutUI(View v, int itemsCount, final Activity context)
    {
        CommonUI.context=context;
        Snackbar.make(v, itemsCount+" Item(s) added", Snackbar.LENGTH_LONG).setAction("Donate", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Confirm Donation");
                        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.activity_general_user, null);

                        address= (TextView) dialogView.findViewById(R.id.address_general_user);
                        getLocation= (Button) dialogView.findViewById(R.id.btn_map);
                        getLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gps = new GPSTracker(context);

                                if(gps.getIsGPSTrackingEnabled()){
                                    if(isNetworkAvailable()) {

                                        Intent intent = new Intent(context, MapActivity.class);
                                        context.startActivityForResult(intent, 2);
                                    }
                                    else
                                    {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                        alertDialog.setTitle("Internet settings");
                                        alertDialog.setMessage("Mobile data is not enabled. Do you want to go to settings menu?");

                                        // On pressing Settings button
                                        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                                context.startActivity(intent);
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
                }).show();
    }

    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==2 && !(resultCode==RESULT_CANCELED))
        {
            System.out.println(mapAddress);
            mapAddress = data.getStringExtra("ADDRESS");
            address.setText(mapAddress);
        }
        return false;
    }
}
