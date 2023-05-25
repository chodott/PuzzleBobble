package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class BobbleManager implements IGameObject {

    public static int nextNum = 0;
    public HashMap<Integer, Bobble> bobbleMap = new HashMap<>();
    public ArrayList<Integer> popTargetBobbles = new ArrayList<>();
    public int curBobbleNum;

    BobbleManager()
    {
        addBobbleLine();
        addNewBobble();
    }

    void addNewBobble()
    {
        //발사할 구슬 생성
        curBobbleNum = nextNum;
        addBobble(new Bobble()
                .setPos(4.5f, 14.f));

    }
    void addBobble()
    {
        bobbleMap.put(Integer.valueOf(nextNum++), new Bobble());
    }
    void addBobble(Bobble bobble)
    {
        bobbleMap.put(Integer.valueOf(nextNum++), bobble);
    }

    void addBobbleLine()
    {
        //정해진 개수만큼 반복하면서 추가
        for(int i=0;i<10; ++i)
        {
            addBobble(new Bobble()
                    .setPos(i * 1.f, 3.f));
        }
    }

    void shotBobble(float direction)
    {
        bobbleMap.get(curBobbleNum).shot(direction);
    }


    void checkBobble(int bobbleNum)
    {   //충돌 발생시 이벤트
        Bobble bb = FindBobble(bobbleNum);
        if(bb.parentsBobbleNum.isEmpty()) return;
        for(int bbNum : bb.parentsBobbleNum)
        {
            if(bb.color == FindBobble(bbNum).color)
            {//컬러가 같을 때 부모 계속 추적
                checkBobble(bbNum);
                popTargetBobbles.add(bbNum);
            }
            else return;
        }
    }

    void popBobbles()
    {
        if(popTargetBobbles.size() >= 3)
        {
            for (int i : popTargetBobbles) {
                for (int j : FindBobble(i).parentsBobbleNum) {
                    //FindBobble(j).parentsBobbleNum.remove(j);
                }
                DeleteBobble(i);
            }
            Log.d("success", "popBobbles: ");
        }
        popTargetBobbles.clear();
    }

    Bobble FindBobble(int num)
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

        //충돌체크
        if(FindBobble(curBobbleNum).getAcitve())
        {
            Bobble curbb = FindBobble(curBobbleNum);
            boolean bHit = false;
            for(int key: bobbleMap.keySet())
            {
                if(key != curBobbleNum)
                {
                    boolean bResult = curbb.checkCollision(bobbleMap.get(key));
                    if(bResult)
                    {
                        curbb.parentsBobbleNum.add(key);
                        bHit = true;
                    }
                }
            }
            if(bHit)
            {
                checkBobble(curBobbleNum);
                popTargetBobbles.add(curBobbleNum);
                popBobbles();
                addNewBobble();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {

        for(Bobble bb : bobbleMap.values())
        {
            bb.draw(canvas);
        }
    }
}
