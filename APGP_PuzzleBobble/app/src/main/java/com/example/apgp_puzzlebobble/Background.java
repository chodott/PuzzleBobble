package com.example.apgp_puzzlebobble;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.BoringLayout;

public class Background extends Sprite{
    static int bitmapId = R.mipmap.background;
    public Background()
    {
        super(bitmapId, Metrics.game_width/2, Metrics.game_height/2, Metrics.game_width, Metrics.game_height);
        bitmap = BitmapFactory.decodeResource(GameView.res, R.mipmap.background);
        float height = bitmap.getHeight() * Metrics.game_width/ bitmap.getWidth();
        setSize(Metrics.game_width, height);
        Resources res = GameView.res;

    }
}
