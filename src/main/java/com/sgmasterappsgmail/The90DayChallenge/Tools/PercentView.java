package com.sgmasterappsgmail.The90DayChallenge.Tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
//import android.util.Log;
import android.view.View;

import com.sgmasterappsgmail.The90DayChallenge.R;


/**
 * Created by shia on 11/16/2015.
 */
public class PercentView extends View {
    // private static final String TAG = PercentView.class.getSimpleName();
    private final Paint paint = new Paint();
    private int background = Color.parseColor("#FFCECECE");
    private int primaryColor = Color.parseColor("#FF00ACC1");
    private static final int PERCENT = 100;
    private int percentWith;
    private int top;
    private int lenght;
    private int currentWith;
    private static final int CURRENT_HEIGHT = 60;
    private int percent = 0;

    public PercentView(Context context) {
        super(context);
    }

    public PercentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PercentView, 0, 0);
            primaryColor = a.getColor(R.styleable.PercentView_backgroundd, 0);
            a.recycle();
        }
    }

    public PercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PercentView, 0, 0);
            primaryColor = a.getColor(R.styleable.PercentView_backgroundd, 0);
            a.recycle();
        }
    }

    public void initWith(int percent) {
        this.percent = percent;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d(TAG, "onDraw");
        // Clear canvas
        canvas.drawColor(background);
        drawView(canvas);
    }

    private void drawView(Canvas canvas) {
        percentWith = getWidth() / PERCENT;
        currentWith = percentWith * percent;
        top = (getHeight() - getHeight());
        lenght = getWidth() - getWidth();
        paint.setColor(primaryColor);
        canvas.drawRect(lenght, top, lenght + currentWith, top + CURRENT_HEIGHT, paint);
    }

}
