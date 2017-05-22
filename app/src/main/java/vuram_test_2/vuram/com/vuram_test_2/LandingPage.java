package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class LandingPage extends AppCompatActivity {
    @Bind(R.id.donarActivity) Button donor;
    @Bind(R.id.orgActivity) Button org;
    RelativeLayout parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ButterKnife.bind(this);
        SharedPreferences preferences=getSharedPreferences(Connectivity.MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean isfirsttme=preferences.getBoolean(Connectivity.Is_First_Time,true);
        parentLayout= (RelativeLayout) findViewById(R.id.activity_landing_page);
        if(!isfirsttme)
        {
            SharedPreferences.Editor editor=getSharedPreferences(Connectivity.MyPREFERENCES,Context.MODE_PRIVATE).edit();
        }
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LandingPage.this.startActivity(new Intent(LandingPage.this,LoginPage.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(myAnim);
                LandingPage.this.startActivity(new Intent(LandingPage.this,RegistrationPage.class));
                Toast.makeText(LandingPage.this,"Donor Clicked",Toast.LENGTH_LONG).show();
            }
        });

        org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAnimation(myAnim);
                Toast.makeText(LandingPage.this,"Organisation Clicked",Toast.LENGTH_LONG).show();
                LandingPage.this.startActivity(new Intent(LandingPage.this,CoordinatorRegistration.class));
            }
        });
        Connectivity.ShowDialogBar(this);
    }

}
