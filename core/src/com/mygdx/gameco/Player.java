package com.mygdx.gameco;

public class Player {
    String id;

    int score;
    int borrow;

    public Player(int score, int borrow) {
        this.score = score;
        this.borrow = borrow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBorrow() {
        return borrow;
    }

    public void setBorrow(int borrow) {
        this.borrow = borrow;
    }

    public void addBorrow(int num) {
        this.borrow+=num;
    }
}


