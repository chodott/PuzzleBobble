package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

public class InventoryScene extends BaseScene
{
    private Background bg;
    private static float speed;
    private float x,y = 0;
    public boolean bTouchItem = false;
    public Item selectedItem;
    float startX, startY;

    public InventoryScene(ArrayList<Integer> itemList)
    {
        bg = new Background(1);

        //Item 획득 목록 전달
        for(int type : itemList)
        {
            add(new Item(4.5f, 4.5f, type));
        }
    }

    public boolean checkItemSelect(float cx, float cy)
    {
        for(IGameObject gobj : gameObjects)
        {
            Item item = (Item)gobj;
            //아이템과 충돌 검사 후 반환
            if(item.checkSelect(cx, cy))
            {
                selectedItem = item;
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(long elapsedNanos) {
        super.update(elapsedNanos);
    }

    @Override
    public void draw(Canvas canvas) {
        bg.draw(canvas);
        super.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
                //위치 동기화 시켜야함
                startX = Metrics.toGameX(event.getX());
                startY = Metrics.toGameY(event.getY());
                bTouchItem = checkItemSelect(startX, startY);
                Log.d("Inventory", "onTouchEvent: " + bTouchItem);
                break;
            case MotionEvent.ACTION_MOVE:
                if(bTouchItem)
                {
                    float curX = Metrics.toGameX(event.getX());
                    float curY = Metrics.toGameY(event.getY());
                    selectedItem.move(curX, curY);
                }
                break;
            case MotionEvent.ACTION_UP:
                float endX = Metrics.toGameX(event.getX());
                float endY = Metrics.toGameY(event.getY());
                if(bTouchItem)
                {
                    //조합 추가해야함
                    if(startX == endX && startY == endY)
                    {
                        selectedItem.useItem();
                        popScene();
                        MainScene mainscene = (MainScene)getTopScene();
                        mainscene.equipItem(selectedItem.type);
                    }
                    selectedItem = null;
                }

                else if(startY < 800.f && endY > 1200.f)
                {
                    popScene();
                }
                break;
        }
        return true;
    }

}
