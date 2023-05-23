package com.example.apgp_puzzlebobble;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class GameView extends View implements Choreographer.FrameCallback{

    public static float scale;
    public static float game_width = 9.0f;
    public static float game_height = 16.0f;
    public static int x_offset, y_offset;

    public static Resources res;



    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        GameView.res = getResources();
        Choreographer.getInstance().postFrameCallback(this);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);

        float view_ratio = (float)w / (float)h;
        float game_ratio = game_width / game_height;
        if(view_ratio > game_ratio)
        {
            x_offset = (int)((w - h * game_ratio)/2);
            y_offset = 0;
            scale = h/game_height;
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(x_offset, y_offset);
        canvas.scale(scale, scale);

       BaseScene.getTopScene().draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = BaseScene.getTopScene().onTouchEvent(event);
        if (handled) {
            return true;
        }
        return super.onTouchEvent(event);
    }
    private long previousNanos;
    @Override
    public void doFrame(long nanos) {
        if(previousNanos != 0)
        {
            long elapsedNanos = nanos - previousNanos;
            BaseScene.getTopScene().update(elapsedNanos);
        }
        previousNanos = nanos;
        invalidate();
        if(isShown())
        {
            Choreographer.getInstance().postFrameCallback(this);
        }
    }
}