package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

public class Bobble extends AnimSprite{
    private static final float BOBBLE_X = 4.5F;
    private static final float BOBBLE_Y = 12.F;
    private static final float BOBBLE_SIZE = 1.F;
    private float accumulatedTime;
    private static Bitmap bitmap = BitmapFactory.decodeResource(GameView.res, R.mipmap.bobblesprite);
    private RectF dstRect = new RectF();
    private float dx, dy;

    public ArrayList<Integer> parentsBobbleNum;

    public int color;

    public Bobble()
    {
        super(R.mipmap.bobblesprite, 4.5f, 4.5f, 1.f, 1.f, 1.955f, 0);
        Random random = new Random();
        color = random.nextInt(4);
        dstRect.set(0,0,1.f,1.f);
        if(bitmap == null)
        {
            setBitmap();
        }
    }

    public static void setBitmap()
    {
        Bobble.bitmap = BitmapFactory.decodeResource(GameView.res, R.mipmap.bobblesprite);
    }

    public void update()
    {
        super.update();
    }
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
    }
}
