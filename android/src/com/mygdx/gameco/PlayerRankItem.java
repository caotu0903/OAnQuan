package com.mygdx.gameco;

public class PlayerRankItem {

    int rank;
    String name;
    int point;

    public PlayerRankItem(int rank, String name, int point) {
        this.rank = rank;
        this.name = name;
        this.point = point;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
