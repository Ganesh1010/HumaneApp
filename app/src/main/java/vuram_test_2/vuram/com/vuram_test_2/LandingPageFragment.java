package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_DONOR;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_ORG;

/**
 * Created by akshayagr on 12-06-2017.
 */

public class LandingPageFragment extends Fragment {

    View v,viewLine;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    Button donor,org;
    Fragment fragment =null;

    static String user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(v==null)
            v=inflater.inflate(R.layout.landing_page_fragment,container,false);

        frameLayout = (FrameLayout)v.findViewById(R.id.fragmentLayout);
        donor = (Button)v.findViewById(R.id.donarActivity);
        org = (Button)v.findViewById(R.id.orgActivity);
        viewLine = v.findViewById(R.id.viewLine);

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

                //Toast.makeText(LandingPage.this,"Donor Clicked",Toast.LENGTH_LONG).show();
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

               // Toast.makeText(LandingPage.this,"Organisation Clicked",Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }
}
