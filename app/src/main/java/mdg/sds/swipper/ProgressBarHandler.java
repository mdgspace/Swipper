package mdg.sds.swipper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by pulkit on 29/11/16.
 */

public class ProgressBarHandler{
    private ProgressBar mProgressBar;
    private Context mContext;

    public ProgressBarHandler(Context context) {
        mContext = context;

        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();

        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setScaleX(0.7f);
        mProgressBar.setScaleY(10);
//        mProgressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        Drawable drawable = ((Activity) context).getResources().getDrawable(R.drawable.custom_bar);
        mProgressBar.setProgressDrawable(drawable);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(context);

        rl.setGravity(Gravity.CENTER);
        rl.addView(mProgressBar);

        layout.addView(rl, params);
        ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams)mProgressBar.getLayoutParams();
        mProgressBar.requestLayout();

        hide();
    }

    public void show(int n) {
        mProgressBar.setProgress(n);
        mProgressBar.setVisibility(View.VISIBLE);

    }

    public void hide() {
        Log.e("pi","hide");
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
