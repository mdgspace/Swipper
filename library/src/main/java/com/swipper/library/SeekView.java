package com.swipper.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by pulkit on 14/12/16.
 */

public class SeekView {

    private Context mContext;
    private View view;
    private TextView textView;
    private TextView title;

    public SeekView(Context context) {
        mContext = context;
        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_seekview, null);
        view.getBackground().setAlpha(100);
        textView=(TextView)view.findViewById(R.id.textView3);
        Typeface.createFromAsset(context.getAssets(),"DroidSans-Bold.ttf");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin = 100;
        params.bottomMargin = 10;
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(view);
        layout.addView(relativeLayout, params);
        view.requestLayout();
        view.setVisibility(View.INVISIBLE);
    }
    public void show()
    {
        view.setVisibility(View.VISIBLE);
    }
    public void hide()
    {
        view.setVisibility(View.INVISIBLE);

    }
    public void setText(String s)
    {
        textView.setText(s);
    }
    public boolean isVisible()
    {
        if(view.getVisibility()==View.VISIBLE)
            return true;
        else
            return false;
    }
}
