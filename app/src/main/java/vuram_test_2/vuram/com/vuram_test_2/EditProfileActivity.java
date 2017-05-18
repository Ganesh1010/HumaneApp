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

import java.io.File;
import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton changeImageButton;
    ImageButton saveButton;

    String userImageFilePath;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int SELECT_PHOTO = 2;
    private final String postImageURL = "http://vuramdevdb.vuram.com:8000/api/photos/";

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
                if (userImageFilePath != null) {
                    new PostDate().execute();
                }
            }
        });
    }

    class PostDate extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] params) {
            postFile(userImageFilePath, postImageURL);
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

    private void postFile(String filepath, String fileURL) {
//        HttpClient httpclient = new DefaultHttpClient();
//        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//
//        HttpPost httppost = new HttpPost(fileURL);
//        File file = new File(filepath);
//        MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
//        FileBody fileBody = new FileBody(file);
//        mpEntity.addPart("image", fileBody);
//
//        httppost.setEntity((HttpEntity) mpEntity);
//        System.out.println("executing request " + httppost.getRequestLine());
//        HttpResponse response = null;
//        try {
//            response = httpclient.execute(httppost);
//            HttpEntity resEntity = response.getEntity();
//            System.out.println(response.getStatusLine());
//            if (resEntity != null) {
//                System.out.println(EntityUtils.toString(resEntity));
//            }
//            if (resEntity != null) {
//                resEntity.getContent();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
       // httpclient.getConnectionManager().shutdown();
        final OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\";filename=\"To Do List 1.png\"\n"),
                        RequestBody.create(MediaType.parse("image/png"), new File(userImageFilePath))).addPart(Headers.of("Content-Disposition","form-data; name=\"name\""),RequestBody.create(MediaType.parse("applicaion/json"),"123")).build();
                Request request = new Request.Builder()
             //   .header("Authorization", "Token " + Connectivity.getAuthToken(EditProfileActivity.this,Connectivity.Donor_Token))
                .url("http://vuramdevdb.vuram.com:8000/api/photos/")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.d("Response",response.body().string()+"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}