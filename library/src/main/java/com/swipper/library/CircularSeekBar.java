package com.swipper.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CircularSeekBar {

    final Context mContext;
    private int percent;
    private int currentVolume;
    private int maxVolume;
    private int valueCounter;
    private float brightness;
    private String Type;
    private AudioManager audio;
    private DialView dailView;
    public static RelativeLayout relativeLayout;

    public CircularSeekBar(Context context) {
        mContext = context;
        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout = new RelativeLayout(context) {


            private int valueCounter = (int) ((android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1) / 255) * 360 / 3f);
            public TextView textView;

            {
                if (Type == "Brightness") {
                    valueCounter = (int) ((android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1) / 255) * 360 / 3f);
                } else if (Type == "Volume") {
                    currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                    valueCounter = (int) (((float) currentVolume / (float) maxVolume) * 360 / 3f);
                }
                addView(dailView = new DialView(getContext()) {
                    {
                        setStepAngle(3f);
                        setDiscArea(.35f, .48f);
                        setLastAngle((android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1) / 255) * 360);
                       valueCounter = (int) ((android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1) / 255) * 360 / 3f);
//                        valueCounter = 23;
                        brightness = android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
                        WindowManager.LayoutParams layout = ((Activity) mContext).getWindow().getAttributes();
                        layout.screenBrightness = brightness / 255;
                        ((Activity) mContext).getWindow().setAttributes(layout);
                        if (Type == "Brightness") {
                            setLastAngle((android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1) / 255) * 360);
                           valueCounter = (int) ((android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1) / 255) * 360 / 3f);
                            valueCounter = (int) ((100 / 255) * 360 / 3f);
                            brightness = android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
                            WindowManager.LayoutParams layout1 = ((Activity) mContext).getWindow().getAttributes();
                           layout1.screenBrightness = brightness / 255;
                            layout1.screenBrightness = 1;
                            ((Activity) mContext).getWindow().setAttributes(layout1);
                        } else if (Type == "Volume") {
                            currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                            setLastAngle(((float) currentVolume / (float) maxVolume) * 360);
                            valueCounter = (int) (((float) currentVolume / (float) maxVolume) * 360 / 3f);
                            audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) currentVolume, 0);

                        }
                    }

                    @Override
                    protected void onRotate(int offset) {
                        valueCounter += offset;
                        if (valueCounter <= 100 && valueCounter >= 0) {
                            percent = valueCounter;
                            textView.setText(String.valueOf(valueCounter) + "%");
                            if (Type == "Brightness") {
                               WindowManager.LayoutParams layout = ((Activity) mContext).getWindow().getAttributes();
                                layout.screenBrightness = (float) ((double) valueCounter / (double) 100);
                                ((Activity) mContext).getWindow().setAttributes(layout);
//                                Swipper.fl.getBackground().setAlpha((int) (((100-(double) valueCounter )/ (double) 100) * 255));
                            } else if (Type == "Volume") {
                                audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (((double) valueCounter / (double) 100) * 15), 0);
                            }
                        }
                        if (valueCounter > 100)
                            valueCounter = 100;
                        if (valueCounter < 0)
                            valueCounter = 0;
                    }
                }, new LayoutParams(0, 0) {
                    {
                        width = MATCH_PARENT;
                        height = MATCH_PARENT;
                        addRule(RelativeLayout.CENTER_IN_PARENT);
                    }
                });
                addView(textView = new TextView(getContext()) {
                    {

                        setText(Integer.toString(valueCounter) + "%");
                        setTextColor(Color.WHITE);
                        setTextSize(40);
                        setTypeface(Typeface.createFromAsset(mContext.getAssets(), "DroidSans-Bold.ttf"));
                    }
                }, new LayoutParams(0, 0) {
                    {
                        width = WRAP_CONTENT;
                        height = WRAP_CONTENT;
                        addRule(RelativeLayout.CENTER_IN_PARENT);
                    }
                });
            }
        };
        relativeLayout.setVisibility(View.INVISIBLE);
        layout.addView(relativeLayout, params);

    }

    public void show() {
        relativeLayout.setVisibility(View.VISIBLE);
    }

    public void hide() {

        relativeLayout.setVisibility(View.INVISIBLE);
    }

    public boolean isVisibile() {
        if (relativeLayout.getVisibility() == View.VISIBLE)
            return true;
        else
            return false;
    }

    public void setType(String type) {
        Type = type;
        audio = (AudioManager) (mContext).getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        valueCounter = (int) ((android.provider.Settings.System.getFloat((mContext).getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1) / 255) * 360 / 3f);

    }
}
