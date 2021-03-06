package com.mygdx.pmd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.*;
import com.mygdx.pmd.controller.Controller;
import com.mygdx.pmd.enumerations.Key;
import com.mygdx.pmd.screens.*;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;


/**
 * Created by Cameron on 9/11/2016.
 */
public class PMD extends Game {
    public static final String TITLE = "Pokemon Mystery Dungeon";
    private final String ATLAS_DIRECTORY = "pokemonassets";
    private final String ATLAS_LIST = "atlas.txt";
    private final String SFX_DIRECTORY = "sfx";
    private final String SFX_LIST = "sfx.txt";
    public static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    public static HashMap<Integer, AtomicBoolean> keys;

    public static PScreen dungeonScreen;
    public static PScreen endScreen;
    public static PScreen introScreen;

    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;

    public static AssetManager manager;
    public Controller controller;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        keys = new HashMap<Integer, AtomicBoolean>();
        manager = new AssetManager();
        sprites = new HashMap<String, Sprite>();

        this.loadKeys();
        this.loadAssets();

        introScreen = new IntroScreen(this);
        dungeonScreen = new DungeonScreen(this);
        endScreen = new EndScreen(this);

        this.switchScreen(introScreen);
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        manager.dispose();
        batch.dispose();
    }

    private void loadKeys() {
        for (Key key : Key.values()) {
            keys.put(key.getValue(), new AtomicBoolean(false));
        }
    }

    private void loadAssets() {
        loadAtlases();
        loadSFX();

        manager.finishLoading();
        loadSprites();
    }

    private void loadAtlases() {
        // must use Gdx.files.internal if want to use android as well should work on desktop though
        final FileHandle atlasFolder = Gdx.files.internal(ATLAS_LIST);

        System.out.println("load atlas");
        System.out.println(atlasFolder.name());
        for (final String atlasName : atlasFolder.readString().split("\\r?\\n")) {
            System.out.println(atlasName);
            manager.load(ATLAS_DIRECTORY + "/" + atlasName, TextureAtlas.class);
        }
    }

    private void loadSFX() {
        final FileHandle sfxFolder = Gdx.files.internal(SFX_LIST);

        for (final String sfxName : sfxFolder.readString().split("\\r?\\n")) {
            if (sfxName.endsWith(".wav")) {
                manager.load(SFX_DIRECTORY + "/" + sfxName, Sound.class);
            } else if (sfxName.endsWith(".ogg")) {
                manager.load(SFX_DIRECTORY + "/" + sfxName, Music.class);
            }
        }
    }

    private void loadSprites() {
        System.out.println("loading sprites");
        Array<TextureAtlas> spriteAtlases = new Array<TextureAtlas>();
        manager.getAll(TextureAtlas.class, spriteAtlases);

        for (TextureAtlas atlas : spriteAtlases) {
            System.out.println("load");
            loadImages(atlas);
        }
    }

    private void loadImages(TextureAtlas textureAtlas) {
        for (TextureAtlas.AtlasRegion textureRegion : textureAtlas.getRegions()) {
            Sprite sprite = textureAtlas.createSprite(textureRegion.name);
            sprites.put(textureRegion.name, sprite);
        }
    }

    //TODO add buffer screen system using stacks
    public void switchScreen(PScreen screen) {
        this.setScreen(screen);
    }

    public static boolean isKeyPressed(Key key) { //TODO perhaps add a buffer system for more control later
        return keys.get(key.getValue()).get();
    }

    //I didn't use the keycode because of time sensitivity
    public static boolean isKeyPressed(int keyCode) { //TODO perhaps add a buffer system for more control later
        if (keys.containsKey(keyCode)) {
            return keys.get(keyCode).get();
        } else {
            return false;
        }
    }

    /**
     * Time sensitive key hits - hits are not consecutive
     *
     * @param key the key entered
     * @return true if the key has been pressed after a certain period of time - returns false if the key is not pressed or if the key has been pressed too soon
     */
    public static boolean isKeyPressedTimeSensitive(Key key) {
        if (keys.get(key.getValue()).get()) {
            if (TimeUtils.timeSinceMillis(key.getLastTimeHit()) > key.getTimeLimit()) {
                key.setLastTimeHit(TimeUtils.millis());
                return true;
            }
        }
        return false;
    }
}
