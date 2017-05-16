package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AndroidGPSTrackingActivity extends Activity {
	Button btnShowLocation;
	TextView address;
	// GPSTracker class
	GPSTracker gps;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
		address=(TextView)findViewById(R.id.textView);
		// show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// create class object
		        gps = new GPSTracker(AndroidGPSTrackingActivity.this);

				// check if GPS enabled		
		        if(gps.canGetLocation()){
		        	if(isNetworkAvailable()) {

						Intent intent = new Intent(AndroidGPSTrackingActivity.this, MapActivity.class);
						startActivityForResult(intent, 2);
					}
					else
					{
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(AndroidGPSTrackingActivity.this);
						alertDialog.setTitle("Internet settings");
						alertDialog.setMessage("Mobile data is not enabled. Do you want to go to settings menu?");

						// On pressing Settings button
						alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(Settings.ACTION_SETTINGS);
								startActivity(intent);
							}
						});
						alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});

						// Showing Alert Message
						alertDialog.show();
					}
		        }else{
		        	gps.showSettingsAlert();
		        }
			}
		});
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==2)
		{
			String message=data.getStringExtra("ADDRESS");
			address.setText(message);
		}
	}
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}