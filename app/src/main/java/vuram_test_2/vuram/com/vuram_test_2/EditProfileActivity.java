package vuram_test_2.vuram.com.vuram_test_2;

import android.Manifest;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity: ";
    Toolbar toolbar;
    FloatingActionButton changeImageButton;
    ImageButton saveButton;
    ImageView userpic;
    EditText fullNameEditText, phoneEditText, emailEditText, currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    CheckBox changePasswordCheckBox;
    LinearLayout changePasswordLayout;

    String userImageFilePath;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int SELECT_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullNameEditText = (EditText) findViewById(R.id.user_name_edittext_edit_profile);
        phoneEditText = (EditText) findViewById(R.id.phone_edittext_edit_profile);
        emailEditText = (EditText) findViewById(R.id.email_edittext_edit_profile);
        currentPasswordEditText = (EditText) findViewById(R.id.current_password_edittext_edit_profile);
        newPasswordEditText = (EditText) findViewById(R.id.new_password_edittext_edit_profile);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirm_password_edittext_edit_profile);
        changePasswordCheckBox = (CheckBox) findViewById(R.id.change_password_checkbox);
        changePasswordLayout = (LinearLayout) findViewById(R.id.change_password_linear_layout_edit_profile);
        userpic= (ImageView) findViewById(R.id.user_image_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorTextIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Glide.with(this)
                .load(getUrlWithHeaders(RestAPIURL.getUserProfilePic))
                .into(userpic);
        // Change Image Button
        changeImageButton = (FloatingActionButton) findViewById(R.id.change_user_image_edit_profile);
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(EditProfileActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfileActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        // Change password checkbox
        changePasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changePasswordLayout.setVisibility(View.VISIBLE);
                } else {
                    changePasswordLayout.setVisibility(View.GONE);
                }
            }
        });

        // Save Button
        saveButton = (ImageButton) findViewById(R.id.save_button_edit_profile);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new PostData().execute();
            }
        });
    }

    class PostData extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditProfileActivity.this);
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            postFile(userImageFilePath);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.cancel();
        }
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
                userImageFilePath = picturePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void postFile(String userImageFilePath) {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();

        // Change User Details
        // fetching country names & codes
        int selectedCountryId = 1;
        CountryCodePicker countryCodePicker = (CountryCodePicker) findViewById(R.id.country_code_picker_editprofile);
        String selectedCountryName = countryCodePicker.getSelectedCountryName();
        DatabaseHelper dbHelper = new DatabaseHelper(EditProfileActivity.this);
        ArrayList<CountryLookUpTableDetails> countryLookUpTableDetailsList =  dbHelper.getAllCountryDetails();
        for (int i = 0; i < countryLookUpTableDetailsList.size(); i++) {
            CountryLookUpTableDetails countryLookUpTableDetails = countryLookUpTableDetailsList.get(i);
            String countryName = countryLookUpTableDetails.getCountryName();
            int countryId = countryLookUpTableDetails.getCountryId();
            if (countryName.equals(selectedCountryName)) {
                selectedCountryId = countryId;
                break;
            }
        }

        params.put("first_name", fullNameEditText.getText().toString());
        params.put("country", selectedCountryId);
        params.put("mobile", phoneEditText.getText().toString());
        params.put("email", emailEditText.getText().toString());

        Log.d(TAG, "postFile: " + fullNameEditText.getText().toString() + "\t" + selectedCountryId
                + "\t" + phoneEditText.getText().toString() + emailEditText.getText().toString());
        try {
           if(userImageFilePath!=null)
               if(new File(userImageFilePath).exists()) {
                   params.put("image", new File(userImageFilePath));
                   params.put("imageType", 1);
               }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.post(RestAPIURL.editUserDetails, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.d("Edit", "onFailure: "+responseString);
            }
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.d("Edit", "onSuccess: "+responseString);
            }
        });

        // Change Password
        DefaultHttpClient httpClient = new DefaultHttpClient();
        if (changePasswordCheckBox.isChecked()) {
            JsonObject jsonObject=new JsonObject();
            JSONObject obj = new JSONObject();
            try {
                obj.put("current_password", currentPasswordEditText.getText().toString());
                obj.put("new_password", newPasswordEditText.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "postFile: "+obj.toString());
           HttpResponse httpResponse = Connectivity.makePostRequest(RestAPIURL.changePassword, obj.toString(), httpClient, null);
            if(httpResponse!=null)
            {
                if(httpResponse.getStatusLine().getStatusCode()==200 && httpResponse.getStatusLine().getStatusCode()==201)
                {
                    Log.d(TAG, "postFile: "+Connectivity.getJosnFromResponse(httpResponse));
                }
                else
                    Log.d(TAG, "postFile: "+httpResponse.getStatusLine().getReasonPhrase());
            }
        }
    }

    public GlideUrl getUrlWithHeaders(String url){
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization","Token "+Connectivity.getAuthToken(EditProfileActivity.this,Connectivity.Donor_Token))
                .build());
    }

}