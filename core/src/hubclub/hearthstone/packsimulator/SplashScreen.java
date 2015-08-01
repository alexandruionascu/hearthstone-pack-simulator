
package hubclub.hearthstone.packsimulator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by alexandru on 01.09.2014.
 *
 * ORIGINALLY MADE BY NICU, ON HUBJUMP
 */

public class SplashScreen implements Screen {
    PackGame game;
    Texture background = new Texture(Gdx.files.internal("HubclubSplashScreen.png"));
    SpriteBatch batch = new SpriteBatch();
    boolean trigger = false;
    public SplashScreen (PackGame game){
        this.game = game;
    }
    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        if (trigger){
            //do nothing yet
        }else{
            Timer.schedule(new Timer.Task() {
                public void run() {
                    game.switchToMainMenuScreen();
                }
            }
                    , 2.5f); // 2.5 second pause to admire our logo
            trigger=true;
        }
    }

    @Override
    public void resize(int width, int height) {
// TODO Auto-generated method stub
    }
    @Override
    public void show() {
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
        batch.dispose();
        background.dispose();
    }
}