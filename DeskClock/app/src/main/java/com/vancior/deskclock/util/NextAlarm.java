package com.vancior.deskclock.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by H on 2016/7/12.
 */
public class NextAlarm {

    static public long calNextAlarm(String repeat, int hour, int minute) {
        long currentTimeMills = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long givenTimeMills = calendar.getTimeInMillis();

        if(repeat.equals("Only once")) {
            if(currentTimeMills < givenTimeMills)
                return givenTimeMills;
            else
                return givenTimeMills + 24*60*60*1000;
        }

        List<Long> nextAlarmTimes = new ArrayList<>();
        long nextAlarmTime;
        if(repeat.equals("Everyday"))
            repeat = "Mon.Tues.Wed.Thur.Fri.Sat.Sun";
        else if(repeat.equals("Weekdays"))
            repeat = "Mon.Tues.Wed.Thur.Fri";
        else if(repeat.equals("Weekends"))
            repeat = "Sat.Sun";
        String[] weekString = repeat.split("\\.");
        for(int i = 0; i < weekString.length; i++) {
            switch (weekString[i]) {
                case "Mon":
                    nextAlarmTime = givenDayAlarm(hour, minute, Calendar.MONDAY);
                    nextAlarmTimes.add(nextAlarmTime);
                    break;
                case "Tues":
                    nextAlarmTime = givenDayAlarm(hour, minute, Calendar.TUESDAY);
                    nextAlarmTimes.add(nextAlarmTime);
                    break;
                case "Wed":
                    nextAlarmTime = givenDayAlarm(hour, minute, Calendar.WEDNESDAY);
                    nextAlarmTimes.add(nextAlarmTime);
                    break;
                case "Thur":
                    nextAlarmTime = givenDayAlarm(hour, minute, Calendar.THURSDAY);
                    nextAlarmTimes.add(nextAlarmTime);
                    break;
                case "Fri":
                    nextAlarmTime = givenDayAlarm(hour, minute, Calendar.FRIDAY);
                    nextAlarmTimes.add(nextAlarmTime);
                    break;
                case "Sat":
                    nextAlarmTime = givenDayAlarm(hour, minute, Calendar.SATURDAY);
                    nextAlarmTimes.add(nextAlarmTime);
                    break;
                case "Sun":
                    nextAlarmTime = givenDayAlarm(hour, minute, Calendar.SUNDAY);
                    nextAlarmTimes.add(nextAlarmTime);
                    break;
                default:
                    break;
            }
        }
        return Collections.min(nextAlarmTimes);
    }

    static public long givenDayAlarm(int hour, int minute, int givenDay) {
        long currentTimeMills = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, givenDay);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long givenTimeMills = calendar.getTimeInMillis();

        if(currentTimeMills < givenTimeMills)
            return givenTimeMills;
        else
            return givenTimeMills + 7*24*60*60*1000;
    }

    static public String nextAlarmString(String repeat, int hour, int minute) {
        long nextAlarmTimeMills = calNextAlarm(repeat, hour, minute);
        int delayTimeMinute = (int) ((nextAlarmTimeMills - System.currentTimeMillis()) / 60000);

        int nextDay = delayTimeMinute / (24*60);
        int nextHour = (delayTimeMinute - nextDay*24*60) / 60;
        int nextMinute = delayTimeMinute - nextDay*24*60 - nextHour*60;

        String result = "Alarm set for ";
        if(nextDay != 0) {
            if(nextDay == 1)
                result += (nextDay + " day ");
            else
                result += (nextDay + " days ");
        }
        if(nextHour != 0) {
            if(nextHour == 1)
                result += (nextHour + " hour ");
            else
                result += (nextHour + " hours ");
        }
        if(nextMinute != 0){
            if(nextMinute == 1)
                result += (nextMinute + " minute ");
            else
                result += (nextMinute + " minutes ");

        }
        result += "from now.";
        return result;
    }
}
