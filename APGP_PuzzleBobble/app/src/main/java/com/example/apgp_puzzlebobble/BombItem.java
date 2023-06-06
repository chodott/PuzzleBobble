package com.example.apgp_puzzlebobble;

public class BombItem extends ItemBobble {
    private static float EXPLOSION_SIZE = 3.f;
    private static int FRAME_COUNT = 10;
    public static int srcWidth = 0;
    public boolean bExplosed = false;
    public BombItem(){
        super(R.mipmap.bombsprite, FRAME_COUNT, 1);
        this.type = ItemType.bomb;
        setBitmapResource(R.mipmap.bombsprite);
    }

    public void explose()
    {
        bExplosed = true;
        setSize(EXPLOSION_SIZE, EXPLOSION_SIZE);
        setActive(false);
    }

    @Override
    public boolean checkCollision(Bobble target)
    {
        double dist = Math.sqrt(Math.pow(x - target.x, 2) + Math.pow(y - target.y,2));
        if(dist < EXPLOSION_SIZE/2)
        {
            explose();
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        super.update();

        if(bExplosed)
        {
            bAnimating = true;
        }
    }
}
