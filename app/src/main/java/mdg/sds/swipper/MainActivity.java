package mdg.sds.swipper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class MainActivity extends AppCompatActivity {
    private int mActivePointerId = INVALID_POINTER_ID;
    EditText et;
    AudioManager audio;
    int maxVolume;
    int currentVolume;
    float volper;
    double per;
     CustomView cv;
    ProgressBarHandler mProgressBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mProgressBarHandler = new ProgressBarHandler(this);
        cv=new CustomView(this);
//        super.onCreate(savedInstanceState);
/*        setContentView((new RelativeLayout(this) {
            private int value = 0;
            private TextView textView;
            {
                addView(new DialView(getContext()) {
                    {
                        setStepAngle(5f);
                        setDiscArea(.30f, .60f);
                    }
                    @Override
                    protected void onRotate(int offset) {

                        textView.setText(String.valueOf(value += offset));
                    }
                }, new RelativeLayout.LayoutParams(0, 0) {
                    {
                        width = MATCH_PARENT;
                        height = MATCH_PARENT;
                        addRule(RelativeLayout.CENTER_IN_PARENT);
                    }
                });
                addView(textView = new TextView(getContext()) {
                    {
                        setText(Integer.toString(value));
                        setTextColor(Color.WHITE);
                        setTextSize(30);
                    }
                }, new RelativeLayout.LayoutParams(0, 0) {
                    {
                        width = WRAP_CONTENT;
                        height = WRAP_CONTENT;
                        addRule(RelativeLayout.CENTER_IN_PARENT);
                    }
                });
            }
        }));*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        et=(EditText)findViewById(R.id.editText);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {

                Log.e("pul","down");
                /*final float x =ev.getX();
                final float y = ev.getY();
                getDistance(x,y,ev);*/
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final float x = ev.getX();
                final float y = ev.getY();
                try {
                    if(x==ev.getHistoricalX(0,0)) {
                        if (y < ev.getHistoricalY(0, 0)) {
                            Log.e("p", "in if");
                            WindowManager.LayoutParams layout = getWindow().getAttributes();
                            Log.e("old", getWindow().getAttributes().screenBrightness + "");
                            Log.e("new", (float) (getDistance(x, y, ev) / 200) + "");
                            if (getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 200) <= 1) {

                                cv.show();
                                cv.setProgress((int)((getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 200))*100));
//                                cv.hide();

                                layout.screenBrightness = getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 200);
                                getWindow().setAttributes(layout);
                                et.setText((Double.valueOf((getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 200)) * 100).toString()));
                            } else {
                                layout.screenBrightness = 1;
                                et.setText("100");
                                getWindow().setAttributes(layout);
                            }
                        } else {
                            Log.e("p", "in else");
                            WindowManager.LayoutParams layout = getWindow().getAttributes();
                            Log.e("old", getWindow().getAttributes().screenBrightness + "");
                            Log.e("new", (float) (getDistance(x, y, ev) / 200) + "");
                            if (getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 200) >= 0) {

                             cv.show();
                             cv.setProgress((int)((getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 200))*100));
//                             cv.hide();

                                layout.screenBrightness = getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 200);
                                getWindow().setAttributes(layout);
                                et.setText((Double.valueOf((getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 200)) * 100).toString()));
                            } else {
                                layout.screenBrightness = 0;
                                getWindow().setAttributes(layout);
                                et.setText("0");
                            }
                        }
                    }
                }catch(IllegalArgumentException e)
                {

                }
             try
             {
                 if(y==ev.getHistoricalY(0,0)) {
                     if (x > ev.getHistoricalX(0, 0)) {
                         currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                         per=(double)currentVolume/maxVolume;
                         try {
                             if (per + (float) (getDistance(x, y, ev) / 110) < 1) {
                                    cv.show();
                                 cv.setProgress((int)((per+(float)(getDistance(x,y,ev)/110))*100));
//                                 cv.hide();
//                                 mProgressBarHandler.show((int)((per+(float)(getDistance(x,y,ev)/110))*100));
                                 volper = ((float) per + (float) (getDistance(x, y, ev) / 110));
                                 et.setText(Double.valueOf(volper * 100).toString());
                                 audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volper * maxVolume), 0);
//                                 mProgressBarHandler.hide();

                             } else {
                                 audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxVolume), 0);
                                 et.setText("100");
                             }
                         }catch(java.lang.SecurityException e)
                         {
/*
                         } catch (InterruptedException e) {
                             e.printStackTrace();*/
                         }
                     } else {
                         currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                         per=(double)currentVolume/maxVolume;

                         if (per- (float) (getDistance(x, y, ev) / 110) >0) {

                             cv.show();
                            cv.setProgress((int)((per-(float)(getDistance(x,y,ev)/110))*100));
//                             cv.hide();
                             volper = ((float)per- (float) (getDistance(x, y, ev) / 110));
                             et.setText(Double.valueOf(volper * 100).toString());
                             audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volper * maxVolume), 0);
//                             mProgressBarHandler.hide();
                            /* long t=10;
                             Thread.currentThread().wait(t);
                             mProgressBarHandler.hide();*/


                         } else {
                             audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                             et.setText("0");
                         }
                     }
                 }
             }catch(IllegalArgumentException e)
             {

             }/* catch (InterruptedException e) {
                 e.printStackTrace();
             }*/
                break;
            }

            case MotionEvent.ACTION_UP: {
                Log.e("pul","up");
                /*final float x = ev.getX();
                final float y = ev.getY();
                getDistance(x,y,ev);*/
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    float getDistance(float startX, float startY, MotionEvent ev) {
        float distanceSum = 0;
        final int historySize = ev.getHistorySize();
        for (int h = 0; h < historySize; h++) {
            // historical point
            float hx = ev.getHistoricalX(0, h);
            float hy = ev.getHistoricalY(0, h);
            // distance between startX,startY and historical point
            float dx = (hx - startX);
            float dy = (hy - startY);
            distanceSum += Math.sqrt(dx * dx + dy * dy);
            // make historical point the start point for next loop iterationd
            startX = hx;
            startY = hy;
        }
        // add distance from last historical point to event's point
        float dx = (ev.getX(0) - startX);
        float dy = (ev.getY(0) - startY);
        distanceSum += Math.sqrt(dx * dx + dy * dy);
      //  Log.e("dis",distanceSum+"");
        return distanceSum;
    }
/*    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
                    return false;
                }
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.e("p","swipe left");
                }
                // left to right swipe
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.e("p","swipe right");
                }
            } catch (Exception e) {

            }
            return false;
        }
    }*/
}
