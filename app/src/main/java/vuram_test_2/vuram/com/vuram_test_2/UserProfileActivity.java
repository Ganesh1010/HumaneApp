package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;
//import vuram_test_2.vuram.com.vuram_test_2.util.CustomPicasso;

import static vuram_test_2.vuram.com.vuram_test_2.util.Connectivity.getAuthToken;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView editImageButton;
    RecyclerView orgListView;
    ScrollView scrollView;
    Toolbar toolbar;
    Button myOrgButton;

    Random randomNumberGenerator = null;
    ArrayList<OrganisationDetails> orgDetailsList = null;
    int orgCount = 0;
    public final String TAG = "UserProfileActivity";

    String authToken;
    HttpClient client;
    HttpResponse response;
    DatabaseHelper db;
    CircleImageView userpic;
    TextView fullNameTextView, emailTextView, phoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_user_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

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

        // Focusing up the page
        scrollView = ((ScrollView) findViewById(R.id.scrollview_user_profile));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        // Edit button functionality
        editImageButton = (CircleImageView) findViewById(R.id.edit_imagebutton_user_profile);
        editImageButton.setOnClickListener(UserProfileActivity.this);

        // My Organisation Button
        myOrgButton = (Button) findViewById(R.id.my_org_button_edit_profile);
        myOrgButton.setOnClickListener(UserProfileActivity.this);

        userpic= (CircleImageView) findViewById(R.id.user_image_user_profile);
        //CustomPicasso.okhttp(this,userpic);

        new PopulatingTask().execute();
    }
    public GlideUrl getUrlWithHeaders(String url){
        Log.d(TAG, "getUrlWithHeaders: "+url);
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization","Token "+ getAuthToken(UserProfileActivity.this,Connectivity.Donor_Token))
                .build());
    }
    class PopulatingTask extends AsyncTask {

        String firstName;
        String email;
        RegisterDetails profile;
        String mobile;
        int countryCode;
        boolean isCoordinator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fullNameTextView = (TextView) findViewById(R.id.username_textview_user_profile);
            emailTextView = (TextView) findViewById(R.id.email_textview_user_profile);
            phoneTextView = (TextView) findViewById(R.id.phone_textview_user_profile);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            authToken = Connectivity.getAuthToken(UserProfileActivity.this, Connectivity.Donor_Token);
            client = new DefaultHttpClient();
            String authToken = Connectivity.getAuthToken(UserProfileActivity.this, Connectivity.Donor_Token);
            response = Connectivity.makeGetRequest(RestAPIURL.getUserDetails, client, authToken);
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                Gson gson = new Gson();

                List<UserDetails> userDetailsList = gson.fromJson(Connectivity.getJosnFromResponse(response), new TypeToken<List<UserDetails>>() {}.getType());
                UserDetails userDetails = userDetailsList.get(0);
                firstName = userDetails.getFirst_name();
                email = userDetails.getEmail();
                profile = userDetails.getProfile();
                mobile = profile.getMobile();
                countryCode = profile.getCountry();
                isCoordinator = profile.isCoordinator();

            } else {
                Log.d("CAll ", "Response null");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            fullNameTextView.setText(firstName);
            emailTextView.setText(email);
            phoneTextView.setText(mobile);
            if (isCoordinator) {
                ((Button)findViewById(R.id.my_org_button_edit_profile)).setVisibility(View.VISIBLE);
            } else {
                ((Button)findViewById(R.id.my_org_button_edit_profile)).setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.edit_imagebutton_user_profile:
                startActivity(new Intent(UserProfileActivity.this, EditUserProfileActivity.class));
                break;
            case R.id.my_org_button_edit_profile:
                Intent intent = new Intent(UserProfileActivity.this, OrgProfileActivity.class);
                startActivity(intent);
                break;
        }
    }
}
