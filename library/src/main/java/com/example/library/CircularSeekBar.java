package com.example.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CircularSeekBar {

        final Context mContext;
        public static RelativeLayout rl;
        int percent;
        float brightness;
    public CircularSeekBar(Context context)
      {
          mContext=context;
          ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
          RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
          rl = new RelativeLayout(context)
          {
              private int value = (int)((android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1)/255)*360/3f);
              public TextView textView;
              {
                  addView(new DialView(getContext()) {
                      {
                          setStepAngle(3f);
                          setDiscArea(.30f, .43f);
                          setLastAngle((android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1)/255)*360);
                          value=(int)((android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1)/255)*360/3f);
                          Log.e("last Angle",(android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1)/255)*360+"");
                          brightness = android.provider.Settings.System.getFloat(getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
                          WindowManager.LayoutParams layout = ((Activity) mContext).getWindow().getAttributes();
                          layout.screenBrightness = brightness / 255;
                          ((Activity) mContext).getWindow().setAttributes(layout);
                      }
                      @Override
                      protected void onRotate(int offset) {
                          value += offset;
                          if(value<=100&&value>=0)
                          {
                              percent=value;
                              textView.setText(String.valueOf(value)+"%");
                              WindowManager.LayoutParams layout = ((Activity) mContext).getWindow().getAttributes();
                              layout.screenBrightness =(float)((double)value/(double)100);
                              ((Activity) mContext).getWindow().setAttributes(layout);
                          }
                          if(value>100)
                              value=100;
                          if(value<0)
                              value=0;
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
                          setText(Integer.toString(value)+"%");
                          setTextColor(Color.BLACK);
                          setTextSize(30);
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
          rl.setVisibility(View.INVISIBLE);
          layout.addView(rl, params);
      }
    public void show()
    {
        rl.setVisibility(View.VISIBLE);
    }
    public void hide()
    {
        Log.e("hide","hide");
        rl.setVisibility(View.INVISIBLE);
    }
    public boolean isVisibile()
    {
        if(rl.getVisibility()==View.VISIBLE)
            return true;
        else
            return false;
    }
}
