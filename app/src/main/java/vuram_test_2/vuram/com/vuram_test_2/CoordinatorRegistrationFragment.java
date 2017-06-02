package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;


public class CoordinatorRegistrationFragment extends Fragment {

    View view;
    EditText emailEditText,firstNameEditText,mobileEditText,passwordEditText;
    Spinner countryFromSpinner;
    Button coordinatorRegisterButton,coordinatorNextButton;
  //  TextView coordinatorLoginTextView;
    String emailId;
    String firstName;
    String mobileNo;
    String password,orgAddress;
    int country;
    Boolean validated,registered;
    ProgressDialog progressDialog;
    UserDetails userDetails;
    Gson gson;
    LandingPage landingPage;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    RegisterDetails registerDetails = new RegisterDetails();
    String coordinatorDetails = null;
    Bundle bundle;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null)
            view = inflater.inflate(R.layout.fragment_coordinator_registration, container, false);

        //coordinatorLoginTextView = (TextView) view.findViewById(R.id.link_login_coordinator_register);
        emailEditText = (EditText) view.findViewById(R.id.email_coordinator_register);
        firstNameEditText = (EditText) view.findViewById(R.id.first_name_coordinator_register);
        mobileEditText = (EditText) view.findViewById(R.id.mobile_coordinator_register);
        passwordEditText = (EditText) view.findViewById(R.id.password_coordinator_register);
       // genderFromSpinner = (Spinner) view.findViewById(R.id.gender_spinner_coordinator_register);
        countryFromSpinner = (Spinner) view.findViewById(R.id.country_spinner_coordinator_register);
       // coordinatorRegisterButton = (Button) view.findViewById(R.id.signup_coordinator_register);
        coordinatorNextButton = (Button)view.findViewById(R.id.signup_coordinator_register);

        landingPage = (LandingPage) getActivity();

        coordinatorNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(register()) {
                    fragment = new OrganisationRegistrationFragment();
                    fragment.setArguments(bundle);
                    fragmentManager = getActivity().getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();
                }

                else
                    Toast.makeText(landingPage,"ENTER VALID DATA",Toast.LENGTH_SHORT).show();

            }
        });



       /* coordinatorRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

            }
        });*/

/*        coordinatorLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                //startActivity(intent);
                //finish();
                fragment = new LoginPageFragment();
                fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();
                landingPage.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });*/

        return view;

    }

    public boolean register(){

        emailId = emailEditText.getText().toString();
        firstName = firstNameEditText.getText().toString();
        mobileNo = mobileEditText.getText().toString();
        password = passwordEditText.getText().toString();
       //gender = genderFromSpinner.getSelectedItem().toString();
        country = countryFromSpinner.getSelectedItemPosition();

       /* Toast.makeText(landingPage,"email ID"+emailId,Toast.LENGTH_LONG).show();
        Toast.makeText(landingPage,"name"+firstName,Toast.LENGTH_LONG).show();
        Toast.makeText(landingPage,"mobileNo"+mobileNo,Toast.LENGTH_LONG).show();
        Toast.makeText(landingPage,"password"+password,Toast.LENGTH_LONG).show();
        Toast.makeText(landingPage,"Country"+country,Toast.LENGTH_LONG).show();*/


        if(validation()){

            registered = true;
            coordinatorNextButton.setEnabled(false);
            registerDetails = new RegisterDetails();
            userDetails = new UserDetails();
            userDetails.setFirstname(firstName);
            userDetails.setPassword(password);
            userDetails.setEmail(emailId);
            registerDetails.setCountry(1);
            registerDetails.setMobile(mobileNo);
            userDetails.setProfile(registerDetails);


            Gson gson=new Gson();
            coordinatorDetails = gson.toJson(userDetails);
            bundle= new Bundle();
            bundle.putString("COORDINATOR",coordinatorDetails);




          /*  progressDialog= new ProgressDialog(landingPage,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();*/
         //   new CreateCoordinatorAccount().execute();
        }
        else {
            registered =false;
            coordinatorNextButton.setEnabled(true);

        }
        return registered;
    }

    public boolean validation(){
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

        // Log.d("Boolean",validated.toString());
        return validated;
    }

  /*  class CreateCoordinatorAccount extends AsyncTask {

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

            //registerDetails.setGender(gender);
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

            //httpResponse = Connectivity.makePostRequest(RestAPIURL.register,gson.toJson(userDetails).toString(),httpClient,null);

            /*if(httpResponse!=null)
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


            if(progressDialog!=null)
                progressDialog.dismiss();
            if(httpResponse!=null)
                if(httpResponse.getStatusLine().getStatusCode()==200 || httpResponse.getStatusLine().getStatusCode()==201)
                {
                    Toast.makeText(landingPage,"Registration Successful",Toast.LENGTH_LONG).show();

                }
            Log.d("GSON",gson.toJson(userDetails).toString());

            super.onPostExecute(o);

        }
    }*/


}
