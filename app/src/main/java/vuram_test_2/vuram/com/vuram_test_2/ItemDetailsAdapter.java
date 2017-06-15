package vuram_test_2.vuram.com.vuram_test_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.viewbadger.BadgeView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.CommonUI;
import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_KEY_TYPE;
import static vuram_test_2.vuram.com.vuram_test_2.util.CommomKeyValues.USER_TYPE_SELECTION_DONOR;

public class ItemDetailsAdapter extends RecyclerView.Adapter<ItemDetailsAdapter.MyViewHolder> {
    int quantityEachNeed=0,needId;
    int countDonatedItems=0;
    ArrayList<NeedItemDetails>needItemDetails;
    ArrayList<DonatedItemDetails> donatedItemDetailsList;
    public ArrayList<MainItemDetails> mainItemDetails;
    public ArrayList<SubItemDetails> subItemDetails;
    public DonationDetails donationDetails=new DonationDetails();
    public DonatedItemDetails donatedItemDetails;
    public DatabaseHelper db;
    View target;
    BadgeView badge;
    public ImageView getLocation;
    public static Context context;
    public static Activity activity;
    public static EditText address;
    public EditText name;
    public EditText mobileNumber;
    public GPSTracker gps;
    public static LocationAddress mapAddress;
    public View dialogView;
    public DonatingUserDetails donatingUserDetails;
    public static String TAG="CommonUI.java";
    public String donor_token;
    public Gson gson;
    public HttpClient client;
    public HttpResponse response;
    public boolean isDataFilled=false;
    public static boolean isLocationSelected=false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView requested;
        ImageView imageView;
        ImageView increment;
        ImageView decrement;
        TextView value;
        TextView requestedQuantity,promisedAmount;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageView=(ImageView)itemView.findViewById(R.id.imageView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.requested=(TextView) itemView.findViewById(R.id.requested);
            this.increment=(ImageView) itemView.findViewById(R.id.increment_custom);
            this.decrement=(ImageView) itemView.findViewById(R.id.decrement_custom);
            this.value=(TextView) itemView.findViewById(R.id.number_custom);
            this.requestedQuantity= (TextView) itemView.findViewById(R.id.requested_amount);
            this.promisedAmount= (TextView) itemView.findViewById(R.id.promisedamount);

        }
    }

    public ItemDetailsAdapter()
    {}
    public ItemDetailsAdapter(ArrayList<NeedItemDetails>needItemDetails,Context context,Activity activity,String needId) {
        this.needItemDetails=needItemDetails;
        this.context=context;
        this.activity=activity;
        this.needId=Integer.parseInt(needId);
        db=new DatabaseHelper(this.activity);
        mainItemDetails=db.getAllMainItemDetails();
        subItemDetails=db.getAllSubItemDetails();
        donatedItemDetailsList = new ArrayList<>();
        target = activity.findViewById(R.id.cart);
        badge = new BadgeView(context, target);
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_details, parent, false);
        view.setOnClickListener(OrgDetailsActivity.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.setIsRecyclable(false);
        final NeedItemDetails particularNeedItemDetails=needItemDetails.get(listPosition);

        holder.imageView.setImageResource(getItemImages(particularNeedItemDetails.getItem_type_id() - 1));

        for(int i=0;i<mainItemDetails.size();i++)
            if(mainItemDetails.get(i).getMainItemCode()==particularNeedItemDetails.getItem_type_id())
                holder.title.setText(mainItemDetails.get(i).getMainItemName());
        for(int i=0;i<subItemDetails.size();i++)
            if(subItemDetails.get(i).getSubItemCode()==particularNeedItemDetails.getSub_item_type_id())
                holder.requested.setText(subItemDetails.get(i).getSubItemName());

        holder.requestedQuantity.setText("Requested: "+particularNeedItemDetails.getQuantity()+"");
        holder.promisedAmount.setText("Promised Amount: "+particularNeedItemDetails.getDonated_amount());
        if(quantityEachNeed==0)
            holder.decrement.setVisibility(View.INVISIBLE);
        holder.increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ++quantityEachNeed;
                    if(quantityEachNeed>0)
                        holder.decrement.setVisibility(View.VISIBLE);
                    boolean isExists = false;
                    for (int i = 0; i < donatedItemDetailsList.size(); i++) {
                        donatedItemDetails = donatedItemDetailsList.get(i);
                        if (donatedItemDetails.getNeed_item_id() == particularNeedItemDetails.getNeed_item_id())
                        {
                            donatedItemDetails.setQuantity(donatedItemDetails.getQuantity() + 1);
                            if(donatedItemDetails.getQuantity()==particularNeedItemDetails.getQuantity())
                                holder.increment.setVisibility(View.INVISIBLE);
                            donatedItemDetailsList.set(i, donatedItemDetails);
                            isExists = true;
                            break;
                        }
                    }
                    if (!isExists) {
                        donatedItemDetails=new DonatedItemDetails();
                        donatedItemDetails.setNeed_item_id(particularNeedItemDetails.getNeed_item_id());
                        donatedItemDetails.setQuantity(1);
                        donatedItemDetailsList.add(donatedItemDetails);
                        ++countDonatedItems;
                    }

                    holder.value.setText((donatedItemDetails.getQuantity()+""));
                    donationDetails.setDonateditems(donatedItemDetailsList);
                    donationDetails.setNeed_id(needId);
                    displayCheckoutUI(activity.findViewById(R.id.activity_donor_org_details),quantityEachNeed,activity,donationDetails);
                    badge.setText(countDonatedItems+"");
                    if(countDonatedItems>0)
                        badge.setVisibility(View.VISIBLE);
                    badge.show();
                }
            });

            holder.decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    --quantityEachNeed;
                    if(quantityEachNeed<particularNeedItemDetails.quantity)
                        holder.increment.setVisibility(View.VISIBLE);
                    for (int i = 0; i < donatedItemDetailsList.size(); i++) {
                        donatedItemDetails = donatedItemDetailsList.get(i);
                        if (donatedItemDetails.getNeed_item_id() == particularNeedItemDetails.getNeed_item_id())
                        {
                            donatedItemDetails.setQuantity(donatedItemDetails.getQuantity() - 1);
                            if(donatedItemDetails.getQuantity()==0)
                            {
                                holder.decrement.setVisibility(View.INVISIBLE);
                                donatedItemDetailsList.remove(i);
                                --countDonatedItems;
                            }
                            else
                                donatedItemDetailsList.set(i, donatedItemDetails);
                            break;
                        }
                    }

                    holder.value.setText((donatedItemDetails.getQuantity()+""));

                    displayCheckoutUI(activity.findViewById(R.id.activity_donor_org_details),quantityEachNeed,activity,donationDetails);
                    badge.setText(countDonatedItems+"");
                    if(countDonatedItems==0)
                        badge.setVisibility(View.INVISIBLE);
                    badge.show();
                }
            });
    }
    private int getItemImages(int listPosition) {
        Integer[] drawableArray = {R.drawable.ic_food_black,R.drawable.ic_cloth_black,R.drawable.ic_blood_black,R.drawable.ic_grocery_cart_black,R.drawable.ic_stationery_black};
        return drawableArray[listPosition];
    }

    @Override
    public int getItemCount() {
        return needItemDetails!=null?needItemDetails.size():0;
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            address.setEnabled(true);
            mapAddress = (LocationAddress)data.getSerializableExtra("ADDRESS");
            address.setText(mapAddress.getAddress());
            isLocationSelected=true;
        }

        if(requestCode == 1){
            address.setEnabled(true);
            String receivedAddress = data.getStringExtra("Location");
            Log.d("Inside Item Details Adater",receivedAddress);
            address.setText(receivedAddress);
            isLocationSelected=true;

        }
    }

    class PostItemDetails extends AsyncTask {

        HttpResponse response;
        HttpClient client;

        @Override
        protected Object doInBackground(Object[] params) {

            client = new DefaultHttpClient();
            //String coordinator_token = Connectivity.getAuthToken(NewNeedActivity.this, Connectivity.Coordinator_Token);
            response = Connectivity.makePostRequest(RestAPIURL.donateNeedURL, gson.toJson(params[0],DonationDetails.class), client, donor_token);
            Log.d("Request JSON", gson.toJson(params[0],DonationDetails.class));
            if (response != null) {
                Log.d("Response Code", response.getStatusLine().getStatusCode() + "");

                try {
                    Connectivity.getJosnFromResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("Response", "Null");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if(response!=null)
                if(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==201)
                {
                    Toast.makeText(activity,"donated successfully...",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(activity,HomeActivity.class);
                    intent.putExtra(USER_KEY_TYPE, USER_TYPE_SELECTION_DONOR);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }

            super.onPostExecute(o);
        }
    }
    public void displayCheckoutUI(View v, int itemsCount, final Activity context, final DonationDetails donationDetails) {
        Snackbar.make(v, itemsCount+" Item(s) added", Snackbar.LENGTH_LONG).setAction("Donate", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                donor_token = Connectivity.getAuthToken(context, Connectivity.Donor_Token);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                dialogView = inflater.inflate(R.layout.layout_general_user, null);
                name= (EditText) dialogView.findViewById(R.id.name_general_user);
                address= (EditText) dialogView.findViewById(R.id.address_general_user);
                mobileNumber= (EditText) dialogView.findViewById(R.id.mobile_number_general_user);
                gson= new Gson();
                isDataFilled=true;

                donationDetails.setReceived(false);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                donationDetails.setdonated_at(dateFormat.format(date)+"");
                //Intent i = new Intent(context,AndroidGPSTrackingActivity.class);
                //activity.startActivityForResult(i,1);


                getLocation= (ImageView) dialogView.findViewById(R.id.btn_map);
                getLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gps = new GPSTracker(context);

                        if(gps.getIsGPSTrackingEnabled()) {
                            if(isNetworkAvailable(context)) {

                                Intent intent = new Intent(context, MapActivity.class);
                                context.startActivityForResult(intent, 2);
                            }
                            else {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                alertDialog.setTitle("Internet settings");
                                alertDialog.setMessage("Mobile data is not enabled. Do you want to go to settings menu?");

                                // On pressing Settings button
                                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                        context.startActivity(intent);
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
                        } else {
                            gps.showSettingsAlert();
                        }
                    }
                });
                builder.setView(dialogView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        donatingUserDetails =new DonatingUserDetails();
                        donatingUserDetails.setAddress(address.getText().toString());
                        donatingUserDetails.setLatitude(mapAddress.getLatitude());
                        donatingUserDetails.setLongitude(mapAddress.getLongitude());
                        donatingUserDetails.setMobile(mobileNumber.getText().toString());
                        donatingUserDetails.setName(name.getText().toString());
                        donationDetails.setDonatingUserDetails(donatingUserDetails);

                        new PostItemDetails().execute(donationDetails);
                        //dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();

                if(donor_token==null)
                {
                    dialog.show();
                    donationDetails.setRegisteredUser(false);
                }
                else {
                    donationDetails.setRegisteredUser(true);
                    System.out.println("donor token is not null");
                    PopulatingTask populatingTask=new PopulatingTask(dialog);
                    populatingTask.execute();
                }
            }
        }).show();
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    class PopulatingTask extends AsyncTask {

        AndroidGPSTrackingActivity androidGPSTrackingActivity=new AndroidGPSTrackingActivity(activity);
        String firstName;
        RegisterDetails profile;
        String mobile;
        int countryCode;
        ProgressDialog progressDialog;
        AlertDialog postItemAlertDialog;
        PopulatingTask(AlertDialog alertDialog)
        {
            this.postItemAlertDialog=alertDialog;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading Your details");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            donor_token = Connectivity.getAuthToken(context, Connectivity.Donor_Token);
            client = new DefaultHttpClient();
            response = Connectivity.makeGetRequest(RestAPIURL.getUserDetails, client, donor_token);
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                Gson gson = new Gson();
                System.out.println("populating details");
                List<UserDetails> userDetailsList = gson.fromJson(Connectivity.getJosnFromResponse(response), new TypeToken<List<UserDetails>>() {}.getType());
                UserDetails userDetails = userDetailsList.get(0);
                firstName = userDetails.getFirst_name();
                profile = userDetails.getProfile();
                mobile = profile.getMobile();
                countryCode = profile.getCountry();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mapAddress=androidGPSTrackingActivity.getAddress();
                    }
                });
            } else {
                Log.d("CAll ", "Response null");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(progressDialog.isShowing())
                progressDialog.cancel();
            name.setText(firstName);
            mobileNumber.setText(mobile);
            if(mapAddress!=null) {
                address.setText(mapAddress.getAddress());
                address.setEnabled(true);
                postItemAlertDialog.show();
            }
        }
    }
}
