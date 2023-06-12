package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class Item extends Sprite{
    private static float CASE_SIZE = 2.f;
    private static float ITEM_SIZE = 1.f;
    private static int ITEM_COUNT = 3;
    private static Bitmap itemBitmap;
    public int type;
    public int srcSize;
    protected RectF itemDstRect = new RectF();
    protected Rect srcRect = new Rect();
    public Item(float cx, float cy, int type) {
        super(R.mipmap.itembobble, cx, cy, CASE_SIZE, CASE_SIZE);
        this.type = type;
        itemBitmap =  BitmapFactory.decodeResource(GameView.res, R.mipmap.itemsprite);
        srcSize = itemBitmap.getWidth()/ITEM_COUNT;
        srcRect.set(type * srcSize, 0 , (type + 1) * srcSize, srcSize);

        float halfSize = ITEM_SIZE/2;
        itemDstRect.set(x - halfSize, y - halfSize, x+ halfSize, y+ halfSize);
    }

    public boolean checkSelect(float cx, float cy)
    {
        double distance = Math.sqrt(Math.pow(cy - y, 2) + Math.pow(cx - x, 2));
        if (distance < CASE_SIZE) return true;
        return false;
    }

    public void move(float cx, float cy)
    {
        x = cx;
        y = cy;
        setDstRect();
        float halfSize = ITEM_SIZE/2;
        itemDstRect.set(x - halfSize, y - halfSize, x+ halfSize, y+ halfSize);
    }

    void useItem()
    {

    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        canvas.drawBitmap(itemBitmap, srcRect, itemDstRect, null);
    }

    public boolean checkCollision(Item item)
    {
        if(this == item) return false;
        double distance = Math.sqrt(Math.pow(item.y - y, 2) + Math.pow(item.x - x, 2));
        if(distance < CASE_SIZE)
            return true;

        return false;
    }

    public void change(int itemtype)
    {
        type = itemtype;
        srcRect.set(type * srcSize, 0 , (type + 1) * srcSize, srcSize);
    }
}
