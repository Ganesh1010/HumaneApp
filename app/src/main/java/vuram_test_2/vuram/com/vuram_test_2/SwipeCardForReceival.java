package vuram_test_2.vuram.com.vuram_test_2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;


public class SwipeCardForReceival extends AppCompatActivity {

    SwipeAdapter swipeAdapter;
    ArrayList<DonationDetailsReadOnly> donorNameList;
    RecyclerView swipeCardRecyclerView;
    private boolean add = false;
    private Paint p = new Paint();
    public boolean confirmation = false;
    AlertDialog alertDialog;
    View view;
    int donationID;
    boolean received ;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card_for_receival);

        swipeCardRecyclerView = (RecyclerView) findViewById(R.id.swipe_card_recycler_view);

        // donorList = getDonorList();
        Log.d("Swipe", "doInBackground: null");
        new SwipeCardData().execute();

    }

  /*  public ArrayList getDonorList() {
        ArrayList temp = new ArrayList();

        for (int i = 0; i < 10; i++) {
            temp.add("akshaya");

        }
        return temp;
    }*/

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                 position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    if (initDialog()) {
                        donationID = donorNameList.get(position).getDonation_id();
                        //setIs_registereduser(true);
                        Log.d("Donation ID", "swipe card: " + donationID);
                        new MarkAsReceived().execute();
                     //   swipeAdapter.removeItem(position);
                    }

                } /*else {
                   // removeView();
                    //edit_position = position;
                   //* alertDialog.setTitle("Edit Country");
                    //et_country.setText(countries.get(position));
                   // alertDialog.show();
                }*/
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_black_delete);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_black_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(swipeCardRecyclerView);
    }


    /*  private void removeView(){
          if(view.getParent()!=null) {
              ((ViewGroup) view.getParent()).removeView(view);
          }
      }*/
    private boolean initDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        // alertDialog.setView(view);
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add) {
                    add = false;
                    dialog.dismiss();
                } else {
                    swipeAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });

        return true;
    }

//         AlertDialog.Builder builder = new AlertDialog.Builder(this);
//         builder.setMessage("Do you want to mark it as received?");
//         builder.setCancelable(true);
//         builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//             @Override
//             public void onClick(DialogInterface dialogInterface, int i) {
//                confirmation =true;
//             }
//         });
//         builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//             @Override
//             public void onClick(DialogInterface dialogInterface, int i) {
//                confirmation = false;
//             }
//         });
//        AlertDialog alertDialog = builder.create();
//         alertDialog.show();
//         return confirmation;

    class SwipeCardData extends AsyncTask {

        HttpResponse response;
        HttpClient client;
        String donations;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            client = new DefaultHttpClient();
            donorNameList = new ArrayList<DonationDetailsReadOnly>();
            response = Connectivity.makeGetRequest(RestAPIURL.donationList, client, Connectivity.getAuthToken(SwipeCardForReceival.this, Connectivity.Donor_Token));
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 201 || response.getStatusLine().getStatusCode() == 200) {
                    donations = Connectivity.getJosnFromResponse(response);
                    Gson gson = new Gson();
                    Log.d("123", "doInBackground: " + donations.toString());
                    donorNameList = gson.fromJson(donations, new TypeToken<List<DonationDetailsReadOnly>>() {
                    }.getType());
                    Log.d("123", "doInBackground: " + donorNameList);

                } else {
                    Log.d("Response", "doInBackground: " + response.getStatusLine().getReasonPhrase());
                }
            } else {
                Log.d("Swipe", "doInBackground: null");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            swipeAdapter = new SwipeAdapter(SwipeCardForReceival.this, donorNameList);
            swipeCardRecyclerView.setAdapter(swipeAdapter);
            swipeCardRecyclerView.setLayoutManager(new LinearLayoutManager(SwipeCardForReceival.this));
            initSwipe();


            super.onPostExecute(o);
        }


    }

    class MarkAsReceived extends AsyncTask{

        HttpResponse httpResponse;
        HttpClient httpClient;
        int code;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                httpClient = new DefaultHttpClient();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", donationID);
                //httpResponse = Connectivity.makePostRequest(RestAPIURL.receivedURL, jsonObject.toString(), httpClient, null);
                if (httpResponse != null) {
                    code = httpResponse.getStatusLine().getStatusCode();
                    Log.d("Swipe", "doInBackground:"+httpResponse.getStatusLine().getStatusCode());
                    Log.d("Swipe", "doInBackground:"+jsonObject.toString());
                    Log.d("SWIPE", "doInBackground: "+httpResponse.getStatusLine().getReasonPhrase());


                    if(code== 200 || code==201) {
                      /*  JSONObject Object = new JSONObject(Connectivity.getJosnFromResponse(httpResponse));
                        String output = Object.getString("is_received");
                        Log.d("Swipe", "doInBackground: null"+httpResponse.getStatusLine().getReasonPhrase());
                        Log.d("Swipe", "doInBackground: "+output);*/

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            swipeAdapter.removeItem(position);

            super.onPostExecute(o);
        }


    }

}

