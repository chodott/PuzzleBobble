package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class Alphabet implements IGameObject{
    private final Bitmap bitmap;
    private final int srcWidth, srcHeight;
    private final float dstWidth, dstHeight;
    private float right, top;

    private float padX = 0.f;
    private final Rect srcRect = new Rect();
    private final RectF dstRect = new RectF();
    private String name = "EMP";
    private int displayScore=0;

    public Alphabet(int mipmapResId, float right, float top, float width)
    {
        this.bitmap = BitmapPool.get(mipmapResId);
        this.right = right;
        this.top = top;
        this.srcWidth = bitmap.getWidth() / 26;
        this.srcHeight = bitmap.getHeight();
        this.dstWidth = width;
        this.dstHeight = dstWidth * srcHeight / srcWidth;
        this.name = "EMP";
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void movePos(float right , float top)
    {
        this.right = right;
        this.top = top;
    }

    public String getName()
    {
        return name;
    }
    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        char[] ch = new char[3];
        name.getChars(0,3, ch, 0);
        float x = right;
        for(int i=2;i>=0;--i)
        {
           int num = (ch[i]) - 65;
           int leftPos = num * srcWidth;
           srcRect.set(leftPos, 0, leftPos + srcWidth, srcHeight);
           x -= dstWidth;
           dstRect.set(x, top, x + dstWidth, top + dstHeight);
           canvas.drawBitmap(bitmap, srcRect, dstRect, null);
        }
    }
}
