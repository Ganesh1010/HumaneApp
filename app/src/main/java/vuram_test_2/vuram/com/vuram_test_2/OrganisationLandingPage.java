package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

public class OrganisationLandingPage extends AppCompatActivity {
    Intent intent;
    GridView gv;
    String[] headings={"My Profile"," ","Organisation","","Received Items"};
    int[] images={R.drawable.icona,0,R.drawable.icomsb,0,R.drawable.iconsc};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation_landing_page);
        gv=(GridView) findViewById(R.id.details);
        gv.setAdapter(new CustomAdapter(this, headings,images));
       /* Button myprofile=(Button)findViewById(R.id.myprofile);
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(OrganisationLandingPage.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });
        Button orgdetails=(Button)findViewById(R.id.myorgdetails);
        orgdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(OrganisationLandingPage.this,OrgProfileActivity.class);
                startActivity(intent);
            }
        });
        Button donations=(Button)findViewById(R.id.donations);
       donations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }
}
