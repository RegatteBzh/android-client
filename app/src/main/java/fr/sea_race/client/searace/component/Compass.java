package fr.sea_race.client.searace.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import fr.sea_race.client.searace.R;

/**
 * Created by cyrille on 05/12/17.
 */

public class Compass extends View {

    public Compass(Context context) {
        super(context);
    }

    public Compass(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
        /*Matrix matrix = new Matrix();
        matrix.setScale(1.0f / bitmap.getWidth(), 1.0f / bitmap.getHeight());*/

        Log.i("SIZE", "View width: "  + getWidth());
        Log.i("SIZE", "View height: "  + getHeight());
        Log.i("SIZE", "Canvas width: "  + canvas.getWidth());
        Log.i("SIZE", "Canvas height: "  + canvas.getHeight());
        Log.i("SIZE", "Bitmap width: "  + bitmap.getWidth());
        Log.i("SIZE", "Bitmap height: "  + bitmap.getHeight());

        //canvas.setBitmap(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(bitmap, new Rect(0,0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0,0, canvas.getHeight(), canvas.getHeight()), null);

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
}
