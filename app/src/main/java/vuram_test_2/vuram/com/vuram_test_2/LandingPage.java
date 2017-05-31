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
    Button donor,org;
    View viewLine;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    static String user;


/*    @Bind(R.id.fragmentLayout)FrameLayout frameLayout;
    @Bind(R.id.donarActivity) Button donor;
    @Bind(R.id.orgActivity) Button org;
    @Bind(R.id.viewLine) View viewLine;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
       // ButterKnife.bind(this);
        frameLayout = (FrameLayout)findViewById(R.id.fragmentLayout);
        donor = (Button)findViewById(R.id.donarActivity);
        org = (Button)findViewById(R.id.orgActivity);
        viewLine = findViewById(R.id.viewLine);



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
        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(LandingPage.this,"Donor Clicked",Toast.LENGTH_LONG).show();

               // v.startAnimation(myAnim);
                user = "DONOR";

                org.setVisibility(View.INVISIBLE);
                donor.setVisibility(View.INVISIBLE);
                viewLine.setVisibility(View.INVISIBLE);

                Bundle bundle = new Bundle();
                bundle.putString(USER_KEY_TYPE, USER_TYPE_SELECTION_DONOR);

                fragment = new  LoginPageFragment();
                fragment.setArguments(bundle);
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();

                Toast.makeText(LandingPage.this,"Donor Clicked",Toast.LENGTH_LONG).show();
            }
        });

        org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  view.setAnimation(myAnim);

                user = "COORDINATOR";
                org.setVisibility(View.INVISIBLE);
                donor.setVisibility(View.INVISIBLE);
                viewLine.setVisibility(View.INVISIBLE);

                Bundle bundle = new Bundle();
                bundle.putString(USER_KEY_TYPE, USER_TYPE_SELECTION_ORG);

                fragment = new LoginPageFragment();
                fragment.setArguments(bundle);
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();

                Toast.makeText(LandingPage.this,"Organisation Clicked",Toast.LENGTH_LONG).show();
            }
        });


    }

}
