package hubclub.hearthstone.packsimulator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PackGame extends Game {

    public MenuScreen menuScreen;
    public SplashScreen splashScreen;
    public GameScreen gameScreen;


    @Override
    public void create () {
        splashScreen = new SplashScreen(this);
        setScreen(splashScreen);
    }

    public void switchToMainMenuScreen() {
        menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
    }

    public void switchToGameScreen() {
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
    }



}
