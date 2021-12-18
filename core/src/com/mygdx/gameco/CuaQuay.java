package com.mygdx.gameco;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class CuaQuay {

    Animation<TextureRegion> cuaQuayAnimation;
    float Timer;

    Rectangle boundingBox;

    public CuaQuay(Texture texture, float totalAnimationTime, float xCenter, float yCenter, float width, float height) {

        this.boundingBox = new Rectangle(xCenter - width/2, yCenter - height/2, width, height);

        //split texture
        TextureRegion[][] textureRegion2D = TextureRegion.split(texture, 474, 382);

        //convert to 1D array
        TextureRegion[] textureRegion1D = new TextureRegion[28];
        int index = 0;
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 1; j++) {
                textureRegion1D[index] = textureRegion2D[i][j];
                index++;
            }
        }

        cuaQuayAnimation = new Animation<>(totalAnimationTime/2, textureRegion1D);
        Timer = 0;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox=boundingBox;
    }

    public void update(float dTime) {
        Timer += dTime;
    }

    public void draw (SpriteBatch batch) {
        batch.draw(cuaQuayAnimation.getKeyFrame(Timer),
                boundingBox.x,
                boundingBox.y,
                boundingBox.width,
                boundingBox.height);
    }

    public void resetTimer() { Timer=0; }

    public boolean isFinished() {
        return cuaQuayAnimation.isAnimationFinished(Timer);
    }

    public void setBoundingBox(OCo oco) {
        this.boundingBox = oco.boundingBox;
    }
}
