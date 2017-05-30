package vuram_test_2.vuram.com.vuram_test_2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

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
    EditText _nameText;
    EditText _mobileText;
    EditText _emailText;
    EditText _passwordText;
    GPSTracker gps;
//    @Bind(R.id.reEnterPassword_register) EditText _reEnterPasswordText;
    Button _signupButton;
  //  @Bind(R.id.org_details_register)
    Button org_details;
    TextView _loginLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        ButterKnife.bind(this);
        _nameText=(EditText)findViewById(R.id.name_register);
        _mobileText=(EditText)findViewById(R.id.input_count);
        _emailText=(EditText)findViewById(R.id.email_register);
        _passwordText=(EditText)findViewById(R.id.password_register);
        _signupButton=(Button) findViewById(R.id.signup_register);
        _loginLink=(TextView)findViewById(R.id.link_login_register);
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
                Intent intent = new Intent(getApplicationContext(),LoginPageFragment.class);
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
                    gps = new GPSTracker(RegistrationPage.this);

                    // check if GPS enabled
                    if(gps.getIsGPSTrackingEnabled()){
                        if(isNetworkAvailable()) {

                            Intent intent = new Intent(RegistrationPage.this, MapActivity.class);
                            startActivityForResult(intent, 2);
                        }
                        else
                        {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegistrationPage.this);
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
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

                response= Connectivity.makePostRequest(RestAPIURL.register,gson.toJson(details).toString(),client,null);
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
                RegistrationPage.this.startActivity(new Intent(RegistrationPage.this,LoginPageFragment.class));
                RegistrationPage.this.finish();
            }
            super.onPostExecute(o);
        }
    }
  }
