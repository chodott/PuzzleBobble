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
    private static final float BOBBLE_SIZE = 0.5F;
    private static float dropSpeed = 5.f;
    private static Random random;

    private static float speed = 10.f;

    private static int COLOR_COUNT = 3;
    private float xShotSpeed;
    private float yShotSpeed;

    private float accumulatedTime;
    private float direction;


    public ArrayList<Integer> parentsBobbleNum = new ArrayList<>();

    public int color;
    private boolean bDestroyed;
    public boolean bActive;

    public boolean bChecked =false;
    public Bobble()
    {
        super(R.mipmap.bobblesprite, 4.5f, 4.5f, 1.f, 1.f, 1.955f, 7, COLOR_COUNT);
        if(random == null) random = new Random();
        color = random.nextInt(3);
        type = color;
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
        float targetX = target.x;
        float targetY = target.y;
        double distance = Math.sqrt(Math.pow(x - target.x,2) + Math.pow(y - target.y,2));
        if(distance == 0.f)
        {
            Log.d("checkk", "checkCollision: " + targetX + "y" + targetY);
            return true;
        }
        if(BOBBLE_SIZE * 2 > distance)
        {
            bAnimating = true;
            setActive(false);

            float gap = 1.f- (float)distance;
            float slope = (targetY - y) / (targetX - x);

            double angleRadians = Math.atan(slope);
            float newX, newY;
            if(slope < 0.f)
            {
                newX = x - (float)Math.cos(angleRadians) * gap;
                newY = y - (float)Math.sin(angleRadians) * gap;
            }
            else
            {
                newX = x + (float)Math.cos(angleRadians) * gap;
                newY = y + (float)Math.sin(angleRadians) * gap;
            }

            setPos(newX, newY);
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
            if(x <= 1.f || x >= 8.f)
            {
                xShotSpeed *= -1.f; //방향 변경
            }
        }

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

        if(direction < 0.f)
        {
            xShotSpeed = -(float)Math.cos(angleRadians);
            yShotSpeed = -(float)Math.sin(angleRadians);

        }
        else
        {
            xShotSpeed = (float)Math.cos(angleRadians);
            yShotSpeed = (float)Math.sin(angleRadians);
        }

        bActive = true;

    }

    public void pop()
    {
        bDestroyed = true;
        bAnimating = true;

        width = 2.f;
        height = 2.f;


    }
}


