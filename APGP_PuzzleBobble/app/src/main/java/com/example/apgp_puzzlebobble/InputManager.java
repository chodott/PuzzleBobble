package com.example.apgp_puzzlebobble;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class InputManager
{
    private List<OnTouchListner> touchListners = new ArrayList<>();

    public void addListener(OnTouchListner listner)
    {
        touchListners.add(listner);
    }

    public boolean touchEvent(MotionEvent event)
    {
        for(OnTouchListner touchListner:touchListners)
        {
            if(touchListner.onTouch(event)) return true;
        }
        return false;
    }

    public interface OnTouchListner
    {
        boolean onTouch(MotionEvent event);
    }
}
