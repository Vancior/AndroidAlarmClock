package com.vancior.deskclock.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.vancior.deskclock.R;
import com.vancior.deskclock.bean.AudioList;
import com.vancior.deskclock.bean.EachAlarm;
import com.vancior.deskclock.service.AlarmReceiver;
import com.vancior.deskclock.util.AlarmDBManager;
import com.vancior.deskclock.util.NextAlarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmSettingActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;
    private int isAdd;
    private int isVibtate;
    private int isRepeat;
    private int hour;
    private int minute;
    private int alarmId;
    private String tag = "None";
    private String repeat;
    private String ringtone = "Default";
    private boolean ifRepeatChange = false;
    private boolean repeatDays[] = new boolean[7];

    private CheckBox checkBox_repeat;
    private CheckBox checkBox_vibrate;
    private TextView textView_repeat;
    private TextView textView_vibrate;
    private TextView textView_label;
    private TextView textView_tag;
    private TextView textView_ringtonetitle;
    private TextView textView_ringtone;

    private EachAlarm currentAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        intent = getIntent();
        isAdd = intent.getIntExtra("isadd", 1);
        alarmId = intent.getIntExtra("alarmid", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);

        textView_label = (TextView) findViewById(R.id.text_label);
        textView_tag = (TextView) findViewById(R.id.text_tag);
        textView_label.setOnClickListener(this);
        textView_tag.setOnClickListener(this);

        checkBox_vibrate = (CheckBox) findViewById(R.id.vibrate_or_not);
        checkBox_vibrate.setOnClickListener(this);
        checkBox_repeat = (CheckBox) findViewById(R.id.repeat_or_not);
        checkBox_repeat.setOnClickListener(this);
        textView_repeat = (TextView) findViewById(R.id.text_repeat);
        textView_repeat.setOnClickListener(this);
        textView_vibrate = (TextView) findViewById(R.id.text_vibrate);
        textView_vibrate.setOnClickListener(this);
        textView_ringtonetitle = (TextView) findViewById(R.id.text_ringtonetitle);
        textView_ringtonetitle.setOnClickListener(this);
        textView_ringtone = (TextView) findViewById(R.id.text_ringtone);
        textView_ringtone.setOnClickListener(this);

        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        if(isAdd == 0) {
            AlarmDBManager alarmDBManager = new AlarmDBManager(this);
            System.out.println("sdfhsdkjf  " + alarmId);
            currentAlarm = alarmDBManager.findOne(alarmId);
            hour = currentAlarm.getHour();
            minute = currentAlarm.getMinute();
            repeat = currentAlarm.getRepeat();
            tag = currentAlarm.getTag();
            isVibtate = currentAlarm.getIsvibrate();
            ringtone = currentAlarm.getRingtone();
            textView_tag.setText(tag);
            textView_ringtone.setText(ringtone);
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
            if(repeat.equals("Only once"))
                checkBox_repeat.setChecked(false);
            else
                checkBox_repeat.setChecked(true);
            checkBox_vibrate.setChecked(isVibtate == 1);
        }

        checkBox_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    isVibtate = 1;
                else
                    isVibtate = 0;
            }
        });

        hour = timePicker.getHour();
        minute = timePicker.getMinute();
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute = i1;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            AlarmDBManager alarmDBManager = new AlarmDBManager(this);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            alarmIntent.setAction("com.vancior.deskclock.ACTION_ALARM");

            repeat = getRepeatString();

            Bundle bundle = new Bundle();
            bundle.putInt("hour", hour);
            bundle.putInt("minute", minute);
            bundle.putInt("isvibrate", isVibtate);
            bundle.putString("repeat", getRepeatString());
            bundle.putString("ringtone", ringtone);
            bundle.putString("tag", tag);
            bundle.putInt("alarmid", alarmId);
            alarmIntent.putExtras(bundle);

            long nextTime = NextAlarm.calNextAlarm(getRepeatString(), hour, minute);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId,
                    alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            switch (isAdd) {
                case 1:
                    alarmId = alarmDBManager.insert(hour, minute, repeat, "erw", isVibtate, tag, 1);


                    alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);
                    finish();
                    break;
                case 0:
                    alarmDBManager.update(alarmId, hour, minute, isVibtate, 1, repeat, "weiru", tag);

                    alarmManager.cancel(PendingIntent.getBroadcast(this, alarmId,
                            alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                    alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);
                    finish();
                    break;
                default:
                    break;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if((view == checkBox_repeat && checkBox_repeat.isChecked())|| view == textView_repeat && !checkBox_repeat.isChecked()) {
            AlertDialog.Builder builder_repeat = new AlertDialog.Builder(this);
            builder_repeat.setTitle("Repeat Days");
            builder_repeat.setMultiChoiceItems(new String[]{"Monday", "Tuesday", "Wednesday",
                    "Thursday", "Firday", "Saturday", "Sunday"}, repeatDays, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                    if (isChecked) {
                        repeatDays[which] = true;
                        System.out.println("Item" + which + "is checked!");
                    } else
                        repeatDays[which] = false;
                }
            });
            builder_repeat.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for(int j = 0; j < 7; j++) {
                        if (repeatDays[j]) {
                            checkBox_repeat.setChecked(true);
                            return;
                        }
                    }
                    checkBox_repeat.setChecked(false);
                }
            });
            builder_repeat.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for (int j = 0; j < 7; j++)
                        repeatDays[j] = false;
                    checkBox_repeat.setChecked(false);
                }
            });
            builder_repeat.show();
        }
        else if((view == checkBox_repeat && !checkBox_repeat.isChecked())|| view == textView_repeat && checkBox_repeat.isChecked()){
            for (int j = 0; j < 7; j++)
                repeatDays[j] = false;
            checkBox_repeat.setChecked(false);
        }
        else if(view == textView_vibrate) {
            if(isVibtate == 1) {
                isVibtate = 0;
                checkBox_vibrate.setChecked(false);
            }
            else{
                isVibtate = 1;
                checkBox_vibrate.setChecked(true);
            }
        }
        else if(view == textView_label || view == textView_tag) {
            AlertDialog.Builder builder_tag = new AlertDialog.Builder(this);
            builder_tag.setTitle("Label");
            final EditText editText = new EditText(this);
            builder_tag.setView(editText);
            builder_tag.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    tag = editText.getText().toString();
                    textView_tag.setText(tag);
                }
            });
            builder_tag.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    tag = "None";
                    textView_tag.setText(tag);
                }
            });
            builder_tag.show();
        }
        else if(view == textView_ringtonetitle || view == textView_ringtone) {
            AudioList audioList = new AudioList(this);
            List<Ringtone> ringtones = new ArrayList<>();
            ringtones = audioList.getRingtoneList(RingtoneManager.TYPE_NOTIFICATION);
            AlertDialog.Builder builder_ringtone = new AlertDialog.Builder(this);
            builder_ringtone.setTitle("Ringtone");
            final String tempringtones[] = new String[ringtones.size()];
            for(int i = 0; i < ringtones.size(); i++) {
                tempringtones[i] = ringtones.get(i).getTitle(this);
            }
            builder_ringtone.setSingleChoiceItems(tempringtones, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ringtone = tempringtones[i];
                    textView_ringtone.setText(ringtone);
                }
            });
            builder_ringtone.setPositiveButton("Done", null);
            builder_ringtone.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ringtone = "Default";
                    textView_ringtone.setText(ringtone);
                }
            });
            builder_ringtone.show();
        }
    }

    private String getRepeatString() {
        String result = "";
        boolean flag = false;
        for(int i = 0; i < 7; i++) {
            if(repeatDays[i]) {
                if(flag)
                    result += ".";
                switch (i) {
                    case 0:
                        result += "Mon";
                        break;
                    case 1:
                        result += "Tues";
                        break;
                    case 2:
                        result += "Wed";
                        break;
                    case 3:
                        result += "Thur";
                        break;
                    case 4:
                        result += "Fri";
                        break;
                    case 5:
                        result += "Sat";
                        break;
                    case 6:
                        result += "Sun";
                        break;
                }
                flag = true;
            }
        }
        if(!flag)
            result = "Only once";
        if(result.equals("Mon.Tues.Wed.Thur.Fri.Sat.Sun"))
            result = "Everyday";
        else if(result.equals("Mon.Tues.Wed.Thur.Fri"))
            result = "Weekdays";
        else if(result.equals("Sat.Sun"))
            result = "Weekends";
        return result;
    }

}
