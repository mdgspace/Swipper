package mdg.sds.swipper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by pulkit on 1/12/16.
 */

public class CircularSeekBar {

        Context mContext;
        DialView dv;
        CircularSeekBar csk;

    public CircularSeekBar(Context context)
      {
          mContext=context;
        dv=new DialView(mContext);
          dv.setStepAngle(5f);
          dv.setDiscArea(.40f, .50f);
          ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
          LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
          RelativeLayout rl = new RelativeLayout(context)
          {
              private int value = 0;
              public TextView textView;
              {
                  addView(new DialView(getContext()) {
                      {
                          setStepAngle(5f);
                          setDiscArea(.40f, .50f);
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
                          setTextColor(Color.BLACK);
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
          };
          rl.setGravity(Gravity.CENTER);
          rl.addView(dv);
          layout.addView(rl, params);
         dv.requestLayout();
      }
    public void show()
    {
        dv.setVisibility(View.VISIBLE);
    }
    public void hide()
    {
        dv.setVisibility(View.INVISIBLE);

    }
}
