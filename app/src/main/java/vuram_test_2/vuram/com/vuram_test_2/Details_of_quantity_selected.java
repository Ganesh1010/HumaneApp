
package vuram_test_2.vuram.com.vuram_test_2;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class Details_of_quantity_selected extends AppCompatActivity {
    static Context context;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = Details_of_quantity_selected.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        PopulateCountryDetails populateCountryDetails=new PopulateCountryDetails(this);
        populateCountryDetails.getCountryDetailsFromAPI();
        Button edit = (Button) findViewById(R.id.edit_details);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button cancel = (Button) findViewById(R.id.cancel_details);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_details);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Fragment fr = null;
                switch (i) {
                    case R.id.doante_by_urself_details:
                        fr = new Urself();
                        break;
                    case R.id.need_volunteer_details:
                        fr = new Need_volunteer();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, fr);
                fragmentTransaction.commit();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Details_of_quantity_selected Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
