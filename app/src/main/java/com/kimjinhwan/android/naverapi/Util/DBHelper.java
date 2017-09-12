package com.kimjinhwan.android.naverapi.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by XPS on 2017-09-11.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "favoriteList.db";
    public static int DATABASE_VERSION = 1;
    public static String TABLE_NAME = "favoriteTable";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = " CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             "PRODUCTNAME TEXT, " + "IMAGEURL TEXT, " + "LOWPRICE TEXT, " + "MALLNAME TEXT, "
                             + "LINK TEXT);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
