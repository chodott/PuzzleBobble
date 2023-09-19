package com.example.apgp_puzzlebobble;

import android.graphics.Canvas;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class RankScene extends BaseScene {

    public ArrayList<Score> ScoreList = new ArrayList<>();
    public ArrayList<Score> RankList = new ArrayList<>();
    public ArrayList<Alphabet> NameList = new ArrayList<>();

    @Override
    protected void onStart() {

        for(int i=0; i< 10; ++i)
        {
            Background bg = new Background(1);
            add(bg);
            Score score = new Score(R.mipmap.scoresprite, Metrics.game_width - 1.6f, Metrics.game_height/10 * i , 0.8f);
            score.setScore(HighScoreManager.getInt(RankActivity.mContext, Integer.toString(i + 1)));
            score.setbAnimating(false);

            Alphabet alphabetName = new Alphabet(R.mipmap.namesprite, Metrics.game_width/2, Metrics.game_height/10 * i, 0.8f);
            alphabetName.setName(HighScoreManager.getString(RankActivity.mContext, Integer.toString(i+1) +"."));

            Score rank = new Score(R.mipmap.scoresprite, 1.6f, Metrics.game_height/10 * i, 0.8f);
            rank.setScore(i+1);
            rank.setbAnimating(false);
            ScoreList.add(score);
            NameList.add(alphabetName);
            RankList.add(rank);
        }

    }

    @Override
    protected void onEnd() {
        Sound.stopMusic();
    }

    @Override
    public void update(long elapsedNanos) {
        super.update(elapsedNanos);
        for(Score score : ScoreList)
        {
            score.update();
        }

    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        for(Score score : ScoreList)
        {
            score.draw(canvas);
        }

        for(Alphabet name : NameList)
        {
            name.draw(canvas);
        }

        for(Score rank : RankList)
        {
            rank.draw(canvas);
        }

    }
}
