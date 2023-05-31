package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;

public class MainScene extends BaseScene {
    public BobbleManager bobbleMgr;
    public Score score;
    public Path shotPath = new Path();
    private static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    float startX, startY;
    static
    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.1f);
        paint.setColor(Color.MAGENTA);
    }
    public MainScene()
    {
        add(new Background());
        bobbleMgr = new BobbleManager();
        add(bobbleMgr);
        score =  new Score(R.mipmap.scoresprite, 9.f, 0.f, 0.8f);
        add(score);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = Metrics.toGameX(event.getX());
                startY = -Metrics.toGameY(event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                shotPath.reset();
                float curX = Metrics.toGameX(event.getX());
                float curY = -Metrics.toGameY(event.getY());
                //shotPath.moveTo(startX/ 120, -startY / 120);
                shotPath.moveTo(4.5f, 14.f);
                float slope = (startY - curY) / (startX - curX);
                //착탄지 정확하게 계산 필요
                float xPos = curX - 4.5f;
                float yPos = 14.f - slope * xPos;
                shotPath.lineTo(xPos, yPos);
                return true;

            case MotionEvent.ACTION_UP:
                float endX = Metrics.toGameX(event.getX());
                float endY = -Metrics.toGameY(event.getY());
                float direction = (startY - endY) / (startX - endX);
                bobbleMgr.shotBobble(direction);
                shotPath.reset();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        canvas.drawPath(shotPath, paint);

    }
}
