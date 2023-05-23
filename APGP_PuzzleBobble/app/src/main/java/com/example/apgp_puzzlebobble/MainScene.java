package com.example.apgp_puzzlebobble;

import android.util.Log;
import android.view.MotionEvent;

public class MainScene extends BaseScene {
    public BobbleManager bobbleMgr;
    float startX, startY;
    public MainScene()
    {
        add(new Background());
        bobbleMgr = new BobbleManager();
        add(bobbleMgr);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = Metrics.toGameX(event.getX());
                startY = -Metrics.toGameY(event.getY());
                Log.d("현재", "onTouchEvent: Down");
                return true;
            case MotionEvent.ACTION_MOVE:

                return true;

            case MotionEvent.ACTION_UP:
                float endX = Metrics.toGameX(event.getX());
                float endY = -Metrics.toGameY(event.getY());
                float direction = (endY - startY) / (endX - startX);
                Log.d("Shot", "Direction = " + direction + "x =" + endX + "y =" + endY );
                bobbleMgr.shotBobble(direction);
                Log.d("현재", "onTouchEvent: ");
                return true;
        }
        return super.onTouchEvent(event);
    }
}
