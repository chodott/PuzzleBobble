package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Sprite implements IGameObject{
    protected Bitmap bitmap;
    protected RectF dstRect = new RectF();
    protected float x, y, width, height;
    public Sprite(int bitmapResId, float cx, float cy, float width, float height)
    {
        this.x = cx;
        this.y = cy;
        this.width = width;
        this.height = height;
        if (bitmapResId != 0) {
            this.setBitmapResource(bitmapResId);
        }
        fixDstRect();
    }

    protected void setBitmapResource(int bitmapResId) {
        bitmap = BitmapPool.get(bitmapResId);
        //this.bitmap =  BitmapFactory.decodeResource(GameView.res, bitmapResId);
    }

    @Override
    public void update()
    {
        setDstRect();
    }

    protected void fixDstRect()
    {
        setSize(width, height);
    }

    protected void setDstRect()
    {
        float half_width = width/2;
        float half_height = height/2;
        dstRect.set(x - half_width,y - half_height, x + half_width, y + half_height);

    }

    protected void setSize(float width, float height)
    {
        this.width = width;
        this.height = height;
        float half_width = width/2;
        float half_height = height/2;
        dstRect.set(x - half_width, y - half_height, x+ half_width, y + half_height);
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(this.bitmap, null, dstRect, null);
    }

}
