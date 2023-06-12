package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public abstract class BaseScene {
    private static ArrayList<BaseScene> stack = new ArrayList<>();
    public static float frameTime;

    public  Sound sound = new Sound();
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

    protected abstract void onStart();
    protected abstract void onEnd();

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

    protected ArrayList<IGameObject> gameObjects = new ArrayList<>();
}
