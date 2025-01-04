package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class BobbleManager implements IGameObject {

    public static int nextNum = 0;
    public static HashMap<Integer, Bobble> bobbleMap = new HashMap<>();
    public static ArrayList<Integer> popTargetBobbles = new ArrayList<>();
    public static ArrayList<Integer> trashList = new ArrayList<>();
    public static int saveNum = 0;
    public static boolean bWaitingAddLine;
    public static Bobble curBobble;
    public static ItemBobble curItem;
    public int curBobbleNum;
    private int targetColor;

    //FPS Checker
    FPSCounter fpsCounter = new FPSCounter();

    BobbleManager()
    {
        addBobbleLine();
        addNewBobble();
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
        curBobbleNum = nextNum++;
        curBobble = new Bobble()
                .setPos(Metrics.game_width/2, 17.0f);
    }
    void addBobble()
    {
        bobbleMap.put(Integer.valueOf(nextNum++), new Bobble());
    }
    static void addBobble(Bobble bobble, int num) {
        bobbleMap.put(num, bobble);
        bobble.num = num;
    }
    static void addBobble(Bobble bobble)
    {
        bobbleMap.put(Integer.valueOf(nextNum++), bobble);
    }

    static void addBobbleLine()
    {
        ArrayList<Integer> newbbNum = new ArrayList<>();
        //정해진 개수만큼 반복하면서 추가
        for(int i=0;i<9; ++i)
        {
            int addNum = nextNum++;
            newbbNum.add(addNum);
            Bobble curbb = new Bobble().setPos(i * 1.f + 1.25f, 2.25f);
            addBobble(curbb, addNum);
            if(i > 0)
            {
                curbb.parentsBobbleNum.add(saveNum);
                FindBobble(saveNum).parentsBobbleNum.add(addNum);
            }

            saveNum = addNum;
        }

        for(int bbnum: bobbleMap.keySet())
        {
            Bobble bb = bobbleMap.get(bbnum);
            for(int curbbnum : newbbNum)
            {
                if(curbbnum != bbnum)
                {
                    Bobble curbb = bobbleMap.get(curbbnum);
                    //boolean bOverlap = bb.checkCollision(curbb);
                    boolean bOverlap = false;
                    if(bb.y <= 2.25f && bb.x <= curbb.x + Bobble.BOBBLE_SIZE && bb.x >= curbb.x - Bobble.BOBBLE_SIZE) bOverlap = true;
                    if(bOverlap)
                    {
                        curbb.parentsBobbleNum.add(bbnum);
                        bb.parentsBobbleNum.add(curbbnum);
                    }
                }
            }
            if(!newbbNum.contains(bbnum))
                bb.setPos(bb.x, bb.y + 1.f);
        }

        newbbNum.clear();
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


    void checkBobble(int bobbleNum)
    {   //충돌 발생시 이벤트
        Bobble bb = FindBobble(bobbleNum);
        bb.bChecked = true;
        for(int bbNum : bb.parentsBobbleNum)
        {
            Bobble checkbb = FindBobble(bbNum);
            if(checkbb == null) continue;
            if(checkbb.bChecked) continue;

            checkbb.bChecked = true;
            if(bb.color == checkbb.color)
            {   //컬러가 같을 때 부모 계속 추적
                checkBobble(bbNum);
                popTargetBobbles.add(bbNum);
            }
        }
    }

    void popBobbles(boolean bUseItem)
    {
        if(bUseItem)
        {
            for (int i : popTargetBobbles)
            {
                for (int j : FindBobble(i).parentsBobbleNum)
                {
                    Bobble parentbb = FindBobble(j);
                    if(parentbb != null)
                    {
                        parentbb.parentsBobbleNum.remove((Object)i);
                    }
                }
            }

            for(int i : popTargetBobbles)
            {
                DeleteBobble(i);
            }

            int score = popTargetBobbles.size() * 10;
            MainScene.score.setScore(MainScene.score.getScore() + score);
        }

        else
        {
            int comboSize = popTargetBobbles.size();
            if(comboSize >= 3)
            {
                //pop bobble sound
                Sound.playEffect(R.raw.popeffect);
                for (int i : popTargetBobbles)
                {
                    Bobble targetbb = FindBobble(i);
                    targetColor = targetbb.color;

                    for (int j : targetbb.parentsBobbleNum)
                    {
                        Bobble checkbb = FindBobble(j);
                        if(checkbb != null) {
                            checkbb.parentsBobbleNum.remove((Object) i);
                        }
                    }

                }

                for(int i : popTargetBobbles)
                {
                    DeleteBobble(i);
                }

                int score = comboSize * 10;
                MainScene.score.setScore(MainScene.score.getScore() + score);
                makeNewItem(comboSize);

            }
        }
        popTargetBobbles.clear();
        uncheckBobble();

        //drop 필요
        dropBobble();
        }

    public void dropBobble() {
        for(Bobble bb : bobbleMap.values())
        {
            for(int key : bb.parentsBobbleNum)
            {   //접촉한 Bobble이 있는지 판단
                bb.bAttached = checkAttached(key);
                //해결 코드 - 접촉한 Bobble이 있울 때 반복문 탈출.
                if(bb.bAttached) break;
            }

            //제일 윗줄, 접촉하지 않은 Bobble Drop
            if(!bb.bAttached && bb.y > 2.25f)
            {
               bb.bDestroyed = true;
            }
            //남은 Bobble상태 초기화
            uncheckBobble();
        }
    }

    private boolean checkAttached(int key) {
        Bobble bb = bobbleMap.get(key);
        if(bb == null) return false;
        if(bb.bChecked) return bb.bAttached;

        bb.bChecked = true;
        if(bb.y <= 2.25f || bb.bAttached)
        {
            bb.bAttached = true;
            return true;
        }
        else
        {
            for(int parentKey : bb.parentsBobbleNum)
            {
                if(checkAttached(parentKey))
                {
                    bb.bAttached = true;
                    return true;
                }
            }
        }

        return false;
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

    static Bobble FindBobble(int num)
    {
        return bobbleMap.get(num);
    }

    void DeleteBobble(int num)
    {
        bobbleMap.get(num).burst();
       //bobbleMap.remove(num);
    }

    @Override
    public void update() {
        for(Bobble bb: bobbleMap.values())
        {
            bb.update();
        }
        curBobble.update();

        //충돌체크
        if(curItem != null)
        {
            curItem.update();
            if(curItem == null) return;
            if(!curItem.bActive) return;

            boolean bHit = false;
            for(int key: bobbleMap.keySet())
            {
                bHit = curItem.checkCollision(bobbleMap.get(key));
                if (bHit) {
                    curItem.applyAbility();
                    popBobbles(true);
                    break;
                }
            }
        }

        else if(curBobble.getAcitve())
        {
            boolean bHit = false;
            if(curBobble.y <= 2.25f)
            {
                bHit = true;
                curBobble.y = 2.25f;
            }
            else
            {
                for(int key: bobbleMap.keySet())
                {
                    if(key != curBobbleNum)
                    {
                        boolean bResult = curBobble.checkCollision(bobbleMap.get(key));
                        if(bResult)
                        {
                            curBobble.parentsBobbleNum.add(key);
                            bobbleMap.get(key).parentsBobbleNum.add(curBobbleNum);
                            bHit = true;
                        }
                    }
                }
            }

            if(bHit)
            {
                long startTime = System.nanoTime(); // 시작 시간 기록

                addBobble(curBobble, curBobbleNum);
                checkBobble(curBobbleNum);
                curBobble.setActive(false);
                popTargetBobbles.add(curBobbleNum);
                addNewBobble();
                uncheckBobble();
                popBobbles(false);

                long endTime = System.nanoTime(); // 종료 시간 기록
                long elapsedTime = endTime - startTime; // 실행 시간 계산
                Log.d("Adjacency List Method ", "NanoTime: " + elapsedTime / 1_000_000);

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

        for(int key : trashList)
        {
            bobbleMap.remove(key);
        }
        trashList.clear();


        MainScene.bGameover = checkGameover();
    }

    private boolean checkGameover()
    {
        for(Bobble bb : bobbleMap.values())
        {
            if(bb.y >14.f && !bb.bDestroyed)
            {
                //HighScoreManager.setInt(this);
                return true;
            }
        }
        return false;
    }

    private void uncheckBobble()

    {

        for(Bobble bb : bobbleMap.values())
        {
            bb.bChecked = false;
            bb.bAttached = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {

        for(Bobble bb : bobbleMap.values())
        {
            bb.draw(canvas);
        }
        curBobble.draw(canvas);

        if(curItem != null)
            curItem.draw(canvas);
    }
}
