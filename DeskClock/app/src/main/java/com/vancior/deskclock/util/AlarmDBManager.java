package com.vancior.deskclock.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vancior.deskclock.bean.EachAlarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H on 2016/7/12.
 */
public class AlarmDBManager {

    private Context context;
    private AlarmDBHelper databaseHelper;
    public static final String ALARM_TABLE_NAME = "alarmtable";

    public AlarmDBManager(Context context) {
        this.context = context;
        databaseHelper = new AlarmDBHelper(context);
    }

    public int insert(int hour, int minute, String repeat, String ringtone,
                      int isvibrate, String tag, int state) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int id = 0;
        if(database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("hour", hour);
            values.put("minute", minute);
            values.put("repeat", repeat);
            values.put("ringtone", ringtone);
            values.put("isvibrate", isvibrate);
            values.put("tag", tag);
            values.put("state", state);
            database.insert(ALARM_TABLE_NAME, null, values);
            Cursor cursor = database.query(ALARM_TABLE_NAME, null, null, null, null, null, null);
            while(cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("_id"));
            }
            cursor.close();
            database.close();
        }
        System.out.println("ID is " +id);
        return id;
    }

    public List<EachAlarm> getAllAlarm() {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        List<EachAlarm> alarminfos = new ArrayList<EachAlarm>();
        if(database.isOpen()) {
            Cursor cursor = database.query(ALARM_TABLE_NAME, null, null, null, null, null, null);
            while(cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int hour = cursor.getInt(cursor.getColumnIndex("hour"));
                int minute = cursor.getInt(cursor.getColumnIndex("minute"));
                int isvibrate = cursor.getInt(cursor.getColumnIndex("isvibrate"));
                int state = cursor.getInt(cursor.getColumnIndex("state"));
                String repeat = cursor.getString(cursor.getColumnIndex("repeat"));
                String ringtone = cursor.getString(cursor.getColumnIndex("ringtone"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                EachAlarm eachAlarm = new EachAlarm(id, hour, minute, isvibrate, state, repeat, ringtone, tag);
                alarminfos.add(eachAlarm);
            }
            cursor.close();
            database.close();
        }
        return alarminfos;
    }

    public void update(int id, int hour, int minute, int isvibrate, int state, String repeat, String ringtone, String tag) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if(database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("hour", hour);
            values.put("minute", minute);
            values.put("isvibrate", isvibrate);
            values.put("state", state);
            values.put("repeat", repeat);
            values.put("ringtone", ringtone);
            values.put("tag", tag);
            database.update(ALARM_TABLE_NAME, values, "_id ="+ id, null);
            database.close();
        }
    }

    public void updateState(int id, int state) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if(database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("state", state);
            database.update(ALARM_TABLE_NAME, values, "_id ="+ id, null);
            database.close();
        }
    }

    public void delete(int id) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if(database.isOpen()) {
            database.delete(ALARM_TABLE_NAME, "_id ="+ id, null);
            database.close();
        }
    }

    public EachAlarm findOne(int id) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(id)};
        Cursor cursor = database.query(ALARM_TABLE_NAME, null, "_id = ?", args, null, null, null);
        cursor.moveToFirst();

        int _id = cursor.getInt(cursor.getColumnIndex("_id"));
        int hour = cursor.getInt(cursor.getColumnIndex("hour"));
        int minute = cursor.getInt(cursor.getColumnIndex("minute"));
        int isvibrate = cursor.getInt(cursor.getColumnIndex("isvibrate"));
        int state = cursor.getInt(cursor.getColumnIndex("state"));
        String repeat = cursor.getString(cursor.getColumnIndex("repeat"));
        String ringtone = cursor.getString(cursor.getColumnIndex("ringtone"));
        String tag = cursor.getString(cursor.getColumnIndex("tag"));
        EachAlarm eachAlarm = new EachAlarm(_id, hour, minute, isvibrate, state, repeat, ringtone, tag);
        cursor.close();
        database.close();
        return eachAlarm;
    }

    public void reset() {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.execSQL("delete from " + ALARM_TABLE_NAME);
        database.execSQL("update sqlite_sequence SET seq = 0 where name = ?", new String[] { ALARM_TABLE_NAME });
        database.close();
    }
}
