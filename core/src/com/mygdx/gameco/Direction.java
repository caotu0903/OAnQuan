package com.mygdx.gameco;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Direction {
    Texture leftTexture;
    Texture rightTexture;

    Rectangle leftBoundingBox;
    Rectangle rightBoundingBox;

    private OCo cell;

    public Direction(Texture leftTexture, Texture rightTexture, OCo cell) {
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;

        Rectangle boundingBox = cell.boundingBox;
        float dimen = boundingBox.height / 2;

        this.leftBoundingBox = new Rectangle(boundingBox.x,
                boundingBox.y + boundingBox.width / 2 - dimen / 2,
                dimen, dimen);
        this.rightBoundingBox = new Rectangle(boundingBox.x + boundingBox.width - dimen,
                boundingBox.y + boundingBox.width / 2 - dimen / 2,
                dimen, dimen);
        this.cell = cell;
    }

    public int getDirection(float xTouch, float yTouch, Viewport viewport) {

        int res = 0;

        Vector2 touch = viewport.unproject(new Vector2(xTouch, yTouch));

        if (leftBoundingBox.contains(touch)) {
            res = -1;
        }

        if (rightBoundingBox.contains(touch)) {
            res = 1;
        }

        return res;
    }

    public void draw(Batch batch) {
        batch.draw(leftTexture, leftBoundingBox.x, leftBoundingBox.y,
                leftBoundingBox.width, leftBoundingBox.height);
        batch.draw(rightTexture, rightBoundingBox.x, rightBoundingBox.y,
                rightBoundingBox.width, rightBoundingBox.height);
//        batch.draw(cross, cell.x + cell.width/2 - dimen/2, cell.y,
//                dimen, dimen);
    }
}
