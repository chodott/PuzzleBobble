package com.example.apgp_puzzlebobble;

public class Metrics {
    public static float scale = 1.0f;
    public static float game_width = 10.5f;
    public static float game_height = 18.5f;
    public static int x_offset = 0, y_offset = 0;

    public static void setGameSize(float width, float height)
    {
        game_width = width;
        game_height = height;
    }
    public static float toGameX(float x)
    {
        return (x - x_offset) / scale;

    }
    public static float toGameY(float y)
    {
        return (y - y_offset)/scale;
    }
}
