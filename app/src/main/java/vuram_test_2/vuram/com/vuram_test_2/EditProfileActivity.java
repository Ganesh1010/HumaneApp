package vuram_test_2.vuram.com.vuram_test_2;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button addOrgButton, submitOrgFormButton;
    EditText orgRegNoEditText, orgNameEditText, addressEditText, emailEditText, phoneEditText, orgDescEditText;
    Spinner orgTypeSpinner;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorTextIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        orgRegNoEditText = (EditText) findViewById(R.id.org_register_num_editText_org_form);
        orgNameEditText = (EditText) findViewById(R.id.org_name_editText_org_form);
        addressEditText = (EditText) findViewById(R.id.org_address_editText_org_form);
        emailEditText = (EditText) findViewById(R.id.org_email_editText_org_form);
        phoneEditText = (EditText) findViewById(R.id.org_phone_editText_org_form);
        orgTypeSpinner = (Spinner) findViewById(R.id.org_type_spinner_org_form);
        orgDescEditText = (EditText) findViewById(R.id.org_desc_editText_org_form);

        addOrgButton = (Button) findViewById(R.id.add_org_button_edit_profile);
        addOrgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addOrgButton.getText().toString().equals("Add Organisation")) {
                    addOrgButton.setText("Cancel");
                    View orgFormView = findViewById(R.id.org_form);
                    orgFormView.setVisibility(View.GONE);
                } else {
                    addOrgButton.setText("Add Organisation");
                    View orgFormView = findViewById(R.id.org_form);
                    orgFormView.setVisibility(View.VISIBLE);
                }
            }
        });
        submitOrgFormButton = (Button) findViewById(R.id.submit_button_org_form);
        submitOrgFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orgRegNo, orgName, address, email, phone, orgType, orgDesc;
                orgRegNo = orgRegNoEditText.getText().toString();
                orgName = orgNameEditText.getText().toString();
                address = addressEditText.getText().toString();
                email = emailEditText.getText().toString();
                phone = phoneEditText.getText().toString();
                orgType = orgTypeSpinner.getSelectedItem().toString();
                orgDesc = orgDescEditText.getText().toString();

                OrganisationDetails orgDetails = new OrganisationDetails();
                orgDetails.setOrg_reg_no(orgRegNo);
                orgDetails.setOrg_name(orgName);
                orgDetails.setLatitude(1);
                orgDetails.setLongitude(1);
                orgDetails.setAddress(address);
                orgDetails.setEmail(email);
                orgDetails.setMobile(phone);
                orgDetails.setOrg_type(orgType);
                orgDetails.setDescription(orgDesc);

                gson = new Gson();
                String jsonString = gson.toJson(orgDetails).toString();
            }
        });
    }
}
