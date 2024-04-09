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

    private boolean bSaveRank = false;

    public static boolean bGameover = false;
    public static boolean bPause = false;
    public static Button pauseBtn;
    public static Button invenBtn;

    public BobbleManager bobbleMgr;
    public static Score score;
    public static Alphabet alphabetName;

    public static TimeItem timeItem;
    public LimitTimer limitTimer;

    public Arrow arrow;
    public EndScreen endScreen;
    public Path shotPath = new Path();
    public static HashMap<ItemType, Integer> itemlistMap = new HashMap<>();

    private Button restartBtn;
    private Button endBtn;

    private Button resumeBtn;

    private Button LeftArrowBtn;
    private Button RightArrowBtn;
    private Button UpArrowBtn;
    private Button DownArrowBtn;

    public int nameNum = 1;
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
        arrow = new Arrow(R.mipmap.gearsprite,Metrics.game_width/2, 17.0f, 5.f, 5.f);
        add(arrow);
        bobbleMgr = new BobbleManager();
        add(bobbleMgr);
        score =  new Score(R.mipmap.scoresprite, Metrics.game_width, 0.f, 0.7f);
        add(score);
        limitTimer = new LimitTimer(R.mipmap.scoresprite, Metrics.game_width/2 + 0.8f, 0.f, 0.8f);
        add(limitTimer);


        invenBtn = new Button(R.mipmap.itembobble, Metrics.game_width - 1.f, Metrics.game_height - 1.f, 1.5f, 1.5f);
        invenBtn.setSrcRect();

        //pause
        pauseBtn = new Button(R.mipmap.pausebtn2, 0.75f, 0.75f, 1.5f, 1.5f);
        pauseBtn.setSrcRect();

        resumeBtn = new Button(R.mipmap.buttonsprite, Metrics.game_width/2, Metrics.game_height/2, 4.f, 1.f);
        resumeBtn.setSrcRect(2, false);

        //Game Over관련
        endScreen = new EndScreen();
        alphabetName = new Alphabet(R.mipmap.namesprite, Metrics.game_width/2 + 1.2f, Metrics.game_height/2 - 0.4f, 0.8f);

        restartBtn = new Button(R.mipmap.buttonsprite, Metrics.game_width/2, Metrics.game_height/2 - 2.2f, 4.f, 1.f);
        endBtn = new Button(R.mipmap.buttonsprite, Metrics.game_width/2, Metrics.game_height/2 + 2.2f, 4.f, 1.f);
        restartBtn.setSrcRect(0, false);
        endBtn.setSrcRect(1, false);

        LeftArrowBtn = new Button(R.mipmap.arrowbtnsprite, Metrics.game_width/2 - 2.f, Metrics.game_height/2, 1.f, 1.f);
        RightArrowBtn = new Button(R.mipmap.arrowbtnsprite, Metrics.game_width/2 + 2.f, Metrics.game_height/2, 1.f, 1.f);
        UpArrowBtn = new Button(R.mipmap.arrowbtnsprite, Metrics.game_width/2, Metrics.game_height/2 - 1.f, 1.f, 1.f);
        DownArrowBtn = new Button(R.mipmap.arrowbtnsprite, Metrics.game_width/2, Metrics.game_height/2 + 1.f, 1.f, 1.f);
        LeftArrowBtn.setSrcRect(1, true);
        RightArrowBtn.setSrcRect(0, true);
        UpArrowBtn.setSrcRect(2, true);
        DownArrowBtn.setSrcRect(3, true);

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
        if(!bSaveRank)
        {
            Sound.pauseMusic();

            int highScore = HighScoreManager.getInt(MainActivity.mContext, "High");
            int curScore = score.getScore();
            if (curScore > highScore)
                HighScoreManager.setInt(MainActivity.mContext, "High", score.getScore());

            //Rank
            int saveScore;
            int saveRank;
            for (int i = 0; i < 10; ++i) {
                int rank = i + 1;
                saveScore = HighScoreManager.getInt(MainActivity.mContext, Integer.toString(rank));
                if (saveScore < curScore) {
                    saveRank = rank;
                    for (int j = 10; j > saveRank; --j) {
                        String string_rank = Integer.toString(j);
                        String string_newRank = Integer.toString(j - 1);

                        HighScoreManager.setInt(MainActivity.mContext, string_rank, HighScoreManager.getInt(MainActivity.mContext, string_newRank));
                        HighScoreManager.setString(MainActivity.mContext, string_rank +".", HighScoreManager.getString(MainActivity.mContext, string_newRank + "."));
                    }
                    HighScoreManager.setInt(MainActivity.mContext, Integer.toString(saveRank), curScore);
                    HighScoreManager.setString(MainActivity.mContext,Integer.toString(saveRank) + ".", alphabetName.getName() );
                    break;

                }
            }

            bSaveRank = true;
        }
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
                startY = 17.f;

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
                        onEnd();
                        popScene();
                        MainScene.restart();
                        new MainScene().pushScene();
                    }
                    else if (endBtn.checkTouched(endX, -endY)) {
                        onEnd();
                        System.exit(0);
                    }

                    else if(RightArrowBtn.checkTouched(endX, -endY))
                    {
                        if(nameNum <2)
                        {
                            DownArrowBtn.x += 0.8f;
                            UpArrowBtn.x += 0.8f;
                            nameNum++;
                            DownArrowBtn.setDstRect();
                            UpArrowBtn.setDstRect();
                        }
                        return true;
                    }

                    else if(LeftArrowBtn.checkTouched(endX, -endY))
                    {
                        if(nameNum>0) {
                            DownArrowBtn.x -= 0.8f;
                            UpArrowBtn.x -= 0.8f;
                            nameNum--;
                            DownArrowBtn.setDstRect();
                            UpArrowBtn.setDstRect();
                        }
                        return true;
                    }

                    else if(DownArrowBtn.checkTouched(endX, -endY))
                    {
                        //글자 다운
                        char ch[] = new char[3];
                        alphabetName.getName().getChars(0, 3, ch, 0);
                        ch[nameNum] += 1;

                        if(ch[nameNum] >= 91)
                        {
                            ch[nameNum] = 'A';
                        }
                        String newName = new String(ch);
                        alphabetName.setName(newName);
                        return true;
                    }

                    else if(UpArrowBtn.checkTouched(endX, -endY))
                    {
                        //글자 업
                        char ch[] = new char[3];
                        alphabetName.getName().getChars(0, 3, ch, 0);
                        ch[nameNum] -= 1;

                        if(ch[nameNum] <= 64)
                        {
                            ch[nameNum] = 'Z';
                        }

                        String newName = new String(ch);
                        alphabetName.setName(newName);
                        return true;
                    }
                }

                else if(bPause)
                {
                    if (restartBtn.checkTouched(endX, -endY))
                    {
                        Sound.playEffect(R.raw.toucheffect);
                        onEnd();
                        popScene();
                        MainScene.restart();
                        new MainScene().pushScene();

                    }
                    else if (endBtn.checkTouched(endX, -endY)) {
                        onEnd();
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
                        if(!BobbleManager.curBobble.getAcitve()) {
                            Sound.playEffect(R.raw.pauseeffect);
                            new InventoryScene(itemlistMap).pushScene();
                        }
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
            score.draw(canvas);
            alphabetName.draw(canvas);

            LeftArrowBtn.draw(canvas);
            RightArrowBtn.draw(canvas);
            UpArrowBtn.draw(canvas);
            DownArrowBtn.draw(canvas);
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
