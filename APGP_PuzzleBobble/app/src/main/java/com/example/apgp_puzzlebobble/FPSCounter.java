package com.example.apgp_puzzlebobble;

public class FPSCounter {
    private long lastTime = System.nanoTime();
    private int frames =0;
    private int fps =0;

    public void update()
    {
        long currentTime = System.nanoTime();
        frames++;

        if(currentTime - lastTime >= 1_000_000_000)
        {
            fps = frames;
            frames = 0;
            lastTime = currentTime;
            System.out.println("FPS: " + fps);
        }
    }
}
