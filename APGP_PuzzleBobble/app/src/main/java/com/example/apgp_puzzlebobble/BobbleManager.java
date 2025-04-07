package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class BobbleManager implements IGameObject {

    public static final int LIST_WIDTH = 9;
    public static final int LIST_HEIGHT = 15;
    public static Bobble[][] bobbleArray = new Bobble[LIST_HEIGHT][LIST_WIDTH];
    private static boolean bOddNumber = false;
    public static int nextNum = 0;
    public static HashMap<Integer, Bobble> bobbleMap = new HashMap<>();
    public static ArrayList<Bobble> popTargetBobbles = new ArrayList<>();
    public static ArrayList<Integer> trashList = new ArrayList<>();
    public static int saveNum = 0;
    public static boolean bWaitingAddLine;
    public static Bobble curBobble;
    public static ItemBobble curItem;

    int targetColor;
    private TimeTracker timeTracker;

    BobbleManager()
    {
        addBobbleLine();
        addNewBobble();
        timeTracker = new TimeTracker();
    }

    public static void restart()
    {
        nextNum = 0;
        bobbleMap.clear();
        popTargetBobbles.clear();
        trashList.clear();
        bWaitingAddLine = false;
        curItem = null;
    }


    void addNewBobble()
    {
        //발사할 구슬 생성
        curBobble = new Bobble()
                .setPos(Metrics.game_width/2, 17.0f);
    }

    static void addBobbleLine()
    {
        //CUSTOM
        for(int i=LIST_HEIGHT-2;i>=0;--i)
        {
            for(int j=0; j < LIST_WIDTH; ++j) {
                Bobble bb = bobbleArray[i][j];
                if (bb != null)
                {
                    bb.setPos(bb.x, bb.y + 0.8f);
                    bb.row = i + 1;
                }
                bobbleArray[i+1][j] = bobbleArray[i][j];
            }
            //GameOver
        }
        bOddNumber = !bOddNumber;
        int createBobbleCnt = LIST_WIDTH - (bOddNumber? 0:1);
        float firstXpos = bOddNumber? 1.25f:1.75f;
        for(int i=0;i<LIST_WIDTH; ++i)
        {
            if(i<createBobbleCnt)
            {
                bobbleArray[0][i] = new Bobble().setPos(i * 1.0f + firstXpos, 2.4f);
                bobbleArray[0][i].row = 0; bobbleArray[0][i].column = i;
            }
            else bobbleArray[0][i] = null;
        }

    }

    void shotBobble(float direction)
    {
        //발사 효과음 출력
        Sound.playEffect(R.raw.shoteffect);
        if(curItem != null)
        {
            if(!curItem.bUsed) curItem.shot(direction);
        }
        else curBobble.shot(direction);
    }

    void popBobbles(boolean bUseItem)
    {
        if(bUseItem)
        {
            for(Bobble bb : popTargetBobbles)
            {
                bb.burst();
            }

            int score = popTargetBobbles.size() * 10;
            MainScene.score.setScore(MainScene.score.getScore() + score);
        }

        else
        {
            //pop bobble sound
            Sound.playEffect(R.raw.popeffect);
            for(Bobble bb : popTargetBobbles)
            {
                bb.burst();
            }
        }
        popTargetBobbles.clear();
    }

    int bfs(Queue<Pair<Integer,Integer>> nextBobbleQueue, int chainColor)
    {
        int chainCnt = 0;
        while(!nextBobbleQueue.isEmpty())
        {
            Pair<Integer, Integer> pos = nextBobbleQueue.poll();
            if(pos.first <0 || pos.second < 0||
                    pos.first>= LIST_HEIGHT || pos.second >=LIST_WIDTH) continue;

            Bobble target = bobbleArray[pos.first][pos.second];
            if(target == null) continue;
            if(chainColor >= 0 && target.color != chainColor) continue;
            if(target.bChecked) continue;
            if(target.bBurst) continue;
            target.bChecked = true;
            target.bAttached = true;
            chainCnt++;
            nextBobbleQueue.add(new Pair<>(pos.first-1, pos.second));
            nextBobbleQueue.add(new Pair<>(pos.first, pos.second-1));
            nextBobbleQueue.add(new Pair<>(pos.first, pos.second+1));
            nextBobbleQueue.add(new Pair<>(pos.first+1, pos.second));

            boolean bOdd = target.row % 2 ==0? bOddNumber:!bOddNumber;
            if(bOdd)
            {
                nextBobbleQueue.add(new Pair<>(pos.first-1, pos.second-1));
                nextBobbleQueue.add(new Pair<>(pos.first+1, pos.second-1));
            }
            else
            {
                nextBobbleQueue.add(new Pair<>(pos.first+1, pos.second+1));
                nextBobbleQueue.add(new Pair<>(pos.first-1, pos.second+1));
            }
            if(chainColor >= 0) popTargetBobbles.add(target);
        }
        return chainCnt;
    }

    boolean checkChain(Bobble bb)
    {   //충돌 발생시 이벤트
        int chainCnt = 0;
        int chainColor = bb.color;

        Queue<Pair<Integer,Integer>> nextBobbleQueue = new LinkedList<>();
        nextBobbleQueue.add(new Pair<>(bb.row, bb.column));
        chainCnt = bfs(nextBobbleQueue, chainColor);
        uncheckBobble();
        if(chainCnt>=3) return true;
        popTargetBobbles.clear();
        return false;
    }

    public void dropBobble() {
        Queue<Pair<Integer,Integer>> nextBobbleQueue = new LinkedList<>();
        for(int j=0;j<LIST_WIDTH;++j)
        {   //제일 윗줄
            Bobble bb = bobbleArray[0][j];
            if(bb == null) continue;
            bb.bAttached = true;
            nextBobbleQueue.add(new Pair<>(bb.row, bb.column));
        }

        bfs(nextBobbleQueue, -1);

        for(int i=1;i<LIST_HEIGHT;++i)
        {
            for(int j=0;j<LIST_WIDTH;++j)
            {
                Bobble bb = bobbleArray[i][j];
                if(bb == null || bb.bAttached) continue;
                bb.bDestroyed = true;
            }
        }
        uncheckBobble();
    }

    public void makeNewItem(int comboSize)
    {
        BaseScene scene = BaseScene.getTopScene();
        MainScene mainscene = (MainScene)scene;
        //콤보 종류
        mainscene.addNewItem(targetColor);
    }

    public void equipItem(int type)
    {
        switch(type)
        {
            default:
                curBobble = new Bobble(type);
                break;
            case 7:
                curItem = new BombItem();
                break;

            case 8:
                curItem = new HorizonBomb();
                break;

            case 9:
                curItem = new VerticalBomb();
                break;

            case 10:
                curItem = new TimeItem();
                break;
        }
        curBobble.setPos(Metrics.game_width/2, 17.f);
    }

    @Override
    public void update() {

        for(int i=0;i<LIST_HEIGHT;++i)
        {
            for(Bobble bb : bobbleArray[i])
            {
                if(bb == null) continue;
                bb.update();
            }
        }
        curBobble.update();

        //충돌체크
//        if(curItem != null)
//        {
//            curItem.update();
//            if(curItem == null) return;
//            if(!curItem.bActive) return;
//
//            boolean bHit = false;
//            for(int key: bobbleMap.keySet())
//            {
//                bHit = curItem.checkCollision(bobbleMap.get(key));
//                if (bHit) {
//                    curItem.applyAbility();
//                    popBobbles(true);
//                    break;
//                }
//            }
//        }

        if(curBobble.getAcitve())
        {
            timeTracker.begin();
            boolean bHit = false;
            if(curBobble.y <= 2.25f)
            {
                bHit = true;
                curBobble.y = 2.25f;
            }
            else
            {
                for(int i = LIST_HEIGHT-1; i>=0;--i)
                 {
                    boolean bOdd = (i % 2 ==0) ? !bOddNumber : bOddNumber;
                    for(int j= 0; j<LIST_WIDTH;++j)
                    {

                        if(bobbleArray[i][j] == null) continue;
                        bHit = curBobble.checkCollision(bobbleArray[i][j], bOdd);
                        if(bHit) break;
                    }

                    if(!bHit) continue;
                    bOdd = (curBobble.row % 2 ==0)? bOddNumber: !bOddNumber;
                    float firstXpos = bOdd? 1.25f:1.75f;
                    float firstYpos = 2.4f;
                    curBobble.setPos(curBobble.column * Bobble.BOBBLE_SIZE*2 + firstXpos,
                            curBobble.row * 0.8f + firstYpos);
                    bobbleArray[curBobble.row][curBobble.column] = curBobble;
                    break;
                }
            }

            if(bHit)
            {
                boolean bResult = checkChain(curBobble);
                addNewBobble();
                if(bResult) popBobbles(false);
                dropBobble();
                timeTracker.end();
            }
        }

        else
        {
            if(bWaitingAddLine)
            {
                addBobbleLine();
                bWaitingAddLine = false;
            }

        }

        MainScene.bGameover = checkGameover();
    }

    private boolean checkGameover()
    {
        for(Bobble bb : bobbleMap.values())
        {
            if(bb.y >14.f && !bb.bDestroyed)
            {
                //HighScoreManager.setInt(this);
                timeTracker.log();
                return true;
            }
        }
        return false;
    }

    private void uncheckBobble()

    {
        for(int i=0;i<LIST_HEIGHT; ++i)
        {
            for(Bobble bb : bobbleArray[i])
            {
                if(bb == null) continue;
                bb.bChecked = false;
                bb.bAttached = false;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {

        for(int i=0;i<LIST_HEIGHT;++i)
        {
            for(Bobble bb : bobbleArray[i])
            {
                if(bb==null) continue;
                bb.draw(canvas);
            }
        }
        curBobble.draw(canvas);

        if(curItem != null)
            curItem.draw(canvas);
    }
}
