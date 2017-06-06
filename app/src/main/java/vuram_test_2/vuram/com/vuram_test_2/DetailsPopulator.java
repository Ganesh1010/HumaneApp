package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

import static com.google.android.gms.wearable.DataMap.TAG;

public class DetailsPopulator {

    Context context;

    public DetailsPopulator(Context c) {
        this.context = c;
    }

    public void getCountryDetailsFromAPI() {
        new CountryDetailsSyncTask().execute();
    }

    public void getMainItemDetailsFromAPI() { new MainItemDetailsSyncTask().execute(); }

    public void getSubItemDetailsFromAPI() { new SubItemDetailsSyncTask().execute(); }

    public void getOrgTypeDetailsFromAPI() { new OrgTypeDetailsSyncTask().execute(); }

    class CountryDetailsSyncTask extends AsyncTask {
        HttpResponse response;
        HttpClient client;
        public ArrayList<CountryLookUpTableDetails> countryDetails;

        @Override
        protected void onPreExecute() {
            client=new DefaultHttpClient();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            response = Connectivity.makeGetRequest(RestAPIURL.countryDetails, client, null);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                    try {
                        JSONArray jsonObject = new JSONArray(Connectivity.getJosnFromResponse(response));
                        Gson gson = new Gson();
                        countryDetails = gson.fromJson(jsonObject.toString(), new TypeToken<List<CountryLookUpTableDetails>>() {
                        }.getType());
                        DatabaseHelper db = new DatabaseHelper(context);
                        db.insertIntoCountryDetails(countryDetails);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // Verifying if all the items are synced
            DatabaseHelper db = new DatabaseHelper(context);
            int count = db.getAllCountryDetails().size();
            Log.d(TAG, "Country details synced: " + count);
        }
    }

    class MainItemDetailsSyncTask extends AsyncTask {
        HttpResponse response;
        HttpClient client;
        public ArrayList<MainItemDetails> mainItemDetailsList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            client = new DefaultHttpClient();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            response = Connectivity.makeGetRequest(RestAPIURL.mainItemDetails, client, null);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                    try {
                        JSONArray jsonObject = new JSONArray(Connectivity.getJosnFromResponse(response));
                        Gson gson = new Gson();
                        Log.d(TAG, "doInBackground: JSON: " + jsonObject.toString());
                        mainItemDetailsList = gson.fromJson(jsonObject.toString(), new TypeToken<List<MainItemDetails>>() {}.getType());
                        DatabaseHelper db = new DatabaseHelper(context);
                        db.insertIntoMainItemDetails(mainItemDetailsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    class SubItemDetailsSyncTask extends AsyncTask {
        HttpResponse response;
        HttpClient client;
        public ArrayList<SubItemDetails> subItemDetailsList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            client = new DefaultHttpClient();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            response = Connectivity.makeGetRequest(RestAPIURL.subItemDetails, client, null);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                    try {
                        JSONArray jsonObject = new JSONArray(Connectivity.getJosnFromResponse(response));
                        Gson gson = new Gson();
                        subItemDetailsList = gson.fromJson(jsonObject.toString(), new TypeToken<List<SubItemDetails>>() {}.getType());
                        DatabaseHelper db = new DatabaseHelper(context);
                        db.insertIntoSubItemDetails(subItemDetailsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // Verifying if all the items are synced
            DatabaseHelper db = new DatabaseHelper(context);
            int mainItemsCount = db.getAllMainItemDetails().size();
            Log.d(TAG, "insertIntoMainItemDetails: Main Items synced: " + mainItemsCount);
            int subItemsCount = db.getAllSubItemDetails().size();
            Log.d(TAG, "insertIntoMainItemDetails: Sub Items synced: " + subItemsCount);
        }
    }

    class OrgTypeDetailsSyncTask extends AsyncTask {
        HttpResponse response;
        HttpClient client;
        public ArrayList<OrgTypeLookUpDetails> orgTypeDetailsList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            client = new DefaultHttpClient();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            response = Connectivity.makeGetRequest(RestAPIURL.orgTypeDetails, client, null);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                    try {
                        JSONArray jsonObject = new JSONArray(Connectivity.getJosnFromResponse(response));
                        Gson gson = new Gson();
                        orgTypeDetailsList = gson.fromJson(jsonObject.toString(), new TypeToken<List<OrgTypeLookUpDetails>>() {}.getType());
                        DatabaseHelper db = new DatabaseHelper(context);
                        db.insertIntoOrgTypeDetails(orgTypeDetailsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // Verifying if all the items are synced
            DatabaseHelper db = new DatabaseHelper(context);
            int count = db.getAllOrgTypeDetails().size();
            Log.d(TAG, "Org Types synced: " + count);
        }
    }
}