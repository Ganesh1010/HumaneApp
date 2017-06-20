package vuram_test_2.vuram.com.vuram_test_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import java.io.File;
import java.io.FileNotFoundException;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;
import static vuram_test_2.vuram.com.vuram_test_2.util.Connectivity.getAuthToken;

public class ProfilePictureActivity extends AppCompatActivity {

    public Button add_profile_pic_button,next;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    String profilePictureFilePath;
    private static final int SELECT_PHOTO = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        add_profile_pic_button= (Button) findViewById(R.id.add_profile_pic_button);
        add_profile_pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(ProfilePictureActivity.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfilePictureActivity.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        next= (Button) findViewById(R.id.next);
        next.setVisibility(View.INVISIBLE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostData().execute();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && null != data) {
            try {
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.profile_pic);
                imageView.setImageBitmap(bitmap);
                next.setVisibility(View.VISIBLE);
                add_profile_pic_button.setText("Edit Photo");

                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor == null) { // Source is Dropbox or other similar local file path
                    profilePictureFilePath =uri.getPath();
                } else {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    profilePictureFilePath=cursor.getString(idx);
                }

                System.out.println(profilePictureFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class PostData extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProfilePictureActivity.this);
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            postFile(profilePictureFilePath);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.cancel();
        }
    }

    private void postFile(String orgImageFilePath)
    {
        SyncHttpClient client = new SyncHttpClient();
        String token= getAuthToken(ProfilePictureActivity.this, Connectivity.Donor_Token);
        client.addHeader("Authorization","Token "+token);
        RequestParams params = new RequestParams();

        try {
            if(orgImageFilePath != null) {
                if (new File(orgImageFilePath).exists()) {
                    params.put("image", new File(orgImageFilePath));
                    params.put("imageType", 1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.post(RestAPIURL.postProfilePic, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.d("Edit", "onFailure: "+statusCode);
            }
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.d("Edit", "onSuccess: "+responseString);
            }
        });

    }

}
