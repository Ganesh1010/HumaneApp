package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;

public class OrganisationLandingPage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    Intent intent;
    GridView gv;
    String[] headings={"My Profile"," ","Organisation","","Received Items"};
    int[] images={R.drawable.icona,0,R.drawable.icomsb,0,R.drawable.iconsc};
    BottomNavigationView bottomNavigationView;

    ImageView userProfileImageView, orgProfileImageView, donationsListImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation_landing_page);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView_org_landing_page);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.action_home:
                break;
            case R.id.action_donations:
                intent = new Intent(OrganisationLandingPage.this, DonationListActivity.class);
                break;
            case R.id.action_org_profile:
                intent = new Intent(OrganisationLandingPage.this, OrgProfileActivity.class);
                break;
            case R.id.action_user_profile:
                intent = new Intent(OrganisationLandingPage.this, UserProfileActivity.class);
                break;
        }
        startActivity(intent);
        return true;
    }
}
