package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


import butterknife.Bind;
import butterknife.ButterKnife;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class LoginPage extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
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

        new CheckUser().execute();
    }
    class CheckUser extends AsyncTask {
        String username,pass;
        int code;
        HttpResponse httpResponse;
        HttpClient httpClient;

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                httpClient = new DefaultHttpClient();
                JSONObject obj = new JSONObject();
                obj.put("username",username);
                obj.put("password",pass);
                httpResponse= Connectivity.makePostRequest(RestAPIURL.login,obj.toString(),httpClient);
                if(httpResponse!=null) {
                    code = httpResponse.getStatusLine().getStatusCode();
                    JSONObject jsonObject=new JSONObject(Connectivity.getJosnFromResponse(httpResponse));
                    String token=jsonObject.getString("auth_token");
                    Connectivity.storeAuthToken(LoginPage.this,token);
                   // Log.d("Shared",Connectivity.getAuthToken(LoginPage.this));
                }
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
                        Toast.makeText(LoginPage.this,"Success",Toast.LENGTH_LONG).show();
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
       // startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();//So far no  email validation
        String password = _passwordText.getText().toString();
        if (password.isEmpty() || password.length() < 8 ) {
            _passwordText.setError("password should be at-least 8 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
}
