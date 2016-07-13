package com.vancior.deskclock.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.vancior.deskclock.adapter.AlarmAdapter;
import com.vancior.deskclock.R;
import com.vancior.deskclock.bean.EachAlarm;
import com.vancior.deskclock.service.AlarmReceiver;
import com.vancior.deskclock.util.AlarmDBManager;

import java.util.List;

public class AlarmListActivity extends AppCompatActivity {

    private List<EachAlarm> alarmList;
    private AlarmAdapter alarmAdapter;
    private AlarmDBManager alarmDBManager;
    private ListView listView;
    private int alarmToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.alarm_list);
        alarmDBManager = new AlarmDBManager(this);
        alarmList = alarmDBManager.getAllAlarm();
        alarmAdapter = new AlarmAdapter(AlarmListActivity.this, alarmList);
        listView.setAdapter(alarmAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.vancior.deskclock.ACTION_SETTING");
                intent.addCategory("com.vancior.deskclock.SETTING_CATEGORY");
                intent.putExtra("isadd", 1);
                intent.putExtra("alarmid", 0);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                alarmToDelete = i;
                registerForContextMenu(view);
                view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                                    ContextMenu.ContextMenuInfo contextMenuInfo) {
                        contextMenu.add(0, 0, 0, "Delete");
                    }
                });
                return false;
            }
        });



    }

    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case 0:
                Snackbar.make(AlarmListActivity.this.getCurrentFocus(), "Alarm has been deleted.", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                EachAlarm tempalarm = alarmList.get(alarmToDelete);
                alarmDBManager.delete(tempalarm.getAlramId());
                AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);

                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                alarmIntent.setAction("com.vancior.deskclok.ACTION_ALARM");

                Bundle bundle = new Bundle();
                bundle.putInt("alarmid", tempalarm.getAlramId());
                bundle.putInt("hour", tempalarm.getHour());
                bundle.putInt("minute", tempalarm.getMinute());
                bundle.putInt("isvibrate", tempalarm.getIsvibrate());
                bundle.putString("repeat", tempalarm.getRepeat());
                bundle.putString("ringtone", tempalarm.getRingtone());
                bundle.putString("tag", tempalarm.getTag());
                alarmIntent.putExtras(bundle);

                alarmManager.cancel(PendingIntent.getBroadcast(this, tempalarm.getAlramId(),
                        alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                updateListAndAdapter();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResumeActivity", "onResume!");
        updateListAndAdapter();
    }

    private void updateListAndAdapter() {
        alarmList = null;
        alarmList = alarmDBManager.getAllAlarm();
        alarmAdapter.updateAdapter();
        alarmAdapter.notifyDataSetChanged();
    }

}
