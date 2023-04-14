package com.example.apgp_puzzlebobble;

public class MainScene extends BaseScene {

    public MainScene()
    {
        add(new Background());
        add(new BobbleManager());
    }

}
