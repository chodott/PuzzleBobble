package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class AnimSprite extends Sprite{
    protected Rect srcRect = new Rect();
    protected int frameIndex, frameCount;
    protected int maxFrame;
    protected int frameWidth, frameHeight;
    protected float time, fps;

    protected boolean bAnimating;
    public AnimSprite(int bitmapResId, float cx, float cy, float width, float height, float fps, int frameCount, int typeCount)
    {
        super(bitmapResId, cx, cy, width, height);
        this.fps = fps;
        int Width = bitmap.getWidth();
        int Height = bitmap.getHeight();
        this.maxFrame = frameCount;
        if(frameCount == 0)
        {
            frameWidth = Width;
            this.frameCount = Width/ Height;
        }
        else
        {
            frameWidth = Width/ frameCount;
            frameHeight = Height/typeCount;
            this.frameCount = frameCount;
        }
        srcRect.set(0,0, frameWidth, frameHeight);
    }


    @Override
    public void update() {
        super.update();
        if(bAnimating) {
            time += BaseScene.frameTime;
            int frameIndex = Math.round(time * fps) % frameCount;
            srcRect.set(frameIndex * frameWidth, 0, (frameIndex + 1) * frameWidth, frameHeight);
            if(maxFrame == frameIndex + 1) bAnimating = false;
        }
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}
