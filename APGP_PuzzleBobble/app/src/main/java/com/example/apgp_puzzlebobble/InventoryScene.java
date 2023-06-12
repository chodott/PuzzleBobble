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

    public HashMap<Integer, Item> ItemMap= new HashMap<>();
    public boolean bTouchItem = false;
    public Item selectedItem;
    float startX, startY;

    public InventoryScene(HashMap<ItemType ,Integer> itemList)
    {
        bg = new Background(1);

        //Item 획득 목록 전달
        for(ItemType key : itemList.keySet())
        {
            int type = key.ordinal();
            addItem(type);
        }
    }

    public boolean checkItemSelect(float cx, float cy)
    {
        for(Item item : ItemMap.values())
        {
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
        ArrayList<Integer> targetList = new ArrayList<>();
        for(int key : ItemMap.keySet())
        {

            Item item = ItemMap.get(key);
            if(item.count <= 0)
                targetList.add(key);
        }
        for(int targetKey : targetList)
        {
            ItemMap.remove(targetKey);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bg.draw(canvas);
        super.draw(canvas);
        for(Item item : ItemMap.values())
        {
            item.draw(canvas);
        }
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
                        for(Item item : ItemMap.values())
                        {
                            //아이템과 충돌 검사 후 반환
                            if(selectedItem.checkCollision(item))
                            {
                                selectedItem.decCount();
                                item.decCount();
                                int type = random.nextInt(2) + 7;

                                addItem(type);
                                break;
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

    private void addItem(int type)
    {
        if(ItemMap.containsKey(type)) ItemMap.get(type).addCount();
        else
        {
            float row = ((type+1) / 4 + 1) * Metrics.game_height/4;
            float column = (type+1) % 3 * Metrics.game_width/3;
            Item item = new Item(column, row, type);
            ItemMap.put(type, item);
        }
    }

}
