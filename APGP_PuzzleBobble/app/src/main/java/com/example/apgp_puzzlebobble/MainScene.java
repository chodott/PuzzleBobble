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

        //임의로 추가
        itemList.add(0);
        itemList.add(1);
        itemList.add(2);
    }

    protected void onStart()
    {
        //배경음악 실행
        Sound.playMusic(R.raw.mainmusic);
    }

    protected void onEnd()
    {
        Sound.stopMusic();
    }

    public void addNewItem(int type)
    {
        itemList.add(type);
    }
    public void equipItem(int type)
    {
        bobbleMgr.equipItem(type);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = Metrics.toGameX(event.getX());
                startY = -Metrics.toGameY(event.getY());
                Log.d("", "onTouchEvent: " +startX + "y" + startY);
                return true;
            case MotionEvent.ACTION_MOVE:
                float curX = Metrics.toGameX(event.getX());
                float curY = -Metrics.toGameY(event.getY());

                //구슬 각도 조절 동작
                shotPath.reset();
                float xCenter = Metrics.game_width/2;
                shotPath.moveTo(xCenter, 14.f);
                float slope = (startY - curY) / (startX - curX);
                float xDist = startX  - curX;
                float xPos = xCenter + (startX - curX);
                float yPos = 14.f - (slope * (xDist));
                shotPath.lineTo(xPos, yPos);
                return true;

            case MotionEvent.ACTION_UP:
                float endX = Metrics.toGameX(event.getX());
                float endY = -Metrics.toGameY(event.getY());
                Log.d("MainScene", "onTouchEvent: " + startY + " " + endY);
                if(-startY > 14.f && -endY < 10.f)
                {
                    //아이템 창 호출
                    new InventoryScene(itemList).pushScene();
                    Sound.playEffect(R.raw.pauseeffect);
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
