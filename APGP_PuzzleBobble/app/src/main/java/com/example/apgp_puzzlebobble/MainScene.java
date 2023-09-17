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
    public static Button invenBtn;

    public BobbleManager bobbleMgr;
    public static Score score;

    public static TimeItem timeItem;
    public LimitTimer limitTimer;

    public Arrow arrow;
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
        arrow = new Arrow(R.mipmap.gearsprite,Metrics.game_width/2, 14.5f, 5.f, 5.f);
        add(arrow);
        bobbleMgr = new BobbleManager();
        add(bobbleMgr);
        score =  new Score(R.mipmap.scoresprite, 9.f, 0.f, 0.8f);
        add(score);
        limitTimer = new LimitTimer(R.mipmap.scoresprite, 4.5f, 0.f, 1.f);
        add(limitTimer);


        invenBtn = new Button(R.mipmap.itembobble, Metrics.game_width - 1.f, Metrics.game_height - 1.f, 1.5f, 1.5f);
        invenBtn.setSrcRect();

        //pause
        pauseBtn = new Button(R.mipmap.pausebtn2, 0.5f, 0.5f, 1.f, 1.f);
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
        Log.d("a", "equipItem:" + type);
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
                startX = Metrics.game_width/2;
                startY = 14.f;

                return true;
            case MotionEvent.ACTION_MOVE:
                float curX = Metrics.toGameX(event.getX());
                float curY = -Metrics.toGameY(event.getY());

                //구슬 각도 조절 동작
//                shotPath.reset();
//                float xCenter = Metrics.game_width/2;
//                shotPath.moveTo(xCenter, 14.f);
//                float slope = (startY + curY) / (startX - curX);
//                float xDist = startX  - curX;
//                float xPos = xCenter + (startX - curX);
//                float yPos = 14.f - (slope * (xDist));
//                shotPath.lineTo(xPos, yPos);

                arrow.setAngle(curX, -curY);
                return true;

            case MotionEvent.ACTION_UP:
                float endX = Metrics.toGameX(event.getX());
                float endY = -Metrics.toGameY(event.getY());

                if(bGameover) {
                    if (restartBtn.checkTouched(endX, -endY))
                    {
                        Sound.playEffect(R.raw.toucheffect);
                        popScene();
                        MainScene.restart();
                        new MainScene().pushScene();
                    }
                    else if (endBtn.checkTouched(endX, -endY)) {
                        System.exit(0);
                    }
                }

                else if(bPause)
                {
                    if (restartBtn.checkTouched(endX, -endY))
                    {
                        Sound.playEffect(R.raw.toucheffect);
                        popScene();
                        MainScene.restart();
                        new MainScene().pushScene();

                    }
                    else if (endBtn.checkTouched(endX, -endY)) {
                        System.exit(0);
                    }
                    else if(resumeBtn.checkTouched(endX, -endY))
                    {
                        Sound.playEffect(R.raw.toucheffect);
                        bPause = false;
                    }
                }

                else
                {
                    if (invenBtn.checkTouched(endX, -endY)) {
                        //아이템 창 호출
                        Sound.playEffect(R.raw.pauseeffect);
                        new InventoryScene(itemlistMap).pushScene();
                        return true;
                    }

                    else if(pauseBtn.checkTouched(endX, -endY))
                    {
                        Sound.playEffect(R.raw.pauseeffect);
                        bPause = true;
                    }

                    else
                    {

                        float direction = (startY + endY) / -(startX - endX);
                        bobbleMgr.shotBobble(direction);
                        //shotPath.reset();
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
        //canvas.drawPath(shotPath, paint);
        if(timeItem != null)
            timeItem.draw(canvas);

        pauseBtn.draw(canvas);
        invenBtn.draw(canvas);

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
