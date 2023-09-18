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
    private static float dropSpeed = 20.f;
    private static Random random;
    private static float speed = 10.f;
    private static int COLOR_COUNT = 7;
    private static Bitmap burstBitmap;
    private float xShotSpeed;
    private float yShotSpeed;
    private float accumulatedTime;
    private float direction;


    public ArrayList<Integer> parentsBobbleNum = new ArrayList<>();

    public int num;
    public int color;
    public boolean bDestroyed;
    public boolean bBurst;
    public boolean bActive;

    public boolean bAttached;
    public boolean bChecked =false;
    public Bobble()
    {
        super(R.mipmap.bobblesprite, 4.5f, 2.5f, 1.f, 1.f, 6.955f, 7, COLOR_COUNT);
        if(R.mipmap.bobbleburstsprite != 0)
        {
            burstBitmap = BitmapPool.get(R.mipmap.bobbleburstsprite);
        }
        if(random == null) random = new Random();
        color = random.nextInt(COLOR_COUNT);
        type = color;
    }

    public Bobble(int type) {
        super(R.mipmap.bobblesprite, 4.5f, 2.5f, 1.f, 1.f, 6.955f, 7, COLOR_COUNT);
        this.type = type;
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

    public void burst()
    {
        bAnimating = true;
        bBurst = true;

        int Width = burstBitmap.getWidth();
        int Height = burstBitmap.getHeight();

        parentsBobbleNum.clear();


        frameWidth = Width/ frameCount;
        frameHeight = Height/COLOR_COUNT;
    }

    public boolean checkCollision(Bobble target)
    {
        float targetX = target.x;
        float targetY = target.y;
        double distance = Math.sqrt(Math.pow(x - target.x,2) + Math.pow(y - target.y,2));
        if(distance == 0.f)
        {
            return true;
        }
        if(BOBBLE_SIZE * 2 > distance)
        {
            setActive(false);
            bAnimating = true;
            float gap = 1.f- (float)distance;
            float slope = (targetY - y) / (targetX - x);

            double angleRadians = Math.atan(slope);
            float newX, newY;
            if(slope < 0.f)
            {
                if(x < targetX)
                {
                    newX = x - (float)Math.cos(angleRadians) * gap;
                    newY = y - (float)Math.sin(angleRadians) * gap;
                }
                else
                {
                    newX = x + (float)Math.cos(angleRadians) * gap;
                    newY = y + (float)Math.sin(angleRadians) * gap;
                }

            }
            else
            {
                if(x < targetX)
                {
                    newX = x - (float)Math.cos(angleRadians) * gap;
                    newY = y - (float)Math.sin(angleRadians) * gap;
                }
                else
                {
                    newX = x + (float)Math.cos(angleRadians) * gap;
                    newY = y + (float)Math.sin(angleRadians) * gap;
                }
            }

            setPos(newX, newY);
            return true;
        }
        return false;
    }

    public void endAnimation()
    {
        super.endAnimation();
        if(bBurst)
        {
            bVisibility = false;
            BobbleManager.trashList.add(num);
        }
    }

    public void update()
    {
        super.update();
        float frameTime = BaseScene.frameTime;
        if(bDestroyed)
        {
            //연쇄 반응 자리 고정
            if(bBurst) return;

            y += dropSpeed * frameTime;

            //삭제 조건
            if(y <= 0.f)
            {
                BobbleManager.trashList.add(num);
            }
        }
        else if(bActive)
        {
            x += xShotSpeed * frameTime * speed;
            y -= yShotSpeed * frameTime * speed;
            if(x <= 1.f || x >= 9.f)
            {
                xShotSpeed *= -1.f; //방향 변경
            }

            else if (y<= 2.f)
            {
                y = 2.f;
            }

        }

    }
    @Override
    public void draw(Canvas canvas)
    {
        if(bBurst)
        {
            if(bVisibility) super.draw(canvas, burstBitmap);
        }
        else super.draw(canvas);
    }

    public void shot(float direction)
    {
        if(bActive) return;
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

        //width = 2.f;
        //height = 2.f;


    }
}


