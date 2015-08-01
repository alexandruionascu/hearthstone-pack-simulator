package hubclub.hearthstone.packsimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

/**
 * Created by alexandru on 22.12.2014.
 */
public class GameScreen implements Screen, InputProcessor {

    private final float CARD_WIDTH = Gdx.graphics.getWidth() / 2;
    private final float CARD_HEIGHT = Gdx.graphics.getHeight() / 3;

    ArrayList<Card> cardList = new ArrayList<Card>();

    SpriteBatch batch;
    Texture backGround;


    long startGameTime = 0; //when the game starts

    private PackGame game;
    private Stage stage;

    public GameScreen(PackGame game) {
        this.game = game;
        stage = new Stage();
    }


    long startTime = 0;
    long toWait = 500; //how much time you have to wait untill the cards flip back

    private int download (byte[] out, String url) {
        InputStream in = null;
        try {
            HttpURLConnection conn = null;
            conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setUseCaches(true);
            conn.connect();
            in = conn.getInputStream();
            int readBytes = 0;
            while (true) {
                int length = in.read(out, readBytes, out.length - readBytes);
                if (length == -1) break;
                readBytes += length;
            }
            return readBytes;
        } catch (Exception ex) {
            return 0;
        } finally {
            StreamUtils.closeQuietly(in);
        }
    }
    //check if the card can be in a pack
    boolean checkCard(JsonValue json, int cardIndex) {

        if(json.get(cardIndex).get("type").asString().equalsIgnoreCase("hero")) {
            return false;
        }

        if(!json.get(cardIndex).get("collectible").asBoolean()) {
            return false;
        }

        if(json.get(cardIndex).get("set").asString().equalsIgnoreCase("basic")) {
            return false;
        }

        return true;
    }

    void addCardToCollection(JsonValue json, int cardIndex, int selected) {
        //download the card image
        byte[] bytes = new byte[500 * 1024]; // assuming the content is not bigger than 500kb.
        //change medium quality to card's original quality
        int numBytes = download(bytes, json.get(cardIndex).get("image").asString().replace("medium", "original"));
        if (numBytes != 0) {
            // load the pixmap, make it a power of two if necessary (not needed for GL ES 2.0!)
            Pixmap pixmap = new Pixmap(bytes, 0, numBytes);
            final int originalWidth = pixmap.getWidth();
            final int originalHeight = pixmap.getHeight();
            int width = MathUtils.nextPowerOfTwo(pixmap.getWidth());
            int height = MathUtils.nextPowerOfTwo(pixmap.getHeight());
            final Pixmap potPixmap = new Pixmap(width, height, pixmap.getFormat());
            potPixmap.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture cardImage = new Texture(pixmap);

            Card card = new Card(selected, cardImage, json.get(cardIndex).get("quality").asString(), false);
            cardList.add(card);
            System.out.println("image " + selected + " downloaded");
        }

    }

    public void drawCards() {

        ArrayList<Integer> randomOrder = new ArrayList<Integer>();
        int uncommon = 0; //number of cards that are rare, epic or legendary

        //get random cards
        //read from json file
        JsonValue json = new JsonReader().parse(Gdx.files.internal("cards.json"));
        int maxNumber = json.size - 1; //maximum range
        int selected = 0;
        Random random = new Random();
        while(selected < 5)
        {
            int cardIndex = random.nextInt(maxNumber);

            //fitler heroes, so there'll be only minions or spells
            if( checkCard(json, cardIndex)) {
                //filter rare
                if(json.get(cardIndex).get("quality").asString().equals("rare")) {
                    int index = random.nextInt(100);
                    if(index % 5 == 0) {
                        selected++;
                        addCardToCollection(json, cardIndex, selected);
                        uncommon++;
                    }
                } else {
                    //filter epic
                    if(json.get(cardIndex).get("quality").asString().equals("epic")) {
                        int index = random.nextInt(100);
                        if(index % 7 == 0) {
                            selected++;
                            addCardToCollection(json, cardIndex, selected);
                            uncommon++;
                        }
                    }
                    else if(json.get(cardIndex).get("quality").asString().equals("legendary")) {
                        //filter legendary
                        int index = random.nextInt(100);
                        if (index % 19 == 0) {
                            selected++;
                            addCardToCollection(json, cardIndex, selected);
                            uncommon++;
                        }
                    } else {
                        //else it is common
                        //if there hasn't been at least a rare card
                        if(!(selected == 4 && uncommon == 0)) {
                            selected++;
                            addCardToCollection(json, cardIndex, selected);
                        }


                    }
                 }


               }
            }

        //shuffle the cards
        Collections.shuffle(cardList);


        //draw first card
        cardList.get(0).addToStage(stage, Gdx.graphics.getWidth() / 2 - (int)CARD_WIDTH / 2, Gdx.graphics.getHeight() - (int)CARD_HEIGHT, (int)CARD_WIDTH, (int)CARD_HEIGHT);
        //draw second card
        cardList.get(1).addToStage(stage, 0, Gdx.graphics.getHeight() / 3, (int)CARD_WIDTH, (int)CARD_HEIGHT);
        //draw third card
        cardList.get(2).addToStage(stage, Gdx.graphics.getWidth() - (int)CARD_WIDTH, Gdx.graphics.getHeight() / 3, (int)CARD_WIDTH, (int)CARD_HEIGHT);
        //draw last two cards
        cardList.get(3).addToStage(stage, 0, 0, (int)CARD_WIDTH, (int)CARD_HEIGHT);
        cardList.get(4).addToStage(stage, Gdx.graphics.getWidth() - (int)CARD_WIDTH, 0, (int)CARD_WIDTH, (int)CARD_HEIGHT);


        //40 cards simulation
        int common = 0;
        int rare = 0;
        int epic = 0;
        int legendary = 0;
        int count = 0;
        while(count < 1000) {
            int cardIndex = random.nextInt(maxNumber);
            //fitler heroes, so there'll be only minions or spells
            if( checkCard(json, cardIndex)) {
                count++;
                switch(json.get(cardIndex).get("quality").asString().charAt(0)) {
                    case 'c' :
                        common++;
                        break;
                    case 'r':
                        rare++;
                        break;
                    case 'e':
                        epic++;
                        break;
                    case 'l' :
                        legendary++;
                        break;
                }

            }
        }


        System.out.println("Common cards: " + common + " Probability: " + (float)((float)common / 10)+ " %");
        System.out.println("Rare cards: " + rare + " Probability: " + (float)((float)rare / 10)+ " %");
        System.out.println("Epic cards: " + epic + " Probability: " + (float)((float)epic / 10)+ " %");
        System.out.println("Legendary cards: " + legendary + " Probability: " + (float)((float)legendary / 10)+ " %");
        //begin waiting to start
        startTime = TimeUtils.millis();

        //begin counting
        startGameTime = TimeUtils.millis();

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        backGround = new Texture(Gdx.files.internal("hearthstone.jpg"));

        stage = new Stage();
        //add done button
        Skin skin = new Skin();
        skin.add("donebutton", new Texture(Gdx.files.internal("done.png")));

        ImageButton doneButton = new ImageButton(skin.getDrawable("donebutton"));
        //add event lisener
        doneButton.addListener(new InputListener() {

                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    game.switchToMainMenuScreen();
                    System.out.println("done clicked");
                    return true;
                }

        });
        //set button width and height
        doneButton.setWidth(Gdx.graphics.getWidth() / 4);
        doneButton.setPosition(0, Gdx.graphics.getHeight() - doneButton.getHeight());





        Gdx.input.setInputProcessor(stage);
        drawCards();
        stage.addActor(doneButton);



    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    void playCardSound(Card card) {
        if(card.isFlipped() == false) {
            //play card turn over sound
            Sound turnSound = Gdx.audio.newSound(Gdx.files.internal("card_turn_over_" + card.getQuality() + ".ogg"));
            turnSound.play(0.5f);
            if(!card.getQuality().equals("common")) {
                Sound announcer = Gdx.audio.newSound(Gdx.files.internal(card.getQuality() + ".ogg"));
                announcer.play();
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backGround, 0 , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if(Gdx.input.isTouched() && TimeUtils.timeSinceMillis(startTime) >= toWait) {

            int x = Gdx.input.getX();
            //reverse the y axis, according to the table layout
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            //check if is the first card from the top
            if(y > Gdx.graphics.getHeight() - CARD_HEIGHT) {
                playCardSound(cardList.get(0));
                cardList.get(0).flip();

            } else {

                if(x > Gdx.graphics.getWidth() / 2) {
                    //the card is in the right side
                    if(y < CARD_HEIGHT) {
                       playCardSound(cardList.get(4));
                        cardList.get(4).flip();
                    } else {

                        playCardSound(cardList.get(2));
                        cardList.get(2).flip();
                    }

                } else {
                    if (y < CARD_HEIGHT) {
                        playCardSound(cardList.get(3));
                        cardList.get(3).flip();
                    } else {
                        playCardSound(cardList.get(1));
                        cardList.get(1).flip();
                    }
                }
            }



            toWait = (long) (Card.DURATION / 2 * 1000);
            startTime = TimeUtils.millis();


        }

        stage.act();
        stage.draw();


    }

    @Override
    public void resize(int arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
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
