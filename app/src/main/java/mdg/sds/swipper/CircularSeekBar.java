package mdg.sds.swipper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by pulkit on 1/12/16.
 */

public class CircularSeekBar {

        Context mContext;
        RelativeLayout rl;
        int percent;
        float brightness;
    public CircularSeekBar(Context context)
      {
          mContext=context;
          ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
          LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
          rl = new RelativeLayout(context)
          {
              private int value = 0;
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
                         // textView.setText(String.valueOf(value));
                       /*   WindowManager.LayoutParams layout = getContext().getWindow().getAttributes();
                          layout.screenBrightness = brightness / 255;
                          getWindow().setAttributes(layout);*/
                      }
                      @Override
                      protected void onRotate(int offset) {
                          value += offset;
                          if(value<=100&&value>=0)
                          {
                              percent=value;
                              textView.setText(String.valueOf(value));
                            /*  WindowManager.LayoutParams layout = getWindow().getAttributes();
                              layout.screenBrightness = value/100;
                              getWindow().setAttributes(layout);*/

                          }
                          if(value>100)
                              value=100;
                          if(value<0)
                              value=0;
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
                      public void setText(String s)
                      {

                      }
                  }, new RelativeLayout.LayoutParams(0, 0) {
                      {
                          width = WRAP_CONTENT;
                          height = WRAP_CONTENT;
                          addRule(RelativeLayout.CENTER_IN_PARENT);
                      }
                  });
              }
          };
          layout.addView(rl, params);
      }
    public void show()
    {
        rl.setVisibility(View.VISIBLE);
    }
    public void hide()
    {
        rl.setVisibility(View.INVISIBLE);
    }
    public int percent()
    {
        return  percent;
    }
}
