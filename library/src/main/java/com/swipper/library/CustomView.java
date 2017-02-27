package com.swipper.library;

/**
 * Created by pulkit on 30/11/16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomView {

    Context mContext;
    View view;
    ProgressBar progressBar;
    private TextView textView;
    private TextView titleText;
    private String title;

    public CustomView(Context context) {
        mContext = context;
        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_view, null);
        view.getBackground().setAlpha(100);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setBackgroundColor(0x00000000);
        textView=(TextView)view.findViewById(R.id.textView2);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"DroidSans-Bold.ttf");
        textView.setTypeface(type);
        titleText=(TextView)view.findViewById(R.id.textView);
        Typeface type1 = Typeface.createFromAsset(context.getAssets(),"DroidSans-Bold.ttf");
        titleText.setTypeface(type1);
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

    public void setProgress(int n) {
        progressBar.setProgress(n);
    }

    public void show() {
        view.setVisibility(View.VISIBLE);
        titleText.setText(title);
    }

    public void hide() {
        view.setVisibility(View.INVISIBLE);
    }
    public boolean isVisible()
    {
        if(view.getVisibility()==View.VISIBLE)
            return true;
        else
            return false;
    }
    public void setTitle(String s)
    {
        title=s;
    }
    public void setProgressText(String s)
    {
        textView.setText(s);
    }
}

