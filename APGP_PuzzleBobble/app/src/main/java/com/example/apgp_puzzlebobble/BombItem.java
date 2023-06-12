package com.example.apgp_puzzlebobble;

import android.util.Log;

public class BombItem extends ItemBobble {
    private static float EXPLOSION_SIZE = 4.f;
    private static int FRAME_COUNT = 10;
    public boolean bExplosed = false;
    public BombItem(){
        super(R.mipmap.bombsprite,7.9f, FRAME_COUNT,1);
        this.type = ItemType.bomb.ordinal();
    }

    public void explose()
    {
        bExplosed = true;
        setSize(EXPLOSION_SIZE, EXPLOSION_SIZE);
        bActive = false;
        bAnimating = true;
    }

    public boolean checkInExplosion(Bobble bb)
    {
        double dist = Math.sqrt(Math.pow(x - bb.x, 2) + Math.pow(y - bb.y,2));
        if(dist < EXPLOSION_SIZE/2)
        {
            return true;
        }
        else return false;
    }

    @Override
    public void update() {
        super.update();

        if (bExplosed && !bAnimating)
        {
            BobbleManager.curItem = null;
            //삭제 코드

        }
    }

    @Override
    public void applyAbility() {
        explose();
        for(int key: BobbleManager.bobbleMap.keySet())
        {
            boolean bResult = checkInExplosion(BobbleManager.bobbleMap.get(key));
            if(bResult)
            {
                BobbleManager.popTargetBobbles.add(key);
            }
        }

    }
}
