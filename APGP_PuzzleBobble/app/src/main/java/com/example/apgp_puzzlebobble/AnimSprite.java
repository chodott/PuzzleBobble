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
    public int type = 1;

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
        srcRect.set(0,type * frameHeight, frameWidth, (type + 1) * frameHeight);
    }



    @Override
    public void update() {
        super.update();
        if(bAnimating) {
            time += BaseScene.frameTime;
            int frameIndex = Math.round(time * fps) % frameCount;
            srcRect.set(frameIndex * frameWidth, type * frameHeight, (frameIndex + 1) * frameWidth, (type+1) * frameHeight);
            if(maxFrame == frameIndex + 1) bAnimating = false;
        }
        srcRect.set(0,type * frameHeight, frameWidth, (type + 1) * frameHeight);
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}
