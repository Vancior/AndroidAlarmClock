package com.vancior.deskclock.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.vancior.deskclock.ui.AlarmActivity;

/**
 * Created by H on 2016/7/12.
 */
public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("AlarmReceiverLog", "Broadcast has received");
        if(action.equals("com.vancior.deskclock.ACTION_ALARM")) {
            Bundle bundle = intent.getExtras();

            Log.d("AlarmReceiverLog", "Broadcast has received");
            Intent ringIntent = new Intent(context, AlarmActivity.class);
            ringIntent.putExtras(bundle);
            ringIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ringIntent);
        }
    }

}
