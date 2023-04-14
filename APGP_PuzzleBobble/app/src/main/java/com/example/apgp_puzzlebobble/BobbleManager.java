package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;

public class BobbleManager implements IGameObject {

    public static int nextNum = 0;
    public HashMap<Integer, Bobble> BobbleMap;

    void addBobble()
    {
        BobbleMap.put(nextNum++, new Bobble());
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

    }
}
