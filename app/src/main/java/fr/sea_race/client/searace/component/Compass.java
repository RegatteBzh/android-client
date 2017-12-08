package fr.sea_race.client.searace.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import fr.sea_race.client.searace.R;

/**
 * Created by cyrille on 05/12/17.
 */

public class Compass extends View {

    private float bitmapRatio;
    private float angle;
    private int ray;
    private Point center;
    private OnCompassEventListener mOnCompassEventListener;

    public Compass(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        angle = 0;
    }

    public Compass(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Compass(Context context) {
        this(context, null, 0);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        center  = new Point(getHeight()/2, getHeight()/2);
        ray = (int)Math.floor(getHeight() * 330.0d / 800.0d + 0.5d);

        loadBackground(canvas);

        drawCursor(canvas, angle);
    }

    public void setAngle(float angle) {
        this.angle = angle % 360;
        invalidate();
    }

    private void loadBackground(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.compass);

        canvas.drawBitmap(
                bitmap,
                new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                getRect(bitmap),
                null
        );
    }

    private void drawCursor(Canvas canvas, float cursorAngle) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cursor);
        Matrix matrix = new Matrix();
        matrix.postTranslate((float)(-bitmap.getWidth()/2), (float)(-bitmap.getHeight()));
        matrix.postRotate(cursorAngle,0,0);
        matrix.postScale(bitmapRatio, bitmapRatio);

        float x = (float)center.x + (float)ray * (float)Math.sin(Math.toRadians(cursorAngle));
        float y = (float)center.y - (float)ray * (float)Math.cos(Math.toRadians(cursorAngle));
        matrix.postTranslate(x, y);

        canvas.drawBitmap(
                bitmap,
                matrix,
                new Paint()
        );
    }

    private void computeAngle(float x, float y) {
        double relativeX = x - center.x;
        double relativeY = y - center.y;
        setAngle(360f + (float)Math.toDegrees(Math.atan2(relativeX, -relativeY)));
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(mOnCompassEventListener != null)
                {
                    mOnCompassEventListener.OnStartAngle(angle);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                computeAngle(motionEvent.getX(), motionEvent.getY());
                return true;
            case MotionEvent.ACTION_UP:
                if(mOnCompassEventListener != null)
                {
                    mOnCompassEventListener.OnAngleUpdate(angle);
                }
                return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    private Rect getRect(Bitmap bitmap) {
        bitmapRatio = (float)getHeight() / (float)bitmap.getHeight();
        return getRect(bitmap, bitmapRatio);
    }
    private Rect getRect(Bitmap bitmap, float bitmapRatio) {
        return new Rect(0, 0, (int)Math.floor(bitmap.getWidth() * bitmapRatio + 0.5d), (int)Math.floor(bitmap.getHeight() * bitmapRatio + 0.5d));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //here you have the size of the view and you can do stuff
    }

    public void setOnCompassEventListener(OnCompassEventListener eventListener)
    {
        mOnCompassEventListener = eventListener;
    }
}
