package com.example.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public abstract class DialView extends View {

    private float centerX;
    private float centerY;
    private float minCircle;
    private float maxCircle;
    private float stepAngle;
    private float deltaAngle = 0;
    private float radius;
    private float modifiedtouchAngle;
    private float lastAngle = 0;
    private int offsetSum = 0;
    private Runnable delay;
    public Paint paint2;
    public Canvas can;
    Handler handler;
    private static final float CIRCLE_LIMIT = 359.9999f;
    public int value = 0;
    int c = 0;


    public DialView(Context context) {
        super(context);
        stepAngle = 1;
        paint2 = new Paint();
        handler = new Handler();
        setOnTouchListener(new OnTouchListener() {
            private boolean isDragging;
            private float startAngle = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float touchX = event.getX();
                float touchY = event.getY();
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        startAngle = touchAngle(touchX, touchY);
                        isDragging = isInDiscArea(touchX, touchY);
                        Log.e("in ACtion down","in Action Down");
                        handler.removeCallbacks(delay);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isDragging) {
                            float touchAngle = touchAngle(touchX, touchY);
                            if(touchAngle>=0&&touchAngle<=180)
                                modifiedtouchAngle=touchAngle;
                            else
                                modifiedtouchAngle=360+touchAngle;
                            Log.e("touchangle", touchAngle + "");
                            if(modifiedtouchAngle>=lastAngle-20&&modifiedtouchAngle<=lastAngle+20) {
                                deltaAngle = (360 + touchAngle - startAngle + 180) % 360 - 180;
                                Log.e("delta angle", deltaAngle + "");
                                if (Math.abs(deltaAngle) > stepAngle) {
                                    int offset = (int) (deltaAngle / stepAngle);
                                    offsetSum += offset;
                                    startAngle = touchAngle;
                                    lastAngle = offsetSum * 3.6f;
                                    onRotate(offset);
                                    invalidate();
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("in Action Up","in Action Up");
                      handler.postDelayed(delay=new Runnable() {
                            @Override
                            public void run() {
                                CircularSeekBar.rl.setVisibility(View.INVISIBLE);
                            }
                        }, 2000);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isDragging = false;
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        centerX = getMeasuredWidth() / 2f;
        centerY = getMeasuredHeight() / 2f;
        super.onLayout(changed, l, t, r, b);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        can = canvas;
        radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2f;

       /* Paint p2=new Paint();
        p2.setColor(0x00000000);
        p2.setAlpha(20);
        canvas.drawCircle(centerX, centerY, (maxCircle+0.1f) * radius,p2);*/

        Paint p3 = new Paint();
        p3.setColor(0xffcccccc);
        drawArcSegment(can, centerX, centerY, (minCircle) * radius, (maxCircle) * radius, 0, 360, p3, p3);
        Paint p = new Paint();
        p.setColor(Color.parseColor("#FB5B0A"));
        if(offsetSum>=0)
        drawArcSegment(can, centerX, centerY, (minCircle) * radius, (maxCircle) * radius, -90,offsetSum*3.6f, p, p);
        super.onDraw(canvas);

    }

    public void setStepAngle(float angle) {
        stepAngle = Math.abs(angle % 360);
    }

    public void setDiscArea(float radius1, float radius2) {
        radius1 = Math.max(0, Math.min(1, radius1));
        radius2 = Math.max(0, Math.min(1, radius2));
        minCircle = Math.min(radius1, radius2);
        maxCircle = Math.max(radius1, radius2);
    }

    private boolean isInDiscArea(float touchX, float touchY) {
        float dX2 = (float) Math.pow(centerX - touchX, 2);
        float dY2 = (float) Math.pow(centerY - touchY, 2);
        float distToCenter = (float) Math.sqrt(dX2 + dY2);
        float baseDist = Math.min(centerX, centerY);
        float minDistToCenter = minCircle * baseDist-0.2f;
        float maxDistToCenter = maxCircle * baseDist+0.2f;
        return distToCenter >= minDistToCenter && distToCenter <= maxDistToCenter;
    }

    private float touchAngle(float touchX, float touchY) {
        float dX = touchX - centerX;
        float dY = centerY - touchY;
        return (float) (270 - Math.toDegrees(Math.atan2(dY, dX))) % 360 - 180;
    }

    protected abstract void onRotate(int offset);


    public void drawArcSegment(Canvas can, float cx, float cy, float rInn, float rOut, float startAngle,
                               float sweepAngle, Paint fill, Paint stroke) {
        if (sweepAngle > CIRCLE_LIMIT) {
            sweepAngle = CIRCLE_LIMIT;
        }
        if (sweepAngle < -CIRCLE_LIMIT) {
            sweepAngle = -CIRCLE_LIMIT;
        }

        RectF outerRect = new RectF(cx - rOut, cy - rOut, cx + rOut, cy + rOut);
        RectF innerRect = new RectF(cx - rInn, cy - rInn, cx + rInn, cy + rInn);

        Path segmentPath = new Path();
        double start = toRadians(startAngle);
        segmentPath.moveTo((float) (cx + rInn * cos(start)), (float) (cy + rInn * sin(start)));
        segmentPath.lineTo((float) (cx + rOut * cos(start)), (float) (cy + rOut * sin(start)));
        segmentPath.arcTo(outerRect, startAngle, sweepAngle);
        double end = toRadians(startAngle + sweepAngle);
        segmentPath.lineTo((float) (cx + rInn * cos(end)), (float) (cy + rInn * sin(end)));
        segmentPath.arcTo(innerRect, startAngle + sweepAngle, -sweepAngle);
        if (fill != null) {
            can.drawPath(segmentPath, fill);
        }
        if (stroke != null) {
            can.drawPath(segmentPath, stroke);
        }
    }
    public void setLastAngle(float lastangle)
    {
        lastAngle=lastangle;
        offsetSum=(int)(lastangle/stepAngle);
      /*  Paint p = new Paint();
        p.setColor(0xffff0000);
        drawArcSegment(can, centerX, centerY, (minCircle) * radius, (maxCircle) * radius, -90,lastangle, p, p);*/
    }

}
