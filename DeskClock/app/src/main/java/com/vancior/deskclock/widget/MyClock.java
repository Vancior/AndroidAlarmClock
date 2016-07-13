package com.vancior.deskclock.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.vancior.deskclock.R;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by H on 2016/7/12.
 */
public class MyClock extends View {

    Bitmap bmpDial, bmpHour, bmpMinute, bmpSecond;
    BitmapDrawable bmdHour, bmdMinute, bmdSecond, bmdDial;
    Paint mPaint;
    Handler tickHandler;

    int mWidth, mHeight;
    int mTempWidth, mTempHeight;
    int centerX, centerY;
    int availableWidth = 1080, availableHeight = 1080;

    private String timeZoneString;

    public MyClock(Context context, AttributeSet attr) {
        this(context, "GMT+8:00");
    }

    public MyClock(Context context, String sTimeZone) {
        super(context);
        timeZoneString = sTimeZone;

        Resources resources = getResources();
        bmpHour = BitmapFactory.decodeResource(getResources(), R.drawable.hour);
        bmdHour = new BitmapDrawable(resources, bmpHour);
        bmpMinute = BitmapFactory.decodeResource(getResources(), R.drawable.minute);
        bmdMinute = new BitmapDrawable(resources, bmpMinute);
        bmpSecond = BitmapFactory.decodeResource(getResources(), R.drawable.second);
        bmdSecond = new BitmapDrawable(resources, bmpSecond);
        bmpDial = BitmapFactory.decodeResource(getResources(), R.drawable.dial);
        bmdDial = new BitmapDrawable(resources, bmpDial);

        mWidth = bmpDial.getWidth();
        mHeight = bmpDial.getHeight();
        centerX = availableWidth / 2;
        centerY = availableHeight / 2;

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        run();
    }

    public void run() {
        tickHandler = new Handler();
        tickHandler.post(tickRunnable);
    }

    private Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
            tickHandler.postDelayed(tickRunnable, 1000);
        }
    };

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneString));
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        float hourRotate = hour * 30.0f + minute / 60.0f * 30.0f;
        float minuteRotate = minute * 6.0f;
        float secondRotate = second * 6.0f;

        float scale = ((float) availableWidth / (float) mWidth) * 0.8f;

        bmdDial.setBounds(centerX - (int) (mWidth * scale / 2), centerY - (int) (mHeight * scale / 2),
                centerX + (int) (mWidth * scale / 2), centerY + (int) (mHeight * scale / 2));
        bmdDial.draw(canvas);

        mTempWidth = (int) (bmdHour.getIntrinsicWidth() * scale);
        mTempHeight = (int) (bmdHour.getIntrinsicHeight() * scale);
        canvas.save();
        canvas.rotate(hourRotate, centerX, centerY);
        bmdHour.setBounds(centerX - (mTempWidth / 2), centerY - (mTempHeight / 2) - 57,
                centerX + (mTempWidth / 2), centerY + (mTempHeight / 2) - 57);
        bmdHour.draw(canvas);

        canvas.restore();

        mTempWidth = (int) (bmdMinute.getIntrinsicWidth() * scale);
        mTempHeight = (int) (bmdMinute.getIntrinsicHeight() * scale);
        canvas.save();
        canvas.rotate(minuteRotate, centerX, centerY);
        bmdMinute.setBounds(centerX - (mTempWidth / 2), centerY - (mTempHeight / 2) - 138,
                centerX + (mTempWidth / 2), centerY + (mTempHeight / 2) - 138);
        bmdMinute.draw(canvas);

        canvas.restore();

        mTempWidth = (int) (bmdSecond.getIntrinsicWidth() * scale);
        mTempHeight = (int) (bmdSecond.getIntrinsicHeight() * scale);
        canvas.rotate(secondRotate, centerX, centerY);
        bmdSecond.setBounds(centerX - (mTempWidth / 2), centerY - (mTempHeight / 2) - 92,
                centerX + (mTempWidth / 2), centerY + (mTempHeight / 2) - 92);
        bmdSecond.draw(canvas);

    }
}
