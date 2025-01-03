package com.example.apgp_puzzlebobble;

import android.util.Log;

public class BombItem extends ItemBobble {
    public static int FRAME_COUNT = 10;

    public float EXPLOSION_SIZE = 4.f;

    public boolean bExplosed = false;
    public BombItem(){
        super(R.mipmap.bombsprite,6.955f, FRAME_COUNT,1);
        type = 0;
    }

    public void explose()
    {
        Sound.playEffect(R.raw.bombeffect);
        bExplosed = true;

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
        //폭파 사운드 출력
        explose();

        for(int i=0;i<BobbleManager.LIST_HEIGHT; ++i)
        {   //폭발 범위 내 Bobble 제거
            for(int j=0;j<BobbleManager.LIST_WIDTH; ++j)
            {
                Bobble target = BobbleManager.bobbleArray[i][j];
                if(target == null) continue;
                boolean bResult = checkInExplosion(target);
                if(!bResult) continue;
                BobbleManager.popTargetBobbles.add(target);

            }
        }
    }

    public boolean checkInExplosion(Bobble bb)
    {   //원형 범위로 충돌 체크
        double dist = Math.sqrt(Math.pow(x - bb.x, 2) + Math.pow(y - bb.y,2));
        return dist < EXPLOSION_SIZE / 2;
    }
}
