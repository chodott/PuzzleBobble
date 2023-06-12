package com.example.apgp_puzzlebobble;

public class TimeItem extends ItemBobble{

    private static int FRAME_COUNT = 8;
    public TimeItem() {
        super(R.mipmap.clocksprite, 3.9f, FRAME_COUNT, 2);
        bLoop = true;
    }

    @Override
    public void update() {
        super.update();
        if(bAnimating == false) MainScene.timeItem = null;
    }

    @Override
    public void applyAbility()
    {
        LimitTimer.stopTimer();
        bActive = false;
        bAnimating = true;
        type = 0;
    }

    public void endTimer()
    {
        type = 1;
        bLoop = false;
    }

}
