package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

public class InventoryScene extends BaseScene
{
    private static float speed;
    private float x,y = 0;
    float startX, startY;

    public InventoryScene(ArrayList<Integer> itemList)
    {
        add(new Background(1));

        //Item 획득 목록 전달
        for(int type : itemList)
        {
            add(new Item(5.f, 5.f, type));
        }
    }

    @Override
    public void update(long elapsedNanos) {
        super.update(elapsedNanos);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
                startY = Metrics.toGameY(event.getY());
                Log.d("Inventory", "onTouchEvent: ");
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float endY = Metrics.toGameY(event.getY());
                if(startY < 800.f && endY > 1200.f)
                {
                    popScene();
                }
                break;
        }
        return true;
    }

}
