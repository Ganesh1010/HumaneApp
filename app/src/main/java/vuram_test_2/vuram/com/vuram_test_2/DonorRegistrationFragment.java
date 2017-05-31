package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;
import vuram_test_2.vuram.com.vuram_test_2.util.Validation;

import static vuram_test_2.vuram.com.vuram_test_2.R.id.fragmentLayout;

public class DonorRegistrationFragment extends Fragment {

    View v;
    EditText nameEditText, emailEditText, mobileEditText, passwordEditText;
    Button registerButton;
    TextView loginLinkTextView;
    ProgressDialog progressDialog;
    LandingPage landingPage;
    Gson gson;
    RegisterDetails registerDetails;
    UserDetails details;
    String name,email,mobilenumber,password;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FrameLayout frameLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (v == null)
            v = inflater.inflate(R.layout.fragment_registeration_page, container, false);

        nameEditText = (EditText) v.findViewById(R.id.name_login);
        emailEditText = (EditText) v.findViewById(R.id.email_login);
        mobileEditText = (EditText) v.findViewById(R.id.mobile_register);
        passwordEditText = (EditText) v.findViewById(R.id.password_register);
        registerButton = (Button) v.findViewById(R.id.signup_register);
        loginLinkTextView = (TextView) v.findViewById(R.id.link_login_register);
        landingPage = (LandingPage) getActivity();
        frameLayout = (FrameLayout) landingPage.findViewById(fragmentLayout);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signup();
            }
        });

        loginLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fragment = new LoginPageFragment();
                fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();

                landingPage.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


            }
        });
        return v;
    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        registerButton.setEnabled(false);

        progressDialog = new ProgressDialog(landingPage,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        gson = new Gson();
        registerDetails = new RegisterDetails();
        registerDetails.setCountry(1);
        new CreateUserAccount().execute();
    }


    public void onSignupSuccess() {
        registerButton.setEnabled(true);
    }

    public void onSignupFailed() {
        Toast.makeText(landingPage, "Login failed", Toast.LENGTH_LONG).show();
         registerButton.setEnabled(true);
    }

    public boolean validate() {

        boolean valid = true;

        name = nameEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        mobilenumber = mobileEditText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameEditText.setError("at least 3 characters");
            valid = false;
        } else {
            nameEditText.setError(null);
        }
      /*  if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("enter a valid email address");
            valid = false;
        } else {
            emailEditText.setError(null);
        }*/

        if(!Validation.validate_email(email)){
            emailEditText.setError("enter valid mail id");
        }

        if (mobilenumber.isEmpty() || mobilenumber.length() != 10) {
            mobileEditText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobileEditText.setError(null);
        }

        /*if(!Validation.validate_email(email))
            emailEditText.setError("enter valid email");*/

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEditText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }

   /* @Override
    public void onClick(View view) {
        if (view == org_details) {
            //     item = (LinearLayout) findViewById(R.id.org_det);
            final View child = getLayoutInflater().inflate(R.layout.layout_org_details, null);

            chooseLocationButton = (Button) child.findViewById(R.id.btn_map);
            chooseLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(new Intent(RegistrationPage.this, AndroidGPSTrackingActivity.class));
                }
            });

            item.addView(child);
            img = (ImageView) child.findViewById(R.id.imageView_details);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.removeView(child);
                }
            });
        }
    }*/

    class CreateUserAccount extends AsyncTask {

        HttpResponse response;
        HttpClient client;

        @Override
        protected Object doInBackground(Object[] params) {

            client = new DefaultHttpClient();
            details = new UserDetails();
            registerDetails.setGender("Male");
            registerDetails.setMobile(mobilenumber);
            registerDetails.setCountry(1);
            details.setFirstname(name);
            details.setPassword(password);
            details.setEmail(email);
            details.setRegisterDetails(registerDetails);

            response = Connectivity.makePostRequest(RestAPIURL.register, gson.toJson(details).toString(), client, null);
            Log.d("Request JSON", gson.toJson(details).toString());
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
