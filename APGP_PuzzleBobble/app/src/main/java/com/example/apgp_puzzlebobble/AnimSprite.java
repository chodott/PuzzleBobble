package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimSprite extends Sprite{
    protected Rect srcRect = new Rect();
    protected int frameIndex, frameCount;
    protected int frameWidth, frameHeight;
    protected float time, fps;
    public AnimSprite(int bitmapResId, float cx, float cy, float width, float height, float fps, int frameCount)
    {
        super(bitmapResId, cx, cy, width, height);
        this.fps = fps;
        int Width = bitmap.getWidth();
        frameHeight = 5;
        if(frameCount == 0)
        {
            frameWidth = frameHeight;
            this.frameCount = Width/ frameHeight;
        }
        else
        {
            frameWidth = Width/ frameCount;
            this.frameCount = frameCount;
        }
        srcRect.set(0,0, 70, 70);
    }

    @Override
    public void update() {
        super.update();
        time += BaseScene.frameTime;
        int frameIndex = Math.round(time * fps) % frameCount;
        //srcRect.set(frameIndex * frameWidth, 0, (frameIndex + 1) * frameWidth, frameHeight);
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}
