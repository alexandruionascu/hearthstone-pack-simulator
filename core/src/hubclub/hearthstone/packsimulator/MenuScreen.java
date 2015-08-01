package hubclub.hearthstone.packsimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.*;

/**
 * Created by alexandru on 22.12.2014.
 */
public class MenuScreen implements Screen, InputProcessor {

    private PackGame game;
    private Stage stage;
    private BitmapFont font;
    private BitmapFont font2;
    private SpriteBatch batch;
    private TextButton playButton;
    private TextButton.TextButtonStyle style;
    private TextButton.TextButtonStyle style2;
    private TextButton scoreButton;
    private TextButton title;
    private TextButton currentScore;
    private long score;
    private Texture backGround;
    Skin skin;

    public MenuScreen(PackGame game) {
        this.game = game;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        stage = new Stage();
        style = new TextButton.TextButtonStyle();
        style2 = new TextButton.TextButtonStyle();


        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("FuegoFont_64.fnt"));
        font2 = new BitmapFont(Gdx.files.internal("FuegoFont_32.fnt"));

        backGround = new Texture(Gdx.files.internal("hearthstone.jpg"));


        style2.font = font2;
        style.font = font;
        font.scale(0.1f);

        title = new TextButton("Pack Simulator", style2);

        title.setPosition(Gdx.graphics.getWidth() /2 - font2.getBounds("Pack Simulator").width / 2, Gdx.graphics.getHeight() - font.getBounds("Pack Simulator").height);
        //todo: resize button according to screen size
        playButton = new TextButton("Open pack", style);
        playButton.setPosition(Gdx.graphics.getWidth() /2 - (font.getBounds("Open pack").width /2), Gdx.graphics.getHeight() / 2);



        skin = new Skin();
        skin.add("pack", new Texture(Gdx.files.internal("pack.png")));
        ImageButton packButton = new ImageButton(skin.getDrawable("pack"));
        packButton.setPosition(Gdx.graphics.getWidth() / 2 - packButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - packButton.getHeight() / 2);
        packButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.switchToGameScreen();
                return true;
            }
        });

        stage.addActor(packButton);





        stage.addActor(title);

       // stage.addActor(playButton);

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
// TODO Auto-generated method stub
    }
    @Override
    public void hide() {
// TODO Auto-generated method stub
    }
    @Override
    public void pause() {
// TODO Auto-generated method stub
    }
    @Override
    public void resume() {
// TODO Auto-generated method stub
    }
    @Override
    public void dispose() {
//game.dispose();
// background.dispose();
// shape.dispose();
// batch.dispose();
// font.dispose();
// TODO Auto-generated method stub
    }
    @Override
    public boolean keyDown(int keycode) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
// TODO Auto-generated method stub
        return false;
    }
}
