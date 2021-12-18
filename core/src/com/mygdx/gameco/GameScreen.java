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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

import jdk.internal.org.jline.utils.Log;

public class GameScreen implements Screen {

    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    public SpriteBatch batch;

    private TextureAtlas textureOCo, textureBackground, textureAniAndDirec, textureODiem;

    private TextureRegion[] oCoThuongRegions, oCoYellow, oCoBlue, oCoDiem, AnimationAndDirection;
    private TextureRegion backGroundRegion;

    //timing
    private int backgroundOffset;

    //world parameters
    private final int WORLD_WIDTH = 128;
    private final int WORLD_HEIGHT = 72;

    //game objects
    private OCo[] oCo;
    Hand hand;

    //Head-Up Display
    BitmapFont font;

    //direction
    int index;
    int ODuocChon;
    Direction direction;
    boolean setVisibleDirection;
    public LinkedList<GrabAnimation> ListGrabAnimation;

    Stage stage;

    //test ne
    Texture textureCuaQuay;
    CuaQuay cuaQuay1, cuaQuay2;

    GameScreen() {

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        //set up the texture atlas
        textureOCo = new TextureAtlas("o_co_image.atlas");
        textureBackground = new TextureAtlas("background_image.atlas");
        textureAniAndDirec = new TextureAtlas("animation_image.atlas");
        textureODiem = new TextureAtlas("o_diem_image.atlas");


        //initialize texture regions
        initTextureRegion();

        backGroundRegion = textureBackground.findRegion("bien_background");
        backgroundOffset = 0;

        batch = new SpriteBatch();

        //set up game object
        initCellArray();
        hand = new Hand(3, 0.5f, textureAniAndDirec.findRegion("grab"), oCo, this);

        //set up direction and animation
        initAnimationAndDirec();

        prepareHUD();

        index=-1;
        ODuocChon=-1;

        direction = new Direction(AnimationAndDirection[2], AnimationAndDirection[3], stage, oCo, this);

        setVisibleDirection = false;

        ListGrabAnimation = new LinkedList<>();

        // test ne
        textureCuaQuay = new Texture("cua_quay.png");
        cuaQuay1 = new CuaQuay(textureCuaQuay, 0.05f, WORLD_WIDTH*0.332f, WORLD_HEIGHT*0.230f, 15, 15);
        cuaQuay2 = new CuaQuay(textureCuaQuay, 0.05f, WORLD_WIDTH*0.693f, WORLD_HEIGHT*0.800f, 15, 15);
    }

    @Override
    public void render(float delta) {
        batch.begin();

        //Background
        batch.draw(backGroundRegion,0,0,WORLD_WIDTH,WORLD_HEIGHT);

        //O Co
        updateOCo();

        //hud rendering
        updateAndRenderHUD();

        //detect input
        detectInput(delta);

        updateAndRenderGA(delta);

        cuaQuay1.update(delta);
        if (cuaQuay1.isFinished()) {
            cuaQuay1.resetTimer();
        } else {
            cuaQuay1.draw(batch);
        }

        cuaQuay2.update(delta);
        if (cuaQuay2.isFinished()) {
            cuaQuay2.resetTimer();
        } else {
            cuaQuay2.draw(batch);
        }


        // Hand moving
        updateHandMoving(delta);

        batch.end();

        stage.act();
        stage.draw();
    }

    private void updateHandMoving(float dTime) {
        if (!hand.isFinishMove()) {
            if (hand.isMoving) {
                hand.translate(dTime);
                hand.draw(batch);
            }
        }
        else {
            hand.isMoving=false;
        }
    }

    private void detectInput(float dTime) {
        //isShowDirection=false;
        int xTouch, yTouch;
        int i=-1;
        if (Gdx.input.isTouched()) {
            xTouch = Gdx.input.getX();
            yTouch = Gdx.input.getY();

            Vector2 touch = viewport.unproject(new Vector2(xTouch, yTouch));
//
//            oCo[5].setNumberCo(xTouch);
//            oCo[11].setNumberCo(yTouch);
            for (i=0; i<5;i++) {
                if (oCo[i].boundingBox.contains(touch)) {
                    index = i;
                    ODuocChon = i;
                    break;
                }
            }
        }

        //Direction direction = null;
        if (index>=0 && index<=4) {
            if (!oCo[index].isQuan) {
                direction.translate(oCo[index]);
                direction.setVisible(true, true);
                direction.setDisable(false, false);
            }
        }

        if (setVisibleDirection) {
            direction.setVisible(false, false);
            direction.setDisable(true, true);
            setVisibleDirection = false;
        }

    }

    public void setIndex(int id) {
        index = id;
    }

    public void AnDirection() {
        setVisibleDirection = true;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    private void initCellArray() {
        oCo = new OCo[14];
        oCo[0] = new OCo(1, false, false, false, false, WORLD_WIDTH*0.700f, WORLD_HEIGHT*0.415f, 15, 15, oCoThuongRegions[5]);
        oCo[1] = new OCo(1, false, false, false, false, WORLD_WIDTH*0.600f, WORLD_HEIGHT*0.415f, 15, 15, oCoThuongRegions[5]);
        oCo[2] = new OCo(0, false, false, false, false, WORLD_WIDTH*0.500f, WORLD_HEIGHT*0.415f, 15, 15, oCoThuongRegions[5]);
        oCo[3] = new OCo(1, false, false, false, false, WORLD_WIDTH*0.400f, WORLD_HEIGHT*0.415f, 15, 15, oCoThuongRegions[5]);
        oCo[4] = new OCo(0, false, false, false, false, WORLD_WIDTH*0.300f, WORLD_HEIGHT*0.415f, 15, 15, oCoThuongRegions[5]);
        oCo[5] = new OCo(1, true, false, true, true, WORLD_WIDTH*0.200f, WORLD_HEIGHT*0.502f, 10, 20, oCoYellow[0]);
        oCo[6] = new OCo(0, false, false, false, false, WORLD_WIDTH*0.300f, WORLD_HEIGHT*0.590f, 15, 15, oCoThuongRegions[5]);
        oCo[7] = new OCo(1, false, false, false, false, WORLD_WIDTH*0.400f, WORLD_HEIGHT*0.590f, 15, 15, oCoThuongRegions[5]);
        oCo[8] = new OCo(0, false, false, false, false, WORLD_WIDTH*0.500f, WORLD_HEIGHT*0.590f, 15, 15, oCoThuongRegions[5]);
        oCo[9] = new OCo(1, false, false, false, false, WORLD_WIDTH*0.600f, WORLD_HEIGHT*0.590f, 15, 15, oCoThuongRegions[5]);
        oCo[10] = new OCo(0, false, false, false, false, WORLD_WIDTH*0.700f, WORLD_HEIGHT*0.590f, 15, 15, oCoThuongRegions[5]);
        oCo[11] = new OCo(1, false, true, true, true, WORLD_WIDTH*0.800f, WORLD_HEIGHT*0.502f, 10, 20, oCoBlue[0]);
        oCo[12] = new OCo(0, false, false, false, false, WORLD_WIDTH*0.500f, WORLD_HEIGHT*0.180f, 30, 10, oCoThuongRegions[0]);
        oCo[13] = new OCo(0, false, false, false, false, WORLD_WIDTH*0.500f, WORLD_HEIGHT*0.850f, 30, 10, oCoThuongRegions[0]);
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

        oCoDiem = new TextureRegion[7];
        oCoDiem[0] = textureODiem.findRegion("n");
        oCoDiem[1] = textureODiem.findRegion("yellow");
        oCoDiem[2] = textureODiem.findRegion("yellow-n");
        oCoDiem[3] = textureODiem.findRegion("blue");
        oCoDiem[4] = textureODiem.findRegion("blue-n");
        oCoDiem[5] = textureODiem.findRegion("yellow-blue");
        oCoDiem[6] = textureODiem.findRegion("yellow-blue-n");
    }

    private void initAnimationAndDirec() {
        AnimationAndDirection = new TextureRegion[4];
        AnimationAndDirection[0] = textureAniAndDirec.findRegion("cross");
        AnimationAndDirection[1] = textureAniAndDirec.findRegion("grab");
        AnimationAndDirection[2] = textureAniAndDirec.findRegion("left");
        AnimationAndDirection[3] = textureAniAndDirec.findRegion("right");
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
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[0].getNumberCo()), WORLD_WIDTH*0.661f, WORLD_HEIGHT*0.353f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[1].getNumberCo()), WORLD_WIDTH*0.559f, WORLD_HEIGHT*0.353f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[2].getNumberCo()), WORLD_WIDTH*0.458f, WORLD_HEIGHT*0.353f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[3].getNumberCo()), WORLD_WIDTH*0.358f, WORLD_HEIGHT*0.353f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[4].getNumberCo()), WORLD_WIDTH*0.258f, WORLD_HEIGHT*0.353f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[5].getNumberCo()), WORLD_WIDTH*0.225f, WORLD_HEIGHT*0.353f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[6].getNumberCo()), WORLD_WIDTH*0.258f, WORLD_HEIGHT*0.535f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[7].getNumberCo()), WORLD_WIDTH*0.358f, WORLD_HEIGHT*0.535f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[8].getNumberCo()), WORLD_WIDTH*0.458f, WORLD_HEIGHT*0.535f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[9].getNumberCo()), WORLD_WIDTH*0.559f, WORLD_HEIGHT*0.535f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[10].getNumberCo()), WORLD_WIDTH*0.661f, WORLD_HEIGHT*0.535f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[11].getNumberCo()), WORLD_WIDTH*0.755f, WORLD_HEIGHT*0.353f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[12].getNumberCo()), WORLD_WIDTH*0.485f, WORLD_HEIGHT*0.100f, 0, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", oCo[13].getNumberCo()), WORLD_WIDTH*0.485f, WORLD_HEIGHT*0.756f, 0, Align.left, false);
    }

    public void updateAndRenderGA(float deltaTime) {
        ListIterator<GrabAnimation> GAListIterator = ListGrabAnimation.listIterator();
        while (GAListIterator.hasNext()) {
            GrabAnimation ga = GAListIterator.next();
            ga.update(deltaTime);
            if (ga.isFinished()) {
                ga.resetTimer();
                GAListIterator.remove();

                // hand continue moving
                hand.isMoving = true;

                if (hand.grabCell!=-1) {
                    oCo[hand.grabCell].setNumberCo(0);
                }
            } else {
                ga.draw(batch);
            }
        }
    }

    public void updateOCo() {
        for (int i=0; i<14; i++) {
            if (i < 12) {
                if (!oCo[i].isQuan()) {
                    ChooseTextureOThuong(i, oCo[i].getNumberCo());
                } else {
                    ChooseTextureOQuan(i, oCo[i].getNumberCo());
                }
            }
            else {
                ChooseTextureODiem(i, oCo[i].getNumberCo());
            }
            oCo[i].draw(batch);
        }
    }

    public void ChooseTextureOThuong(int i, int number) {
        if (number <= 7)
            oCo[i].setOcoTexture(oCoThuongRegions[number]);
        else if (number > 7)
            oCo[i].setOcoTexture(oCoThuongRegions[8]);
    }

    public void ChooseTextureOQuan(int i, int number) {
        if (oCo[i].isAliveQuan && number >= 10) {
            if (i == 11 && number <= 17)
                oCo[i].setOcoTexture(oCoBlue[number - 10]);
            else if (i == 11 && number > 17)
                oCo[i].setOcoTexture(oCoBlue[8]);
            else if (i == 5 && number <= 17)
                oCo[i].setOcoTexture(oCoYellow[number - 10]);
            else if (i == 5 && number > 17)
                oCo[i].setOcoTexture(oCoYellow[8]);
        }
        else {
            if (number <= 7)
                oCo[i].setOcoTexture(oCoThuongRegions[number]);
            else if (number > 7)
                oCo[i].setOcoTexture(oCoThuongRegions[8]);
        }
    }

    public void ChooseTextureODiem(int i, int number) {
        if (number == 0) {
            oCo[i].setOcoTexture(oCoThuongRegions[0]);
        }
        else if (oCo[i].isQuanXanh() && oCo[i].isQuanVang()) {
            if (number > 20) {
                oCo[i].setOcoTexture(oCoDiem[6]);
            }
            else {
                oCo[i].setOcoTexture(oCoDiem[5]);
            }
        }
        else if (number > 10) {
            if (oCo[i].isQuanXanh()) {
                oCo[i].setOcoTexture(oCoDiem[4]);
            }
            else if (oCo[i].isQuanVang()) {
                oCo[i].setOcoTexture(oCoDiem[2]);
            }
            else {
                oCo[i].setOcoTexture(oCoDiem[0]);
            }
        }
        else {
            oCo[i].setOcoTexture(oCoDiem[0]);
        }
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
