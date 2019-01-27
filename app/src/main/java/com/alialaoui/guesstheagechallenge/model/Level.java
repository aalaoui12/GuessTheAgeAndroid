package com.alialaoui.guesstheagechallenge.model;

import android.graphics.drawable.Drawable;

public class Level {
    private int age;
    private int numLevel;
    private int picID;

    
    //need age(answer), picture, and level number to initialize level
    public Level(int age, int picID, int numLevel) {
        this.age = age;
        this.numLevel = numLevel;
        this.picID = picID;
    }
    
    //getters and setters

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

    
    //calculates the first and second numbers of the ages
    public int firstNum() {
        return age / 10;
    }
    public int secondNum() {
        return age % 10;
    }
}
