package vuram_test_2.vuram.com.vuram_test_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private static final String COUNTRY_TABLE_NAME="COUNTRY_DETAILS";
    private static final String MAIN_ITEM_TABLE_NAME="MAIN_ITEM_DETAILS";
    private static final String SUB_ITEM_TABLE_NAME="SUB_ITEM_DETAILS";
    private static final String COUNTRY_NAME="COUNTRY_NAME";
    private static final String ID="ID";
    private static final String COUNTRY_ID="COUNTRY_ID";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+COUNTRY_TABLE_NAME +
                        " ("+ID+" integer primary key autoincrement, "+COUNTRY_ID+" integer, "+COUNTRY_NAME+" text)"
        );
        db.execSQL("CREATE TABLE ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public ArrayList<CountryLookUpTableDetails> getAllCountryDetails()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CountryLookUpTableDetails>  countryDetails=new ArrayList<CountryLookUpTableDetails>();
        Cursor cursor = db.rawQuery("SELECT * from "+COUNTRY_TABLE_NAME,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                CountryLookUpTableDetails lookUpTableDetails=new CountryLookUpTableDetails();
                lookUpTableDetails.setCountryId(cursor.getInt(cursor.getColumnIndex(COUNTRY_ID)));
                lookUpTableDetails.setCountryName(cursor.getString(cursor.getColumnIndex(COUNTRY_NAME)));
                countryDetails.add(lookUpTableDetails);
                cursor.moveToNext();
            }
        }
        return  countryDetails;
    }
    public void insertIntoCountryDetails(ArrayList<CountryLookUpTableDetails> countryLookUpTableDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+COUNTRY_TABLE_NAME);
        for(CountryLookUpTableDetails lookup:countryLookUpTableDetails){
            ContentValues contentValues = new ContentValues();
            contentValues.put(COUNTRY_ID,lookup.getCountryId());
            contentValues.put(COUNTRY_NAME,lookup.getCountryName());
            db.insert(COUNTRY_TABLE_NAME,null,contentValues);
        }
        db.close();
    }
}
