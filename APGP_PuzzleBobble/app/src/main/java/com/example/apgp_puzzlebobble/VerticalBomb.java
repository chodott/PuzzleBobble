package com.example.apgp_puzzlebobble;

public class VerticalBomb extends BombItem
{
    public VerticalBomb()
    {
        super();
        EXPLOSION_SIZE = 0.5f;
    }
    public void applyAbility()
    {
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

    public boolean checkInExplosion(Bobble bb)
    {
        if (bb.x <= x + EXPLOSION_SIZE && bb.x >= x - EXPLOSION_SIZE)
        {
            return true;
        }

        return false;
    }
}
