package com.mygdx.gameco;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Direction {
    TextureRegion leftTexture;
    TextureRegion rightTexture;

    ImageButton leftImgButton;
    ImageButton rightImgButton;

    Viewport viewport;

//    Rectangle leftBoundingBox;
//    Rectangle rightBoundingBox;

    private OCo cell;
    private Stage stage;

    int direction;

    public Direction(TextureRegion leftTexture, TextureRegion rightTexture, Stage stage) {
        this.stage = stage;
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;
//        this.cell = cell;
        direction=0;

//        stage = new Stage(viewport);
//        Gdx.input.setInputProcessor(stage);

//        Rectangle boundingBox = cell.boundingBox;
//        float dimen = boundingBox.height / 2;

        Drawable leftDrawable = new TextureRegionDrawable(new TextureRegion(leftTexture));
        leftImgButton = new ImageButton(leftDrawable);
//        leftImgButton.setPosition(boundingBox.x, boundingBox.y + boundingBox.width / 2 - dimen / 2);
//        leftImgButton.setSize(dimen, dimen);

        Drawable rightDrawable = new TextureRegionDrawable(new TextureRegion(rightTexture));
        rightImgButton = new ImageButton(rightDrawable);
//        rightImgButton.setPosition(boundingBox.x + boundingBox.width - dimen, boundingBox.y + boundingBox.width / 2 - dimen / 2);
//        rightImgButton.setSize(dimen, dimen);

        leftImgButton.setVisible(false);
        rightImgButton.setVisible(false);
        leftImgButton.setDisabled(false);
        rightImgButton.setDisabled(false);

        leftImgButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                direction=-1;
                leftImgButton.setVisible(false);
                rightImgButton.setVisible(false);
                leftImgButton.setDisabled(true);
                rightImgButton.setDisabled(true);
            }
        });
        rightImgButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                direction=1;
                leftImgButton.setVisible(false);
                rightImgButton.setVisible(false);
                leftImgButton.setDisabled(true);
                rightImgButton.setDisabled(true);
            }
        });

        stage.addActor(leftImgButton);
        stage.addActor(rightImgButton);

//        Rectangle boundingBox = cell.boundingBox;
//        float dimen = boundingBox.height / 2;
//
//        this.leftBoundingBox = new Rectangle(boundingBox.x,
//                boundingBox.y + boundingBox.width / 2 - dimen / 2,
//                dimen, dimen);
//        this.rightBoundingBox = new Rectangle(boundingBox.x + boundingBox.width - dimen,
//                boundingBox.y + boundingBox.width / 2 - dimen / 2,
//                dimen, dimen);
    }

    public int getDirection() {

        return direction;
    }

    public void setVisible(boolean left, boolean right) {
        leftImgButton.setVisible(left);
        rightImgButton.setVisible(right);
    }

    public void setDisable(boolean left, boolean right) {
        leftImgButton.setDisabled(left);
        rightImgButton.setDisabled(right);
    }

    public void translate(OCo oCo) {
        Rectangle boundingBox = new Rectangle(oCo.boundingBox);
        float dimen = boundingBox.height / 2;

        leftImgButton.setSize(dimen, dimen);
        //leftImgButton.setPosition(boundingBox.x, boundingBox.y + boundingBox.width / 2 - dimen / 2);
        rightImgButton.setSize(dimen, dimen);
        //rightImgButton.setPosition(boundingBox.x + boundingBox.width - dimen, boundingBox.y + boundingBox.width / 2 - dimen / 2);
        leftImgButton.setPosition(boundingBox.x, boundingBox.y-dimen);
        rightImgButton.setPosition(boundingBox.x + boundingBox.width-dimen, boundingBox.y-dimen);
    }

    public void draw(Batch batch) {
//        batch.draw(leftTexture, leftBoundingBox.x, leftBoundingBox.y,
//                leftBoundingBox.width, leftBoundingBox.height);
//        batch.draw(rightTexture, rightBoundingBox.x, rightBoundingBox.y,
//                rightBoundingBox.width, rightBoundingBox.height);
//        batch.draw(cross, cell.x + cell.width/2 - dimen/2, cell.y,
//                dimen, dimen);

        stage.act();
        stage.draw();
    }
}
