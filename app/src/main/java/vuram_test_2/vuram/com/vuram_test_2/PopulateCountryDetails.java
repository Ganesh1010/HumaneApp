package vuram_test_2.vuram.com.vuram_test_2;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vuram_test_2.vuram.com.vuram_test_2.util.Connectivity;

/**
 * Created by gokulrajk on 5/15/2017.
 */
public class PopulateCountryDetails {

    Context context;
    public PopulateCountryDetails(Context c) {
        this.context=c;
    }
     public void getCountryDetailsFromAPI()
     {
       new Country().execute();
     }




    class Country extends AsyncTask {
        HttpResponse response;
        HttpClient client;
        public ArrayList<CountryLookUpTableDetails> countrydetails;

        @Override
        protected Object doInBackground(Object[] objects) {
            //response = Connectivity.makeGetRequest(RestAPIURL.code, client, null);
            if (response != null)
// if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
            {
                try {
                    JSONObject jsonObject = new JSONObject(Connectivity.getJosnFromResponse(response));
                    JSONArray results = jsonObject.getJSONArray("results");
                    Gson gson = new Gson();
                    countrydetails = gson.fromJson(results.toString(), new TypeToken<List<CountryLookUpTableDetails>>() {
                    }.getType());
                    DatabaseHelper db = new DatabaseHelper(context);
                    db.insertCountrydetails(countrydetails);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}