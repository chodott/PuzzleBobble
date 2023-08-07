package com.example.apgp_puzzlebobble;

public class HorizonBomb extends BombItem
{
    public HorizonBomb()
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
        if(bb.y <= y + EXPLOSION_SIZE && bb.y >= y - EXPLOSION_SIZE)
        {
            //y값 반대 아닌지 확인 필요
            return true;
        }

        return false;
    }
}
