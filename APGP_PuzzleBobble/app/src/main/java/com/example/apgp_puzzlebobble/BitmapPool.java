package com.example.apgp_puzzlebobble;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.util.HashMap;

public class BitmapPool
{
    private static final String TAG = BitmapPool.class.getSimpleName();
    private static HashMap<Integer, Bitmap> bitmaps = new HashMap<>();
    private static BitmapFactory.Options opts;
    public static Bitmap get(int mipmapResId) {
    Bitmap bitmap = bitmaps.get(mipmapResId);
        if (bitmap == null) {
        Resources res = GameView.view.getResources();
        if (opts == null) {
            opts = new BitmapFactory.Options();
            opts.inScaled = false;
        }
        bitmap = BitmapFactory.decodeResource(res, mipmapResId, opts);
        Log.d(TAG, "Bitmap ID " + mipmapResId + " : " + bitmap.getWidth() + "x" + bitmap.getHeight());
        bitmaps.put(mipmapResId, bitmap);
    }
        return bitmap;
}
}
