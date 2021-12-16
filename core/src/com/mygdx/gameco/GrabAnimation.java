package com.mygdx.gameco;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class GrabAnimation {

    Animation<TextureRegion> grabAnimation;
    float grabTimer;
    OCo oCo;

    Rectangle boundingBox;

    public GrabAnimation(Texture texture, float totalAnimationTime, OCo oCO) {

        this.oCo = oCO;
        this.boundingBox = oCo.boundingBox;

        //split texture
        TextureRegion[][] textureRegion2D = TextureRegion.split(texture, 1024, 1024);

        //convert to 1D array
        TextureRegion[] textureRegion1D = new TextureRegion[3];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                textureRegion1D[index] = textureRegion2D[i][j];
                index++;
            }
        }

        grabAnimation = new Animation<>(totalAnimationTime/2, textureRegion1D);
        grabTimer = 0;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox=boundingBox;
    }

    public void update(float dTime) {
        grabTimer += dTime;
    }

    public void draw (SpriteBatch batch) {
        batch.draw(grabAnimation.getKeyFrame(grabTimer),
                boundingBox.x,
                boundingBox.y,
                boundingBox.width,
                boundingBox.height);
    }

    public void resetTimer() {
        grabTimer=0;
    }

    public boolean isFinished() {
        return grabAnimation.isAnimationFinished(grabTimer);
    }

    public void setBoundingBox(OCo oco) {
        this.boundingBox = oco.boundingBox;
    }
}
