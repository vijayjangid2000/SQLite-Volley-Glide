package com.vijayjangid.cubereum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperClass extends SQLiteOpenHelper {

    Context context;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "userDetails_Database";
    public static final String TABLE_NAME = "Details";

    public static final String KEY_ID = "id";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public DatabaseHelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_FIRST_NAME + " TEXT NOT NULL, " +
                KEY_LAST_NAME + " TEXT NOT NULL, " +
                KEY_PHONE_NUMBER + " TEXT NOT NULL UNIQUE, " +
                KEY_ADDRESS + " TEXT NOT NULL, " +
                KEY_EMAIL + " TEXT NOT NULL UNIQUE, " +
                KEY_PASSWORD + " TEXT NOT NULL" +
                ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    boolean addNewRecord(String firstName, String lastName, String phoneNumber,
                         String address, String email, String password) {

        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, firstName);
        values.put(KEY_LAST_NAME, lastName);
        values.put(KEY_PHONE_NUMBER, phoneNumber);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);

        SQLiteDatabase db = getWritableDatabase();
        try {
            db.insert(TABLE_NAME, null, values);
        } catch (Exception ignored) {
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    boolean checkEmailExists(String email) {
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                    " WHERE " + " email " + " = '" + email + "'", null);
            if (cursor.getCount() > 0) return true;
            cursor.close();
        } catch (Exception ignored) {
        }
        db.close();
        return false;
    }

    boolean checkPhoneExists(String phoneNumber) {
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                    " WHERE " + " phoneNumber = " + "'" + phoneNumber + "'", null);
            if (cursor.getCount() > 0) return true;
            cursor.close();
        } catch (Exception ignored) {
        }
        db.close();
        return false;
    }

    public boolean checkPassword(String email, String password) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + "password" + " FROM " + TABLE_NAME
                + " WHERE " + " email " + " = '" + email + "'", null);
        cursor.moveToFirst();
        if (cursor.getString(0).equals(password)) return true;
        cursor.close();
        db.close();
        return false;
    }

    String[] getUserDetails(String emailID) {

        String[] userData = new String[6];

        SQLiteDatabase db = getReadableDatabase();
        String statement =
                " SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_EMAIL + " = '" +
                        emailID + "'";

        Cursor cursor = db.rawQuery(statement, null);
        cursor.moveToFirst();
        userData[0] = cursor.getString(1);
        userData[1] = cursor.getString(2);
        userData[2] = cursor.getString(3);
        userData[3] = cursor.getString(4);
        userData[4] = cursor.getString(5);
        userData[5] = cursor.getString(6);
        cursor.close();
        db.close();

        return userData;
    }

}
