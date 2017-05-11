package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CoordinatorRegistration extends AppCompatActivity {

    EditText emailEditText,firstNameEditText,lastNameEditText,mobileEditText,passwordEditText;
    Spinner genderFromSpinner,countryFromSpinner;
    Button coordinatorRegisterButton;
    TextView coordinatorLoginTextView;
    String emailId,firstName,lastName,mobileNo,password,gender,country;
    Boolean validated;
    ProgressDialog progressDialog;
    UserAccountDetails userAccountDetails;
    RegistrationPage registrationPage;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_registration);

        coordinatorLoginTextView = (TextView)findViewById(R.id.link_login_coordinator_register);
        emailEditText=(EditText)findViewById(R.id.email_coordinator_register);
        firstNameEditText = (EditText)findViewById(R.id.first_name_coordinator_register);
        lastNameEditText = (EditText)findViewById(R.id.last_name_coordinator_register);
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

       /* emailId = emailEditText.getText().toString();
        firstName = firstNameEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();
        mobileNo = mobileEditText.getText().toString();
        password = passwordEditText.getText().toString();
        gender = genderFromSpinner.getSelectedItem().toString();
        country = countryFromSpinner.getSelectedItem().toString();*/

    }

    public void register(){

        if(validation()){
            coordinatorRegisterButton.setEnabled(false);
            progressDialog= new ProgressDialog(CoordinatorRegistration.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            new CreateCoordinatorAccount().execute();
        }
        else
            coordinatorRegisterButton.setEnabled(true);


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
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEditText.setError("Between 4 and 10 alphanumeric characters");
            validated = false;

        } else {
            passwordEditText.setError(null);
        }

        return validated;
    }

     class CreateCoordinatorAccount extends AsyncTask{

         HttpResponse httpResponse;
         HttpClient httpClient;

         @Override
         protected void onPreExecute() {

             emailId = emailEditText.getText().toString();
             firstName = firstNameEditText.getText().toString();
             lastName = lastNameEditText.getText().toString();
             mobileNo = mobileEditText.getText().toString();
             password = passwordEditText.getText().toString();
             gender = genderFromSpinner.getSelectedItem().toString();
             country = countryFromSpinner.getSelectedItem().toString();

             super.onPreExecute();
         }

         @Override
         protected Object doInBackground(Object[] objects)
         {
             registrationPage = new RegistrationPage();
             userAccountDetails =  new UserAccountDetails();
             gson =  new Gson();
             httpClient = new DefaultHttpClient();

             userAccountDetails.setFirstName(firstName);
             userAccountDetails.setEmail(emailId);
             userAccountDetails.setLastName(lastName);
             userAccountDetails.setMobile(mobileNo);
             userAccountDetails.setPassword(password);
             userAccountDetails.setGender(gender);
             userAccountDetails.setCountry(country);

             httpResponse = registrationPage.makeRequest(RestAPIURL.register,gson.toJson(userAccountDetails).toString());

             if(httpResponse!=null)
             {
                 Log.d("Response Code",httpResponse.getStatusLine().getStatusCode()+"");

                 try {
                     InputStream ips = httpResponse.getEntity().getContent();
                     BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
                     StringBuilder sb = new StringBuilder();
                     String s;
                     while(true )
                     {
                         s = buf.readLine();
                         if(s==null || s.length()==0)
                             break;
                         sb.append(s);

                     }

                     buf.close();
                     ips.close();
                     Log.d("Response body",sb.toString());
                 } catch (IOException e) {
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
             if(progressDialog!=null)
                 progressDialog.dismiss();
             if(httpResponse!=null)
                 if(httpResponse.getStatusLine().getStatusCode()==200 || httpResponse.getStatusLine().getStatusCode()==201)
                 {
                     Toast.makeText(CoordinatorRegistration.this,"Registration Successful",Toast.LENGTH_LONG).show();
                     CoordinatorRegistration.this.startActivity(new Intent(CoordinatorRegistration.this,LoginPage.class));
                     CoordinatorRegistration.this.finish();
                 }
            Log.d("GSON",gson.toJson(userAccountDetails).toString());

             super.onPostExecute(o);

         }
     }


}

