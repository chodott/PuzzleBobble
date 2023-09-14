package com.example.apgp_puzzlebobble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TitleActivity extends AppCompatActivity {

    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
        new TitleScene().pushScene();
        BaseScene.getTopScene().onStart();

        setContentView(R.layout.activity_title);

    }

    public void onBtnStart(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
        BaseScene.getTopScene().onEnd();
    }
    public void onBtnQuit(View view)
    {
        Sound.stopMusic();
        finish();
    }

    public void onBtnRank(View view)
    {

    }
}