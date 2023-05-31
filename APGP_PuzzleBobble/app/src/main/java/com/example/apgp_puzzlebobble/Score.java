package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class Score implements IGameObject{
    private final Bitmap bitmap;
    private final int srcWidth, srcHeight;
    private final float dstWidth, dstHeight;
    private final float right, top;
    private final Rect srcRect = new Rect();
    private final RectF dstRect = new RectF();
    private int score= 1000;

    public Score(int mipmapResId, float right, float top, float width)
    {
        this.bitmap = BitmapFactory.decodeResource(GameView.res, mipmapResId);
        this.right = right;
        this.top = top;
        this.srcWidth = bitmap.getWidth() / 10;
        this.srcHeight = bitmap.getHeight();
        this.dstWidth = width;
        this.dstHeight = dstWidth * srcHeight / srcWidth;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public int getScore()
    {
        return score;
    }
    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        int value = this.score;
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
