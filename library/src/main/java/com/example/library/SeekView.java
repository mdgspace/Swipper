package com.example.library;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by pulkit on 14/12/16.
 */

public class SeekView {

    Context mContext;
    View v;
    private TextView tv;
    private TextView title;

    public SeekView(Context context) {
        mContext = context;
        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.custom_view, null);
        v.setScaleY(3.3f);
        v.setScaleX(2f);
        v.getBackground().setAlpha(100);
        tv=(TextView)v.findViewById(R.id.textView3);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin = 100;
        params.bottomMargin = 10;
        RelativeLayout rl = new RelativeLayout(context);
        rl.setGravity(Gravity.CENTER);
        rl.addView(v);
        layout.addView(rl, params);
        v.requestLayout();
        v.setVisibility(View.INVISIBLE);
    }

    public void show() {
        v.setVisibility(View.VISIBLE);
    }

    public void hide() {
        v.setVisibility(View.INVISIBLE);

    }
    public void setText(String s)
    {
        tv.setText(s);
    }
}
