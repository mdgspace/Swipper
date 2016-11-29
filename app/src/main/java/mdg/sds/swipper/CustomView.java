package mdg.sds.swipper;

/**
 * Created by pulkit on 30/11/16.
 */
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import mdg.sds.swipper.R;

/**
 * Created by pulkit on 30/11/16.
 */

public class CustomView  {

    Context mContext;
    View v;

    public CustomView(Context context)
    {
        mContext=context;
        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.custom_view, null);
        v.setScaleY(5);
        v.setScaleX(0.6f);
        v.getBackground().setAlpha(100);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin=30;
//        params.bottomMargin=10;
        params.topMargin=40;
        RelativeLayout rl = new RelativeLayout(context);
//       rl.setGravity(Gravity.CENTER);
        rl.addView(v);
        layout.addView(rl, params);
        v.requestLayout();
    }
}

