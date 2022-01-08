package com.mygdx.gameco;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.net.Socket;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

import jdk.internal.org.jline.utils.Log;

public class GameScreen implements Screen {

    OperationNetwork operationNetwork;
    String roomID, userName, opponentName;
    boolean canGo;

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
    BitmapFont fontBorrow;

    //direction
    int index;
    int ODuocChon;
    Direction direction;
    boolean setVisibleDirection;
    public LinkedList<GrabAnimation> ListGrabAnimation;

    Stage stage;

    //Cua animation
    Texture textureCuaQuay;
    CuaQuay cuaQuay1, cuaQuay2;

    //lượt chơi
    int turnNumber;
    boolean isGameOver;

    // kiểm tra rải lính
    int spreadingLinhCount;

    boolean isDetectInput;

    // người chơi
    Player[] players;

    Dialog gameOverDialog;

    //Sound/Music
    Sound dropSound;
    Sound grabSound;
    Music[] backgroundMusic;

    GameScreen(OperationNetwork operationNetwork, String roomID, String userName, String opponentName, boolean canGo) {
        this.operationNetwork = operationNetwork;
        this.roomID = roomID;
        this.userName = userName;
        this.opponentName = opponentName;
        this.canGo = canGo;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

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
        hand = new Hand(3, 0.1f, textureAniAndDirec.findRegion("grab"), oCo, this);

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

        // lượt chơi (false - player, true - opponent)
        if (canGo) {
            turnNumber = 0;
        }
        else {
            turnNumber = 1;
        }
        spreadingLinhCount = 0;
        isDetectInput = true;

        // nguoi choi
        players = new Player[2];
        players[0] = new Player(0,0);
        players[1] = new Player(0,0);

        //
        isGameOver = false;

        // sound
        initSoundAndMusic();
    }

    private void initSoundAndMusic() {
        dropSound = Gdx.audio.newSound(Gdx.files.internal("audio/drop_rock.mp3"));
        grabSound = Gdx.audio.newSound(Gdx.files.internal("audio/grab_rock.mp3"));
        backgroundMusic = new Music[3];
        backgroundMusic[0] = Gdx.audio.newMusic(Gdx.files.internal("audio/bensound-ukulele.mp3"));
        backgroundMusic[1] = Gdx.audio.newMusic(Gdx.files.internal("audio/bensound-sunny.mp3"));
        backgroundMusic[2] = Gdx.audio.newMusic(Gdx.files.internal("audio/bensound-buddy.mp3"));
        backgroundMusic[0].play();
        for (int i=0;i<3;i++) {
            final int finalI = i;
            backgroundMusic[i].setVolume(0.1f);
            backgroundMusic[i].setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    backgroundMusic[(finalI +1)%3].play();
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        batch.begin();

        //Background
        batch.draw(backGroundRegion,0,0,WORLD_WIDTH,WORLD_HEIGHT);

        receiveData();

        //O Co
        updateOCo();

        //hud rendering
        updateAndRenderHUD();

        //detect input
        detectInput(delta);

        updateAndRenderGA(delta);

        //update player turn sign
        updatePlayerTurnSign(delta);


        // Hand moving
        updateHandMoving(delta);

        //
        updateAnLinhAnimation();

        // Trải thêm lính
        updateSpreadLinh(delta);

        batch.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            initQuitGameDialog();
        }
        stage.act();
        stage.draw();
    }

    private void initQuitGameDialog() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Dialog quitGameDialog = new Dialog("Quit Game", skin) {

            {
                text("Are you sure you want to quit?");
                button("Sure", "1");
                button("Cancel", "0");
                setScale(0.3f);
                setKeepWithinStage(false);
                setMovable(false);
            }

            @Override
            protected void result(Object object) {
                //xu ly khi click button OK
                String res = (String) object;
                if (res == "1") {
                    //xu ly quit game - ve lai phong cho
                    operationNetwork.CallFinish();
                } else if (res == "0") {
                    setVisible(false);
                }
            }
        };
        quitGameDialog.pack();
        quitGameDialog.setPosition(WORLD_WIDTH / 2 - quitGameDialog.getWidth() / 2 * 0.3f,
                WORLD_HEIGHT / 2 - quitGameDialog.getHeight() / 2 * 0.3f);
        //quitGameDialog.setVisible(false);
        stage.addActor(quitGameDialog);

    }

    private void initGameOverDialog(final String winner) {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        gameOverDialog = new Dialog("Game Over", skin) {

            {
                text("Winner: " + winner);
                button("OK");
                setScale(0.3f);
                setKeepWithinStage(false);
                setMovable(false);
            }

            @Override
            protected void result(Object object) {
                //xu ly khi click button OK
                //dispose();
                //operationNetwork.SendMessage("303" + roomID);
                operationNetwork.CallFinish();
            }
        };
        gameOverDialog.pack();
        gameOverDialog.setPosition(WORLD_WIDTH/2 - gameOverDialog.getWidth()/2*0.3f,
                WORLD_HEIGHT/2 - gameOverDialog.getHeight()/2*0.3f);
        gameOverDialog.setVisible(false);
        stage.addActor(gameOverDialog);
    }

    // kiểm tra 5 ô trống
    private boolean is5EmptyCell() {

        int[] startEnd = getCellIndexOfCurrentTurn();
        int dem = 0;
        for (int i = startEnd[0]; i <= startEnd[1]; i++) {
            if (oCo[i].getNumberCo()==0) {
                dem++;
            }
        }
        return dem==5;
    }

    public void updateSpreadLinh(float dTime){

//        if (spreadingLinhCount==0 && hand.isEndTurn==true) {
//            if (is5EmptyCell()) {
//                spreadingLinhCount=6;
//            }
//        }

        int index=0;
        if (spreadingLinhCount>0) {
            if (spreadingLinhCount==6) {
                index = (turnNumber%2!=0)?13:12;

            }
            else {
                index = (5-spreadingLinhCount) + ((turnNumber%2!=0)?6:0);
            }

            if (this.ListGrabAnimation.isEmpty()) {
                hand.grabAnimation.setPosition(oCo[index].boundingBox);
                this.ListGrabAnimation.add(hand.grabAnimation);
            }
        }
    }

    public void updatePlayerTurnSign(float delta) {
        if (turnNumber%2==0) {
            cuaQuay1.update(delta);
            if (cuaQuay1.isFinished()) {
                cuaQuay1.resetTimer();
            } else {
                cuaQuay1.draw(batch);
            }
        }
        else {
            cuaQuay2.update(delta);
            if (cuaQuay2.isFinished()) {
                cuaQuay2.resetTimer();
            } else {
                cuaQuay2.draw(batch);
            }
        }
    }

    public void switchPlayerTurn() {
        turnNumber++;
    }

    private void updateAnLinhAnimation() {
        //
        if (hand.isShowAnLinh) {
            int next1 = hand.calcNextIndex(hand.curCell, hand.direction);
            int next2 = hand.calcNextIndex(next1, hand.direction);
            if (oCo[next1].getNumberCo() == 0 && hand.checkGrabContinueNumber(hand.curCell, hand.direction, 2) && hand.isFinishMove()) {
                if (this.ListGrabAnimation.size()==0) {
                    hand.curCell=next2;
                    hand.grabAnimation.setPosition(oCo[next2].boundingBox);
                    this.ListGrabAnimation.add(hand.grabAnimation);

                    //sound
                    long id = grabSound.play(0.1f);
                    grabSound.setPitch(id, 2f);

                    int dst = (turnNumber%2==0)?12:13;
                    oCo[dst].setNumberCo(oCo[dst].getNumberCo() + oCo[next2].getNumberCo());
                    oCo[next2].setNumberCo(0);

                    if (oCo[next2].isQuanVang && oCo[next2].isAliveQuan()) {
                        oCo[dst].setQuanVang(true);
                        oCo[next2].setAliveQuan(false);
                    } else if (oCo[next2].isQuanXanh && oCo[next2].isAliveQuan()) {
                        oCo[dst].setQuanXanh(true);
                        oCo[next2].setAliveQuan(false);
                    }
                }
            }
            else{
                hand.isShowAnLinh=false;
                if(spreadingLinhCount==0)
                    hand.isEndTurn=true;
            }
        }
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
        if (isDetectInput==true) {
            if (Gdx.input.isTouched()) {
                xTouch = Gdx.input.getX();
                yTouch = Gdx.input.getY();

                Vector2 touch = viewport.unproject(new Vector2(xTouch, yTouch));
//
//            oCo[5].setNumberCo(xTouch);
//            oCo[11].setNumberCo(yTouch);
                int[] startEnd;
                startEnd = getCellIndexOfCurrentTurn();
                for (i = startEnd[0]; i <= startEnd[1]; i++) {
                    if (oCo[i].boundingBox.contains(touch)) {
                        index = i;
                        ODuocChon = i;
                        break;
                    }
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

    public int[] getCellIndexOfCurrentTurn() {
        int start=0, end=0;
        if (turnNumber%2==0) {
            start = 0; end=4;
        }
        else {
            start = 6; end = 10;
        }
        int []res = {start, end};
        return res;
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
        oCo[0] = new OCo(5, false, false, false, false, WORLD_WIDTH*0.700f, WORLD_HEIGHT*0.415f, 15, 15, oCoThuongRegions[5]);
        oCo[1] = new OCo(5, false, false, false, false, WORLD_WIDTH*0.600f, WORLD_HEIGHT*0.415f, 15, 15, oCoThuongRegions[5]);
        oCo[2] = new OCo(5, false, false, false, false, WORLD_WIDTH*0.500f, WORLD_HEIGHT*0.415f, 15, 15, oCoThuongRegions[5]);
        oCo[3] = new OCo(5, false, false, false, false, WORLD_WIDTH*0.400f, WORLD_HEIGHT*0.415f, 15, 15, oCoThuongRegions[5]);
        oCo[4] = new OCo(5, false, false, false, false, WORLD_WIDTH*0.300f, WORLD_HEIGHT*0.415f, 15, 15, oCoThuongRegions[5]);
        oCo[5] = new OCo(10, true, false, true, true, WORLD_WIDTH*0.200f, WORLD_HEIGHT*0.502f, 10, 20, oCoYellow[0]);
        oCo[6] = new OCo(5, false, false, false, false, WORLD_WIDTH*0.300f, WORLD_HEIGHT*0.590f, 15, 15, oCoThuongRegions[5]);
        oCo[7] = new OCo(5, false, false, false, false, WORLD_WIDTH*0.400f, WORLD_HEIGHT*0.590f, 15, 15, oCoThuongRegions[5]);
        oCo[8] = new OCo(5, false, false, false, false, WORLD_WIDTH*0.500f, WORLD_HEIGHT*0.590f, 15, 15, oCoThuongRegions[5]);
        oCo[9] = new OCo(5, false, false, false, false, WORLD_WIDTH*0.600f, WORLD_HEIGHT*0.590f, 15, 15, oCoThuongRegions[5]);
        oCo[10] = new OCo(5, false, false, false, false, WORLD_WIDTH*0.700f, WORLD_HEIGHT*0.590f, 15, 15, oCoThuongRegions[5]);
        oCo[11] = new OCo(10, false, true, true, true, WORLD_WIDTH*0.800f, WORLD_HEIGHT*0.502f, 10, 20, oCoBlue[0]);
        oCo[12] = new OCo(0, false, false, false, false, WORLD_WIDTH*0.500f, WORLD_HEIGHT*0.180f, 30, 10, oCoThuongRegions[0]);
        oCo[13] = new OCo(0, false, false, false, false, WORLD_WIDTH*0.500f, WORLD_HEIGHT*0.850f, 30, 10, oCoThuongRegions[0]);
    }

    private void resetOCo() {
        oCo[0].setAttribute(5, false, false, false, false, oCoThuongRegions[5]);
        oCo[1].setAttribute(5, false, false, false, false, oCoThuongRegions[5]);
        oCo[2].setAttribute(5, false, false, false, false, oCoThuongRegions[5]);
        oCo[3].setAttribute(5, false, false, false, false, oCoThuongRegions[5]);
        oCo[4].setAttribute(5, false, false, false, false, oCoThuongRegions[5]);
        oCo[5].setAttribute(10, true, false, true, true, oCoYellow[0]);
        oCo[6].setAttribute(5, false, false, false, false, oCoThuongRegions[5]);
        oCo[7].setAttribute(5, false, false, false, false, oCoThuongRegions[5]);
        oCo[8].setAttribute(5, false, false, false, false, oCoThuongRegions[5]);
        oCo[9].setAttribute(5, false, false, false, false, oCoThuongRegions[5]);
        oCo[10].setAttribute(5, false, false, false, false, oCoThuongRegions[5]);
        oCo[11].setAttribute(10, false, true, true, true, oCoBlue[0]);
        oCo[12].setAttribute(0, false, false, false, false, oCoThuongRegions[0]);
        oCo[13].setAttribute(0, false, false, false, false, oCoThuongRegions[0]);
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

        fontParameter.size = 30;
        fontParameter.color = new Color(Color.RED);
        fontBorrow = fontGenerator.generateFont(fontParameter);

        //scale the font to fit world
        font.getData().setScale(0.085f);
        fontBorrow.getData().setScale(0.085f);
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

        fontBorrow.draw(batch, String.format(Locale.getDefault(), "%d", players[0].borrow), WORLD_WIDTH*0.535f, WORLD_HEIGHT*0.100f, 0, Align.left, false);
        fontBorrow.draw(batch, String.format(Locale.getDefault(), "%d", players[1].borrow), WORLD_WIDTH*0.535f, WORLD_HEIGHT*0.756f, 0, Align.left, false);

        //font.draw(batch, String.format(Locale.getDefault(), "Player 1: %d\nPlayer 2: %d", players[0].score, players[1].score), oCo[11].getCenterXY()[0] + oCo[11].boundingBox.width, oCo[11].getCenterXY()[1], 0, Align.left, false);

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

                if (hand.grabCell != -1) {
                    oCo[hand.grabCell].setNumberCo(0);
                }

//                if (spreadingLinhCount == 0) {
//                    hand.isEndTurn = true;
//                }

                if (spreadingLinhCount > 0) {
                    if (spreadingLinhCount != 6) {
                        oCo[5 - spreadingLinhCount + ((turnNumber%2!=0) ? 6 : 0)].setNumberCo(1);
                        int player=12, opponent=13;
                        if (turnNumber%2!=0) {
                            player=13;
                            opponent=12;
                        }

                        int minimum = oCo[player].isQuanVang?10:0 + (oCo[player].isQuanXanh?10:0);
                        if (!(oCo[player].getNumberCo()-1< minimum)) {
                            oCo[player].setNumberCo(oCo[player].getNumberCo() - 1);
                        }
                        else  {
                            players[turnNumber%2].addBorrow(1);
                            oCo[opponent].setNumberCo(oCo[opponent].getNumberCo() - 1);
                        }
                    }

                    spreadingLinhCount--;
                }

                if (hand.isEndTurn == true && spreadingLinhCount==0) {

                    switchPlayerTurn();

                    //kiem tra dieu kien thang
                    if (oCo[5].getNumberCo()==0 && oCo[11].getNumberCo()==0) {
                        isGameOver = true;
                        turnNumber=0;

                        players[0].score = oCo[12].getNumberCo() + players[1].borrow;
                        players[1].score = oCo[13].getNumberCo() + players[0].borrow;

                        for (int i=0; i<5; i++) {
                            players[0].score += oCo[i].getNumberCo();
                            players[1].score += oCo[i+6].getNumberCo();
                        }

                        //show end game dialog
                        String winner="";
                        String SendResultInfo = "402";
                        if (players[0].score>players[1].score) {
                            SendResultInfo += "OAQ/**/win";
                            winner = userName;

                        }
                        else if (players[0].score<players[1].score) {
                            SendResultInfo += "OAQ/**/lose";
                            winner = opponentName;
                        }
                        else {
                            SendResultInfo += "OAQ/**/draw";
                            winner = "Hoa";
                        }
                        operationNetwork.SendMessage(SendResultInfo);

                        initGameOverDialog(winner);
                        gameOverDialog.setVisible(true);

                    }

                    isDetectInput=true;
                    hand.isEndTurn = false;

                    // kiểm tra 5 ô trống để rải thêm quân
                    if (is5EmptyCell()) {
                        spreadingLinhCount=6;
                    }
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
//        else if (number > 10) {
//            if (oCo[i].isQuanXanh()) {
//                oCo[i].setOcoTexture(oCoDiem[4]);
//            }
//            else if (oCo[i].isQuanVang()) {
//                oCo[i].setOcoTexture(oCoDiem[2]);
//            }
//            else {
//                oCo[i].setOcoTexture(oCoDiem[0]);
//            }
//        }
        else if (oCo[i].isQuanXanh) {
            if (number>10) {
                oCo[i].setOcoTexture(oCoDiem[4]);
            }
            else {
                oCo[i].setOcoTexture(oCoDiem[3]);
            }
        }
        else if (oCo[i].isQuanVang) {
            if (number>10) {
                oCo[i].setOcoTexture(oCoDiem[2]);
            }
            else {
                oCo[i].setOcoTexture(oCoDiem[1]);
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

    public void receiveData() {
        String moveMessage = operationNetwork.GetMessage();
        if (!moveMessage.isEmpty()) {
            moveMessage = moveMessage.replaceFirst("400", "");
            String[] listStringMove = moveMessage.split("\\/\\*\\*\\/");

            ODuocChon = Integer.valueOf(listStringMove[0]);

            direction.grabAnimation.setPosition(oCo[ODuocChon].boundingBox);
            ListGrabAnimation.add(direction.grabAnimation);

            //sound
            long id = this.grabSound.play(0.2f);
            this.grabSound.setPitch(id, 2f);

            // set direction for hand
            hand.setDirection(Integer.valueOf(listStringMove[1]));
            hand.setPoint(oCo[ODuocChon].numberCo);
            hand.setCurCell(ODuocChon);
            hand.isEndTurn=false;

            // cap nhat diem cho oCo
            hand.grabCell=ODuocChon;
            //oCO[gs.ODuocChon].setNumberCo(0);

        }
    }
}
