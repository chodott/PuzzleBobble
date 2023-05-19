package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;

public class BobbleManager implements IGameObject {

    public static int nextNum = 0;
    public HashMap<Integer, Bobble> BobbleMap = new HashMap<>();
    public ArrayList<Integer> popTargetBobbles;
    public int curBobbleNum;

    BobbleManager()
    {
        addBobbleLine();
    }
    void addBobble()
    {

        BobbleMap.put(Integer.valueOf(nextNum++), new Bobble());
    }

    void addBobbleLine()
    {
        //정해진 개수만큼 반복하면서 추가
        for(int i=0;i<10; ++i)
        {
            addBobble();
        }
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
        return BobbleMap.get(num);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {

        for(Bobble bb : BobbleMap.values())
        {
            bb.draw(canvas);
        }
    }
}
