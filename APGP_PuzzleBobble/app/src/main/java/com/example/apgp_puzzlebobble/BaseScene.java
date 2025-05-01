package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public abstract class BaseScene {

    public static float frameTime;

    public  Sound sound = new Sound();
    public static BaseScene getTopScene()
    {
        return stack.get(stack.size()-1);
    }

    private static ArrayList<BaseScene> stack = new ArrayList<>();
    public int pushScene()
    {
        stack.add(this);
        return stack.size();
    }

    public void popScene()
    {
        stack.remove(this);
    }

    protected abstract void onStart();
    protected abstract void onPause();
    protected abstract void onEnd();

    protected ArrayList<IGameObject> gameObjects = new ArrayList<>();
    public void add(IGameObject gobj)
    {
        gameObjects.add(gobj);
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

    public void onPause()
    {
        Sound.pauseMusic();
    }

    public void onResume()
    {
        Sound.resumeMusic();
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
