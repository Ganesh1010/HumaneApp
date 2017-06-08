package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static vuram_test_2.vuram.com.vuram_test_2.util.Connectivity.getAuthToken;

public class EditOrgProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditOrgProfileActivity";
    Toolbar toolbar;
    ImageButton saveButton;
    FloatingActionButton changeImageButton;
    LinearLayout orgRegNoLayout, addButtonLayout;
    EditText orgNameEditText, orgAddressEditText, emailEditText, mobileEditText, orgDescEditText;
    CountryCodePicker countryCodePicker;
    Spinner orgTypeSpinner;

    String authToken;
    HttpClient client;
    HttpResponse response;
    DatabaseHelper db;

    String orgImageFilePath;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int SELECT_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_org_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_org_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        orgNameEditText = (EditText) findViewById(R.id.org_name_editText_org_form);
        orgAddressEditText = (EditText) findViewById(R.id.org_address_editText_org_form);
        emailEditText = (EditText) findViewById(R.id.org_email_editText_org_form);
        mobileEditText = (EditText) findViewById(R.id.org_phone_editText_org_form);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.country_code_org_form);
        orgDescEditText = (EditText) findViewById(R.id.org_desc_editText_org_form);
        orgTypeSpinner = (Spinner) findViewById(R.id.org_type_spinner_org_form);
        changeImageButton = (FloatingActionButton) findViewById(R.id.change_org_image_edit_org_profile);
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(EditOrgProfileActivity.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditOrgProfileActivity.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        // Back Arrow
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorTextIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Hiding unwanted fields in the form
        orgRegNoLayout = (LinearLayout) findViewById(R.id.org_reg_no_column_org_form);
        addButtonLayout = (LinearLayout) findViewById(R.id.add_column_org_form);
        orgRegNoLayout.setVisibility(View.GONE);
        addButtonLayout.setVisibility(View.GONE);

        // Populating the details
        new PopulatingTask().execute();

        // Save Button
        saveButton = (ImageButton) findViewById(R.id.save_button_edit_org_profile);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostData().execute();
            }
        });
    }

    class PopulatingTask extends AsyncTask {

        String orgName;
        String address;
        String email;
        String mobile;
        int orgType;
        String orgDesc;
        int countryCode;

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditOrgProfileActivity.this);
            progressDialog.setMessage("Loading Organisation details");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            authToken = Connectivity.getAuthToken(EditOrgProfileActivity.this, Connectivity.Donor_Token);
            client = new DefaultHttpClient();
            String authToken = Connectivity.getAuthToken(EditOrgProfileActivity.this, Connectivity.Donor_Token);
            response = Connectivity.makeGetRequest(RestAPIURL.getOrgDetails, client, authToken);
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                Gson gson = new Gson();

                List<OrganisationDetails> orgDetailsList = gson.fromJson(Connectivity.getJosnFromResponse(response), new TypeToken<List<OrganisationDetails>>() {}.getType());
                Log.d(TAG, "doInBackground: Received org count: " + orgDetailsList.size());
                OrganisationDetails orgDetails = orgDetailsList.get(0);
                orgName = orgDetails.getOrg_name();
                address = orgDetails.getAddress();
                email = orgDetails.getEmail();
                mobile = orgDetails.getPhone();
                orgType = orgDetails.getOrg_type();
                orgDesc = orgDetails.getDescription();
                countryCode = orgDetails.getCountryCode();
                Log.d(TAG, "doInBackground: Received details: " + orgName + ", " + address + ", "
                        + email + ", " + mobile + ", " + orgType + ", " + orgDesc + ", " + countryCode);
            } else {
                Log.d("CAll ", "Response null");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            orgNameEditText.setText(orgName);
            orgAddressEditText.setText(address);
            emailEditText.setText(email);
            countryCodePicker.setCountryForPhoneCode(countryCode);
            mobileEditText.setText(mobile);
            orgDescEditText.setText(orgDesc);

            /* Loading org type spinner with static(synchronized) data */
            db = new DatabaseHelper(EditOrgProfileActivity.this);
            ArrayList<OrgTypeLookUpDetails> orgTypeDetailsList = db.getAllOrgTypeDetails();
            ArrayList<String> orgTypesNameList = new ArrayList<String>();
            for (OrgTypeLookUpDetails orgTypeDetails: orgTypeDetailsList) {
                orgTypesNameList.add(orgTypeDetails.getOrgTypeName());
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(EditOrgProfileActivity.this,android.R.layout.simple_spinner_dropdown_item, orgTypesNameList);
            orgTypeSpinner.setAdapter(spinnerArrayAdapter);

            /* dynamically selecting the item corresponding to the received org type id */
            OrgTypeLookUpDetails orgTypeLookUpDetails = new DatabaseHelper(EditOrgProfileActivity.this).getOrgTypeLookUpDetails(orgType);
            if (orgTypeLookUpDetails != null) {
                String orgTypesName = orgTypeLookUpDetails.getOrgTypeName();
                int spinnerPosition = spinnerArrayAdapter.getPosition(orgTypesName);
                orgTypeSpinner.setSelection(spinnerPosition);
            }

            progressDialog.cancel();
        }

    }

    class PostData extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditOrgProfileActivity.this);
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            postFile(orgImageFilePath);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.cancel();
        }
    }

    private void postFile(String userImageFilePath) {
        SyncHttpClient client = new SyncHttpClient();
        String token= getAuthToken(EditOrgProfileActivity.this,Connectivity.Donor_Token);
        client.addHeader("Authorization","Token "+token);
        RequestParams params = new RequestParams();

        // Change User Details
        // fetching country names & codes
        int selectedCountryId = Integer.parseInt(countryCodePicker.getSelectedCountryCode().substring(1));

        String orgName = orgNameEditText.getText().toString();
        if (orgName != null) {
            if (!orgName.isEmpty()) {
                params.put("org_name", orgName);
            }
        }
        String orgAddress = orgAddressEditText.getText().toString();
        if (orgAddress != null) {
            if (!orgAddress.isEmpty()) {
                params.put("address", orgAddress);
            }
        }
        String email = emailEditText.getText().toString();
        if (email != null) {
            if (!email.isEmpty()) {
                params.put("email", email);
            }
        }
        String mobile = mobileEditText.getText().toString();
        if (mobile != null) {
            if (!mobile.isEmpty()) {
                params.put("phone", mobile);
            }
        }
        String orgType = orgTypeSpinner.getSelectedItem().toString();
        if (orgType != null) {
            if (!orgType.isEmpty()) {
                ArrayList<OrgTypeLookUpDetails> orgTypeLookUpDetailsList = new DatabaseHelper(this).getAllOrgTypeDetails();
                int orgTypeNo;
                for (int i = 0; i < orgTypeLookUpDetailsList.size(); i++) {
                    OrgTypeLookUpDetails orgTypeLookUpDetails = orgTypeLookUpDetailsList.get(i);
                    if (orgTypeLookUpDetails.getOrgTypeName().equals(orgType)) {
                        orgTypeNo = orgTypeLookUpDetails.getOrgTypeNo();
                        params.put("org_type", orgTypeNo);
                    }
                }
            }
        }
        String orgDesc = orgDescEditText.getText().toString();
        if (orgDesc != null) {
            if (!orgDesc.isEmpty()) {
                params.put("description", orgDesc);
            }
        }
        params.put("country", selectedCountryId);

        Log.d(TAG, "postFile: " + orgNameEditText.getText().toString() + "\t" + selectedCountryId
                + "\t" + mobileEditText.getText().toString() + emailEditText.getText().toString());
        try {
            if(userImageFilePath != null) {
                if (new File(userImageFilePath).exists()) {
                    params.put("image", new File(userImageFilePath));
                    params.put("imageType", 1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.post(RestAPIURL.editOrgDetails, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.d("Edit", "onFailure: "+responseString);
            }
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.d("Edit", "onSuccess: "+responseString);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                ImageView imageView = (ImageView) findViewById(R.id.user_image_edit_profile);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                orgImageFilePath = picturePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public GlideUrl getUrlWithHeaders(String url){
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization","Token "+ getAuthToken(EditOrgProfileActivity.this,Connectivity.Donor_Token))
                .build());
    }

}
