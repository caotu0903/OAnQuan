package com.mygdx.gameco;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;

    private TextureAtlas textureAtlas,textureAtlas2;

    private TextureRegion[] oCoTexureRegions;
    private TextureRegion oCoTextureRegion,backGroundRegion;

    //timing
    private int backgroundOffset;

    //world parameters
    private final int WORLD_WIDTH = 128;
    private final int WORLD_HEIGHT = 72;

    //game objects
    private OCo[] oCo;

    GameScreen() {

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

        //set up the texture atlas
        textureAtlas = new TextureAtlas("image.atlas");
        textureAtlas2 = new TextureAtlas("image2.atlas");

        //initialize texture regions
        oCoTexureRegions = new TextureRegion[9];
        oCoTexureRegions[0] = textureAtlas.findRegion("0-rock");
        oCoTexureRegions[1] = textureAtlas.findRegion("1-rock");
        oCoTexureRegions[2] = textureAtlas.findRegion("2-rock");
        oCoTexureRegions[3] = textureAtlas.findRegion("3-rock");
        oCoTexureRegions[4] = textureAtlas.findRegion("4-rock");
        oCoTexureRegions[5] = textureAtlas.findRegion("5-rock");
        oCoTexureRegions[6] = textureAtlas.findRegion("6-rock");
        oCoTexureRegions[7] = textureAtlas2.findRegion("7-rock");
        oCoTexureRegions[8] = textureAtlas2.findRegion("n-rock");


        backGroundRegion = textureAtlas2.findRegion("o-an-quan");
        backgroundOffset = 0;

        batch = new SpriteBatch();

        //set up game object
        oCo = new OCo[12];
        oCo[0] = new OCo(5, false, false, WORLD_WIDTH*0.751f, WORLD_HEIGHT*0.4f, 15, 15, oCoTexureRegions[5]);
        oCo[1] = new OCo(5, false, false, WORLD_WIDTH*0.631f, WORLD_HEIGHT*0.4f, 15, 15, oCoTexureRegions[5]);
        oCo[2] = new OCo(5, false, false, WORLD_WIDTH*0.504f, WORLD_HEIGHT*0.4f, 15, 15, oCoTexureRegions[5]);
        oCo[3] = new OCo(5, false, false, WORLD_WIDTH*0.386f, WORLD_HEIGHT*0.4f, 15, 15, oCoTexureRegions[5]);
        oCo[4] = new OCo(5, false, false, WORLD_WIDTH*0.259f, WORLD_HEIGHT*0.4f, 15, 15, oCoTexureRegions[5]);
        oCo[5] = new OCo(5, true, true, WORLD_WIDTH*0.143f, WORLD_HEIGHT*0.501f, 15, 15, oCoTexureRegions[8]);
        oCo[6] = new OCo(5, false, false, WORLD_WIDTH*0.259f, WORLD_HEIGHT*0.605f, 15, 15, oCoTexureRegions[5]);
        oCo[7] = new OCo(5, false, false, WORLD_WIDTH*0.386f, WORLD_HEIGHT*0.605f, 15, 15, oCoTexureRegions[5]);
        oCo[8] = new OCo(5, false, false, WORLD_WIDTH*0.504f, WORLD_HEIGHT*0.605f, 15, 15, oCoTexureRegions[5]);
        oCo[9] = new OCo(5, false, false, WORLD_WIDTH*0.631f, WORLD_HEIGHT*0.605f, 15, 15, oCoTexureRegions[5]);
        oCo[10] = new OCo(5, false, false, WORLD_WIDTH*0.751f, WORLD_HEIGHT*0.605f, 15, 15, oCoTexureRegions[5]);
        oCo[11] = new OCo(5, true, true, WORLD_WIDTH*0.860f, WORLD_HEIGHT*0.501f, 15, 15, oCoTexureRegions[8]);

    }

    @Override
    public void render(float delta) {
        batch.begin();

        //Background
        batch.draw(backGroundRegion, 0,0,WORLD_WIDTH,WORLD_HEIGHT);

        //O Co
        for (int i=0; i<12; i++)
            oCo[i].draw(batch);

        batch.end();
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
