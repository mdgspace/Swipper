package mdg.sds.swipper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import static android.R.attr.radius;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public abstract class DialView extends View {

    private float centerX;
    private float centerY;
    private float minCircle;
    private float maxCircle;
    private float stepAngle;
    private float radius;
    public Paint paint2;
    public Canvas can;
    private static final float CIRCLE_LIMIT = 359.9999f;
    public int value=0;
    int c=0;


    public DialView(Context context) {
        super(context);
        stepAngle = 1;
        paint2=new Paint();
        setOnTouchListener(new OnTouchListener() {
            private float startAngle;
            private boolean isDragging;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float touchX = event.getX();
                float touchY = event.getY();
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        startAngle = touchAngle(touchX, touchY);
                        isDragging = isInDiscArea(touchX, touchY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isDragging) {
                            Log.e("pul","rotate");
                            float touchAngle = touchAngle(touchX, touchY);
                            float deltaAngle = (360 + touchAngle - startAngle + 180) % 360 - 180;
                            if (Math.abs(deltaAngle) > stepAngle) {
                                drawArcSegment(centerX+5f,centerY+10f,(minCircle-.07f)*radius,(maxCircle-0.03f)*radius,startAngle,10f,paint2,paint2);
                                int offset = (int) deltaAngle / (int) stepAngle;
                                startAngle = touchAngle;
                                onRotate(offset);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
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

            Log.e("o", "in draw");
            can = canvas;
            radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2f;
            Paint paint = new Paint();
            paint.setDither(true);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xff888888);
            paint.setAlpha(100);
            paint.setXfermode(null);
            Paint paint1 = new Paint();
            paint1.setDither(true);
            paint1.setAntiAlias(true);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setColor(0xff888888);
            paint1.setStrokeWidth(50);
            paint1.setXfermode(null);
            LinearGradient linearGradient = new LinearGradient(
                    radius, 0, radius, radius, 0x00000000, 0x00000000, Shader.TileMode.CLAMP);
            paint.setShader(linearGradient);
            canvas.drawCircle(centerX, centerY, maxCircle * radius, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawCircle(centerX, centerY, minCircle * radius, paint1);
  /*      for (int i = 0, n =  360 / (int) stepAngle; i < n; i++) {
            double rad = Math.toRadians((int) stepAngle * i);
            int startX = (int) (centerX + minCircle * radius * Math.cos(rad));
            int startY = (int) (centerY + minCircle * radius * Math.sin(rad));
            int stopX = (int) (centerX + maxCircle * radius * Math.cos(rad));
            int stopY = (int) (centerY + maxCircle * radius * Math.sin(rad));
            canvas.drawLine(startX, startY, stopX, stopY, paint);
      }*/
            Paint p = new Paint();
            drawArcSegment(centerX, centerY, (minCircle - .07f) * radius, (maxCircle - 0.03f) * radius, 0, 10f, p, p);
            super.onDraw(canvas);

    }
    public void setStepAngle(float angle) {/*
        Paint p = new Paint();
        drawArcSegment(centerX+10f, centerY+10f, (minCircle - .07f) * radius, (maxCircle - 0.03f) * radius, 0, 10f, p, p);*/
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
        float minDistToCenter = minCircle * baseDist;
        float maxDistToCenter = maxCircle * baseDist;
        return distToCenter >= minDistToCenter && distToCenter <= maxDistToCenter;
    }

    private float touchAngle(float touchX, float touchY) {
        float dX = touchX - centerX;
        float dY = centerY - touchY;
        return (float) (270 - Math.toDegrees(Math.atan2(dY, dX))) % 360 - 180;
    }

    protected abstract void onRotate(int offset);




    public void drawArcSegment( float cx, float cy, float rInn, float rOut, float startAngle,
                                      float sweepAngle, Paint fill, Paint stroke) {
        Log.e("pulkit","in arc segment");
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
        segmentPath.moveTo((float)(cx + rInn * cos(start)), (float)(cy + rInn * sin(start)));
        segmentPath.lineTo((float)(cx + rOut * cos(start)), (float)(cy + rOut * sin(start)));
        segmentPath.arcTo(outerRect, startAngle, sweepAngle);
        double end = toRadians(startAngle + sweepAngle);
        segmentPath.lineTo((float)(cx + rInn * cos(end)), (float)(cy + rInn * sin(end)));
        segmentPath.arcTo(innerRect, startAngle + sweepAngle, -sweepAngle);
        if (fill != null) {
            can.drawPath(segmentPath, fill);
        }
        if (stroke != null) {
            can.drawPath(segmentPath, stroke);
        }
    }

}
