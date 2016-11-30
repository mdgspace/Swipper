package mdg.sds.swipper;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
          dv.setDiscArea(.30f, .60f);
          ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
          LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
          RelativeLayout rl = new RelativeLayout(context);
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
