package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Arrow extends AnimSprite {

    private static Sprite arrow;
    private float angle;
    private static int FRAME_COUNT = 12;
    private static int TYPE_COUNT = 1;

    public Arrow(int bitmapResId, float cx, float cy, float width, float height) {
        super(bitmapResId, cx, cy, 4.8f, 3.f, 6.955f, FRAME_COUNT, TYPE_COUNT);
        arrow = new Sprite(R.mipmap.arrow, cx, cy, width, height);

    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);

        canvas.save();
        canvas.rotate(angle, x, y);
        canvas.drawBitmap(arrow.bitmap, null, arrow.dstRect, null);
        canvas.restore();

    }

    @Override
    public void update()
    {

    }

    public void setAngle(float x, float y)
    {
        float dx = x - this.x;
        float dy = y - this.y;
        double radian = Math.atan2(dy, dx);
        float newAngle = (float) Math.toDegrees(radian) + 90.f;

        if(newAngle > angle) frameIndex = (frameIndex + 1) % FRAME_COUNT;
        else
        {
            frameIndex -= 1;
            if(frameIndex < 0) frameIndex = FRAME_COUNT - 1;
        }

        srcRect.set(frameIndex * frameWidth, type * frameHeight, (frameIndex + 1) * frameWidth, (type+1) * frameHeight);
        angle = newAngle;

    }
}
