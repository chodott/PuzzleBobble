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
        {   //폭발 범위 출력 오브젝트 생성
            explosionList  = new ArrayList<>();
            for(int i = 0; i<9; ++i)
            {
                explosionList.add(new AnimSprite(R.mipmap.bombsprite, x, y, width, height, fps, FRAME_COUNT, 1));
            }
        }
        EXPLOSION_SIZE = 0.5f;
    }

    @Override
    public void applyAbility()
    {
        for(int i=0; i<explosionList.size(); ++i)
        {   //폭발 범위 애니메이션 동작
            AnimSprite explosion = explosionList.get(i);
            explosion.frameIndex = 1;
            explosion.bAnimating = true;

            explosion.y = y;
            explosion.x = i * 1.f + 1.f;
        }
        super.applyAbility();
    }
    public boolean checkInExplosion(Bobble bb)
    {   //y값만 비교하여 가로 범위 폭파
        return (bb.y >= y - EXPLOSION_SIZE && bb.y <= y + EXPLOSION_SIZE);
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
