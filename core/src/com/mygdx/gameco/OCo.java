package com.mygdx.gameco;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class OCo {

    //OCo characteristics
    int numberCo;
    boolean isQuan, isAliveQuan;

    //position & dimension
    Rectangle boundingBox;

    //graphics
    TextureRegion ocoTexture;

    public OCo(int numberCo, boolean isQuan, boolean isAliveQuan, float xCenter, float yCenter, float width, float height, TextureRegion ocoTexture) {
        this.numberCo = numberCo;
        this.isQuan = isQuan;
        this.isAliveQuan = isAliveQuan;

        this.boundingBox = new Rectangle(xCenter - width/2, yCenter - height/2, width, height);

        this.ocoTexture = ocoTexture;
    }

    public void draw(Batch batch) {
        batch.draw(ocoTexture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public void update(int numberCo) {

    }

    public float[] getCenterXY() {
        float[] center = new float[2];
        center[0] = boundingBox.x;
        center[1] = boundingBox.y;
        return center;
    }

    public int getNumberCo() {
        return numberCo;
    }

    public boolean isQuan() {
        return isQuan;
    }

    public boolean isAliveQuan() {
        return isAliveQuan;
    }

    void setOcoTexture (TextureRegion texture) {
        this.ocoTexture = texture;
    }

    public void setNumberCo(int numberCo) {
        this.numberCo = numberCo;
    }
}
