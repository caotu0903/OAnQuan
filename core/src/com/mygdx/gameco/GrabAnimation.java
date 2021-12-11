package com.mygdx.gameco;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class GrabAnimation {

    Animation<TextureRegion> grabAnimation;
    float grabTimer;

    Rectangle boundingBox;

    public GrabAnimation(Texture texture, float totalAnimationTime, Rectangle boundingBox) {

        this.boundingBox = boundingBox;

        //split texture
        TextureRegion[][] textureRegion2D = TextureRegion.split(texture, 1024, 1024);

        //convert to 1D array
        TextureRegion[] textureRegion1D = new TextureRegion[6];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 6; j++) {
                textureRegion1D[index] = textureRegion2D[i][j];
                index++;
            }
        }

        grabAnimation = new Animation<TextureRegion>(totalAnimationTime/6, textureRegion1D);
        grabTimer = 0;
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

    public boolean isFinished() {
        return grabAnimation.isAnimationFinished(grabTimer);
    }
}
