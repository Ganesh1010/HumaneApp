package layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import vuram_test_2.vuram.com.vuram_test_2.DatabaseHelper;
import vuram_test_2.vuram.com.vuram_test_2.OrganisationDetails;
import vuram_test_2.vuram.com.vuram_test_2.R;
import vuram_test_2.vuram.com.vuram_test_2.RegisterDetails;
import vuram_test_2.vuram.com.vuram_test_2.RestAPIURL;
import vuram_test_2.vuram.com.vuram_test_2.UserDetails;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static vuram_test_2.vuram.com.vuram_test_2.util.Connectivity.getAuthToken;

public class UserProfileFragment extends Fragment implements View.OnClickListener {

    Activity activity;

    CircleImageView editImageButton;
    RecyclerView orgListView;
    ScrollView scrollView;
    Toolbar toolbar;
    Button myOrgButton;

    Random randomNumberGenerator = null;
    ArrayList<OrganisationDetails> orgDetailsList = null;
    int orgCount = 0;
    public final String TAG = "UserProfileFragment";

    String authToken;
    HttpClient client;
    HttpResponse response;
    DatabaseHelper db;
    CircleImageView userpic;
    TextView fullNameTextView, emailTextView, phoneTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.activity = getActivity();
        View inflatedView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        fullNameTextView = (TextView) inflatedView.findViewById(R.id.username_textview_user_profile_fragment);
        emailTextView = (TextView) inflatedView.findViewById(R.id.email_textview_user_profile_fragment);
        phoneTextView = (TextView) inflatedView.findViewById(R.id.phone_textview_user_profile_fragment);

        // Focusing up the page
        scrollView = ((ScrollView) inflatedView.findViewById(R.id.scrollview_user_profile_fragment));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        // Edit button functionality
        editImageButton = (CircleImageView) inflatedView.findViewById(R.id.edit_imagebutton_user_profile_fragment);
        editImageButton.setOnClickListener(UserProfileFragment.this);

        // My Organisation Button
        myOrgButton = (Button) inflatedView.findViewById(R.id.my_org_button_user_profile__fragment);
        myOrgButton.setOnClickListener(UserProfileFragment.this);

        userpic= (CircleImageView) inflatedView.findViewById(R.id.user_image_user_profile_fragment);
        //CustomPicasso.okhttp(this,userpic);

        new PopulatingTask().execute();

        return inflatedView;
    }

    public GlideUrl getUrlWithHeaders(String url) {
        Log.d(TAG, "getUrlWithHeaders: "+url);
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization","Token "+ getAuthToken(this.activity, Connectivity.Donor_Token))
                .build());
    }

    class PopulatingTask extends AsyncTask {

        String firstName;
        String email;
        RegisterDetails profile;
        String mobile;
        int countryCode;
        boolean isCoordinator;

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading Your details");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            authToken = Connectivity.getAuthToken(activity, Connectivity.Donor_Token);
            client = new DefaultHttpClient();
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
                activity.findViewById(R.id.my_org_button_user_profile__fragment).setVisibility(View.VISIBLE);
            } else {
                activity.findViewById(R.id.my_org_button_user_profile__fragment).setVisibility(View.GONE);
            }

            progressDialog.cancel();
        }

    }

    @Override
    public void onClick(View v) {

    }
}
