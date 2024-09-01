package com.github.sniconmc.oblivion.config;

public class OblivionSkin {

    private String player;
    private String signature;
    private String texture;
    private boolean cape;
    private boolean jacket;
    private boolean left_sleeve;
    private boolean right_sleeve;
    private boolean left_pants;
    private boolean right_pants;
    private boolean hat;

    public String getPlayer() {
        return player;
    }

    public String getSignature() {
        return signature;
    }

    public boolean isHat() {
        return hat;
    }

    public boolean isRight_pants() {
        return right_pants;
    }

    public boolean isLeft_pants() {
        return left_pants;
    }

    public boolean isLeft_sleeve() {
        return left_sleeve;
    }

    public boolean isRight_sleeve() {
        return right_sleeve;
    }

    public boolean isJacket() {
        return jacket;
    }

    public boolean isCape() {
        return cape;
    }

    public String getTexture() {
        return texture;
    }
}
