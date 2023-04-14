package com.example.apgp_puzzlebobble;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


public class GameView extends View {

    public static float scale;
    public static float game_width = 9.0f;
    public static float game_height = 16.0f;
    public static int x_offset, y_offset;


    public GameView(Context context) {
        super(context);
        init(null, 0);
    }


    private void init(AttributeSet attrs, int defStyle) {

    }

    private void update()
    {
        BaseScene.getTopScene().update();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);

        float view_ratio = (float)w / (float)h;
        float game_ratio = game_width / game_height;
        if(view_ratio > game_ratio)
        {
            x_offset = (int)((w - h * game_ratio)/2);
            y_offset = 0;
            scale = h/game_height;
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(x_offset, y_offset);
        canvas.scale(scale, scale);
        canvas.restore();

       BaseScene.getTopScene().draw(canvas);
    }
}