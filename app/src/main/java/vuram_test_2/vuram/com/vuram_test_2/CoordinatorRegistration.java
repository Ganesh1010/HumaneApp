package vuram_test_2.vuram.com.vuram_test_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CoordinatorRegistration extends AppCompatActivity {

    EditText emailEditText,firstNameEditText,lastNameEditText,mobileEditText,passwordEditText;
    Spinner genderFromSpinner,countryFromSpinner;
    Button coordinatorRegisterButton;
    TextView coordinatorLoginTextView;
    String emailId,firstName,lastName,mobileNo,password,gender,country;

    RegistrationPage registrationPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_registration);
        registrationPage=new RegistrationPage();
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
                registrationPage.signup();
            }
        });

        emailId = emailEditText.getText().toString();
        firstName = firstNameEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();
        mobileNo = mobileEditText.getText().toString();
        password = passwordEditText.getText().toString();
        gender = genderFromSpinner.getSelectedItem().toString();
        country = countryFromSpinner.getSelectedItem().toString();



    }
}
