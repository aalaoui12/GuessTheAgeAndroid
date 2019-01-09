package com.alialaoui.guesstheagechallenge.model;

import android.graphics.drawable.Drawable;

public class Level {
    private int age;
    private int numLevel;
    private int picID;

    public Level(int age, int picID, int numLevel) {
        this.age = age;
        this.numLevel = numLevel;
        this.picID = picID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNumLevel() {
        return numLevel;
    }

    public void setNumLevel(int numLevel) {
        this.numLevel = numLevel;
    }

    public int getGirl() {
        return picID;
    }

    public void setGirl(Drawable girl) {
        this.picID = picID;
    }

    public int firstNum() {
        return age / 10;
    }
    public int secondNum() {
        return age % 10;
    }
}
