package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Bobble extends AnimSprite{
    private static final float BOBBLE_X = 4.5F;
    private static final float BOBBLE_Y = 12.F;
    private static final float BOBBLE_SIZE = 0.5F;
    private static float dropSpeed = 5.f;
    private static Bitmap bitmap;
    private static Random random;

    private static float speed = 5.f;
    private float xShotSpeed;
    private float yShotSpeed;

    private float accumulatedTime;
    private float direction;

    //private RectF dstRect = new RectF();

    public ArrayList<Integer> parentsBobbleNum = new ArrayList<>();

    public int color;
    private boolean bDestroyed;
    public boolean bActive;

    public Bobble()
    {
        super(R.mipmap.bobblesprite, 4.5f, 4.5f, 1.f, 1.f, 1.955f, 0);
        if(random == null) random = new Random();
        color = random.nextInt(3);
        if(bitmap == null)
        {
            setBitmap();
        }
    }
    public static void setBitmap()
    {
        bitmap = BitmapFactory.decodeResource(GameView.res, R.mipmap.bobblesprite);
    }

    public Bobble setPos(float xPos, float yPos)
    {
        x = xPos;
        y = yPos;
        setDstRect();
        return this;
    }

    public boolean getAcitve()
    {
        return bActive;
    }

    public void setActive(boolean b)
    {
        bActive = b;
    }

    public boolean checkCollision(Bobble target)
    {
        double distance = Math.sqrt(Math.pow(x - target.x,2) + Math.pow(y - target.y,2));
        if(BOBBLE_SIZE * 2 > distance)
        {
            setActive(false);
            return true;
        }
        return false;
    }

    public void update()

    {
        super.update();
        float frameTime = BaseScene.frameTime;
        if(bDestroyed)
        {
            y -= dropSpeed * frameTime;

            //삭제 조건
            if(y <= 0.f)
            {

            }
        }
        else if(bActive)
        {
            x += xShotSpeed * frameTime * speed;
            y -= yShotSpeed * frameTime * speed;
            if(x <= 0.f || x >= 9.f)
            {
                xShotSpeed *= -1.f; //방향 변경
            }
        }

        srcRect.set(0,color * 70,70, color * 70 + 70);
    }
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
    }

    public void shot(float direction)
    {
        this.direction = direction;
        double angleRadians = Math.atan(direction);
        double angleDegrees = Math.toDegrees(angleRadians);
        Log.d("shot", "Degree: " + angleDegrees);

        if(direction < 0.f)
        {
            xShotSpeed = -(float) Math.cos(angleRadians);
            yShotSpeed = -(float)Math.tan(angleRadians);

        }
        else
        {
            xShotSpeed = (float) Math.cos(angleRadians);
            yShotSpeed = (float)Math.tan(angleRadians);
        }

        //yShotSpeed = direction * xShotSpeed;
        bActive = true;
        Log.d("shot", "xspeed: " + yShotSpeed);
        Log.d("shot", "yspeed: " + xShotSpeed);

    }
}


