package com.vancior.deskclock.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by H on 2016/7/12.
 */
public class AlarmDBHelper extends SQLiteOpenHelper{

    static final String DATABASE_NAME = "MyDataBase";
    static final int DATABASE_VERSION = 1;

    public AlarmDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        String sql = "create table alarmtable (_id integer primary key autoincrement, hour integer, minute integer, "
                + "isvibrate integer, state integer, repeat varchar(20), ringtone varchar(20), tag varchar(20))";
        database.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        //Log.wtf(TAG, "Upgrading database from version "+ oldVersion + "to "+
        //newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(database);
    }

}
