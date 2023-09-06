package com.example.apgp_puzzlebobble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class Item extends Sprite{
    private static float CASE_SIZE = 2.f;
    private static float ITEM_SIZE = 1.f;
    private static int ITEM_COUNT = 11;

    private static float  CNT_SIZE = 0.4f;
    private static Bitmap itemBitmap;
    private static Bitmap countBitmap;

    public int type;
    public int count = 1;
    public int srcSize;
    public int cntSrcSize;
    protected RectF itemDstRect = new RectF();
    protected RectF cntDstRect = new RectF();
    protected Rect srcRect = new Rect();
    protected Rect countSrcRect = new Rect();
    public Item(float cx, float cy, int type) {
        super(R.mipmap.itembobble, cx, cy, CASE_SIZE, CASE_SIZE);
        this.type = type;
        itemBitmap =  BitmapFactory.decodeResource(GameView.res, R.mipmap.itemsprite);
        countBitmap = BitmapFactory.decodeResource(GameView.res, R.mipmap.scoresprite);
        srcSize = itemBitmap.getWidth()/ITEM_COUNT;
        cntSrcSize = countBitmap.getWidth()/10;
        srcRect.set(type * srcSize, 0 , (type + 1) * srcSize, srcSize);
        countSrcRect.set(count * cntSrcSize, 0, (count + 1) * cntSrcSize, countBitmap.getHeight());

        float halfSize = ITEM_SIZE/2;
        itemDstRect.set(x - halfSize, y - halfSize, x+ halfSize, y+ halfSize);
        cntDstRect.set(x - ITEM_SIZE, y - ITEM_SIZE, x - ITEM_SIZE + CNT_SIZE, y - ITEM_SIZE + CNT_SIZE );
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
        cntDstRect.set(x - ITEM_SIZE, y - ITEM_SIZE, x - ITEM_SIZE + CNT_SIZE, y - ITEM_SIZE + CNT_SIZE );
    }

    void useItem()
    {
        decCount();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(itemBitmap, srcRect, itemDstRect, null);
        canvas.drawBitmap(countBitmap, countSrcRect, cntDstRect, null);
    }

    public boolean checkCollision(Item item)
    {
        if(this == item) return false;
        double distance = Math.sqrt(Math.pow(item.y - y, 2) + Math.pow(item.x - x, 2));
        if(distance < CASE_SIZE)
            return true;

        return false;
    }

    @Override
    public void update() {
        super.update();

        countSrcRect.set(count * cntSrcSize, 0, (count + 1) * cntSrcSize, countBitmap.getHeight());
    }

    public void addCount()
    {
        count++;
    }

    public void decCount()
    {
        count--;
    }
}
