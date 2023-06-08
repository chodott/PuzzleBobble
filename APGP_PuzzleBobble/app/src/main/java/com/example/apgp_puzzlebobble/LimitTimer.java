package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class LimitTimer implements IGameObject{
    private final Bitmap bitmap;
    private final int srcWidth, srcHeight;
    private final float dstWidth, dstHeight;
    private final float right, top;
    private final Rect srcRect = new Rect();
    private final RectF dstRect = new RectF();
    private int limitTime= 60;
    private float runningTime = 0;

    public LimitTimer(int mipmapResId, float right, float top, float width)
    {
        this.bitmap = BitmapFactory.decodeResource(GameView.res, mipmapResId);
        this.right = right;
        this.top = top;
        this.srcWidth = bitmap.getWidth() / 10;
        this.srcHeight = bitmap.getHeight();
        this.dstWidth = width;
        this.dstHeight = dstWidth * srcHeight / srcWidth;
    }

    public void setLimitTime(int limitTime) {this.limitTime = limitTime;}
    public int getLimitTime(){ return limitTime;}

    @Override
    public void update() {
        runningTime += BaseScene.frameTime;
    }

    @Override
    public void draw(Canvas canvas) {
        int value = this.limitTime - (int)runningTime;
        float x = right;
        while(value > 0)
        {
            int digit = value % 10;
            int leftPos = digit * srcWidth;
            srcRect.set(leftPos, 0, leftPos + srcWidth, srcHeight);
            x -= dstWidth;
            dstRect.set(x, top, x + dstWidth, top + dstHeight);
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            value /= 10;
        }
    }
}
