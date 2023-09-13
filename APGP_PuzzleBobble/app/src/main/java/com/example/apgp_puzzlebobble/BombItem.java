package com.example.apgp_puzzlebobble;

import android.util.Log;

public class BombItem extends ItemBobble {
    private static int FRAME_COUNT = 10;

    public float EXPLOSION_SIZE = 4.f;

    public boolean bExplosed = false;
    public BombItem(){
        super(R.mipmap.bombsprite,3.9f, FRAME_COUNT,1);
        type = 0;
    }

    public void explose()
    {
        Sound.playEffect(R.raw.bombeffect);
        bExplosed = true;

    }

    public boolean checkInExplosion(Bobble bb)
    {
        double dist = Math.sqrt(Math.pow(x - bb.x, 2) + Math.pow(y - bb.y,2));
        if(dist < EXPLOSION_SIZE/2)
        {
            return true;
        }

        return false;
    }

    @Override
    public void update() {
        super.update();

        if (bExplosed && !bAnimating)
        {
            BobbleManager.curItem = null;

        }
        else if(frameIndex != 0)
            setSize(EXPLOSION_SIZE, EXPLOSION_SIZE);
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
