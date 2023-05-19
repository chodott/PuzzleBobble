package com.example.apgp_puzzlebobble;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private ImageView DragImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(R.layout.activity_main);
        DragImageView = (ImageView) findViewById(R.id.imageView);
        DragImageView.setOnTouchListener(new TouchListener());
        DragImageView.setOnDragListener(new DragListener());
        setContentView(gameView);

        new MainScene().pushScene();
    }
    class TouchListener implements View.OnTouchListener
    {
        float startX;
        float startY;
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    startX = motionEvent.getX();
                    startY = motionEvent.getY();
                    break;

                case MotionEvent.ACTION_UP:
                    //발사
                    float endX = motionEvent.getX();
                    float endY = motionEvent.getY();
                    float direction = endY - startY/endX - startX;
                    //방향을 넘겨라
                    break;
            }
            return false;
        }
    }

    class DragListener implements View.OnDragListener
    {

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            return false;
        }
    }

}
