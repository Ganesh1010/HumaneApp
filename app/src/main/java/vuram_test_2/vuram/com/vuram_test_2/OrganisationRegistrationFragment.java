package vuram_test_2.vuram.com.vuram_test_2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.lang.reflect.Type;
import java.util.ArrayList;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;
import vuram_test_2.vuram.com.vuram_test_2.util.Validation;

import static android.app.Activity.RESULT_CANCELED;

public class OrganisationRegistrationFragment extends Fragment {

    View v;
    EditText orgNoEditText,orgNameEditText,orgaddressEditText,orgEmailEditText,orgMobNoEditText,orgDescEditText;
    Spinner orgTypeFromSpinner;
    Button orgRegisterButton,chooseLocationButton;
    String orgNo,orgName,orgAddress,orgMail,orgMobile,orgDesc,orgType,lastName;
    int latitude,longitude;
    Gson gson;
    ProgressDialog progressDialog;
    LandingPage landingPage;
    //ArrayList coordinatorDetails;
    OrganisationDetails organisationDetails;
    RegisterDetails coordinatorDetails;
    UserDetails userDetails;
    String orgDetailsString,mapAddress;
    GPSTracker gps;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && !(resultCode==RESULT_CANCELED))
        {
            mapAddress = data.getStringExtra("ADDRESS");
            orgaddressEditText.setText(mapAddress);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle i=getArguments();
        if(i!=null) {
            orgDetailsString=i.getString("COORDINATOR");
        }
         if(v==null)
            v = inflater.inflate(R.layout.layout_org_form,container,false);
        landingPage = (LandingPage)getActivity();
        orgNoEditText = (EditText)v.findViewById(R.id.org_register_num_editText_org_form);
        orgNameEditText = (EditText)v.findViewById(R.id.org_name_editText_org_form);
        orgaddressEditText = (EditText)v.findViewById(R.id.org_address_editText_org_form);
        orgEmailEditText = (EditText)v.findViewById(R.id.org_email_editText_org_form);
        orgMobNoEditText = (EditText)v.findViewById(R.id.org_phone_editText_org_form);
        orgDescEditText = (EditText)v.findViewById(R.id.org_desc_editText_org_form);
        orgTypeFromSpinner = (Spinner)v.findViewById(R.id.org_type_spinner_org_form);
        orgRegisterButton = (Button)v.findViewById(R.id.register_button_org_form);
        chooseLocationButton = (Button)v.findViewById(R.id.map);
        v.getRootView().setBackgroundColor(Color.WHITE);

        orgRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        chooseLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(landingPage);

                // check if GPS enabled
                if(gps.getIsGPSTrackingEnabled()){
                    if(isNetworkAvailable()) {

                        Intent intent = new Intent(landingPage, MapActivity.class);
                        startActivityForResult(intent, 2);
                    }
                    else
                    {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(landingPage);
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


        return v;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) landingPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void register(){

        orgNo = orgNoEditText.getText().toString();
        orgName = orgNameEditText.getText().toString();
        orgMail =orgEmailEditText.getText().toString();
        orgMobile = orgMobNoEditText.getText().toString();
        orgAddress = orgaddressEditText.getText().toString();
        orgDesc = orgDescEditText.getText().toString();
        orgType = orgTypeFromSpinner.getSelectedItem().toString();

        if(!validate()){
            onSignupFailed();
            return;
        }

        progressDialog = new ProgressDialog(landingPage, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        gson = new Gson();

        new OrganisationAccount().execute();

    }

    public boolean validate() {

        boolean valid = true;

        if (orgNo.isEmpty()) {
            orgNoEditText.setError("at least 3 characters");
            valid = false;
        } else {
            orgNoEditText.setError(null);
        }
        if(!Validation.validate_email(orgMail)){
            orgEmailEditText.setError("enter valid mail id");
        }

        if (orgMobile.isEmpty() || orgMobile.length() != 10) {
            orgMobNoEditText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            orgMobNoEditText.setError(null);
        }

        return valid;
    }

    public void onSignupFailed() {
       // Toast.makeText(landingPage, "Login failed", Toast.LENGTH_LONG).show();

        orgRegisterButton.setEnabled(true);
    }

    class OrganisationAccount extends AsyncTask{

        HttpResponse response;
        HttpClient client;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            client = new DefaultHttpClient();
            organisationDetails = new OrganisationDetails();
            coordinatorDetails = new RegisterDetails();
            userDetails = new UserDetails();

            organisationDetails.setEmail(orgMail);
            organisationDetails.setMobile(orgMobile);
            organisationDetails.setAddress(orgAddress);
            organisationDetails.setDescription(orgDesc);
            organisationDetails.setOrg_name(orgName);
            organisationDetails.setOrg_reg_no(orgNo);
            organisationDetails.setOrg_type(orgType);
            organisationDetails.setLatitude(12);
            organisationDetails.setLongitude(50);

            coordinatorDetails.setOrg(organisationDetails);
//          Bundle bundle = new Bundle();
//            coordinatorInfo = bundle.getString("COORDINATOR")
//  Type type = new TypeToken<Class<UserDetails>>() {}.getType();
            Log.d("Org", "doInBackground: "+orgDetailsString);
            userDetails = gson.fromJson(orgDetailsString,UserDetails.class);
            userDetails.setRegisterDetails(coordinatorDetails);
            //userDetails.setOrganisationDetails(gson.fromJson(coordinatorInfo Class<UserDetails>()));

            response = Connectivity.makePostRequest(RestAPIURL.registerOrgandProfile, gson.toJson(userDetails).toString(), client, null);

            Log.d("Request JSON", gson.toJson(userDetails).toString());
            if (response != null) {
                Log.d("Response Code", response.getStatusLine().getReasonPhrase() + "");

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
        protected void onPostExecute(Object o) {

            if (progressDialog != null)
                progressDialog.dismiss();
            if (response != null)
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                    Toast.makeText(landingPage, "Registration Successful.Kindly Login to continue", Toast.LENGTH_LONG).show();
                    // landingPage.startActivity(new Intent(landingPage, LoginPage.class));
                    //landingPage.finish();
                }

            super.onPostExecute(o);
        }
    }





}

