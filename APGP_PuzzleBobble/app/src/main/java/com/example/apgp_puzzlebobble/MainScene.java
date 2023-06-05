package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

public class MainScene extends BaseScene {
    private static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BobbleManager bobbleMgr;
    public Score score;
    public Path shotPath = new Path();
    public ArrayList<Integer> itemList = new ArrayList<Integer>();
    float startX, startY;
    static
    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.1f);
        paint.setColor(Color.MAGENTA);
    }
    public MainScene()
    {
        add(new Background(0));
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
                float curX = Metrics.toGameX(event.getX());
                float curY = -Metrics.toGameY(event.getY());
                //구슬 각도 조절 동작
                shotPath.reset();
                shotPath.moveTo(4.5f, 14.f);
                float slope = (startY - curY) / (startX - curX);
                curX /= 120.f;
                float xDist = startX / 120.f - curX;
                float xPos = 4.5f + (startX / 120.f - curX);
                if (curX < 4.5f) slope *= -1;
                float yPos = 14.f - (slope * (xDist));
                shotPath.lineTo(xPos, yPos);
                return true;

            case MotionEvent.ACTION_UP:
                float endX = Metrics.toGameX(event.getX());
                float endY = -Metrics.toGameY(event.getY());
                Log.d("MainScene", "onTouchEvent: " + startY + " " + endY);
                if(-startY > 1640.f && -endY < 1040.f)
                {
                    //아이템 창 호출
                    new InventoryScene(itemList).pushScene();
                    return true;
                }
                else
                {

                    float direction = (startY - endY) / (startX - endX);
                    bobbleMgr.shotBobble(direction);
                    shotPath.reset();
                    return true;
                }
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
