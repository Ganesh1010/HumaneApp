package vuram_test_2.vuram.com.vuram_test_2.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import vuram_test_2.vuram.com.vuram_test_2.GPSTracker;
import vuram_test_2.vuram.com.vuram_test_2.MapActivity;
import vuram_test_2.vuram.com.vuram_test_2.R;
import static android.app.Activity.RESULT_CANCELED;

public class CommonUI{
    static ImageView getLocation;
    static Context context;
    static EditText address,name;
    static GPSTracker gps;
    static String mapAddress;
    static View dialogView;
    public  static  String TAG="CommonUI.java";
    public static void displayCheckoutUI(View v, int itemsCount, final Activity context)
    {
        Snackbar.make(v, itemsCount+" Item(s) added", Snackbar.LENGTH_LONG).setAction("Donate", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //builder.setTitle("Confirm Donation");
                CommonUI.context=context.getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                dialogView = inflater.inflate(R.layout.layout_general_user, null);
                name= (EditText) dialogView.findViewById(R.id.name_general_user);
                address= (EditText) dialogView.findViewById(R.id.address_general_user);
                getLocation= (ImageView) dialogView.findViewById(R.id.btn_map);
                getLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gps = new GPSTracker(context);

                        if(gps.getIsGPSTrackingEnabled()) {
                            if(isNetworkAvailable(context)) {

                                Intent intent = new Intent(context, MapActivity.class);
                                context.startActivityForResult(intent, 2);
                            }
                            else {
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
                        } else {
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

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 || !(resultCode == RESULT_CANCELED)) {
            address.setEnabled(true);
            mapAddress = data.getStringExtra("ADDRESS");
            address.setText(mapAddress);
        }
    }
    public  static  void internetConnectionChecking(final Context context, final View v, final AsyncTask asyncTask)
    {
        if(asyncTask != null && context !=null && v != null) {
            if (isNetworkAvailable(context)) {
                asyncTask.execute();
            } else {
                Snackbar.make(v, "Internet Connection not available.", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        internetConnectionChecking(context, v, asyncTask);
                    }
                }).show();
            }
        }
        else
        {
            Log.e(TAG, "internetConnectionChecking: ",new Throwable("Null Value Input") );
        }
    }

    public  static  void internalValidation(final Context context, final View v, final String data)
    {
        if(data != null && context !=null && v != null) {
            if (isNetworkAvailable(context)) {
                //asyncTask.execute();
                return;
            } else {
                Snackbar.make(v, data , Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                }).show();
            }
        }
        else
        {
            Log.e(TAG, "internetConnectionChecking: ",new Throwable("Null Value Input") );
        }
    }
}