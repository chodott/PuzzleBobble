package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;

public class BobbleManager implements IGameObject {

    public static int nextNum = 0;
    public HashMap<Integer, Bobble> bobbleMap = new HashMap<>();
    public ArrayList<Integer> popTargetBobbles;
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

    //충돌 발생시 이벤트
    void checkBobble(int bobbleNum)
    {
        Bobble bb = FindBobble(bobbleNum);
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
        if(popTargetBobbles.size() < 3) return;
        for(int i : popTargetBobbles)
        {
            for(int j : FindBobble(i).parentsBobbleNum)
            {
                FindBobble(j).parentsBobbleNum.remove(j);
            }
        }
    }

    Bobble FindBobble(int num)
    {
        return bobbleMap.get(num);
    }

    @Override
    public void update() {
        for(Bobble bb: bobbleMap.values())
        {
            bb.update();
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
