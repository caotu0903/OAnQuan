package com.mygdx.gameco;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Hand {

    // position and dimension
    Rectangle boundingBox;

    // graphic
    TextureRegion textureRegion;
    float movementSpeed;

    OCo nextCell;
    int point;

    //Grab/drop timer

    public Hand(float xCenter, float yCenter,
                float width, float height,
                int point, float movementSpeed,
                TextureRegion textureRegion, OCo nextCell) {
        this.boundingBox = new Rectangle(boundingBox.x - width/2, boundingBox.y -height/2,
                                            width, height);
        this.point = point;
        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
        this.nextCell = nextCell;
    }

    public void update(float dTime) {

    }

    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }

    public void draw(Batch batch) {
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }
}
