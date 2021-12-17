package com.mygdx.gameco;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Hand {

    private final int HAND_MOVE_SPEED = 5;
    GameScreen gameScreen;
    // position and dimension
    Rectangle boundingBox;

    // graphic
    TextureRegion textureRegion;
    float moveTimeBetweenCell;

    // parameter and object
    int curCell;
    int direction;
    OCo[] board;
    int point;

    boolean isMoving;

    // destination

    public Hand(int point, float moveTimeBetweenCell,
                TextureRegion textureRegion, int curCell, OCo[] board, GameScreen gs) {
        this.gameScreen = gs;
        Rectangle cellBounding = board[curCell].boundingBox;
        this.boundingBox = new Rectangle(cellBounding.x,
                cellBounding.y,
                cellBounding.width,
                cellBounding.height);
        this.point = point;
        this.moveTimeBetweenCell = moveTimeBetweenCell;
        this.textureRegion = textureRegion;
        this.curCell = curCell;
        this.board = board;
        this.isMoving = false;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void update(float dTime) {

    }

    public boolean isFinishMove() {
        return point==0;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void translate(float dTime) {
        int nextCellIndex = (curCell + direction)%12;
        if (nextCellIndex<0)
            nextCellIndex = 11;

        float[] nextXY = board[nextCellIndex].getCenterXY();
        float[] curXY = board[curCell].getCenterXY();
        float[] handXY = this.getCenterXY();

        float xChange = (dTime/moveTimeBetweenCell) * (nextXY[0] - curXY[0]);
        float yChange = (dTime/moveTimeBetweenCell) * (nextXY[1] - curXY[1]);
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);

        if (isReachNextCell(nextCellIndex)) {
            //isMoving = false;
            curCell = nextCellIndex;
        }
    }

    public boolean isReachNextCell(int nextIndex) {

        float[] nextXY = board[nextIndex].getCenterXY();
        float[] handXY = this.getCenterXY();

        if (direction==1) {
            if (nextIndex==5) {
                if (handXY[0]<nextXY[0] && handXY[1]>nextXY[1])
                    return true;
            }
            else if (nextIndex==11) {
                if (handXY[0]>nextXY[0] && handXY[1]<nextXY[1])
                    return true;
            }
            else {
                if (handXY[0]<nextXY[0])
                    return true;
            }
        }

        if (direction==-1) {
            if (nextIndex==5) {
                if (handXY[0]<nextXY[0] && handXY[1]<nextXY[1])
                    return true;
            }
            else if (nextIndex==11) {
                if (handXY[0]>nextXY[0] && handXY[1]>nextXY[1])
                    return true;
            }
            else {
                if (handXY[0]>nextXY[0])
                    return true;
            }
        }

        return false;
    }

    public float[] getCenterXY() {
        float[] center = new float[2];
        center[0] = boundingBox.x + boundingBox.width/2;
        center[1] = boundingBox.y + boundingBox.height/2;
        return center;
    }

    public void draw(Batch batch) {
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }
}
