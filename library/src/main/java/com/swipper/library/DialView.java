package com.swipper.library;

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
    Handler handler;
    private float CIRCLE_LIMIT = 359.9999f;

    public DialView(Context context) {
        super(context);
        stepAngle = 1;
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
                        handler.removeCallbacks(delay);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isDragging) {
                            float touchAngle = touchAngle(touchX, touchY);
                            if(touchAngle>=0&&touchAngle<=180)
                                modifiedtouchAngle=touchAngle;
                            else
                                modifiedtouchAngle=360+touchAngle;
                            if(modifiedtouchAngle>=lastAngle-10&&modifiedtouchAngle<=lastAngle+10) {
                                deltaAngle = (360 + touchAngle - startAngle + 180) % 360 - 180;
                                Log.e("delta angle", deltaAngle + "");
                                if (Math.abs(deltaAngle) > stepAngle) {
                                    int offset = (int) (deltaAngle / stepAngle);
                                    offsetSum += offset;
                                    if(offsetSum<1)
                                        offsetSum=1;
                                    else
                                    if(offsetSum>99)
                                        offsetSum=99;
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
                                CircularSeekBar.relativeLayout.setVisibility(View.INVISIBLE);
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
        centerX = getMeasuredWidth()/2f;
        centerY = getMeasuredHeight()/2f;
        super.onLayout(changed, l, t, r, b);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2f;
        Paint paintGrey = new Paint();
        paintGrey.setColor(0xffcccccc);
        Paint innerCirclePaint=new Paint();
        innerCirclePaint.setColor(0x00000000);
        innerCirclePaint.setAlpha(100);
        canvas.drawCircle(centerX, centerY, (minCircle) * radius,innerCirclePaint);
        drawArcSegment(canvas, centerX, centerY, (minCircle) * radius, (maxCircle) * radius, 0, 360, paintGrey, paintGrey);
        Paint progressPaint = new Paint();
        progressPaint.setColor(Color.parseColor(Swipper.color));
        if(offsetSum>=0)
        drawArcSegment(canvas, centerX, centerY, (minCircle) * radius, (maxCircle) * radius, -90,offsetSum*3.6f, progressPaint, progressPaint);
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
        float minDistToCenter = minCircle * baseDist-02f;
        float maxDistToCenter = maxCircle * baseDist+0.2f;
        return distToCenter >= minDistToCenter && distToCenter <= maxDistToCenter;
    }

    private float touchAngle(float touchX, float touchY) {
        float dX = touchX - centerX;
        float dY = centerY - touchY;
        return (float) (270 - Math.toDegrees(Math.atan2(dY, dX))) % 360 - 180;
    }

    protected abstract void onRotate(int offset);

    public void drawAgain()
    {
        invalidate();
    }

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
    }

}
