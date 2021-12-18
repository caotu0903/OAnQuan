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

    OCo[] oCO;

    private Stage stage;

    //int direction;
    GameScreen gs;

    Texture grabAni;
    GrabAnimation grabAnimation;

    public Direction(TextureRegion leftTexture, TextureRegion rightTexture, Stage stage, final OCo[] oCo, GameScreen gss) {
        this.stage = stage;
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;
        this.oCO = oCo;
        this.gs = gss;
        //direction=0;

        grabAni = new Texture("grab_hand.png");
        grabAnimation = new GrabAnimation(grabAni, 0.6f, oCO[0]);

        Drawable leftDrawable = new TextureRegionDrawable(new TextureRegion(leftTexture));
        leftImgButton = new ImageButton(leftDrawable);

        Drawable rightDrawable = new TextureRegionDrawable(new TextureRegion(rightTexture));
        rightImgButton = new ImageButton(rightDrawable);

        leftImgButton.setVisible(false);
        rightImgButton.setVisible(false);
        leftImgButton.setDisabled(false);
        rightImgButton.setDisabled(false);

        leftImgButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                //direction = -1;

                gs.AnDirection();
                gs.setIndex(-1);

                grabAnimation.setPosition(oCO[gs.ODuocChon].boundingBox);
                gs.ListGrabAnimation.add(grabAnimation);

                // set direction for hand
                gs.hand.setDirection(1);
                gs.hand.setPoint(oCO[gs.ODuocChon].numberCo);
                gs.hand.setCurCell(gs.ODuocChon);

                // cap nhat diem cho oCo
                oCO[gs.ODuocChon].setNumberCo(0);
            }
        });

        rightImgButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                //direction = 1;

                gs.AnDirection();
                gs.setIndex(-1);

                grabAnimation.setPosition(oCO[gs.ODuocChon].boundingBox);
                gs.ListGrabAnimation.add(grabAnimation);

                // set direction for hand
                gs.hand.setDirection(-1);
                gs.hand.setPoint(oCO[gs.ODuocChon].numberCo);
                gs.hand.setCurCell(gs.ODuocChon);

                // cap nhat diem cho oCo
                oCO[gs.ODuocChon].setNumberCo(0);
            }
        });

        stage.addActor(leftImgButton);
        stage.addActor(rightImgButton);
    }

//    public int getDirection() {
//
//        return direction;
//    }

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
        rightImgButton.setSize(dimen, dimen);
        leftImgButton.setPosition(boundingBox.x, boundingBox.y-dimen);
        rightImgButton.setPosition(boundingBox.x + boundingBox.width-dimen, boundingBox.y-dimen);
    }

    public void draw(Batch batch) {
        stage.act();
        stage.draw();
    }
}
