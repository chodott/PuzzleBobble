package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

public class HorizonBomb extends BombItem
{

    private static ArrayList<AnimSprite> explosionList;
    public HorizonBomb()
    {
        super();

        if(explosionList == null)
        {
            explosionList  = new ArrayList<>();
            for(int i = 0; i<9; ++i)
            {
                explosionList.add(new AnimSprite(R.mipmap.bombsprite, x, y, width, height, fps, FRAME_COUNT, 1));
            }
        }

        EXPLOSION_SIZE = 0.5f;
    }

    public void applyAbility()
    {
        explose();

        for(int i=0; i<explosionList.size(); ++i)
        {
            AnimSprite explosion = explosionList.get(i);
            explosion.frameIndex = 1;
            explosion.bAnimating = true;

            explosion.y = y;
            explosion.x = i * 1.f + 1.f;
        }

        for(int key: BobbleManager.bobbleMap.keySet())
        {
            boolean bResult = checkInExplosion(BobbleManager.bobbleMap.get(key));
            if(bResult)
            {
                BobbleManager.popTargetBobbles.add(key);
            }
        }
    }
    public boolean checkInExplosion(Bobble bb)
    {

        if(bb.y >= y - EXPLOSION_SIZE && bb.y <= y + EXPLOSION_SIZE)
        {
            //y값 반대 아닌지 확인 필요
            return true;
        }

        return false;
    }

    @Override
    public void update() {
        super.update();

        if(bExplosed) {
            for (AnimSprite explosion : explosionList) {
                explosion.update();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(bExplosed)
        {
            for(AnimSprite explosion : explosionList)
            {
                explosion.draw(canvas);
            }
        }
    }
}
