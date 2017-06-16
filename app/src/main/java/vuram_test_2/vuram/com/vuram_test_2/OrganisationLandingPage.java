package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import layout.UserProfileFragment;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

public class OrganisationLandingPage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    public final String TAG = "HomeActivity.java";
    public final String myProfile = "My Profile";
    public final String aboutUs = "About Us";
    public final String logout = "Logout";

    public final int MENU_ITEM_ONE = 1;
    public final int MENU_ITEM_TWO = 2;
    public final int MENU_ITEM_THREE = 3;

    public static final int FILTER_REQUEST = 5;
    public static final int LOCATION_REQUEST = 6;

    public static String locationName = "Chennai";

    ImageButton menuButton;
    ImageButton currentLocationImageButton;
    ImageButton filterImageButton;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation_landing_page);

        /* Choose Location */
        currentLocationImageButton = (ImageButton) findViewById(R.id.current_location_imagebutton_org_landing_page);
        currentLocationImageButton.setOnClickListener(OrganisationLandingPage.this);

        /* Filter */
        filterImageButton = (ImageButton) findViewById(R.id.filter_imagebutton_org_landing_page);
        filterImageButton.setOnClickListener(OrganisationLandingPage.this);

        /* Menu Button */
        menuButton = (ImageButton) findViewById(R.id.menu_imagebutton_org_landing_page);
        menuButton.setOnClickListener(OrganisationLandingPage.this);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView_org_landing_page);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_home:
                break;
            case R.id.action_donations:
                loadFragment(new DonationListFragment());
                break;
            case R.id.action_org_profile:
                loadFragment(new OrgProfileFragment());
                break;
            case R.id.action_user_profile:
                loadFragment(new UserProfileFragment());
                break;
        }
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder_org_landing_page, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.menu_imagebutton_org_landing_page:
                PopupMenu popupMenu = new PopupMenu(OrganisationLandingPage.this, menuButton);
                Menu menu = popupMenu.getMenu();
                menu.add(Menu.NONE, MENU_ITEM_TWO, Menu.NONE, aboutUs);

                if(Connectivity.getAuthToken(OrganisationLandingPage.this,Connectivity.Donor_Token) != null) {
                    menu.add(Menu.NONE, MENU_ITEM_ONE, Menu.NONE, myProfile);
                    menu.add(Menu.NONE, MENU_ITEM_THREE, Menu.NONE, logout);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(OrganisationLandingPage.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        String itemSelected = item.getTitle().toString();
                        if (itemSelected.equals(myProfile)) {
                            startActivity(new Intent(OrganisationLandingPage.this, UserProfileActivity.class));
                        }
                        else if(itemSelected.equals(logout)) {
                            Connectivity.deleteAuthToken(OrganisationLandingPage.this,Connectivity.Donor_Token);
                            startActivity(new Intent(OrganisationLandingPage.this,LoginPageFragment.class));
                        } else if (itemSelected.equals(aboutUs)) {

                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;

            case R.id.current_location_imagebutton_org_landing_page:
                Intent intent = new Intent(OrganisationLandingPage.this, ChooseLocationActivity.class);
                startActivityForResult(intent, LOCATION_REQUEST);
                break;

            case R.id.filter_imagebutton_org_landing_page:
                intent = new Intent(OrganisationLandingPage.this, FilterActivity.class);
                startActivityForResult(intent, FILTER_REQUEST);
                break;

            case R.id.new_need_home_page:
                startActivity(new Intent(OrganisationLandingPage.this, NewNeedActivity.class));
                break;
        }
    }

}
