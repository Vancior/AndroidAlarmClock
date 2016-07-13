package com.vancior.deskclock.ui;

import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.vancior.deskclock.R;
import com.vancior.deskclock.bean.AudioInfo;
import com.vancior.deskclock.bean.AudioList;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alarm);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Time is on!");
        builder.setPositiveButton("Okay", null);
        builder.show();*/

        /*List<AudioInfo> audioInfos = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, new String[] { "_data",
                "title", "duration" }, "is_alarm = 1", null, null);
        while(cursor.moveToNext()) {
            String audioPath = cursor.getString(0);
            String ringName = cursor.getString(1);
            int duration = cursor.getInt(2);
            AudioInfo audioInfo = new AudioInfo(ringName, Uri.parse(audioPath),
                    Ring)
                    }*/
        List<Ringtone> ringtones = new ArrayList<>();
        AudioList audioList = new AudioList(this);
        ringtones = audioList.getRingtoneList(RingtoneManager.TYPE_ALARM);

    }

}
