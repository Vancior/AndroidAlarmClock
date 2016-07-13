package com.vancior.deskclock.ui;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.vancior.deskclock.util.AudioList;
import com.vancior.deskclock.util.NextAlarm;

import java.util.List;

public class AlarmActivity extends AppCompatActivity{

    private boolean is_close_or_delay;
    private TextView textView;
    private Intent ringIntent;
    private Bundle bundle;
    private Ringtone ringtoneToPlay;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.activity_alarm);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        ringIntent  = getIntent();
        bundle = ringIntent.getExtras();
        String givenRingtone = bundle.getString("ringtone");
        if(givenRingtone.equals("Default"))
            givenRingtone = "Alarm clock 1";
        AudioList audioList = new AudioList(this);
        List<Ringtone> ringtones = audioList.getRingtoneList(RingtoneManager.TYPE_ALARM);
        for(int i = 0; i < ringtones.size(); i++) {
            if(givenRingtone.equals(ringtones.get(i).getTitle(this))) {
                ringtoneToPlay = ringtones.get(i);
                Log.d("Ringtone", ringtoneToPlay.getTitle(this));
                break;
            }
        }

        String textHour = bundle.getInt("hour")+"";
        if(textHour.length() == 1)
            textHour = "0" + textHour;
        String textMinute = bundle.getInt("minute")+"";
        if(textMinute.length() == 1)
            textMinute = "0" + textMinute;

        String textLabel = bundle.getString("tag");

        textView = new TextView(this);
        textView.setTextSize(54);
        textView.setText("  " + textHour + ":" + textMinute);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        int isVibrate = bundle.getInt("isvibrate");
        if(isVibrate == 1)
            vibrator.vibrate(60*1000);
        ringtoneToPlay.play();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(textLabel.equals("None"))
            builder.setTitle("Alarm");
        else
            builder.setTitle(textLabel);
        builder.setView(textView);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                is_close_or_delay = true;
                ringtoneToPlay.stop();
                vibrator.cancel();
                setNextAlarm();
                finish();
            }
        });
        builder.setNegativeButton("Delay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                is_close_or_delay = true;
                ringtoneToPlay.stop();
                vibrator.cancel();
                set5minAlarm();
                finish();
            }
        });
        builder.show();


    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message message) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if(!is_close_or_delay) {
                        ringtoneToPlay.stop();
                        vibrator.cancel();
                        set5minAlarm();
                    }
                }
            }, 60*1000);
        }
    };

    private void setNextAlarm() {
        String repeat = bundle.getString("repeat");
        if(!repeat.equals("Only once")) {
            int hour = bundle.getInt("hour");
            int minute = bundle.getInt("minute");
            long nextTime = NextAlarm.calNextAlarm(repeat, hour, minute);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, bundle.getInt("alarmid"),
                    ringIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);
            String displayNext = NextAlarm.nextAlarmString(repeat, hour, minute);
            Log.d("SetNext", displayNext);
        }
    }

    private void set5minAlarm() {
        long nextTime = System.currentTimeMillis() + 5*60*1000;
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, bundle.getInt("alarmid"),
                ringIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);
    }
}
