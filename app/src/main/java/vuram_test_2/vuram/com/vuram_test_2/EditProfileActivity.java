package vuram_test_2.vuram.com.vuram_test_2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton changeImageButton;
    ImageButton saveButton;

    String userImageFilePath;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int SELECT_PHOTO = 2;
//    private final String postImageURL = "http://vuramdevdb.vuram.com:8000/api/photos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile);
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

        // Change Image Button
        changeImageButton = (FloatingActionButton) findViewById(R.id.change_user_image_edit_profile);
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(EditProfileActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfileActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                /*Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);*/
            }
        });

        // Save Button
        saveButton = (ImageButton) findViewById(R.id.save_button_edit_profile);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new PostData().execute();
            }
        });
    }

    class PostData extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] params) {
            postFile(userImageFilePath);
            return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                ImageView imageView = (ImageView) findViewById(R.id.user_image_edit_profile);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                userImageFilePath = picturePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void postFile(String userImageFilePath) {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("address","a2323");
        try {
           if(userImageFilePath!=null)
               if(new File(userImageFilePath).exists())
                    params.put("image", new File(userImageFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(RestAPIURL.registerOrg, params,
                new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

                Log.d("Edit", "onFailure: "+responseString);

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {

                Log.d("Edit", "onSuccess: "+responseString);

            }
        });
    }

}