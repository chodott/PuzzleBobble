package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class MainScene extends BaseScene {
    private static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static boolean bGameover = false;
    public static boolean bPause = false;
    public static Button pauseBtn;

    public BobbleManager bobbleMgr;
    public static Score score;

    public static TimeItem timeItem;
    public LimitTimer limitTimer;
    public EndScreen endScreen;
    public Path shotPath = new Path();
    public static HashMap<ItemType, Integer> itemlistMap = new HashMap<>();

    private Button restartBtn;
    private Button endBtn;

    private Button resumeBtn;
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
        limitTimer = new LimitTimer(R.mipmap.scoresprite, 4.5f, 0.f, 1.f);
        add(limitTimer);

        //pause
        pauseBtn = new Button(R.mipmap.pausebtn, 0.5f, 0.5f, 1.f, 1.f);
        pauseBtn.setSrcRect();

        resumeBtn = new Button(R.mipmap.buttonsprite, Metrics.game_width/2, Metrics.game_height/2, 4.f, 1.f);
        resumeBtn.setSrcRect(2);

        //Game Over관련
        endScreen = new EndScreen();

        restartBtn = new Button(R.mipmap.buttonsprite, Metrics.game_width/2, Metrics.game_height/2 - 1.f, 4.f, 1.f);
        endBtn = new Button(R.mipmap.buttonsprite, Metrics.game_width/2, Metrics.game_height/2 + 1.f, 4.f, 1.f);
        restartBtn.setSrcRect(0);
        endBtn.setSrcRect(1);

        //임의로 추가
    }

    private static void restart()
    {
        bGameover = false;
        bPause = false;
        score.setScore(0);
        itemlistMap.clear();
        BobbleManager.restart();
        Sound.playMusic(R.raw.mainmusic);
    }

    protected void onStart()
    {
        //배경음악 실행
        Sound.playMusic(R.raw.mainmusic);

    }

    protected void onEnd()
    {
        Sound.pauseMusic();
    }

    public void addNewItem(int type)
    {
        ItemType itemType = ItemType.values()[type];
        if(itemlistMap.containsKey(itemType))
            itemlistMap.put(itemType, itemlistMap.get(itemType) + 1);
        else itemlistMap.put(itemType, 1);
    }
    public void equipItem(int type)
    {
        if(type == ItemType.timer.ordinal()) {
            timeItem = new TimeItem();
            timeItem.setPos(Metrics.game_width/2, 1.f);
            timeItem.applyAbility();
            Sound.playMusic(R.raw.ticktock);
        }
        else
            bobbleMgr.equipItem(type);
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

                if(bGameover) {
                    if (restartBtn.checkTouched(startX, -startY))
                    {
                        Sound.playEffect(R.raw.toucheffect);
                        popScene();
                        MainScene.restart();
                        new MainScene().pushScene();
                    }
                    else if (endBtn.checkTouched(startX, -startY)) {
                        System.exit(0);
                    }
                }

                else if(bPause)
                {
                    if (restartBtn.checkTouched(startX, -startY))
                    {
                        Sound.playEffect(R.raw.toucheffect);
                        popScene();
                        MainScene.restart();
                        new MainScene().pushScene();

                    }
                    else if (endBtn.checkTouched(startX, -startY)) {
                        System.exit(0);
                    }
                    else if(resumeBtn.checkTouched(startX, -startY))
                    {
                        Sound.playEffect(R.raw.toucheffect);
                        bPause = false;
                    }
                }

                else {
                    if (-startY > 14.f && -endY < 10.f) {
                        //아이템 창 호출
                        Sound.playEffect(R.raw.pauseeffect);
                        new InventoryScene(itemlistMap).pushScene();
                        return true;
                    }

                    else if(pauseBtn.checkTouched(startX, -startY))
                    {
                        Sound.playEffect(R.raw.pauseeffect);
                        bPause = true;
                    }

                    else
                    {

                        float direction = (startY - endY) / (startX - endX);
                        bobbleMgr.shotBobble(direction);
                        shotPath.reset();
                        return true;
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void update(long elapsedNanos) {
        if(bGameover)
        {
            onEnd();
        }
        else if(bPause)
        {

        }
        else
        {
            super.update(elapsedNanos);
            if(timeItem!= null)
                timeItem.update();
        }


    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        canvas.drawPath(shotPath, paint);
        if(timeItem != null)
            timeItem.draw(canvas);
        pauseBtn.draw(canvas);

        if(bGameover)
        {
            endScreen.draw(canvas);
            restartBtn.draw(canvas);
            endBtn.draw(canvas);
        }

        if(bPause)
        {
            endScreen.draw(canvas);
            restartBtn.draw(canvas);
            endBtn.draw(canvas);
            resumeBtn.draw(canvas);

        }

    }
}
