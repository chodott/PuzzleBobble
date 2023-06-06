package com.example.apgp_puzzlebobble;

public class BombItem extends ItemBobble {
    private float EXPLOSION_SIZE = 3.f;
    public BombItem(){

    }

    @Override
    public boolean checkCollision(Bobble target)
    {
        double dist = Math.sqrt(Math.pow(x - target.x, 2) + Math.pow(y - target.y,2));
        if(dist < EXPLOSION_SIZE/2)
        {
            return true;
        }
        return false;
    }
}
