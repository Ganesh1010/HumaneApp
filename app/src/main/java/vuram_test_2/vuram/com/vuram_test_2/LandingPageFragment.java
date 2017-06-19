package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import static com.google.android.gms.internal.zzt.TAG;
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
    Fragment fragment = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(v==null)
            v=inflater.inflate(R.layout.fragment_landing_page,container,false);

        frameLayout = (FrameLayout)v.findViewById(R.id.fragmentLayout);
        donor = (Button)v.findViewById(R.id.donarActivity);
        org = (Button)v.findViewById(R.id.orgActivity);
        viewLine = v.findViewById(R.id.viewLine);

        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(USER_KEY_TYPE, USER_TYPE_SELECTION_DONOR);
                if(bundle !=null) {
                    fragment = new LoginPageFragment();
                    fragment.setArguments(bundle);
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).addToBackStack("tag").commit();

                }
                else
                {
                    Log.e(TAG, "Arguments Passed in Bundle from LandingPageFragment as donor is null", new NullPointerException());
                }
            }
        });

        org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  view.setAnimation(myAnim);
                /*sets the user type as organisation & login page is loaded */
                Bundle bundle = new Bundle();
                bundle.putString(USER_KEY_TYPE, USER_TYPE_SELECTION_ORG);
                fragment = new LoginPageFragment();

                if( bundle != null) {
                    fragment.setArguments(bundle);
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).addToBackStack("tag").commit();
                }

                else
                {
                    Log.e(TAG, "Arguments Passed in Bundle from LandingPageFragment as org is null", new NullPointerException());
                }
               // Toast.makeText(LandingPage.this,"Organisation Clicked",Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }
}
