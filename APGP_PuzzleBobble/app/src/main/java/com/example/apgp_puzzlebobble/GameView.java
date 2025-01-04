package com.example.apgp_puzzlebobble;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class GameView extends View implements Choreographer.FrameCallback{

    public static Resources res;
    public static GameView view;

    private long frameCount = 0;     // 프레임 카운터
    private long fpsLastTime = 0;   // 마지막 FPS 계산 시간
    private int currentFPS = 0;     // 계산된 FPS 값


    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        GameView.res = getResources();
        GameView.view = this;
        Choreographer.getInstance().postFrameCallback(this);
        //setFullScreen();
    }

    public void setFullScreen() {
        setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);
//
//        float view_ratio = (float)w / (float)h;
//        float game_ratio = game_width / game_height;
//        if(view_ratio > game_ratio)
//        {
//            x_offset = (int)((w - h * game_ratio)/2);
//            y_offset = 0;
//            scale = h/game_height;
//        }

        float view_ratio = (float)w / (float)h;
        float game_ratio = Metrics.game_width / Metrics.game_height;
        if (view_ratio > game_ratio) {
            Metrics.x_offset = (int) ((w - h * game_ratio) / 2);
            Metrics.y_offset = 0;
            Metrics.scale = h / Metrics.game_height;
        } else {
            Metrics.x_offset = 0;
            Metrics.y_offset = (int)((h - w / game_ratio) / 2);
            Metrics.scale = w / Metrics.game_width;
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(Metrics.x_offset, Metrics.y_offset);
        canvas.scale(Metrics.scale, Metrics.scale);

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
        frameCount++; // 프레임 카운터 증가

        // FPS 계산 (1초마다)
        if (fpsLastTime == 0) {
            fpsLastTime = nanos; // 초기화
        } else if (nanos - fpsLastTime >= 1_000_000_000) { // 1초 경과
            currentFPS = (int) frameCount; // 1초 동안 카운트된 프레임 수를 FPS로 저장
            frameCount = 0; // 카운터 초기화
            fpsLastTime = nanos; // 마지막 시간 갱신
            Log.d("FPS", "FPS: "+currentFPS);
        }

        invalidate();
        if(isShown())
        {
            Choreographer.getInstance().postFrameCallback(this);
        }
    }
}