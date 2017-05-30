package vuram_test_2.vuram.com.vuram_test_2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    ArrayList donorList;
    RecyclerView swipeCardRecyclerView;
    private boolean add = false;
    private Paint p = new Paint();
    public boolean confirmation = false;
    AlertDialog alertDialog;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card_for_receival);

       swipeCardRecyclerView = (RecyclerView) findViewById(R.id.swipe_card_recycler_view);

        donorList = getDonorList();
        swipeAdapter = new SwipeAdapter(this, donorList);
        swipeCardRecyclerView.setAdapter(swipeAdapter);
        swipeCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initSwipe();

    }

    public ArrayList getDonorList() {
        ArrayList temp = new ArrayList();

        for (int i = 0; i < 10; i++) {
            temp.add("akshaya");

        }
        return temp;
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    if (initDialog())
                        swipeAdapter.removeItem(position);
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
        view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add) {
                    add = false;
                   // swipeAdapter.addItem(et_country.getText().toString());
                    dialog.dismiss();
                } else {
//                    countries.set(edit_position, et_country.getText().toString());
                    swipeAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });
       // et_country = (EditText) view.findViewById(R.id.et_country);
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

    class SwipeCardData extends AsyncTask{

        HttpResponse response;
        HttpClient client;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            client =  new DefaultHttpClient();
            response = Connectivity.makeGetRequest("",client,Connectivity.getAuthToken(SwipeCardForReceival.this,Connectivity.Donor_Token));
            if(response != null){
                if(response.getStatusLine().getStatusCode() == 201 || response.getStatusLine().getStatusCode() == 200){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONArray results = jsonObject.getJSONArray("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    //   Log.d("123", "doInBackground: "+results.toString());
                //    needItemResult = gson.fromJson(results.toString(), new TypeToken<List<NeedDetails>>() {
                //    }.getType());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {


            super.onPostExecute(o);
        }


    }

}
