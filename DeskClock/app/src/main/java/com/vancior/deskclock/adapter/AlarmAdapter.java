package com.vancior.deskclock.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vancior.deskclock.R;
import com.vancior.deskclock.bean.EachAlarm;
import com.vancior.deskclock.util.AlarmDBManager;
import com.vancior.deskclock.util.NextAlarm;
import com.vancior.deskclock.service.AlarmReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H on 2016/7/12.
 */
public class AlarmAdapter extends BaseAdapter{

    private Context context;
    private List<EachAlarm> alarminfos;
    private List<Integer> statelist;
    private View view;
    private AlarmDBManager alarmDBManager;
    //private AlarmDBManager alarmDBManager;

    public void updateAdapter() {
        alarminfos.clear();
        List<EachAlarm> tempinfos = alarmDBManager.getAllAlarm();
        alarminfos.addAll(tempinfos);
        statelist.clear();
        statelist = new ArrayList<Integer>(alarminfos.size());
        for(EachAlarm alarmcycle : alarminfos) {
            statelist.add(alarmcycle.getState());
        }
    }

    public AlarmAdapter(Context context, List<EachAlarm> alarminfos) {
        this.context = context;
        this.alarminfos = alarminfos;
        alarmDBManager = new AlarmDBManager(context);
        statelist = new ArrayList<Integer>(alarminfos.size());
        for(EachAlarm alarmcycle : alarminfos) {
            statelist.add(alarmcycle.getState());
            System.out.println("issss"+alarmcycle.getState());
        }
    }

    @Override
    public int getCount() {
        return alarminfos.size();
    }

    @Override
    public Object getItem(int position) {
        return alarminfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final EachAlarm eachAlarm = alarminfos.get(position);
        String hour = eachAlarm.getHour() + "";
        String minute = eachAlarm.getMinute() + "";
        final String repeat = eachAlarm.getRepeat() + "";
        String tag = eachAlarm.getTag() + "";

        ViewHolder viewHolder;
        if(convertView == null) {
            view = View.inflate(context, R.layout.alarm_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.displaytime = (TextView) view.findViewById(R.id.display_time);
            viewHolder.displayrepeat = (TextView) view.findViewById(R.id.display_repeat);
            viewHolder.displaytag = (TextView) view.findViewById(R.id.display_tag);
            viewHolder.switchisopen = (SwitchCompat) view.findViewById(R.id.switch_isopen);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if(hour.length() == 1) {
            hour = "0" + hour;
        }
        if(minute.length() == 1) {
            minute = "0" + minute;
        }

        viewHolder.displaytime.setText(hour + ":" + minute);
        viewHolder.displaytag.setText(tag);
        viewHolder.displayrepeat.setText(repeat);
        /*
        if(TextUtils.isEmpty(tag)) {
            viewHolder.display_tag.setVisibility(View.GONE);
        }
        else {
            viewHolder.display_tag.setVisibility(View.VISIBLE);
        }
        */

        viewHolder.switchisopen.setOnCheckedChangeListener(null);
        //System.out.println("ALARM" + position+" is " + statelist.get(position));
        viewHolder.switchisopen.setChecked(statelist.get(position) != 0);
        viewHolder.switchisopen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                alarmIntent.setAction("com.vancior.deskclok.ACTION_ALARM");

                Bundle bundle = new Bundle();
                bundle.putInt("alarmid", eachAlarm.getAlramId());
                bundle.putInt("hour", eachAlarm.getHour());
                bundle.putInt("minute", eachAlarm.getMinute());
                bundle.putInt("isvibrate", eachAlarm.getIsvibrate());
                bundle.putString("repeat", eachAlarm.getRepeat());
                bundle.putString("ringtone", eachAlarm.getRingtone());
                bundle.putString("tag", eachAlarm.getTag());
                alarmIntent.putExtras(bundle);

                if(b) {
                    if(statelist.get(position) == 0)
                        statelist.set(position, 1);
                    alarmDBManager.updateState(eachAlarm.getAlramId(), 1);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, eachAlarm.getAlramId(),
                            alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    long nextTime = NextAlarm.calNextAlarm(eachAlarm.getRepeat(), eachAlarm.getHour(), eachAlarm.getMinute());
                    alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);
                }
                else {
                    if(statelist.get(position) == 1)
                        statelist.set(position, 0);
                    alarmDBManager.updateState(eachAlarm.getAlramId(), 0);
                    alarmManager.cancel(PendingIntent.getBroadcast(context, eachAlarm.getAlramId(),
                            alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                }
            }
        });
        viewHolder.switchisopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(statelist.get(position) == 1) {
                    String displayNext = NextAlarm.nextAlarmString(eachAlarm.getRepeat(), eachAlarm.getHour(), eachAlarm.getMinute());
                    Snackbar.make(view, displayNext, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                else {
                    Snackbar.make(view, "Alarm is off.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        viewHolder.displaytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.vancior.deskclock.ACTION_SETTING");
                intent.addCategory("com.vancior.deskclock.SETTING_CATEGORY");
                intent.putExtra("isadd", 0);
                intent.putExtra("alarmid", eachAlarm.getAlramId());
                System.out.println(eachAlarm.getAlramId());
                context.startActivity(intent);
            }
        });

        return view;
    }

    private class ViewHolder {
        public TextView displaytime;
        public TextView displayrepeat;
        public TextView displaytag;
        public SwitchCompat switchisopen;
    }
}
