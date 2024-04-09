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

    //Button
    private Button returnBtn;

    public HashMap<Integer, Item> ItemMap= new HashMap<>();
    public boolean bTouchItem = false;
    public Item selectedItem;
    float startX, startY;

    public InventoryScene(HashMap<ItemType ,Integer> itemList)
    {
        bg = new Background(1);

        returnBtn = new Button(R.mipmap.itembobble, Metrics.game_width - 1.f, Metrics.game_height - 1.f, 1.5f, 1.5f);
        returnBtn.setSrcRect();

        //Item 획득 목록 전달
        for(ItemType key : itemList.keySet())
        {
            int type = key.ordinal();
            addItem(type, itemList.get(key));
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
        MainScene.itemlistMap.clear();
        for(int key : ItemMap.keySet())
        {
            MainScene.itemlistMap.put(ItemType.values()[key], ItemMap.get(key).count);
        }
    }

    @Override
    public void update(long elapsedNanos) {
        super.update(elapsedNanos);
        ArrayList<Integer> targetList = new ArrayList<>();
        for(int key : ItemMap.keySet())
        {

            Item item = ItemMap.get(key);
            item.update();
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
        returnBtn.draw(canvas);
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
                startX = Metrics.toGameX(event.getX());
                startY = Metrics.toGameY(event.getY());
                bTouchItem = checkItemSelect(startX, startY);
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
                    Double touchDistance = Math.sqrt(Math.pow(startX - endX,2) + Math.pow(startY - endY, 2));
                    if(touchDistance <= 1.f)
                    {
                        selectedItem.useItem();
                        popScene();
                        onEnd();
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
                                int type = random.nextInt(4) + 7;
                                addItem(type, endX, endY);
                                break;
                            }
                        }
                        selectedItem.release(endX, endY);
                    }
                    selectedItem = null;
                }

                else if(returnBtn.checkTouched(endX, endY))
                {
                    onEnd();
                    popScene();
                }
                break;
        }
        return true;
    }

    private void addItem(int type, float x, float y)
    {
        if(ItemMap.containsKey(type)) ItemMap.get(type).addCount();
        else
        {
            float row = ((int)(type / 3) + 1) * Metrics.game_height/5;
            float column = (type % 3 + 1) * Metrics.game_width/4;
            Item item = new Item(x, y, type, column, row);
            item.bNeedMove = true;
            ItemMap.put(type, item);
        }
    }
    private void addItem(int type, int count)
    {
        float row = ((int)(type / 3) + 1) * Metrics.game_height/5;
        float column = (type % 3 + 1) * Metrics.game_width/4;
        Item item = new Item(column, row, type, column, row);
        item.count = count;
        ItemMap.put(type, item);
    }

}
