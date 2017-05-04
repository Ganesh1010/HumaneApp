package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LandingPage extends AppCompatActivity {
    @Bind(R.id.donarActivity) Button donor;
    @Bind(R.id.orgActivity) Button org;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ButterKnife.bind(this);
       // Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
        //        android.R.anim.);
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
                Toast.makeText(LandingPage.this,"Donor Clicked",Toast.LENGTH_LONG).show();
            }
        });
    }
}
