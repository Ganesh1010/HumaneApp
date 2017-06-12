package vuram_test_2.vuram.com.vuram_test_2.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
public class Connectivity {
    public static final String MyPREFERENCES = "Creds" ;
    public static final String Donor_Token = "DONOR_TOKEN";
    public static final String Is_First_Time = "IS_FIRST_TIME";
    public static final String Coordinator_Token="COORDINATOR_TOKEN";
    public static final String First_Pref="FIRST_PREF";
    public static String getJsonFromInputSterm(InputStreamReader inputStreamReader ) {
        BufferedReader br = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static HttpResponse makePostRequest(String uri, String json, HttpClient client,String token) {
        try {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(json));
            if(token!=null)
                httpPost.setHeader("Authorization","Token "+token);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            return client.execute(httpPost);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static HttpResponse makeGetRequest(String uri, HttpClient client, String token) {
        try {
            HttpGet httpGet = new HttpGet(uri);
           // Log.d("T",token);
            //httpGet.setHeader(new BasicHeader("Authorization: Token ",token));
            if(token!=null)
            httpGet.setHeader("Authorization","Token "+token);
//            httpGet.addHeader("Authorization"," Token "+token);
            //Log.d("Header",httpGet.("Authorization").toString());
            return client.execute(httpGet);
        } catch (UnsupportedEncodingException e) {
            Log.e("Exception", "makeGetRequest: ",e);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("Exception", "makeGetRequest: ",e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", "makeGetRequest: ",e);
        }
        return null;
    }
    public  static  String getJosnFromResponse(HttpResponse httpResponse )
    {
        StringBuilder sb = new StringBuilder();
        Log.d("Response Code",httpResponse.getStatusLine().getStatusCode()+"");
        if(httpResponse!=null)
        {
            if(httpResponse.getStatusLine().getStatusCode()==200) {
                try {
                    InputStream ips = httpResponse.getEntity().getContent();
                    BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                    String s;
                    while (true) {
                        s = buf.readLine();
                        if (s == null || s.length() == 0)
                            break;
                        sb.append(s);

                    }
                    buf.close();
                    ips.close();
                    Log.d("Response body", sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  sb.toString();
    }
    public  static  String getAuthToken(Context c,String type)
    {
       SharedPreferences pref=c.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
        return  pref.getString(type,null);
    }
    public static void storeAuthToken(Context c,String token,String type)
    {
        SharedPreferences.Editor pref=c.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE).edit();
        pref.putString(type,token);
        pref.commit();
    }
    public  static void deleteAuthToken(Context c,String type)
    {
        SharedPreferences.Editor pref=c.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE).edit();
        pref.remove(type);
        pref.commit();
    }
    public static boolean ShowDialogBar(final Context context) {
        if (isNetworkAvailable(context))
        {
            return true;
            }
        else
        {
            try {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Connection Failed");
                alertDialog.setMessage("Please Check your Internet Connection");
                alertDialog.setPositiveButton ("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetworkAvailable(context))
                        dialog.dismiss();
                    }
                });
                alertDialog.create().show();
                return false;
            }
            catch(Exception e)
            {
                Log.d("Dialog", "Show Dialog: "+e.getMessage());
            }
            return true;
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
