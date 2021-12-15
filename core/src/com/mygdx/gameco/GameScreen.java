package com.mygdx.gameco;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

public class GameScreen implements Screen {

    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;

    private TextureAtlas textureOCo, textureBackground, textureAniAndDirec;

    private TextureRegion[] oCoThuongRegions, oCoYellow, oCoBlue, AnimationAndDirection;
    private TextureRegion backGroundRegion;

    //timing
    private int backgroundOffset;

    //world parameters
    private final int WORLD_WIDTH = 128;
    private final int WORLD_HEIGHT = 72;

    //game objects
    private OCo[] oCo;

    //Head-Up Display
    BitmapFont font;

    //direction
    int nextCell;
    int originCell;
    boolean isShowDirection;
    int index;

    GameScreen() {

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

        //set up the texture atlas
        textureOCo = new TextureAtlas("o_co_image.atlas");
        textureBackground = new TextureAtlas("background_image.atlas");
        textureAniAndDirec = new TextureAtlas("animation_image.atlas");


        //initialize texture regions
        initTextureRegion();

        backGroundRegion = textureBackground.findRegion("o-an-quan");
        backgroundOffset = 0;

        batch = new SpriteBatch();

        //set up game object
        initCellArray();

        //set up direction and animation
        initAnimationAndDirec();

        prepareHUD();

        nextCell = -1;
        originCell = -1;
        index=-1;
        isShowDirection=false;
    }

    @Override
    public void render(float delta) {
        batch.begin();

        //Background
        batch.draw(backGroundRegion, 0,0,WORLD_WIDTH,WORLD_HEIGHT);

        //O Co
        for (int i=0; i<12; i++)
            oCo[i].draw(batch);

        //hud rendering
        updateAndRenderHUD();

        //detect input
        detectInput(delta);

        batch.end();
    }

    private void detectInput(float dTime) {

        if (Gdx.input.isTouched()) {

            float xTouch = Gdx.input.getX();
            float yTouch = Gdx.input.getY();

            Vector2 touch = viewport.unproject(new Vector2(xTouch, yTouch));

            for (int i=0; i<oCo.length;i++) {
                if (oCo[i].boundingBox.contains(touch)) {
                    index = i;
                    break;
                }
            }
        }

        if (index<=11 && index>=0) {
            Rectangle cell = oCo[index].boundingBox;
            if (!oCo[index].isQuan) {
                float dimen = cell.height / 3;
                Texture left = new Texture("left.png");
                Texture right = new Texture("right.png");
                Texture cross = new Texture("cross.png");
                batch.draw(left, cell.x, cell.y + cell.width / 2 - dimen / 2,
                        dimen, dimen);
                batch.draw(right, cell.x + cell.width - dimen, cell.y + cell.width / 2 - dimen / 2,
                        dimen, dimen);
                batch.draw(cross, cell.x + cell.width/2 - dimen/2, cell.y,
                        dimen, dimen);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    private void initCellArray() {
        oCo = new OCo[12];
        oCo[0] = new OCo(5, false, false, WORLD_WIDTH*0.751f, WORLD_HEIGHT*0.4f, 15, 15, oCoThuongRegions[5]);
        oCo[1] = new OCo(5, false, false, WORLD_WIDTH*0.631f, WORLD_HEIGHT*0.4f, 15, 15, oCoThuongRegions[5]);
        oCo[2] = new OCo(5, false, false, WORLD_WIDTH*0.504f, WORLD_HEIGHT*0.4f, 15, 15, oCoThuongRegions[5]);
        oCo[3] = new OCo(5, false, false, WORLD_WIDTH*0.386f, WORLD_HEIGHT*0.4f, 15, 15, oCoThuongRegions[5]);
        oCo[4] = new OCo(5, false, false, WORLD_WIDTH*0.259f, WORLD_HEIGHT*0.4f, 15, 15, oCoThuongRegions[5]);
        oCo[5] = new OCo(10, true, true, WORLD_WIDTH*0.143f, WORLD_HEIGHT*0.501f, 10, 20, oCoYellow[0]);
        oCo[6] = new OCo(5, false, false, WORLD_WIDTH*0.259f, WORLD_HEIGHT*0.605f, 15, 15, oCoThuongRegions[5]);
        oCo[7] = new OCo(5, false, false, WORLD_WIDTH*0.386f, WORLD_HEIGHT*0.605f, 15, 15, oCoThuongRegions[5]);
        oCo[8] = new OCo(5, false, false, WORLD_WIDTH*0.504f, WORLD_HEIGHT*0.605f, 15, 15, oCoThuongRegions[5]);
        oCo[9] = new OCo(5, false, false, WORLD_WIDTH*0.631f, WORLD_HEIGHT*0.605f, 15, 15, oCoThuongRegions[5]);
        oCo[10] = new OCo(5, false, false, WORLD_WIDTH*0.751f, WORLD_HEIGHT*0.605f, 15, 15, oCoThuongRegions[5]);
        oCo[11] = new OCo(10, true, true, WORLD_WIDTH*0.860f, WORLD_HEIGHT*0.501f, 10, 20, oCoBlue[0]);
    }

    private void initTextureRegion() {
        oCoThuongRegions = new TextureRegion[9];
        oCoThuongRegions[0] = textureOCo.findRegion("0-rock");
        oCoThuongRegions[1] = textureOCo.findRegion("1-rock");
        oCoThuongRegions[2] = textureOCo.findRegion("2-rock");
        oCoThuongRegions[3] = textureOCo.findRegion("3-rock");
        oCoThuongRegions[4] = textureOCo.findRegion("4-rock");
        oCoThuongRegions[5] = textureOCo.findRegion("5-rock");
        oCoThuongRegions[6] = textureOCo.findRegion("6-rock");
        oCoThuongRegions[7] = textureOCo.findRegion("7-rock");
        oCoThuongRegions[8] = textureOCo.findRegion("n-rock");

        oCoYellow = new TextureRegion[10];
        oCoYellow[0] = textureOCo.findRegion("yellow-0");
        oCoYellow[1] = textureOCo.findRegion("yellow-1");
        oCoYellow[2] = textureOCo.findRegion("yellow-2");
        oCoYellow[3] = textureOCo.findRegion("yellow-3");
        oCoYellow[4] = textureOCo.findRegion("yellow-4");
        oCoYellow[5] = textureOCo.findRegion("yellow-5");
        oCoYellow[6] = textureOCo.findRegion("yellow-6");
        oCoYellow[7] = textureOCo.findRegion("yellow-7");
        oCoYellow[8] = textureOCo.findRegion("yellow-many");
        oCoYellow[9] = textureOCo.findRegion("quan-null");

        oCoBlue = new TextureRegion[10];
        oCoBlue[0] = textureOCo.findRegion("blue-0");
        oCoBlue[1] = textureOCo.findRegion("blue-1");
        oCoBlue[2] = textureOCo.findRegion("blue-2");
        oCoBlue[3] = textureOCo.findRegion("blue-3");
        oCoBlue[4] = textureOCo.findRegion("blue-4");
        oCoBlue[5] = textureOCo.findRegion("blue-5");
        oCoBlue[6] = textureOCo.findRegion("blue-6");
        oCoBlue[7] = textureOCo.findRegion("blue-7");
        oCoBlue[8] = textureOCo.findRegion("blue-many");
        oCoBlue[9] = textureOCo.findRegion("quan-null");
    }

    private void initAnimationAndDirec() {
        AnimationAndDirection = new TextureRegion[5];
        AnimationAndDirection[0] = textureAniAndDirec.findRegion("cross");
        AnimationAndDirection[1] = textureAniAndDirec.findRegion("grab");
        AnimationAndDirection[2] = textureAniAndDirec.findRegion("grab_hand");
        AnimationAndDirection[3] = textureAniAndDirec.findRegion("left");
        AnimationAndDirection[4] = textureAniAndDirec.findRegion("right");
    }

    private void prepareHUD() {
        //Create a BitmapFont from our font file
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("PartyConfettiRegular-eZOn3.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 30;
        fontParameter.color = new Color(Color.BLACK);

        font = fontGenerator.generateFont(fontParameter);

        //scale the font to fit world
        font.getData().setScale(0.085f);
    }

    private void updateAndRenderHUD() {
        //render
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[0].getNumberCo()), WORLD_WIDTH*0.6993f, WORLD_HEIGHT*0.330f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[1].getNumberCo()), WORLD_WIDTH*0.580f, WORLD_HEIGHT*0.330f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[2].getNumberCo()), WORLD_WIDTH*0.455f, WORLD_HEIGHT*0.330f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[3].getNumberCo()), WORLD_WIDTH*0.335f, WORLD_HEIGHT*0.330f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[4].getNumberCo()), WORLD_WIDTH*0.2073f, WORLD_HEIGHT*0.330f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[5].getNumberCo()), WORLD_WIDTH*0.174f, WORLD_HEIGHT*0.340f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[6].getNumberCo()), WORLD_WIDTH*0.2073f, WORLD_HEIGHT*0.535f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[7].getNumberCo()), WORLD_WIDTH*0.335f, WORLD_HEIGHT*0.535f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[8].getNumberCo()), WORLD_WIDTH*0.455f, WORLD_HEIGHT*0.535f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[9].getNumberCo()), WORLD_WIDTH*0.580f, WORLD_HEIGHT*0.535f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[10].getNumberCo()), WORLD_WIDTH*0.6993f, WORLD_HEIGHT*0.535f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[11].getNumberCo()), WORLD_WIDTH*0.820f, WORLD_HEIGHT*0.344f, 0, Align.left, false);
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
