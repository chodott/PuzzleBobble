package com.example.apgp_puzzlebobble;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.BoringLayout;
import android.util.Log;

public class Background extends Sprite{
    static int bitmapId = R.mipmap.background;
    protected Rect srcRect = new Rect();


    public Background(int type)
    {
        super(bitmapId, Metrics.game_width/2, Metrics.game_height/2, Metrics.game_width, Metrics.game_height);
        bitmap = BitmapFactory.decodeResource(GameView.res, R.mipmap.background);
        float height = bitmap.getHeight() * Metrics.game_width/ bitmap.getWidth();
        setSize(Metrics.game_width, Metrics.game_height);
        Resources res = GameView.res;
        int srcWidth = bitmap.getWidth()/2;
        srcRect.set(type * srcWidth,0, (type + 1) * srcWidth, bitmap.getHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}
