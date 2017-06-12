package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_DONOR;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_ORG;

public class LandingPage extends AppCompatActivity {


    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    //static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        frameLayout = (FrameLayout)findViewById(R.id.fragmentLayout);
        fragment = new LandingPageFragment();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();



        SharedPreferences preferences=getSharedPreferences(Connectivity.MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean isfirsttme=preferences.getBoolean(Connectivity.Is_First_Time,true);
        if(!isfirsttme)
        {
            SharedPreferences.Editor editor = getSharedPreferences(Connectivity.MyPREFERENCES,Context.MODE_PRIVATE).edit();
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
    }

}
