package com.vancior.wakeup;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_alarm = (Button) findViewById(R.id.button_alarm);
        //Button button_exit = (Button) findViewById(R.id.button_exit);
        //AssetManager assetManager = this.getAssets();

        /*button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        button_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.vancior.wakeup.ACTION_START");
                intent.addCategory("com.vancior.wakeup.ALARM");
                startActivity(intent);
            }
        });
    }
}
