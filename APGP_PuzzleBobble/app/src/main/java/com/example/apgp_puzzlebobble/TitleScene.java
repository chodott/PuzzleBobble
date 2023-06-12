package com.example.apgp_puzzlebobble;

public class TitleScene extends BaseScene
{
    @Override
    protected void onStart() {
        Sound.playMusic(R.raw.titlemusic);
    }

    @Override
    protected void onEnd() {
        Sound.stopMusic();
    }
}
