package vuram_test_2.vuram.com.vuram_test_2;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.ArrayList;

import vuram_test_2.vuram.com.vuram_test_2.util.CommonUI;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;
import vuram_test_2.vuram.com.vuram_test_2.util.Validation;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.internal.zzt.TAG;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_DONOR;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_ORG;

public class LoginPageFragment extends Fragment {

    View v;
    EditText emailEditText, passwordEditText;
    Button loginButton, signupButton;
    private static final int REQUEST_SIGNUP = 0;
    ProgressDialog progressDialog;
    LandingPage landingPage;
    String email,password;
    String user_selection;
    Snackbar snackbar;
    Boolean isCoordiantor=false;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    TextView skipTextView,linkLoginTextView;
    LinearLayout login_page_linearLayout;
    RelativeLayout homeActivityLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (v == null)
            v = inflater.inflate(R.layout.fragment_loign_page, container, false);

        emailEditText = (EditText) v.findViewById(R.id.email_login);
        passwordEditText = (EditText) v.findViewById(R.id.password_login);
        skipTextView = (TextView)v.findViewById(R.id.skip_textview_loginPage);
        loginButton = (Button) v.findViewById(R.id.btn_login);
        signupButton = (Button) v.findViewById(R.id.link_login);
        linkLoginTextView = (TextView)v.findViewById(R.id.link_login_register);

        landingPage = (LandingPage) getActivity();

        login_page_linearLayout= (LinearLayout) v.findViewById(R.id.login_page_linearlayout);
        homeActivityLayout = (RelativeLayout)getActivity().findViewById(R.id.activity_main);

        Bundle bundle = getArguments();

        if(bundle != null)
             user_selection = bundle.getString(USER_KEY_TYPE);
        else
            Log.e(TAG, "onCreateView: user type from bundle is null",new NullPointerException() );


        if (user_selection.equals(USER_TYPE_SELECTION_DONOR))
                skipTextView.setVisibility(View.VISIBLE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(snackbar!=null)
                {
                    if(snackbar.isShown())
                    {
                        snackbar.dismiss();
                    }
                }
                login();
            }
        });

        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString(USER_KEY_TYPE,USER_TYPE_SELECTION_DONOR);
                fragmentManager = getFragmentManager();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();
                landingPage.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        linkLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_selection.equals(USER_TYPE_SELECTION_DONOR)) {

                    fragment = new DonorRegistrationFragment();
                    if (landingPage != null) {
                        fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();
                        isCoordiantor = false;
                        landingPage.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    } else {
                        Log.e(TAG, "Activity is null", new NullPointerException());
                    }
                }

                if (user_selection.equals(USER_TYPE_SELECTION_ORG)) {

                    fragment = new CoordinatorRegistrationFragment();
                    if (landingPage!= null) {
                        fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();
                        landingPage.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        isCoordiantor = true;

                    }
                    else{
                        Log.e(TAG, "Activity is null", new NullPointerException());
                }
            }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user_selection.equals(USER_TYPE_SELECTION_DONOR)) {
                    fragment = new DonorRegistrationFragment();
                    if (landingPage != null) {
                        fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();
                        landingPage.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                }else
                {
                    Log.e(TAG, "Activity is null", new NullPointerException());
                }

                if(user_selection.equals(USER_TYPE_SELECTION_ORG)){

                    fragment = new CoordinatorRegistrationFragment();
                    if (landingPage != null) {
                        fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();

                        landingPage.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                    }
                }
                else
                {
                    Log.e(TAG, "Activity is null", new NullPointerException());
                }
            }
        });


            DetailsPopulator populator = new DetailsPopulator(landingPage);
            populator.getMainItemDetailsFromAPI();
            DatabaseHelper helper = new DatabaseHelper(landingPage);
            ArrayList<MainItemDetails> list = helper.getAllMainItemDetails();
            Log.d("Size", list.size() + "");
            for (MainItemDetails details : list) {
                Log.d("Look up", details.getMainItemName());
            }

        return v;
    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        snackbar= CommonUI.internetConnectionChecking(getActivity(),login_page_linearLayout,new CheckUser());
    }

    public boolean validate() {

        boolean valid = true;

        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if(!Validation.validate(email)){
        //   Toast.makeText(landingPage,"invalid user name",Toast.LENGTH_SHORT).show();
            emailEditText.setError("enter valid user name");
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEditText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }
        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(landingPage, "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

  /*  @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        landingPage.moveTaskToBack(true);
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                landingPage.finish();
            }
        }
    }

    class CheckUser extends AsyncTask {
        int code;
        HttpResponse httpResponse;
        HttpClient httpClient;

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                httpClient = new DefaultHttpClient();
                JSONObject obj = new JSONObject();
                obj.put("username", email);
                obj.put("password", password);
                httpResponse = Connectivity.makePostRequest(RestAPIURL.login, obj.toString(), httpClient, null);
                if (httpResponse != null) {
                    code = httpResponse.getStatusLine().getStatusCode();
                    if(code== 200 || code==201) {
                        JSONObject jsonObject = new JSONObject(Connectivity.getJosnFromResponse(httpResponse));
                        String token = jsonObject.getString("auth_token");
                        if(isCoordiantor) {
                            Connectivity.storeAuthToken(landingPage, token, Connectivity.Coordinator_Token);
                        }
                        else if(!isCoordiantor)
                        {

                        } Connectivity.storeAuthToken(landingPage, token, Connectivity.Donor_Token);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            loginButton.setEnabled(false);

            progressDialog = new ProgressDialog(landingPage, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            if(email != null)
                email = email.trim();
            else
                Log.e(TAG, "onPreExecute: Email Is null ",new NullPointerException() );
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (progressDialog != null)
                progressDialog.dismiss();
            if (code == 200 || code == 201) {
                Toast.makeText(landingPage, "Success", Toast.LENGTH_LONG).show();
                onLoginSuccess();
            } else {
              //  CommonUI.internalValidation(getActivity(),homeActivityLayout,"REGISTRATION SUCCESSFUL");
                onLoginFailed();
            }
            super.onPostExecute(o);
        }



        public void onLoginSuccess() {
            loginButton.setEnabled(true);

            // Main Item & Sub Item details Synchronization Test
            DetailsPopulator detailsPopulator = new DetailsPopulator(landingPage);
            detailsPopulator.getMainItemDetailsFromAPI();
            detailsPopulator.getSubItemDetailsFromAPI();
            detailsPopulator.getCountryDetailsFromAPI();
            detailsPopulator.getOrgTypeDetailsFromAPI();

            if(getArguments()!=null) {
                if (user_selection.equals(USER_TYPE_SELECTION_DONOR)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(USER_KEY_TYPE,USER_TYPE_SELECTION_DONOR);
                    fragment = new HomeFragment();
                    fragment.setArguments(bundle);
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();


//                    Intent intent = new Intent(landingPage,HomeActivity.class);

                }
                if (user_selection.equals(USER_TYPE_SELECTION_ORG)) {

//                    Bundle bundle = new Bundle();
 //                   bundle.putString(USER_KEY_TYPE,USER_TYPE_SELECTION_ORG);
                    // set Fragmentclass Arguments
                  //  fragment = new Fragment();
                    //fragment.setArguments(bundle);
                    //fragmentManager = getActivity().getFragmentManager();

   //                 fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();


                    Intent intent = new Intent(landingPage, OrganisationLandingPage.class);
                    if(intent!=null)
                    {
                        intent.putExtra(USER_KEY_TYPE, USER_TYPE_SELECTION_ORG);
                        Toast.makeText(landingPage, "Organisation", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        landingPage.finish();
                    }
                    else
                    {
                        Log.e(TAG, "Intent  is null", new NullPointerException());
                    }
                }
            }
        }

        public void onLoginFailed() {
           // Toast.makeText(landingPage, "Login failed", Toast.LENGTH_LONG).show();
            CommonUI.internalValidation(getActivity(),login_page_linearLayout,"Invalid Password");
            loginButton.setEnabled(true);
        }
    }
}

