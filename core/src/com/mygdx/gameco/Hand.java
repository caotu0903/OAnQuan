package com.mygdx.gameco;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import java.util.ListIterator;
import java.util.Locale;

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
    int grabCell;

    boolean isMoving;
    GrabAnimation grabAnimation;

    boolean isShowAnLinh;
    boolean isEndTurn;

    // destination

    public Hand(int point, float moveTimeBetweenCell,
                TextureRegion textureRegion, OCo[] board, GameScreen gs) {
        this.gameScreen = gs;
        Rectangle cellBounding = board[0].boundingBox;
        this.boundingBox = new Rectangle(cellBounding.x,
                cellBounding.y,
                cellBounding.width,
                cellBounding.height);
        this.point = point;
        this.moveTimeBetweenCell = moveTimeBetweenCell;
        this.textureRegion = textureRegion;
        this.board = board;
        this.isMoving = false;
        this.grabCell = -1;

        Texture grabAni = new Texture("grab_hand.png");
        grabAnimation = new GrabAnimation(grabAni, 0.15f, board[3]);

        isShowAnLinh = false;
        isEndTurn = true;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void update(float dTime) {

    }

    public void setCurCell(int curCellIndex) {
        this.curCell = curCellIndex;
        this.boundingBox.x = board[curCell].getCenterXY()[0] - boundingBox.width/2;
        this.boundingBox.y = board[curCell].getCenterXY()[1] - boundingBox.height/2;
    }

    public boolean isFinishMove() {
        return point==0;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int calcNextIndex(int curCell, int direction) {
        //int nextCellIndex = (curCell + direction)%12;
        int nextCellIndex = curCell + direction;
        if (nextCellIndex<0) {
            nextCellIndex = 11;
        }
        else if (nextCellIndex>11) {
            nextCellIndex=0;
        }
        return nextCellIndex;
    }

    public int calcNextIndexWithNumber(int curCell, int direction, int number) {
        int nextCellIndexNumber = (curCell + direction * number)%12;
        if (nextCellIndexNumber<0)
            nextCellIndexNumber = 11;
        return nextCellIndexNumber;
    }

    public void setPosition(float x, float y) {
        boundingBox.x = x;
        boundingBox.y = y;
    }

    public void translate(float dTime, int curCell, int nextCell) {
        float[] nextXY = board[nextCell].getCenterXY();
        float[] curXY = board[curCell].getCenterXY();

        //Xu ly hinh anh
        float xChange = (dTime / moveTimeBetweenCell) * (nextXY[0] - curXY[0]);
        float yChange = (dTime / moveTimeBetweenCell) * (nextXY[1] - curXY[1]);
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }

    public void translate(float dTime) {

        int nextCellIndex = calcNextIndex(curCell, direction);
        if (this.point==-1) {
            grabCell = nextCellIndex;

            this.point=board[nextCellIndex].getNumberCo();
            curCell = nextCellIndex;
            this.setPosition(board[curCell].boundingBox.x, board[curCell].boundingBox.y);

            grabAnimation.setPosition(board[curCell].boundingBox);
            gameScreen.ListGrabAnimation.add(grabAnimation);

            this.isMoving = false;

            nextCellIndex = calcNextIndex(curCell, direction);
        }

        if(this.point >= 0) {

            translate(dTime, curCell, nextCellIndex);

            if (isReachNextCell(nextCellIndex)) {

                // cap nhat vi tri tay
                this.curCell = nextCellIndex;
                this.boundingBox.setPosition(board[nextCellIndex].getCenterXY()[0] - boundingBox.width / 2,
                        board[nextCellIndex].getCenterXY()[1] - boundingBox.height / 2);

                //cap nhat diem
                this.point--;
                board[nextCellIndex].increaseNumberCo();

                // show grab animation
                this.isMoving = false;
                grabAnimation.setPosition(board[nextCellIndex].boundingBox);
                gameScreen.ListGrabAnimation.add(grabAnimation);

                // kiem tra xem co duoc lay da tiep hay k
                if (this.point==0) {
                    int next1 = calcNextIndex(curCell,direction);
                    int next2 = calcNextIndex(next1, direction);
                    if (board[next1].getNumberCo()==0) {
                        if (board[next2].getNumberCo()==0) { //stop
                            isEndTurn=true;
                        }
                        else { //An linh
                            isShowAnLinh = true;
                            this.grabCell = -1;
                        }
                    }
                    else {
                        if (next1==11 || next1==5) { //stop
                            isEndTurn=true;
                        }
                        else { //continue
                            this.point = -1;
                            this.grabCell = -1;
                        }
                    }

                }
            }
        }

    }

    public boolean checkGrabContinue(int curCell, int direction) {
        int nextCellIndex = calcNextIndex(curCell, direction);
        if (nextCellIndex==5 || nextCellIndex==11) {
            return false;
        }

        if (board[nextCellIndex].getNumberCo()==0 &&
                board[calcNextIndex(nextCellIndex, direction)].getNumberCo()==0) {
            return false;
        }

        return true;
    }

    public boolean checkGrabContinueNumber(int curCell, int direction, int number) {
        int nextCellIndexNumber = calcNextIndexWithNumber(curCell, direction, number);
        if (number == 1 && (nextCellIndexNumber==5 || nextCellIndexNumber==11)) {
            return false;
        }

        if (board[nextCellIndexNumber].getNumberCo()==0) {
            return false;
        }

        return true;
    }

    public boolean isReachNextCell(int nextIndex) {

        float[] nextXY = board[nextIndex].getCenterXY();
        float[] handXY = this.getCenterXY();

        if (direction==1) {
            switch (nextIndex) {
                case 5:
                    if (handXY[0]<=nextXY[0] && handXY[1]>=nextXY[1])
                        return true;
                    break;
                case 11:
                    if (handXY[0]>=nextXY[0] && handXY[1]<=nextXY[1])
                        return true;
                    break;
                case 6: case 7: case 8: case 9: case 10:
                    if (handXY[0]>=nextXY[0])
                        return true;
                    break;
                case 4: case 3: case 2: case 1: case 0:
                    if (handXY[0]<=nextXY[0])
                        return true;
                    break;
                default:
            }

        }

        if (direction==-1) {
            switch (nextIndex) {
                case 5:
                    if (handXY[0]<=nextXY[0] && handXY[1]<=nextXY[1])
                        return true;
                    break;
                case 11:
                    if (handXY[0]>=nextXY[0] && handXY[1]>=nextXY[1])
                        return true;
                    break;
                case 6: case 7: case 8: case 9: case 10:
                    if (handXY[0]<=nextXY[0])
                        return true;
                    break;
                case 4: case 3: case 2: case 1: case 0:
                    if (handXY[0]>=nextXY[0])
                        return true;
                    break;
                default:
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
