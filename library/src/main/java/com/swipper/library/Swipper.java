package com.swipper.library;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.VideoView;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class Swipper extends Activity {
    private int mActivePointerId = INVALID_POINTER_ID;
    private AudioManager audio;
    private CustomView customView;
    private CircularSeekBar circularSeekBar;
    private SeekView seekView;
    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private int maxVolume;
    private int currentVolume;
    private int numberOfTaps = 0;
    private long lastTapTimeMs = 0;
    private long touchDownMs = 0;
    private double volper;
    private double per;
    private float brightness;
    private float seekdistance = 0;
    private float distanceCovered = 0;
    private boolean checkBrightness = true;
    private boolean checkVolume = true;
    private boolean checkSeek = true;
    private String onHorizontal;
    private String onVertical;
    private String onCircular;
    public static String color = "#FB5B0A";


    public enum Orientation {
        HORIZONTAL, VERTICAL, CIRCULAR
    }

    public void set(Context context) {
        customView = new CustomView(context);
        seekView = new SeekView(context);
        brightness = android.provider.Settings.System.getFloat(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = brightness / 255;
//        layout.screenBrightness = 1;
        getWindow().setAttributes(layout);
        customView.setProgress((int) ((brightness / 255) * 100));
        customView.setProgress((int)((200/255) * 100));
        customView.setProgressText(Integer.valueOf((int) ((brightness / 255) * 100)).toString() + "%");
        circularSeekBar = new CircularSeekBar(context);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public static void setColor(String s) {
        color = s;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                seekdistance = 0;
                distanceCovered = 0;
                touchDownMs = System.currentTimeMillis();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                distanceCovered = getDistance(x, y, ev);
                try {
                    if (onVertical == "Brightness" && checkBrightness == true)
                        changeBrightness(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "Y");
                    else if (onVertical == "Volume" && checkVolume == true)
                        changeVolume(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "Y");
                    else if (onVertical == "Seek" && checkSeek == true) {
                        changeSeek(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "Y");
                    }
                    if (onHorizontal == "Brightness" && checkBrightness == true)
                        changeBrightness(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "X");
                    else if (onHorizontal == "Volume" && checkVolume == true)
                        changeVolume(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "X");
                    else if (onHorizontal == "Seek" && checkSeek == true) {
                        changeSeek(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "X");
                    }
                } catch (IllegalArgumentException e) {

                } catch (IndexOutOfBoundsException e) {

                }

                break;
            }

            case MotionEvent.ACTION_UP: {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (customView.isVisible())
                            customView.hide();
                        if (seekView.isVisible())
                            seekView.hide();
                    }
                }, 2000);

                if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                    numberOfTaps = 0;
                    lastTapTimeMs = 0;
                    break;
                }

                if (numberOfTaps > 0 && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                    numberOfTaps += 1;
                } else {
                    numberOfTaps = 1;
                }

                lastTapTimeMs = System.currentTimeMillis();
                if (numberOfTaps == 2) {
                    if (onCircular == "Brightness") {
                        circularSeekBar.setType("Brightness");
                        if (circularSeekBar.isVisibile())
                            circularSeekBar.hide();
                        else
                            circularSeekBar.show();
                    } else if (onCircular == "Volume") {
                        circularSeekBar.setType("Volume");
                        if (circularSeekBar.isVisibile())
                            circularSeekBar.hide();
                        else
                            circularSeekBar.show();
                    }
                }
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
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    public void disableBrightness() {
        checkBrightness = false;
    }

    public void disableSeek() {
        checkSeek = false;
    }

    public void disableVolume() {
        checkVolume = false;
    }

    public void enableBrightness() {
        checkBrightness = true;
    }

    public void enableSeek() {
        checkSeek = true;
    }

    public void enableVolume() {
        checkVolume = true;
    }

    public void changeVolume(float X, float Y, float x, float y, float distance, String type) {
        customView.setTitle("  Volume  ");
        seekView.hide();
        if (type == "Y" && x == X) {
            if (y < Y) {
                distance = distance / 100;
                commonVolume(distance);
            } else {
                distance = distance / 150;
                commonVolume(-distance);
            }
        } else if (type == "X" && y == Y) {
            if (x > X) {
                distance = distance / 200;
                commonVolume(distance);
            } else {
                distance = distance / 250;
                commonVolume(-distance);
            }
        }
    }

    public void commonVolume(float distance) {
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        per = (double) currentVolume / (double) maxVolume;
        Log.e("per", per + "");
        if (per + distance <= 1 && per + distance >= 0) {
            customView.show();
            if (distance > 0.05 || distance < -0.05) {
                customView.setProgress((int) ((per + distance) * 100));
                customView.setProgressText((int) ((per + distance) * 100) + "%");
                volper = (per + (double) distance);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volper * 15), 0);
            }
        }
    }

    public void changeBrightness(float X, float Y, float x, float y, float distance, String type) {
        customView.setTitle("Brightness");
        seekView.hide();
        if (type == "Y" && x == X) {
            distance = distance / 270;
            if (y < Y) {
                commonBrightness(distance);
            } else {
                commonBrightness(-distance);
            }
        } else if (type == "X" && y == Y) {
            distance = distance / 160;
            if (x > X) {
                commonBrightness(distance);
            } else {
                commonBrightness(-distance);
            }
        }
    }
    public void commonBrightness(float distance) {
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        if (getWindow().getAttributes().screenBrightness + distance <= 1 && getWindow().getAttributes().screenBrightness + distance >= 0) {
            customView.show();
            if ((int) ((getWindow().getAttributes().screenBrightness + distance) * 100) > 100) {
                customView.setProgress(100);
                customView.setProgressText("100");
            } else if ((int) ((getWindow().getAttributes().screenBrightness + distance) * 100) < 0) {
                customView.setProgress(0);
                customView.setProgressText("0");
            } else {
                customView.setProgress((int) ((getWindow().getAttributes().screenBrightness + distance) * 100));
                customView.setProgressText(Integer.valueOf((int) ((getWindow().getAttributes().screenBrightness + distance) * 100)).toString() + "%");
            }
            layout.screenBrightness = getWindow().getAttributes().screenBrightness + distance;
            getWindow().setAttributes(layout);
        }
    }

    public void changeSeek(float X, float Y, float x, float y, float distance, String type) {

        if (type == "Y" && x == X) {
            distance = distance / 300;
            if (y < Y) {
                seekCommon(distance);
            } else {
                seekCommon(-distance);
            }
        } else if (type == "X" && y == Y) {
            distance = distance / 200;
            if (x > X) {
                seekCommon(distance);
            } else {
                seekCommon(-distance);
            }
        }
    }

    public void seekCommon(float distance) {
        seekdistance += distance * 60000;
        seekView.show();
        if (mediaPlayer != null) {
            Log.e("after", mediaPlayer.getCurrentPosition() + (int) (distance * 60000) + "");
            Log.e("seek distance", (int) (seekdistance) + "");
            if (mediaPlayer.getCurrentPosition() + (int) (distance * 60000) > 0 && mediaPlayer.getCurrentPosition() + (int) (distance * 60000) < mediaPlayer.getDuration() + 10) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + (int) (distance * 60000));
                if (seekdistance > 0)
                    seekView.setText("+" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((mediaPlayer.getCurrentPosition() + (int) (distance * 60000)) / 60000) + ":" + String.valueOf((int) ((mediaPlayer.getCurrentPosition() + (int) (distance * 60000)) % 60000)).substring(0, 2) + ")");
                else
                    seekView.setText("-" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((mediaPlayer.getCurrentPosition() + (int) (distance * 60000)) / 60000) + ":" + String.valueOf((int) ((mediaPlayer.getCurrentPosition() + (int) (distance * 60000)) % 60000)).substring(0, 2) + ")");
            }
        } else if (videoView != null) {
            Log.e("after", videoView.getCurrentPosition() + (int) (distance * 60000) + "");
            Log.e("seek distance", (int) (seekdistance) + "");
            if (videoView.getCurrentPosition() + (int) (distance * 60000) > 0 && videoView.getCurrentPosition() + (int) (distance * 60000) < videoView.getDuration() + 10) {
                videoView.seekTo(videoView.getCurrentPosition() + (int) (distance * 60000));
                if (seekdistance > 0)
                    seekView.setText("+" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((videoView.getCurrentPosition() + (int) (distance * 60000)) / 60000) + ":" + String.valueOf((int) ((videoView.getCurrentPosition() + (int) (distance * 60000)) % 60000)).substring(0, 2) + ")");
                else
                    seekView.setText("-" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((videoView.getCurrentPosition() + (int) (distance * 60000)) / 60000) + ":" + String.valueOf((int) ((videoView.getCurrentPosition() + (int) (distance * 60000)) % 60000)).substring(0, 2) + ")");

            }
        }
    }

    public void Seek(Orientation orientation, VideoView v) {

        if (orientation.equals(Orientation.VERTICAL))
            onVertical = "Seek";
        else if (orientation.equals(Orientation.HORIZONTAL))
            onHorizontal = "Seek";
        videoView = v;
    }

    public void Seek(Orientation orientation, MediaPlayer v) {

        if (orientation.equals(Orientation.VERTICAL))
            onVertical = "Seek";
        else if (orientation.equals(Orientation.HORIZONTAL))
            onHorizontal = "Seek";
        mediaPlayer = v;
    }

    public void Brightness(Orientation orientation) {

        if (orientation.equals(Orientation.VERTICAL))
            onVertical = "Brightness";
        else if (orientation.equals(Orientation.HORIZONTAL))
            onHorizontal = "Brightness";
        else if (orientation.equals(Orientation.CIRCULAR))
            onCircular = "Brightness";
    }

    public void Volume(Orientation orientation) {

        if (orientation.equals(Orientation.VERTICAL))
            onVertical = "Volume";
        else if (orientation.equals(Orientation.HORIZONTAL))
            onHorizontal = "Volume";
        else if (orientation.equals(Orientation.CIRCULAR))
            onCircular = "Volume";
    }


    float getDistance(float startX, float startY, MotionEvent ev) {
        float distanceSum = 0;
        final int historySize = ev.getHistorySize();
        for (int h = 0; h < historySize; h++) {
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
