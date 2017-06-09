package vuram_test_2.vuram.com.vuram_test_2.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.DonationDetails;
import vuram_test_2.vuram.com.vuram_test_2.GPSTracker;
import vuram_test_2.vuram.com.vuram_test_2.HomeActivity;
import vuram_test_2.vuram.com.vuram_test_2.LocationAddress;
import vuram_test_2.vuram.com.vuram_test_2.MapActivity;
import vuram_test_2.vuram.com.vuram_test_2.R;
import vuram_test_2.vuram.com.vuram_test_2.RegisterDetails;
import vuram_test_2.vuram.com.vuram_test_2.RestAPIURL;
import vuram_test_2.vuram.com.vuram_test_2.UnregisteredUser;
import vuram_test_2.vuram.com.vuram_test_2.UserDetails;
import vuram_test_2.vuram.com.vuram_test_2.UserProfileActivity;

import static android.app.Activity.RESULT_CANCELED;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_DONOR;

public class CommonUI{
    public ImageView getLocation;
    public static Context context;
    public static Activity activity;
    public EditText address,name,mobileNumber;
    public GPSTracker gps;
    public LocationAddress mapAddress;
    public View dialogView;
    public UnregisteredUser unregisteredUser;
    public  static  String TAG="CommonUI.java";
    public String donor_token;
    public Gson gson;
    public HttpClient client;
    public HttpResponse response;
    public boolean isDataFilled=false;
    public boolean isLocationSelected=false;
    public void displayCheckoutUI(View v, int itemsCount, final Activity context, final DonationDetails donationDetails) {
        Snackbar.make(v, itemsCount+" Item(s) added", Snackbar.LENGTH_LONG).setAction("Donate", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donor_token = Connectivity.getAuthToken(context, Connectivity.Donor_Token);
                System.out.println("donor token : "+donor_token);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                CommonUI.context=context.getApplicationContext();
                CommonUI.activity=context;
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                dialogView = inflater.inflate(R.layout.layout_general_user, null);
                name= (EditText) dialogView.findViewById(R.id.name_general_user);
                address= (EditText) dialogView.findViewById(R.id.address_general_user);
                mobileNumber= (EditText) dialogView.findViewById(R.id.mobile_number_general_user);
                gson= new Gson();
                isDataFilled=true;
                if(donor_token==null)
                {
                    unregisteredUser=new UnregisteredUser();
                    unregisteredUser.setAddress(address.getText().toString());
                    unregisteredUser.setLatitude(mapAddress.getLatitude());
                    unregisteredUser.setLongitude(mapAddress.getLongitude());
                    unregisteredUser.setMobile(mobileNumber.getText().toString());
                    donationDetails.setUnregisteredUser(unregisteredUser);
                    donationDetails.setRegisteredUser(false);
                }
                else {
                    donationDetails.setRegisteredUser(true);
                    System.out.println("donor token is not null");
                    new PopulatingTask().execute();
                }

                donationDetails.setReceived(false);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                donationDetails.setdonated_at(dateFormat.format(date)+"");

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

                        new PostItemDetails().execute(donationDetails);
                        //dialog.dismiss();
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

    class PostItemDetails extends AsyncTask {

        HttpResponse response;
        HttpClient client;

        @Override
        protected Object doInBackground(Object[] params) {

            client = new DefaultHttpClient();
            //String coordinator_token = Connectivity.getAuthToken(NewNeedActivity.this, Connectivity.Coordinator_Token);
            response = Connectivity.makePostRequest(RestAPIURL.donateNeedURL, gson.toJson(params[0],DonationDetails.class), client, donor_token);
            Log.d("Request JSON", gson.toJson(params[0],DonationDetails.class));
            if (response != null) {
                Log.d("Response Code", response.getStatusLine().getStatusCode() + "");

                try {
                    Connectivity.getJosnFromResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("Response", "Null");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if(response!=null)
                if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201)
                {
                    Toast.makeText(context,"donated successfully...",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(context,HomeActivity.class);
                    intent.putExtra(USER_KEY_TYPE, USER_TYPE_SELECTION_DONOR);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            super.onPostExecute(o);
        }
    }
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 || !(resultCode == RESULT_CANCELED)) {
            address.setEnabled(true);
            mapAddress = (LocationAddress)data.getSerializableExtra("ADDRESS");
            address.setText(mapAddress.getAddress());
            isLocationSelected=true;
        }
    }

    public  static  void internetConnectionChecking(final Context context, final View v, final AsyncTask asyncTask) {
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

    public  static  void internalValidation(final Context context, final View v, final String data) {
        if(data != null && context !=null && v != null) {
                Snackbar.make(v, data , Snackbar.LENGTH_LONG).show();
           }
        else
        {
            Log.e(TAG, "internalValidation ",new Throwable("Null Value Input") );
        }
    }

    class PopulatingTask extends AsyncTask {

        String firstName;
        RegisterDetails profile;
        String mobile;
        int countryCode;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommonUI.activity);
            progressDialog.setMessage("Loading Your details");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            donor_token = Connectivity.getAuthToken(context, Connectivity.Donor_Token);
            client = new DefaultHttpClient();
            response = Connectivity.makeGetRequest(RestAPIURL.getUserDetails, client, donor_token);
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                Gson gson = new Gson();
                System.out.println("populating details");
                List<UserDetails> userDetailsList = gson.fromJson(Connectivity.getJosnFromResponse(response), new TypeToken<List<UserDetails>>() {}.getType());
                UserDetails userDetails = userDetailsList.get(0);
                firstName = userDetails.getFirst_name();
                profile = userDetails.getProfile();
                mobile = profile.getMobile();
                countryCode = profile.getCountry();

            } else {
                Log.d("CAll ", "Response null");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(progressDialog.isShowing())
                progressDialog.cancel();
            name.setText(firstName);
            mobileNumber.setText(mobile);
        }
    }
}