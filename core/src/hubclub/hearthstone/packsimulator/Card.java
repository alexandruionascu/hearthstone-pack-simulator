package hubclub.hearthstone.packsimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

/**
 * Created by alexandru on 22.12.2014.
 */
public class Card extends Actor {

    public static final float DURATION = 0.8f;

    private Image backImage;
    private Image frontImage;

    private float width;
    private float height;

    private float x;

    private float y;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    private int type; //number from 1 to 6

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    private String quality;
    private boolean golden;

    public Image getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(Image frontImage) {
        this.frontImage = frontImage;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    private boolean flipped = false;
    private Vector2 position;



    public Card(int type, Texture texture, String quality, boolean golden) {
        this.type = type;
        this.backImage = new Image(texture);
        backImage.addAction(Actions.scaleTo(0, 1));
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.quality = quality;
        this.golden = golden;
    }

    public void addToStage(Stage stage, int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        System.out.println("Drawn at " + x + " " + y + " Dimensions " + width +  " " + height);

        Texture texture = new Texture(Gdx.files.internal("Fuego_Back.png"));
        frontImage = new Image(texture);
        frontImage.setSize(width,height);
        frontImage.setPosition(x,y);

        backImage.setPosition(x, y);
        backImage.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getWidth() / 2 * 4/3);
        backImage.addAction(Actions.scaleTo(0, 1));
        stage.addActor(frontImage);
        stage.addActor(backImage);
    }

    public void removeFromStage() {
        frontImage.addAction(Actions.scaleTo(1,0));
        backImage.addAction(Actions.delay(DURATION));
        backImage.addAction(Actions.scaleTo(1, 0, DURATION));

        frontImage.remove();
        backImage.remove();
    }

    public void flip() {
        frontImage.addAction(Actions.scaleTo(0, 1, DURATION / 2));
        backImage.addAction(Actions.delay(DURATION / 2));
        backImage.addAction(Actions.scaleTo(1, 1, DURATION / 2));

        flipped = true;

    }

    public void unflip() {
        backImage.addAction(Actions.scaleTo(0, 1, DURATION / 2));
        frontImage.addAction(Actions.delay(DURATION / 2));
        frontImage.addAction(Actions.scaleTo(1, 1, DURATION / 2));

    }

    public Image getBackImage() {
        return backImage;
    }

    public void setBackImage(Image backImage) {
        this.backImage = backImage;
    }
}
