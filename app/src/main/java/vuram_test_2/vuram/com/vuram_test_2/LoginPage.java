package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginPage extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    static String email=null;
    ProgressDialog progressDialog=null;
    @Bind(R.id.email_login) EditText _emailText;
    @Bind(R.id.password_login) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_login) TextView _signupLink;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegistrationPage.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                //finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog= new ProgressDialog(LoginPage.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        new CheckUser().execute();
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }
    class CheckUser extends AsyncTask {
        String username,pass;
        int code;
        HttpURLConnection connectionString;
        @Override
        protected Object doInBackground(Object[] params) {
            String authString = username+ ":" + pass;
            String URLaddress ="";
            try {
                final String basicAuth = "Basic " + Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
                URL address = new URL(URLaddress);
                connectionString = (HttpURLConnection) address.openConnection();
                connectionString.setRequestMethod("GET");
                connectionString.setRequestProperty("Authorization", basicAuth);
                connectionString.setConnectTimeout(10000);
                connectionString.setReadTimeout(10000);
                connectionString.connect();
                code=connectionString.getResponseCode();
        } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            username=_emailText.getText().toString();
            pass=_passwordText.getText().toString();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {

            if(progressDialog!=null)
                progressDialog.dismiss();
                    if(code==200 ||code==201 )
                    {
                        onLoginSuccess();
                    }
                    else
                    {
                        onLoginFailed();
                    }
            super.onPostExecute(o);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        //finish();

        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }

        if (password.isEmpty() || password.length() < 8 ) {
            _passwordText.setError("password should be at-least 8 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }

}
