package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Bobble implements IGameObject {
    private static Bitmap bitmap;
    private RectF dstRect = new RectF();
    private float dx, dy;

    public int color;

    public Bobble()
    {
        dstRect.set(0,0,2.5f,2.5f);
    }

    public static void setBitmap(Bitmap bitmap)
    {
        Bobble.bitmap = bitmap;
    }

    public void update()
    {
        if(dx>0) {
            if (dstRect.right > 10.0f) {
                dx = -dx;
            }
        }
        else
        {
            if(dstRect.left <0)
            {
                dx = -dx;
            }
        }
        if(dy>0)
        {
            if(dstRect.bottom > 15.0)
            {
                dy = -dy;
            }
        }
        else
        {
            if(dstRect.top < 0)
            {
                dy = -dy;
            }
        }
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(bitmap, null, dstRect, null);
    }
}
