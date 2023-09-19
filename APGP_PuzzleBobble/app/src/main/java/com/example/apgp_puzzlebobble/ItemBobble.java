package com.example.apgp_puzzlebobble;

import java.util.Random;

public abstract class ItemBobble extends AnimSprite
{
    private static float ITEM_SIZE = 1.f;
    private float direction;
    private float xShotSpeed;
    private float yShotSpeed;
    private static float speed = 10.f;
    public boolean bActive = false;
    public boolean bUsed = false;

    public ItemBobble(int bitmapId, float fps, int frameCount, int typeCount)
    {
        super(bitmapId, Metrics.game_width/2, 17.f, 1.f, 1.f, fps, frameCount, typeCount);
    }

    public ItemBobble setPos(float xPos, float yPos)
    {
        x = xPos;
        y = yPos;
        setDstRect();
        return this;
    }

    public void update()

    {
        super.update();
        float frameTime = BaseScene.frameTime;

        if(bActive)
        {
            x += xShotSpeed * frameTime * speed;
            y -= yShotSpeed * frameTime * speed;
            if(x <= 1.f || x >= 9.f)
            {
                xShotSpeed *= -1.f; //방향 변경
            }
        }

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

        bUsed = true;
        bActive = true;

    }

    public boolean checkCollision(Bobble target)
    {
        double distance = Math.sqrt(Math.pow(x - target.x,2) + Math.pow(y - target.y,2));
        if(ITEM_SIZE > distance)
        {
            bAnimating = true;
            bActive = false;
            return true;
        }
        return false;

    }

    public abstract void applyAbility();
}

