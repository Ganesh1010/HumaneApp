package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class RegistrationPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = "Main2Activity";
    String name,email,mobilenumber,password;

    RegisterDetails registerDetails;
    UserDetails details;
    ImageView img=null;
    Gson gson;
    LinearLayout item=null;
    ProgressDialog progressDialog;
    Button chooseLocationButton;
    @Bind(R.id.name_register) EditText _nameText;
    @Bind(R.id.input_count)EditText _mobileText;
    @Bind(R.id.email_register) EditText _emailText;
    @Bind(R.id.password_register) EditText _passwordText;
//    @Bind(R.id.reEnterPassword_register) EditText _reEnterPasswordText;
    @Bind(R.id.signup_register) Button _signupButton;
  //  @Bind(R.id.org_details_register)
    Button org_details;
    @Bind(R.id.link_login_register)

    TextView _loginLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        ButterKnife.bind(this);
//        org_details.setOnClickListener(this);
       // imageButton=(ImageButton)findViewById(R.id.back_button);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(RegistrationPage.this,LoginPage.class);
//                startActivity(i);
//            }
//        });
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog= new ProgressDialog(RegistrationPage.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        gson= new Gson();
        registerDetails=new RegisterDetails();
        registerDetails.setCountry(1);
        new CreateUserAccount().execute();
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        mobilenumber=_mobileText.getText().toString();
      //  String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobilenumber.isEmpty() || mobilenumber.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

//        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
//            _reEnterPasswordText.setError("Password Do not match");
//            valid = false;
//        } else {
//            _reEnterPasswordText.setError(null);
//        }

        return valid;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if(view==org_details)
        {
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
            img=(ImageView)child.findViewById(R.id.imageView_details);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.removeView(child);
                }
            });
        }
    }
    class CreateUserAccount extends AsyncTask{

        HttpResponse response;
        HttpClient client;

        @Override
        protected Object doInBackground(Object[] params) {

                client = new DefaultHttpClient();
                details=new UserDetails();
                registerDetails.setGender("Male");
                registerDetails.setMobile(mobilenumber);
                registerDetails.setCountry(1);
                details.setFirstname(name);
                details.setPassword(password);
                details.setEmail(email);
                details.setRegisterDetails(registerDetails);

                response= Connectivity.makePostRequest(RestAPIURL.register,gson.toJson(details).toString(),client);
                Log.d("Request JSON",gson.toJson(details).toString());
                if(response!=null)
                {
                    Log.d("Response Code",response.getStatusLine().getStatusCode()+"");

                    try {
                        Connectivity.getJosnFromResponse(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Log.d("Response","Null");
                }

            return null;
        }
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if(progressDialog!=null)
                 progressDialog.dismiss();
            if(response!=null)
            if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201)
            {
                Toast.makeText(RegistrationPage.this,"Registration Successful.Kindly Login to continue",Toast.LENGTH_LONG).show();
                RegistrationPage.this.startActivity(new Intent(RegistrationPage.this,LoginPage.class));
                RegistrationPage.this.finish();
            }
          //  Log.d("GSON",gson.toJson(registerDetails).toString());

            super.onPostExecute(o);
        }
    }

}