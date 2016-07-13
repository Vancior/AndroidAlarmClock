package com.vancior.deskclock.bean;

/**
 * Created by H on 2016/7/12.
 */
public class EachAlarm {

    private int id, hour, minute, isvibrate, state;
    private String repeat, ringtone, tag;

    public EachAlarm(int id, int hour, int minute, int isvibrate, int state,
                     String repeat, String ringtone, String tag) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.isvibrate = isvibrate;
        this. state = state;
        this.repeat = repeat;
        this.ringtone = ringtone;
        this.tag = tag;
    }

    public int getAlramId() { return id; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public int getIsvibrate() { return isvibrate; }
    public int getState() { return state; }
    public String getRepeat() { return repeat; }
    public String getRingtone() { return ringtone; }
    public String getTag() { return tag; }

}
