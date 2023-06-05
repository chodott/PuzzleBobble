package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Item extends Sprite{

    private static float width, height;
    private static Bitmap bitmap;
    private int type;
    protected Rect srcRect = new Rect();
    public Item(float cx, float cy, int type) {
        super(R.mipmap.itembobble, cx, cy, width, height);
        this.type = type;
    }

    void useItem()
    {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }
}
