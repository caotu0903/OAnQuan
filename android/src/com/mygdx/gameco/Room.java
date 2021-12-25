package com.mygdx.gameco;

import java.io.Serializable;

public class Room implements Serializable {
    String roomID;
    int roomImage;
    int numberPlayer;
    String roomName;
    String nameHost;
    String namePlayer;

    public Room(String roomID, int numberPlayer) {
        this.roomID = roomID;
        this.numberPlayer = numberPlayer;

        String[] splitRoomID = this.roomID.split("-");
        if (splitRoomID[0].equals("OAQ"))
        {
            roomImage = R.mipmap.icon;
        }

        roomName = "Room " + splitRoomID[1];
    }

    public Room(String roomID, int numberPlayer, String nameHost) {
        this.nameHost = nameHost;
        this.roomID = roomID;
        this.numberPlayer = numberPlayer;

        String[] splitRoomID = this.roomID.split("-");
        if (splitRoomID[0].equals("OAQ"))
        {
            roomImage = R.mipmap.icon;
        }

        roomName = "Room " + splitRoomID[1];
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getRoomImage() {
        return roomImage;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getNumberPlayer() {
        return numberPlayer;
    }

    public void setNumberPlayer(int numberPlayer) {
        this.numberPlayer = numberPlayer;
    }

    public String getNameHost() {
        return nameHost;
    }

    public void setNameHost(String nameHost) {
        this.nameHost = nameHost;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }
}
