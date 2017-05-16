package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.os.AsyncTask;

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

public class PopulateCountryDetails {

    Context context;

    public PopulateCountryDetails(Context c) {
        this.context = c;
    }

    public void getCountryDetailsFromAPI() {
        new Country().execute();
    }


    class Country extends AsyncTask {
        HttpResponse response;
        HttpClient client;
        public ArrayList<CountryLookUpTableDetails> countryDetails;

        @Override
        protected Object doInBackground(Object[] objects) {
            response = Connectivity.makeGetRequest(RestAPIURL.code, client, null);
            if (response != null)
 if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
     {
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
        protected void onPreExecute() {
            client=new DefaultHttpClient();
            super.onPreExecute();
        }
    }
}