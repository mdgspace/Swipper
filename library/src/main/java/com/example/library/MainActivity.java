package com.example.library;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.VideoView;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class MainActivity extends Activity {
    private int mActivePointerId = INVALID_POINTER_ID;
    EditText et;
    AudioManager audio;
    int maxVolume;
    int currentVolume;
    double volper;
    double per;
    float brightness;
    float d;
    CustomView cv;
    CircularSeekBar csk;
    int numberOfTaps = 0;
    long lastTapTimeMs = 0;
    long touchDownMs = 0;
    VideoView video;

   public void set(Context context)
   {
       cv = new CustomView(context);
       brightness = android.provider.Settings.System.getFloat(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
       WindowManager.LayoutParams layout = getWindow().getAttributes();
       layout.screenBrightness = brightness / 255;
       getWindow().setAttributes(layout);
       cv.setProgress((int) ((brightness / 255) * 100));
       cv.setProgressText(Integer.valueOf((int) ((brightness / 255) * 100)).toString() + "%");
       csk = new CircularSeekBar(context);
       audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
       currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
       maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
   }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                touchDownMs = System.currentTimeMillis();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                final float X= ev.getHistoricalX(0, 0);
                final float Y=ev.getHistoricalY(0, 0);
                try {
                    if (x == ev.getHistoricalX(0, 0))
                    {
                        d=(getDistance(x, y, ev) / 270);
                        cv.setTitle("Brightness");
                        if (y < ev.getHistoricalY(0, 0)) {
                            WindowManager.LayoutParams layout = getWindow().getAttributes();
                            if (getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 270) <=1 && getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 270) >=0)
                            {
                                cv.show();
                                cv.setProgress((int) ((getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 270)) * 100));
                                layout.screenBrightness = getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 270);
                                getWindow().setAttributes(layout);
                                cv.setProgressText(Integer.valueOf((int) ((getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 270)) * 100)).toString() + "%");

                            }else
                            {
                                cv.show();
                                cv.setProgress(100);
                                layout.screenBrightness = 1;
                                getWindow().setAttributes(layout);
                                cv.setProgressText("100" + "%");

                            }
                        }else {
                            Log.e("p", "in else");
                            WindowManager.LayoutParams layout = getWindow().getAttributes();
                            if (getWindow().getAttributes().screenBrightness-(float)(getDistance(x, y, ev) / 270)>=0 && getWindow().getAttributes().screenBrightness-(float)(getDistance(x, y, ev) / 270)<=1) {
                                cv.show();
                                cv.setProgress((int) ((getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 270)) * 100));
                                layout.screenBrightness = getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 270);
                                getWindow().setAttributes(layout);
                                cv.setProgressText(Integer.valueOf((int) ((getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 270)) * 100)).toString() + "%");
                            }else
                            {
                                cv.show();
                                cv.setProgress(0);
                                layout.screenBrightness = 0;
                                getWindow().setAttributes(layout);
                                cv.setProgressText("0" + "%");
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {

                }
                try {
                    if (y == ev.getHistoricalY(0, 0)) {
                        cv.setTitle("  Volume     ");
                        if (x > ev.getHistoricalX(0, 0)) {
                            Log.e("in if","in if");
                            currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                            Log.e("current volume",currentVolume+"");
                            per =  (double)currentVolume /(double) maxVolume;
                            try {
                                if (per + ((double)getDistance(x, y, ev) /(double)160) < 1) {
                                    Log.e("((double)getDistance",((double)getDistance(x, y, ev) /(double)160)+"");
                                    cv.show();
                                    cv.setProgress((int)((per + ((double)getDistance(x, y, ev) /(double)160)) * 100));
                                    cv.setProgressText((int) ((per + ((double)getDistance(x, y, ev) /(double)160)) * 100)+"%");
                                    volper = (per + ((double)getDistance(x, y, ev) /(double)160));
                                    Log.e("volper",volper+"");
                                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volper * 15), 0);
                                    Log.e("pulkit",(int) (volper * maxVolume)+" ");
                                } else {
                                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume), 0);
                                    cv.setProgressText("100"+"%");
                                }
                            } catch (SecurityException e) {
                            }
                        } else {
                            Log.e("in else","inelse");
                            currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                            Log.e("current volume",currentVolume+"");
                            per = (double) currentVolume / maxVolume;
                            if (per - (float) (getDistance(x, y, ev) / 160) > 0) {
                                cv.show();
                                cv.setProgress((int) ((per -((double)getDistance(x, y, ev)/(double)160)) * 100));
                                cv.setProgressText((int) ((per - ((double)getDistance(x, y, ev) /(double)160)) * 100)+"%");
                                volper = (per - ((double)getDistance(x, y, ev) / (double)160));
                                audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volper * maxVolume), 0);
                            } else {
                                audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                                cv.setProgressText("0"+"%");
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {

                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cv.hide();
                    }
                }, 2000);

                if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                    numberOfTaps = 0;
                    lastTapTimeMs = 0;
                    break;
                }

                if (numberOfTaps > 0
                        && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                    numberOfTaps += 1;
                } else {
                    numberOfTaps = 1;
                }

                lastTapTimeMs = System.currentTimeMillis();
                if (numberOfTaps == 2) {
                    Log.e("in tap","in tap");
                    if(csk.isVisibile())
                    csk.hide();
                    else
                     csk.show();
                }


                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
            {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    public void setBrightness(float percentBrightness)
    {
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = percentBrightness;
        getWindow().setAttributes(layout);
    }

    public float getBrightnessPercent()
    {
        brightness = android.provider.Settings.System.getFloat(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
        return getWindow().getAttributes().screenBrightness;
    }

    public void BrightnessY(float y ,float Y,float d)
    {
        cv.setTitle("Brightness");
        if (y < Y) {
            WindowManager.LayoutParams layout = getWindow().getAttributes();
            if (getWindow().getAttributes().screenBrightness + d <=1 && getWindow().getAttributes().screenBrightness + d >=0)
            {
                cv.show();
                cv.setProgress((int) ((getWindow().getAttributes().screenBrightness + d) * 100));
                layout.screenBrightness = getWindow().getAttributes().screenBrightness + d;
                getWindow().setAttributes(layout);
                cv.setProgressText(Integer.valueOf((int) ((getWindow().getAttributes().screenBrightness + d) * 100)).toString() + "%");

            }else
            {
                cv.show();
                cv.setProgress(100);
                layout.screenBrightness = 1;
                getWindow().setAttributes(layout);
                cv.setProgressText("100" + "%");

            }
        }else {
            Log.e("p", "in else");
            WindowManager.LayoutParams layout = getWindow().getAttributes();
            if (getWindow().getAttributes().screenBrightness-d>=0 && getWindow().getAttributes().screenBrightness-d<=1) {
                cv.show();
                cv.setProgress((int) ((getWindow().getAttributes().screenBrightness - d) * 100));
                layout.screenBrightness = getWindow().getAttributes().screenBrightness - d;
                getWindow().setAttributes(layout);
                cv.setProgressText(Integer.valueOf((int) ((getWindow().getAttributes().screenBrightness -d) * 100)).toString() + "%");
            }else
            {
                cv.show();
                cv.setProgress(0);
                layout.screenBrightness = 0;
                getWindow().setAttributes(layout);
                cv.setProgressText("0" + "%");
            }
        }
    }
    public void BrightnessX(float y ,float Y,float d)
    {

        cv.setTitle("Brightness");
    }
    float getDistance(float startX, float startY, MotionEvent ev) {
        float distanceSum = 0;
        final int historySize = ev.getHistorySize();
        for (int h = 0; h < historySize; h++)
        {
            float hx = ev.getHistoricalX(0, h);
            float hy = ev.getHistoricalY(0, h);
            float dx = (hx - startX);
            float dy = (hy - startY);
            distanceSum += Math.sqrt(dx * dx + dy * dy);
            startX = hx;
            startY = hy;
        }
        float dx = (ev.getX(0) - startX);
        float dy = (ev.getY(0) - startY);
        distanceSum += Math.sqrt(dx * dx + dy * dy);
        return distanceSum;
    }
}
