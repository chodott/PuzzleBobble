package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class EndScreen extends AnimSprite{
    static float SCREEN_WIDTH = 5.f;
    static float SCREEN_HEIGHT = 5.f;


    public EndScreen()
    {
        super(R.mipmap.endsprite, Metrics.game_width/2,  Metrics.game_height/2, SCREEN_WIDTH, SCREEN_HEIGHT, 1.955f, 3, 1);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

}
