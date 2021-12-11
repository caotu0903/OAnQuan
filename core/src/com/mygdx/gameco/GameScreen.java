package com.mygdx.gameco;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ListIterator;

public class GameScreen implements Screen {

    //GBBBBBBB
    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;
    private Texture background;
    private TextureAtlas textureAtlas;
    private Texture grabTexture;

    private TextureRegion oCoTextureRegion,backGroundRegion;

    //timing
    private int backgroundOffset;

    //world parameters
    private final int WORLD_WIDTH = 128;
    private final int WORLD_HEIGHT = 72;

    //game objects
    private OCo oCo;
    GrabAnimation grabAnimation;

    GameScreen() {

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

        //set up the texture atlas
        textureAtlas = new TextureAtlas("image.atlas");
        grabTexture = new Texture("grab_hand.png");

        //initialize texture regions
        oCoTextureRegion = textureAtlas.findRegion("5-rock");

        backGroundRegion = textureAtlas.findRegion("o-an-quan");
        backgroundOffset = 0;

        batch = new SpriteBatch();

        //set up game objects
        oCo = new OCo (5, false, false, WORLD_WIDTH*0.386f, WORLD_HEIGHT*0.4f, 15, 15, oCoTextureRegion);

    }

    @Override
    public void render(float delta) {
        batch.begin();

        //Background
        batch.draw(backGroundRegion, 0,0,WORLD_WIDTH,WORLD_HEIGHT);

        //O Co
        oCo.draw(batch);

        //grab animation;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            grabAnimation = new GrabAnimation(grabTexture, 0.6f,
                    new Rectangle(oCo.boundingBox));
            updateAndRenderGrabAnimation(delta);
        }

        batch.end();
    }

    public void updateAndRenderGrabAnimation(float deltaTime) {
        grabAnimation.update(deltaTime);
        if (!grabAnimation.isFinished()) {
            grabAnimation.draw(batch);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {

    }
}
