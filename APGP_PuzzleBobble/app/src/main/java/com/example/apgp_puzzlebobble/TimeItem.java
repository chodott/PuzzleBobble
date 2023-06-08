package com.example.apgp_puzzlebobble;

public class TimeItem extends ItemBobble{

    private static int FRAME_COUNT = 8;
    public TimeItem() {
        super(R.mipmap.clocksprite, FRAME_COUNT, 2);
    }



    @Override
    public void applyAbility()
    {
        LimitTimer.stopTimer();
        bActive = false;
    }

}
