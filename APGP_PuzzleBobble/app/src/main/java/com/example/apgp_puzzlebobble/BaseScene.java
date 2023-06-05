package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public class BaseScene {
    private static ArrayList<BaseScene> stack = new ArrayList<>();
    public static float frameTime;
    public static BaseScene getTopScene()
    {
        return stack.get(stack.size()-1);
    }
    public int pushScene()
    {
        stack.add(this);
        return stack.size();
    }

    public void popScene()
    {
        stack.remove(this);
    }
    public int add(IGameObject gobj)
    {
        gameObjects.add(gobj);
        return gameObjects.size();
    }

    public void update(long elapsedNanos)
    {
        frameTime = elapsedNanos/ 1_000_000_000f;
        for(IGameObject gobj:gameObjects)
        {
            gobj.update();
        }
    }
    public void draw(Canvas canvas)
    {
        for (IGameObject gobj :gameObjects)
        {
            gobj.draw(canvas);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private ArrayList<IGameObject> gameObjects = new ArrayList<>();
}
