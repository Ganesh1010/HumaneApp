package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import vuram_test_2.vuram.com.vuram_test_2.util.CommonUI;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_DONOR;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_ORG;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences preferences=getSharedPreferences(Connectivity.MyPREFERENCES, Context.MODE_PRIVATE);
        final String first_pref=preferences.getString(Connectivity.First_Pref,null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String donor=Connectivity.getAuthToken(SplashScreenActivity.this,Connectivity.Donor_Token);
                String organisation=Connectivity.getAuthToken(SplashScreenActivity.this,Connectivity.Coordinator_Token);
                if(donor!=null)
                {
                    i=new Intent(SplashScreenActivity.this,HomeActivity.class);
                    i.putExtra(USER_KEY_TYPE,USER_TYPE_SELECTION_DONOR);
                }
                else if(organisation!=null)
                {
                    i=new Intent(SplashScreenActivity.this,OrganisationLandingPage.class);
                    i.putExtra(USER_KEY_TYPE,USER_TYPE_SELECTION_ORG);
                }
                else

                {
                    i=new Intent(SplashScreenActivity.this,LoginPageFragment.class);
                }

//                if(first_pref==null)
//                {
//                    i=new Intent(SplashScreenActivity.this,LandingPage.class);
//                }
//                else
//                //if(first_pref=RestAPIURL.DONOR)
//                {
//                    i=new Intent(SplashScreenActivity.this,LoginPageFragment.class);
//
//                }
                startActivity(i);

                //CommonUI.displayCheckoutUI(findViewById(R.id.activity_splash_screen),10,SplashScreenActivity.this);
            }
        }, SPLASH_TIME_OUT);
    }
}
