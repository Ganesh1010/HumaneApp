package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class CoordinatorRegistration extends AppCompatActivity {

    EditText emailEditText,firstNameEditText,mobileEditText,passwordEditText;
    Spinner genderFromSpinner,countryFromSpinner;
    Button coordinatorRegisterButton;
    TextView coordinatorLoginTextView;
    String emailId,firstName,mobileNo,password,gender,country;
    Boolean validated;
    ProgressDialog progressDialog;
    UserDetails userDetails;
    RegisterDetails registerDetails;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_registration);

        coordinatorLoginTextView = (TextView)findViewById(R.id.link_login_coordinator_register);
        emailEditText=(EditText)findViewById(R.id.email_coordinator_register);
        firstNameEditText = (EditText)findViewById(R.id.first_name_coordinator_register);
        mobileEditText = (EditText)findViewById(R.id.mobile_coordinator_register);
        passwordEditText = (EditText)findViewById(R.id.password_coordinator_register);
        genderFromSpinner = (Spinner)findViewById(R.id.gender_spinner_coordinator_register);
        countryFromSpinner = (Spinner)findViewById(R.id.country_spinner_coordinator_register);
        coordinatorRegisterButton = (Button)findViewById(R.id.signup_coordinator_register);

        coordinatorRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        coordinatorLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


    }

    public void register(){

            emailId = emailEditText.getText().toString();
            firstName = firstNameEditText.getText().toString();
            mobileNo = mobileEditText.getText().toString();
            password = passwordEditText.getText().toString();
            gender = genderFromSpinner.getSelectedItem().toString();
            country = countryFromSpinner.getSelectedItem().toString();


        if(validation()){
            Toast.makeText(CoordinatorRegistration.this,"Country"+country,Toast.LENGTH_LONG).show();
            coordinatorRegisterButton.setEnabled(false);
            progressDialog= new ProgressDialog(CoordinatorRegistration.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            new CreateCoordinatorAccount().execute();
          }
       else {
            coordinatorRegisterButton.setEnabled(true);
         //   Toast.makeText(CoordinatorRegistration.this,"validation else part",Toast.LENGTH_LONG).show();
        }
    }

    public Boolean validation(){
         validated = true;
        if(firstName.isEmpty()||firstName.length()<=3){
            firstNameEditText.setError("Atleast 3 characters");
            validated = false;
        }else {
            firstNameEditText.setError(null);
        }

        if (emailId.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            emailEditText.setError("Enter a valid email");
            validated = false;
        } else {
            emailEditText.setError(null);
        }

        if (mobileNo.isEmpty() || mobileNo.length()!=10) {
            mobileEditText.setError("Enter a valid Mobile Number");
            validated = false;
        } else {
            mobileEditText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            passwordEditText.setError("Between 4 and 10 alphanumeric characters");
            validated = false;

        } else {
            passwordEditText.setError(null);
        }
        //Toast.makeText(CoordinatorRegistration.this,"boolean"+validated,Toast.LENGTH_LONG).show();
        // Log.d("Boolean",validated.toString());
        return validated;
    }

     class CreateCoordinatorAccount extends AsyncTask{

         HttpResponse httpResponse;
         HttpClient httpClient;

         @Override
         protected void onPreExecute() {

             super.onPreExecute();
         }

         @Override
         protected Object doInBackground(Object[] objects)
         {
             userDetails =  new UserDetails();
             registerDetails =  new RegisterDetails();

             gson =  new Gson();
             httpClient = new DefaultHttpClient();

             registerDetails.setGender(gender);
             registerDetails.setMobile(mobileNo);
             registerDetails.setCountry(1);

             userDetails.setEmail(emailId);
             userDetails.setFirstname(firstName);
             userDetails.setPassword(password);
             userDetails.setProfile(registerDetails);


            /* userAccountDetails.setFirstName(firstName);
             userAccountDetails.setEmail(emailId);
             userAccountDetails.setLastName(lastName);
             userAccountDetails.setMobile(mobileNo);
             userAccountDetails.setPassword(password);
             userAccountDetails.setGender(gender);
             userAccountDetails.setCountry(country);*/

             httpResponse = Connectivity.makePostRequest(RestAPIURL.register,gson.toJson(userDetails).toString(),httpClient,null);

             if(httpResponse!=null)
             {
                 Log.d("Response Code",httpResponse.getStatusLine().getStatusCode()+"");

                 try {

                    Connectivity.getJosnFromResponse(httpResponse);

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
         protected void onPostExecute(Object o) {

             //Toast.makeText(CoordinatorRegistration.this,"Registration Successful",Toast.LENGTH_LONG).show();
             if(progressDialog!=null)
                 progressDialog.dismiss();
             if(httpResponse!=null)
                 if(httpResponse.getStatusLine().getStatusCode()==200 || httpResponse.getStatusLine().getStatusCode()==201)
                 {
                     Toast.makeText(CoordinatorRegistration.this,"Registration Successful",Toast.LENGTH_LONG).show();
                     CoordinatorRegistration.this.startActivity(new Intent(CoordinatorRegistration.this,LoginPage.class));
                     CoordinatorRegistration.this.finish();
                 }
            Log.d("GSON",gson.toJson(userDetails).toString());

             super.onPostExecute(o);

         }
     }


}

