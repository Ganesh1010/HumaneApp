package vuram_test_2.vuram.com.vuram_test_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by gokulrajk on 5/16/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    private static final String DATABASE_NAME = "Test.db";
    private static final int DATABASE_VERSION = 2;
    private static final String ID = "ID";
    private static final String COUNTRY_TABLE_NAME = "COUNTRY_DETAILS";
    private static final String COUNTRY_NAME = "COUNTRY_NAME";
    private static final String COUNTRY_ID = "COUNTRY_ID";
    private static final String MAIN_ITEM_TABLE_NAME = "MAIN_ITEM_DETAILS";
    private static final String MAIN_ITEM_CODE = "MAIN_ITEM_CODE";
    private static final String MAIN_ITEM_NAME = "MAIN_ITEM_NAME";
    private static final String SUB_ITEM_TABLE_NAME = "SUB_ITEM_DETAILS";
    private static final String SUB_ITEM_CODE = "SUB_ITEM_CODE";
    private static final String SUB_ITEM_NAME = "SUB_ITEM_NAME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + COUNTRY_TABLE_NAME +
                        " (" + ID + " integer primary key autoincrement, " + COUNTRY_ID + " integer, " + COUNTRY_NAME + " text)"
        );
        db.execSQL(
                "create table " + MAIN_ITEM_TABLE_NAME +
                        " (" + ID + " integer primary key autoincrement, " + MAIN_ITEM_CODE + " integer, " + MAIN_ITEM_NAME + " text)"
        );
        db.execSQL(
                "create table " + SUB_ITEM_TABLE_NAME +
                        " (" + ID + " integer primary key autoincrement, " + SUB_ITEM_CODE + " integer, " + SUB_ITEM_NAME + " text, " + MAIN_ITEM_CODE + " integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<CountryLookUpTableDetails> getAllCountryDetails() {
        db = this.getReadableDatabase();
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
        db.close();
        return  countryDetails;
    }

    public void insertIntoCountryDetails(ArrayList<CountryLookUpTableDetails> countryLookUpTableDetails) {
        db = this.getWritableDatabase();
        db.execSQL("delete from "+COUNTRY_TABLE_NAME);
        for(CountryLookUpTableDetails lookup:countryLookUpTableDetails){
            ContentValues contentValues = new ContentValues();
            contentValues.put(COUNTRY_ID,lookup.getCountryId());
            contentValues.put(COUNTRY_NAME,lookup.getCountryName());
            db.insert(COUNTRY_TABLE_NAME,null,contentValues);
        }
        db.close();
    }

    public ArrayList<MainItemDetails> getAllMainItemDetails() {
        db = this.getReadableDatabase();
        ArrayList<MainItemDetails> mainItemDetailsList = new ArrayList<MainItemDetails>();
        Cursor cursor = db.rawQuery("select * from " + MAIN_ITEM_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MainItemDetails mainItemDetails = new MainItemDetails();
                int mainItemCode = cursor.getInt(cursor.getColumnIndex(MAIN_ITEM_CODE));
                String mainItemName = cursor.getString(cursor.getColumnIndex(MAIN_ITEM_NAME));
                mainItemDetails.setMainItemCode(mainItemCode);
                mainItemDetails.setMainItemName(mainItemName);
                mainItemDetailsList.add(mainItemDetails);
                cursor.moveToNext();
            }
        }
        db.close();
        return mainItemDetailsList;
    }

    public void insertIntoMainItemDetails(ArrayList<MainItemDetails> mainItemDetailsList) {
        db = this.getWritableDatabase();
        db.execSQL("delete from " + MAIN_ITEM_TABLE_NAME);
        for(MainItemDetails mainItemDetails : mainItemDetailsList){
            ContentValues contentValues = new ContentValues();
            contentValues.put(MAIN_ITEM_CODE, mainItemDetails.getMainItemCode());
            contentValues.put(MAIN_ITEM_NAME, mainItemDetails.getMainItemName());
            db.insert(MAIN_ITEM_TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    public ArrayList<SubItemDetails> getAllSubItemDetails() {
        db = this.getReadableDatabase();
        ArrayList<SubItemDetails> subItemDetailsList = new ArrayList<SubItemDetails>();
        Cursor cursor = db.rawQuery("select * from " + SUB_ITEM_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                SubItemDetails subItemDetails = new SubItemDetails();
                int subItemCode = cursor.getInt(cursor.getColumnIndex(SUB_ITEM_CODE));
                String subItemName = cursor.getString(cursor.getColumnIndex(SUB_ITEM_NAME));
                int mainItemCode = cursor.getInt(cursor.getColumnIndex(MAIN_ITEM_CODE));
                subItemDetails.setSubItemCode(subItemCode);
                subItemDetails.setSubItemName(subItemName);
                subItemDetails.setMainItemCode(mainItemCode);
                subItemDetailsList.add(subItemDetails);
                cursor.moveToNext();
            }
        }
        db.close();
        return subItemDetailsList;
    }

    public void insertIntoSubItemDetails(ArrayList<SubItemDetails> subItemDetailsList) {
        db = this.getWritableDatabase();
        db.execSQL("delete from " + SUB_ITEM_TABLE_NAME);
        for(SubItemDetails subItemDetails : subItemDetailsList){
            ContentValues contentValues = new ContentValues();
            contentValues.put(SUB_ITEM_CODE, subItemDetails.getSubItemCode());
            contentValues.put(SUB_ITEM_NAME, subItemDetails.getSubItemName());
            contentValues.put(MAIN_ITEM_CODE, subItemDetails.getMainItemCode());
            db.insert(SUB_ITEM_TABLE_NAME, null, contentValues);
        }
        db.close();
    }
}