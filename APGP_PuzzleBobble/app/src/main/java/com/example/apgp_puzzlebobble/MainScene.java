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
    private InventoryScene inventoryScene;
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

    private Button restartBtn;
    private Button endBtn;

    private Button resumeBtn;

    private Button LeftArrowBtn;
    private Button RightArrowBtn;
    private Button UpArrowBtn;
    private Button DownArrowBtn;

    private InputManager inputManager = new InputManager();

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
        arrow = new Arrow(R.mipmap.gearsprite,Metrics.game_width/2, 18.0f, 5.f, 5.f);
        add(arrow);
        bobbleMgr = new BobbleManager();
        add(bobbleMgr);
        score =  new Score(R.mipmap.scoresprite, Metrics.game_width, 0.f, 0.7f);
        add(score);
        limitTimer = new LimitTimer(R.mipmap.scoresprite, Metrics.game_width/2 + 0.8f, 0.f, 0.8f);
        add(limitTimer);

        //인벤토리는 좀 미루자
        invenBtn = new Button(R.mipmap.itembobble, Metrics.game_width - 1.f, Metrics.game_height - 1.f, 1.5f, 1.5f);
        inputManager.addListener(invenBtn);
        invenBtn.setSrcRect();
        invenBtn.setOnClickToDo(this::openInventory);

        //pause
        pauseBtn = new Button(R.mipmap.pausebtn2, 0.75f, 0.75f, 1.5f, 1.5f);
        pauseBtn.setOnClickToDo(this::onPause);
        pauseBtn.setSrcRect();
        inputManager.addListener((pauseBtn));


        //Resume
        resumeBtn = new Button(R.mipmap.buttonsprite, Metrics.game_width/2, Metrics.game_height/2, 4.f, 1.f);
        resumeBtn.setOnClickToDo(this::onResume);
        resumeBtn.setSrcRect(2, false);
        inputManager.addListener((resumeBtn));

        //Game Over관련
        endScreen = new EndScreen();
        alphabetName = new Alphabet(R.mipmap.namesprite, Metrics.game_width/2 + 1.2f, Metrics.game_height/2 - 0.4f, 0.8f);

        restartBtn = new Button(R.mipmap.buttonsprite, Metrics.game_width/2, Metrics.game_height/2 - 2.2f, 4.f, 1.f);
        restartBtn.setSrcRect(0, false);
        restartBtn.setOnClickToDo(this::restart);
        inputManager.addListener((restartBtn));

        endBtn = new Button(R.mipmap.buttonsprite, Metrics.game_width/2, Metrics.game_height/2 + 2.2f, 4.f, 1.f);
        endBtn.setSrcRect(1, false);
        endBtn.setOnClickToDo(this::onEnd);
        inputManager.addListener((endBtn));

        LeftArrowBtn = new Button(R.mipmap.arrowbtnsprite, Metrics.game_width/2 - 2.f, Metrics.game_height/2, 1.f, 1.f);
        LeftArrowBtn.setSrcRect(1, true);

        RightArrowBtn = new Button(R.mipmap.arrowbtnsprite, Metrics.game_width/2 + 2.f, Metrics.game_height/2, 1.f, 1.f);
        RightArrowBtn.setSrcRect(0, true);

        UpArrowBtn = new Button(R.mipmap.arrowbtnsprite, Metrics.game_width/2, Metrics.game_height/2 - 1.f, 1.f, 1.f);
        UpArrowBtn.setSrcRect(2, true);

        DownArrowBtn = new Button(R.mipmap.arrowbtnsprite, Metrics.game_width/2, Metrics.game_height/2 + 1.f, 1.f, 1.f);
        DownArrowBtn.setSrcRect(3, true);

        inventoryScene = new InventoryScene();
        inventoryScene.pushScene();
    }

    public void restart()
    {
        Sound.playEffect(R.raw.toucheffect);
        onEnd();
        popScene();
        bGameover = false;
        bPause = false;
        score.setScore(0);
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

            int curScore = score.getScore();
            //Rank
            int saveScore;
            for (int i = 0; i < 10; ++i) {
                int rank = i + 1;
                saveScore = HighScoreManager.getInt(MainActivity.mContext, Integer.toString(rank));
                if (saveScore < curScore) {
                    int curRank = rank;
                    for (int j = 10; j > curRank; --j) {
                        String string_rank = Integer.toString(j);
                        String string_newRank = Integer.toString(j - 1);

                        HighScoreManager.setInt(MainActivity.mContext, string_rank, HighScoreManager.getInt(MainActivity.mContext, string_newRank));
                        HighScoreManager.setString(MainActivity.mContext, string_rank +".", HighScoreManager.getString(MainActivity.mContext, string_newRank + "."));
                    }
                    HighScoreManager.setInt(MainActivity.mContext, Integer.toString(curRank), curScore);
                    HighScoreManager.setString(MainActivity.mContext,Integer.toString(curRank) + ".", alphabetName.getName() );
                    break;

                }
            }
            bSaveRank = true;
        }
    }

    public void onPause()
    {
        super.onPause();
        Sound.playEffect(R.raw.pauseeffect);
        bPause = true;
    }

    public void OnResume()
    {
        Sound.playEffect(R.raw.toucheffect);
        bPause = false;
    }

    public void addNewItem(int type)
    {
        inventoryScene.addItem(type,1);
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

    public void MoveNameCursor(int type)
    {
        switch(type) {
            case 0: //left Btn
                if (nameNum > 0) {
                    DownArrowBtn.x -= 0.8f;
                    UpArrowBtn.x -= 0.8f;
                    nameNum--;
                    DownArrowBtn.setDstRect();
                    UpArrowBtn.setDstRect();
                }
                break;

            case 1: //rightBtn
                if (nameNum < 2) {
                    DownArrowBtn.x += 0.8f;
                    UpArrowBtn.x += 0.8f;
                    nameNum++;
                    DownArrowBtn.setDstRect();
                    UpArrowBtn.setDstRect();
                }
            case 2: //DownBtn
            {
                char ch[] = new char[3];
                alphabetName.getName().getChars(0, 3, ch, 0);
                ch[nameNum] += 1;

                if (ch[nameNum] >= 91) {
                    ch[nameNum] = 'A';
                }
                String newName = new String(ch);
                alphabetName.setName(newName);
                break;
            }
            case 3: //UpBtn
            {
                char ch[] = new char[3];
                alphabetName.getName().getChars(0, 3, ch, 0);
                ch[nameNum] -= 1;

                if(ch[nameNum] <= 64)
                {
                    ch[nameNum] = 'Z';
                }

                String newName = new String(ch);
                alphabetName.setName(newName);
            }
        }

    }

    public void openInventory()
    {
        if(!BobbleManager.curBobble.getAcitve()) {
            Sound.playEffect(R.raw.pauseeffect);
            swapScene();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(inputManager.touchEvent(event)) return true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = Metrics.game_width/2;
                startY = 17.f;

                return true;
            case MotionEvent.ACTION_MOVE:
                float curX = Metrics.toGameX(event.getX());
                float curY = -Metrics.toGameY(event.getY());

                arrow.setAngle(curX, -curY);
                return true;

            case MotionEvent.ACTION_UP:
                float endX = Metrics.toGameX(event.getX());
                float endY = -Metrics.toGameY(event.getY());
                float direction = (startY + endY) / -(startX - endX);
                bobbleMgr.shotBobble(direction);
                //shotPath.reset();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void update(long elapsedNanos) {
        if(!bGameover && !bPause)
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
