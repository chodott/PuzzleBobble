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

    public void touchEvent(MotionEvent event)
    {
        for(OnTouchListner touchListner:touchListners)
        {
            touchListner.onTouch(event);
        }
    }

    public interface OnTouchListner
    {
        void onTouch(MotionEvent event);
    }
}
