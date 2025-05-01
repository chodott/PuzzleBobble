package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Button extends Sprite implements InputManager.OnTouchListner
{
    public Rect srcRect = new Rect();
    private Runnable onClickToDo;
    public Button(int bitmapResId, float cx, float cy, float width, float height) {
        super(bitmapResId, cx, cy, width, height);
    }

    public void setSrcRect(int type, boolean Horizon)
    {
        if(Horizon)
        {
            int srcWidth = bitmap.getWidth()/4;
            int srcHeight = bitmap.getHeight();
            srcRect.set(type * srcWidth, 0, (type+1) * srcWidth, srcHeight);
        }
        else
        {
            int srcHeight = bitmap.getHeight();
            srcRect.set(0, type * srcHeight / 3, bitmap.getWidth(), (type + 1) * srcHeight / 3);
        }
    }


    public void setSrcRect()
    {
        srcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    public void setOnClickToDo(Runnable todo)
    {
        onClickToDo = todo;
    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(bitmap,srcRect,dstRect,null);
    }

    @Override
    public boolean onTouch(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if(checkTouched(Metrics.toGameX(event.getX()), Metrics.toGameY(event.getY())))
            {
                onClickToDo.run();
                return true;
            }
        }
        return false;
    }

    public boolean checkTouched(float cx, float cy)
    {
        if(cx <= x + width/2 && cx >= x -width/2 && cy <= y + height/2 && cy >= y - height/2) return true;
        else return false;
    }
}
