package com.vancior.deskclock.util;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H on 2016/7/13.
 */
public class AudioList {

    private Context context;

    public AudioList(Context context) {
        this.context = context;
    }

    public List<Ringtone> getRingtoneList(int type) {
        List<Ringtone> ringtones = new ArrayList<>();
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(type);
        Cursor cursor = ringtoneManager.getCursor();
        int count = cursor.getCount();
        for(int i = 0; i < count; i++)
            ringtones.add(ringtoneManager.getRingtone(i));
        return ringtones;
    }
}
