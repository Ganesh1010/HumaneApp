package vuram_test_2.vuram.com.vuram_test_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by gokulrajk on 5/16/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Test.db";
    private static final int DATABASE_VERSION = 2;
    private static final String name1="countrydetails";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table countrydetails " +
                        "(id integer primary key autoincrement, countryid integer,countryname text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertCountrydetails(ArrayList<CountryLookUpTableDetails> countryLookUpTableDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("truncate table countrydetails");
        ContentValues contentValues = new ContentValues();
        for(int i=0;i<countryLookUpTableDetails.size();i++) {
            if(i%2==0) {
                contentValues.put("countryid", String.valueOf(countryLookUpTableDetails.get(i)));
                Log.d("one", String.valueOf(countryLookUpTableDetails.get(i)));
            }
            else {
                contentValues.put("countryname", String.valueOf(countryLookUpTableDetails.get(i)));
                Log.d("two", String.valueOf(countryLookUpTableDetails.get(i)));
            }
        }
        db.close();
    }
}
