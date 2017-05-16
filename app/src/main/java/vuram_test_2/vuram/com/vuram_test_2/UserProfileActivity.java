package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    CircleImageView editImageButton;
    RecyclerView orgListView;
    ScrollView scrollView;
    Toolbar toolbar;

    Random randomNumberGenerator = null;
    ArrayList<OrgDetails> orgDetailsList = null;
    int orgCount = 0;
    public final String TAG = "UserProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_user_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorTextIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        scrollView = ((ScrollView) findViewById(R.id.scrollview_user_profile));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        orgListView = (RecyclerView) findViewById(R.id.org_details_recyclerview_user_profile);
        editImageButton = (CircleImageView) findViewById(R.id.edit_imagebutton_user_profile);
        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, EditProfileActivity.class));
            }
        });

        orgDetailsList = new ArrayList<OrgDetails>();
        randomNumberGenerator = new Random();
        orgCount = randomNumberGenerator.nextInt(2) + 2;
        Log.d(TAG, "onCreate: Org Count " + orgCount);
        for (int i = 0; i < orgCount; i++) {
            OrgDetails orgDetails = new OrgDetails();
            orgDetails.orgRegNo = "CBE12345678";
            orgDetails.orgName = "Aalayam Education And Charitable Trust";
            orgDetails.orgLogo = "www.aalayam.com/img";
            orgDetailsList.add(orgDetails);
        }
        Log.d(TAG, "onCreate: Org List Size " + orgDetailsList.size());
        OrgListAdapter orgListAdapter = new OrgListAdapter(this, orgDetailsList);
        orgListView.setAdapter(orgListAdapter);
        orgListView.setLayoutManager(new LinearLayoutManager(this));
    }
}
