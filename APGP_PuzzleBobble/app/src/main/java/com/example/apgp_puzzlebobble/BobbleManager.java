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

    public Bobble curBobble;
    public ItemBobble curItem;
    public int curBobbleNum;

    BobbleManager()
    {
        addBobbleLine();
        addNewBobble();
    }

    void addNewBobble()
    {
        //발사할 구슬 생성
        curBobbleNum = nextNum++;
        curBobble = new Bobble()
                .setPos(Metrics.game_width/2, 14.f);
    }
    void addBobble()
    {
        bobbleMap.put(Integer.valueOf(nextNum++), new Bobble());
    }
    static void addBobble(Bobble bobble, int num) {bobbleMap.put(num, bobble);}
    static void addBobble(Bobble bobble)
    {
        bobbleMap.put(Integer.valueOf(nextNum++), bobble);
    }

    static void addBobbleLine()
    {
        ArrayList<Integer> newbbNum = new ArrayList<>();
        //정해진 개수만큼 반복하면서 추가
        for(int i=0;i<8; ++i)
        {
            newbbNum.add(BobbleManager.nextNum);
            Bobble curbb = new Bobble().setPos(i * 1.f + 1.f, 2.f);
            addBobble(curbb);
            if(i > 0)
            {
                curbb.parentsBobbleNum.add(nextNum - 2);
                FindBobble(nextNum-2).parentsBobbleNum.add(nextNum - 1);
            }

        }

        for(int bbnum: bobbleMap.keySet())
        {
            Bobble bb = bobbleMap.get(bbnum);
            for(int curbbnum : newbbNum)
            {
                if(curbbnum != bbnum)
                {
                    Bobble curbb = bobbleMap.get(curbbnum);
                    boolean bOverlap = bb.checkCollision(curbb);
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
        if(curItem != null) curItem.shot(direction);
        else curBobble.shot(direction);
    }


    void checkBobble(int bobbleNum)
    {   //충돌 발생시 이벤트
        Bobble bb = FindBobble(bobbleNum);
        if(bb == null) return;
        if(bb.parentsBobbleNum.isEmpty()) return;
        for(int bbNum : bb.parentsBobbleNum)
        {
            Bobble checkbb = FindBobble(bbNum);
            if(checkbb == null) return;
            if(checkbb.bChecked) return;

            checkbb.bChecked = true;
            if(bb.color == checkbb.color)
            {
                //컬러가 같을 때 부모 계속 추적
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
                    //FindBobble(j).parentsBobbleNum.remove(j);
                }
                FindBobble(i).pop();
            }

        }
        else
        {
            int comboSize = popTargetBobbles.size();
            if(comboSize >= 3)
            {
                for (int i : popTargetBobbles) {
//                    for (int j : FindBobble(i).parentsBobbleNum) {
//                        FindBobble(j).parentsBobbleNum.remove(j);
//                    }
                    DeleteBobble(i);
                }

                //pop bobble sound
                //Sound.playEffect(R.raw);
            }
            makeNewItem(comboSize);

        }
         popTargetBobbles.clear();
        }

    public void makeNewItem(int comboSize)
    {
        BaseScene scene = BaseScene.getTopScene();
        MainScene mainscene = (MainScene)scene;
        //콤보 종류
        mainscene.addNewItem(0);
    }

    public void equipItem(int type)
    {
        switch(type)
        {
            case 0:
                curBobble = new Bobble();
                break;
            case 1:
                curItem = new BombItem();
                break;

            case 2:
                curItem = new TimeItem();
                break;
        }
        curBobble.setPos(Metrics.game_width/2, 14.f);
    }

    static Bobble FindBobble(int num)
    {
        return bobbleMap.get(num);
    }

    void DeleteBobble(int num)
    {
        bobbleMap.remove(num);
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
            if(!curItem.bActive) return;
            boolean bHit = false;
            for(int key: bobbleMap.keySet())
            {
                bHit = curItem.checkCollision(bobbleMap.get(key));
                if (bHit) {
                    curItem.applyAbility();
                    break;
                }
            }
            popBobbles(true);
        }

        else if(curBobble.getAcitve())
        {
            boolean bHit = false;
            for(int key: bobbleMap.keySet())
            {
                if(key != curBobbleNum)
                {
                    boolean bResult = curBobble.checkCollision(bobbleMap.get(key));
                    if(bResult)
                    {
                        curBobble.parentsBobbleNum.add(key);
                        bHit = true;
                    }
                }
            }
            if(bHit)
            {
                addBobble(curBobble, curBobbleNum);
                checkBobble(curBobbleNum);
                popTargetBobbles.add(curBobbleNum);
                addNewBobble();
            }
            uncheckBobble();
            popBobbles(false);
        }
    }

    private void uncheckBobble() {
        for(Bobble bb : bobbleMap.values()) bb.bChecked = false;
    }

    @Override
    public void draw(Canvas canvas) {

        for(Bobble bb : bobbleMap.values())
        {
            bb.draw(canvas);
        }
        curBobble.draw(canvas);
        if(curItem != null) curItem.draw(canvas);
    }
}
