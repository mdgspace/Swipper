package mdg.sds.swipper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
public abstract class DialView extends View {

    private float centerX;
    private float centerY;
    private float minCircle;
    private float maxCircle;
    private float stepAngle;

    public DialView(Context context) {
        super(context);
        stepAngle = 1;
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
        Log.e("o","in draw");
        float radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2f;
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFFFFFFFF);
        paint.setXfermode(null);
        LinearGradient linearGradient = new LinearGradient(
                radius, 0, radius, radius, 0xFFFFFFFF, 0xFFFFFFFF, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawCircle(centerX, centerY, maxCircle * radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawCircle(centerX, centerY, minCircle * radius, paint);
        paint.setXfermode(null);
        paint.setShader(null);
       // paint.setColor(0xFFFFFFFF);
        for (int i = 0, n =  360 / (int) stepAngle; i < n; i++) {
            double rad = Math.toRadians((int) stepAngle * i);
            int startX = (int) (centerX + minCircle * radius * Math.cos(rad));
            int startY = (int) (centerY + minCircle * radius * Math.sin(rad));
            int stopX = (int) (centerX + maxCircle * radius * Math.cos(rad));
            int stopY = (int) (centerY + maxCircle * radius * Math.sin(rad));
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
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

}
