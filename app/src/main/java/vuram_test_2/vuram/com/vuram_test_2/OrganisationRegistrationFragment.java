package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class OrganisationRegistrationFragment extends Fragment {

    View v;
    EditText orgNoEditText,orgNameEditText,orgaddressEditText,orgEmailEditText,orgMobNoEditText,orgDescEditText;
    Spinner orgTypeFromSpinner;
    Button orgRegisterButton;
    String orgNo,orgName,orgAddress,orgMail,orgMobile,orgDesc,orgType,lastName;
    int latitude,longitude;
    Gson gson;
    ProgressDialog progressDialog;
    LandingPage landingPage;
    //ArrayList coordinatorDetails;
    OrganisationDetails organisationDetails;
    RegisterDetails coordinatorDetails;
    UserDetails userDetails;
    String orgDetailsString;

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

        orgRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        return v;
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

        progressDialog = new ProgressDialog(landingPage,
                R.style.AppTheme_Dark_Dialog);
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
        String coordinatorInfo;

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
  //          Bundle bundle = new Bundle();
//            coordinatorInfo = bundle.getString("COORDINATOR");
           // Type type = new TypeToken<Class<UserDetails>>() {}.getType();
            Log.d("Org", "doInBackground: "+orgDetailsString);
            userDetails = gson.fromJson(orgDetailsString,UserDetails.class);
            userDetails.setOrganisationDetails(organisationDetails);
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
    }





}


