package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vuram_test_2.vuram.com.vuram_test_2.util.CommonUI;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences preferences=getSharedPreferences(Connectivity.MyPREFERENCES, Context.MODE_PRIVATE);
        final String first_pref=preferences.getString(Connectivity.First_Pref,null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=null;
                if(first_pref==null)
                {
                    i=new Intent(SplashScreenActivity.this,LandingPage.class);

                }
                else
                //if(first_pref=RestAPIURL.DONOR)
                {
                    i=new Intent(SplashScreenActivity.this,LoginPage.class);

                }
                //startActivity(i);
                // finish();
                CommonUI.displayCheckoutUI(findViewById(R.id.activity_splash_screen),10,SplashScreenActivity.this);
            }
        }, SPLASH_TIME_OUT);
    }
}
