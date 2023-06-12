package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class InventoryScene extends BaseScene
{
    private static Random random = new Random();
    private Background bg;
    private static float speed;
    private float x,y = 0;
    public boolean bTouchItem = false;
    public Item selectedItem;
    float startX, startY;

    public InventoryScene(HashMap<ItemType ,Integer> itemList)
    {
        bg = new Background(1);

        //Item 획득 목록 전달
        for(ItemType key : itemList.keySet())
        {
            int size = ItemType.values().length;
            int type = key.ordinal() + 1;
            float row = (type / 4 + 1) * Metrics.game_height/4;
            float column = type % 3 * Metrics.game_width/3;
            add(new Item(column, row, type-1));
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
    protected void onStart() {

    }

    @Override
    protected void onEnd() {

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

                    if(startX == endX && startY == endY)
                    {
                        selectedItem.useItem();
                        popScene();
                        MainScene mainscene = (MainScene)getTopScene();
                        mainscene.equipItem(selectedItem.type);

                        //아이템 사용 사운드 추가
                        Sound.playEffect(R.raw.itemuseeffect);
                    }

                    else
                    {
                        for(IGameObject gobj : gameObjects)
                        {
                            Item item = (Item)gobj;
                            //아이템과 충돌 검사 후 반환
                            if(selectedItem.checkCollision(item))
                            {
                                int type = random.nextInt(ItemType.values().length);
                                selectedItem.change(type);
                                gameObjects.remove(item);
                            }
                        }
                    }

                    selectedItem = null;
                }

                else if(startY < 4.f && endY > 10.f)
                {
                    popScene();
                }
                break;
        }
        return true;
    }

}
