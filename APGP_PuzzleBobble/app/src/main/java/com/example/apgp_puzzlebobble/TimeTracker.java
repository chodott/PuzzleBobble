package com.example.apgp_puzzlebobble;

import android.util.Log;

public class TimeTracker {
    private long total = 0;
    private int count = 0;
    private long start = 0;

    public void begin() {
        start = System.nanoTime();
    }

    public void end() {
        long delta = System.nanoTime() - start;
        total += delta;
        Log.d("TimeTracker", "LAP: "+delta/1000000 + "ms" );
        count++;
    }


    public void log() {
        if (count == 0) return;
        long avgUs = (total / count) / 1000000;
        Log.d("TimeTracker", " avg: " + avgUs + "ms (" + count + " calls)");
    }

    public void reset() {
        total = 0;
        count = 0;
    }
}