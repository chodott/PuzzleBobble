package com.example.apgp_puzzlebobble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class RankActivity extends AppCompatActivity {

    private GameView gameView;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);

        setContentView(R.layout.activity_rank);
        setContentView(gameView);
        mContext = this;

        new RankScene().pushScene();
        BaseScene.getTopScene().onStart();
    }
}