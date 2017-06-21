package vuram_test_2.vuram.com.vuram_test_2.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

public class CommonUI{

    public static String TAG="CommonUI.java";

    public void displayCheckoutUI(View v, int itemsCount) {
        Snackbar.make(v, itemsCount+" Item(s) added", Snackbar.LENGTH_LONG).setAction("Donate", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static Snackbar internetConnectionChecking(final Context context, final View v, final AsyncTask asyncTask) {
        Snackbar snackbar = null;
        if(asyncTask != null && context !=null && v != null) {
            if (isNetworkAvailable(context)) {
                asyncTask.execute();
            } else {
                snackbar= Snackbar.make(v, "Internet Connection not available.", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        internetConnectionChecking(context, v, asyncTask);
                    }
                });
                snackbar.show();
            }
        }
        else
        {
            Log.e(TAG, "internetConnectionChecking: ",new Throwable("Null Value Input") );
        }
        return snackbar;
    }

    public static void internalValidation(final Context context, final View v, final String data) {
        if(data != null && context !=null && v != null) {
            Snackbar.make(v, data , Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Log.e(TAG, "internalValidation ",new Throwable("Null Value Input") );
        }
    }
}