package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.FragmentManager;import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class LandingPage extends AppCompatActivity {



    @Bind(R.id.fragmentLayout)FrameLayout frameLayout;
    @Bind(R.id.donarActivity) Button donor;
    @Bind(R.id.orgActivity) Button org;
    @Bind(R.id.viewLine) View viewLine;
    Fragment fragment = null;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ButterKnife.bind(this);

        SharedPreferences preferences=getSharedPreferences(Connectivity.MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean isfirsttme=preferences.getBoolean(Connectivity.Is_First_Time,true);
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
               // LandingPage.this.startActivity(new Intent(LandingPage.this,LoginPageFragment.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(LandingPage.this,"Donor Clicked",Toast.LENGTH_LONG).show();

                v.startAnimation(myAnim);

                org.setVisibility(View.INVISIBLE);
                donor.setVisibility(View.INVISIBLE);
                viewLine.setVisibility(View.INVISIBLE);
                fragment = new DonorRegistrationFragment();
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();

                Toast.makeText(LandingPage.this,"Donor Clicked",Toast.LENGTH_LONG).show();
            }
        });

        org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAnimation(myAnim);
// must be changed to organisation registration fragment

                org.setVisibility(View.INVISIBLE);
                donor.setVisibility(View.INVISIBLE);
                viewLine.setVisibility(View.INVISIBLE);
                fragment = new LoginPageFragment();
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();

                Toast.makeText(LandingPage.this,"Organisation Clicked",Toast.LENGTH_LONG).show();
            }
        });


    }

}
