package com.example.apgp_puzzlebobble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

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

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        new RankScene().pushScene();
        BaseScene.getTopScene().onStart();
    }
}