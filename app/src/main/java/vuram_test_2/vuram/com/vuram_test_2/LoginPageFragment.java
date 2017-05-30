package vuram_test_2.vuram.com.vuram_test_2;


import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.ArrayList;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;
import vuram_test_2.vuram.com.vuram_test_2.util.Validation;

import static android.app.Activity.RESULT_OK;

public class LoginPageFragment extends Fragment {

    View v;
    EditText emailEditText, passwordEditText;
    Button loginButton, signupButton;
    private static final int REQUEST_SIGNUP = 0;
    ProgressDialog progressDialog;
    LandingPage landingPage;
    String email,password;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    TextView registerLater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (v == null)
            v = inflater.inflate(R.layout.fragment_loign_page, container, false);
        emailEditText = (EditText) v.findViewById(R.id.email_login);
        passwordEditText = (EditText) v.findViewById(R.id.password_login);
        loginButton = (Button) v.findViewById(R.id.btn_login);
        signupButton = (Button) v.findViewById(R.id.link_login);
       // registerLater=
        landingPage = (LandingPage) getActivity();
//        registerLater.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(landingPage, HomeActivity.class);
//                startActivity(intent);
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LandingPage.user.equals("DONOR")) {
                    fragment = new DonorRegistrationFragment();
                    fragmentManager = getActivity().getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();

                    landingPage.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                    //  Intent intent = new Intent(landingPage, RegistrationPage.class);
                    // startActivityForResult(intent, REQUEST_SIGNUP);
                    //finish();

                    landingPage.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }

                if(LandingPage.user.equals("COORDINATOR")){

                    fragment = new CoordinatorRegistrationFragment();
                    fragmentManager = getActivity().getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();

                    landingPage.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


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
        // Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(landingPage, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        //DetailsPopulator populator=new DetailsPopulator(this);
        //populator.getCountryDetailsFromAPI();
        new CheckUser().execute();


    }

    public boolean validate() {

        boolean valid = true;

        email = emailEditText.getText().toString();//So far no  email validation
        password = passwordEditText.getText().toString();

        if(!Validation.validate(email)){
           Toast.makeText(landingPage,"invalid user name",Toast.LENGTH_SHORT).show();
            emailEditText.setError("enter valid user name");
        }
        if (password.isEmpty() || password.length() < 8) {
            passwordEditText.setError("password should be at-least 8 characters");
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
                    JSONObject jsonObject = new JSONObject(Connectivity.getJosnFromResponse(httpResponse));
                    String token = jsonObject.getString("auth_token");
                    Connectivity.storeAuthToken(landingPage, token, Connectivity.Donor_Token);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPreExecute() {

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
                onLoginFailed();
            }
            super.onPostExecute(o);
        }



        public void onLoginSuccess() {
            loginButton.setEnabled(true);
            //finish();

            // Main Item & Sub Item details Synchronization Test
            DetailsPopulator detailsPopulator = new DetailsPopulator(landingPage);
            detailsPopulator.getMainItemDetailsFromAPI();
            detailsPopulator.getSubItemDetailsFromAPI();

            Intent intent = new Intent(landingPage, HomeActivity.class);
            startActivity(intent);
            landingPage.finish();
        }

        public void onLoginFailed() {
            Toast.makeText(landingPage, "Login failed", Toast.LENGTH_LONG).show();

            loginButton.setEnabled(true);
        }
    }
}

