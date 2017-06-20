package vuram_test_2.vuram.com.vuram_test_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper.java";
    SQLiteDatabase db;
    String itemName;

    private static final String DATABASE_NAME = "Test.db";
    private static final int DATABASE_VERSION = 2;

    private static final String COUNTRY_TABLE_NAME = "COUNTRY_DETAILS";
    private static final String COUNTRY_NAME = "COUNTRY_NAME";
    private static final String COUNTRY_CODE = "COUNTRY_CODE";

    private static final String MAIN_ITEM_TABLE_NAME = "MAIN_ITEM_DETAILS";
    private static final String MAIN_ITEM_CODE = "MAIN_ITEM_CODE";
    private static final String MAIN_ITEM_NAME = "MAIN_ITEM_NAME";

    private static final String SUB_ITEM_TABLE_NAME = "SUB_ITEM_DETAILS";
    private static final String SUB_ITEM_CODE = "SUB_ITEM_CODE";
    private static final String SUB_ITEM_NAME = "SUB_ITEM_NAME";

    private static final String ORG_TYPE_TABLE_NAME = "ORG_TYPE_LOOK_UP_DETAILS";
    private static final String ORG_TYPE_NO = "ORG_TYPE_NO";
    private static final String ORG_TYPE_NAME = "ORG_TYPE_NAME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + MAIN_ITEM_TABLE_NAME +
                        " (" + MAIN_ITEM_CODE + " integer primary key, " + MAIN_ITEM_NAME + " text)"
        );
        db.execSQL(
                "create table " + SUB_ITEM_TABLE_NAME +
                        " (" + SUB_ITEM_CODE + " integer primary key, " + SUB_ITEM_NAME + " text, " + MAIN_ITEM_CODE + " integer)"
        );
        db.execSQL(
                "create table " + COUNTRY_TABLE_NAME +
                        " (" + COUNTRY_CODE + " integer primary key, " + COUNTRY_NAME + " text)"
        );
        db.execSQL(
                "create table " + ORG_TYPE_TABLE_NAME +
                        " (" + ORG_TYPE_NO + " integer primary key, " + ORG_TYPE_NAME + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }

    /* Main Item Details */
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
            Log.d(TAG, "insertIntoMainItemDetails: Item Code: " + mainItemDetails.getMainItemCode());
            Log.d(TAG, "insertIntoMainItemDetails: Item Name: " + mainItemDetails.getMainItemName());
        }
        db.close();
    }

    /* Sub Item Details */
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

    /* Country Lookup Details */
    public ArrayList<CountryLookUpTableDetails> getAllCountryDetails() {
        db = this.getReadableDatabase();
        ArrayList<CountryLookUpTableDetails>  countryDetails = new ArrayList<CountryLookUpTableDetails>();
        Cursor cursor = db.rawQuery("SELECT * from " + COUNTRY_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                CountryLookUpTableDetails countryLookUpTableDetails=new CountryLookUpTableDetails();
                countryLookUpTableDetails.setCountryName(cursor.getString(cursor.getColumnIndex(COUNTRY_NAME)));
                countryLookUpTableDetails.setCountry_code(cursor.getInt(cursor.getColumnIndex(COUNTRY_CODE)));
                countryDetails.add(countryLookUpTableDetails);
                cursor.moveToNext();
            }
        }
        db.close();
        return  countryDetails;
    }
    public void insertIntoCountryDetails(ArrayList<CountryLookUpTableDetails> countryLookUpTableDetailsList) {
        db = this.getWritableDatabase();
        db.execSQL("delete from "+COUNTRY_TABLE_NAME);
        for(CountryLookUpTableDetails lookup:countryLookUpTableDetailsList){
            ContentValues contentValues = new ContentValues();
            contentValues.put(COUNTRY_CODE, lookup.getCountry_code());
            contentValues.put(COUNTRY_NAME, lookup.getCountryName());
            db.insert(COUNTRY_TABLE_NAME,null,contentValues);
        }
        db.close();
    }

    /* Org Type Details */
    public ArrayList<OrgTypeLookUpDetails> getAllOrgTypeDetails() {
        db = this.getReadableDatabase();
        ArrayList<OrgTypeLookUpDetails>  orgTypeLookUpDetailsList = new ArrayList<OrgTypeLookUpDetails>();
        Cursor cursor = db.rawQuery("SELECT * from " + ORG_TYPE_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                OrgTypeLookUpDetails orgTypeLookUpDetails = new OrgTypeLookUpDetails();
                orgTypeLookUpDetails.setOrgTypeNo(cursor.getInt(cursor.getColumnIndex(ORG_TYPE_NO)));
                orgTypeLookUpDetails.setOrgTypeName(cursor.getString(cursor.getColumnIndex(ORG_TYPE_NAME)));
                orgTypeLookUpDetailsList.add(orgTypeLookUpDetails);
                cursor.moveToNext();
            }
        }
        db.close();
        return orgTypeLookUpDetailsList;
    }
    public void insertIntoOrgTypeDetails(ArrayList<OrgTypeLookUpDetails> orgTypeLookUpDetailsList) {
        db = this.getWritableDatabase();
        db.execSQL("delete from " + ORG_TYPE_TABLE_NAME);
        for(OrgTypeLookUpDetails lookup:orgTypeLookUpDetailsList){
            ContentValues contentValues = new ContentValues();
            Log.d(TAG, "insertIntoOrgTypeDetails: Inserting " + lookup.getOrgTypeNo() + " - " + lookup.getOrgTypeName());
            contentValues.put(ORG_TYPE_NO, lookup.getOrgTypeNo());
            contentValues.put(ORG_TYPE_NAME, lookup.getOrgTypeName());
            db.insert(ORG_TYPE_TABLE_NAME,null,contentValues);
        }
        db.close();
    }

    /* Get one record using primary key */
    public String getMainItemNameFromLookUp(int itemCode) {

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + MAIN_ITEM_NAME + " from " + MAIN_ITEM_TABLE_NAME + " where " + MAIN_ITEM_CODE + " = " + itemCode, null);

        if(itemCode == 1 || itemCode == 2) {
            if (cursor != null)
                cursor.moveToFirst();
                itemName = cursor.getString(cursor.getColumnIndex(MAIN_ITEM_NAME));
        }
        else
              itemName = getSubItemNameFromLookUp(itemCode);
        db.close();
        return itemName;
    }
    public String getSubItemNameFromLookUp(int subItemCode) {

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + SUB_ITEM_NAME + " from " + MAIN_ITEM_TABLE_NAME + " where " + SUB_ITEM_CODE + " = " + subItemCode, null);

        if(cursor!=null)
            cursor.moveToFirst();
        String subItemName = cursor.getString(cursor.getColumnIndex(SUB_ITEM_NAME));
        db.close();
        return subItemName;
    }
    public OrgTypeLookUpDetails getOrgTypeLookUpDetails(int orgTypeNo) {
        OrgTypeLookUpDetails orgTypeLookUpDetails = new OrgTypeLookUpDetails();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + ORG_TYPE_NAME + " from " + ORG_TYPE_TABLE_NAME + " where " + ORG_TYPE_NO + " = " + orgTypeNo, null);

        if(cursor!=null)
            cursor.moveToFirst();
        orgTypeLookUpDetails.setOrgTypeNo(orgTypeNo);
        orgTypeLookUpDetails.setOrgTypeName(cursor.getString(cursor.getColumnIndex(ORG_TYPE_NAME)));
        db.close();
        return orgTypeLookUpDetails;
    }
    public CountryLookUpTableDetails getCountryLookUpTableDetails(int countryCode) {
        CountryLookUpTableDetails countryLookUpTableDetails = new CountryLookUpTableDetails();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + COUNTRY_NAME + " from " + COUNTRY_TABLE_NAME + " where " + COUNTRY_CODE + " = " + countryCode, null);

        if(cursor!=null)
            cursor.moveToFirst();
        countryLookUpTableDetails.setCountry_code(countryCode);
        countryLookUpTableDetails.setCountryName(cursor.getString(cursor.getColumnIndex(COUNTRY_NAME)));
        db.close();
        return countryLookUpTableDetails;
    }

}