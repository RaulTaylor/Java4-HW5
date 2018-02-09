package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.Background;
import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;
import ru.geekbrains.stargame.ship.MainShip;
import ru.geekbrains.stargame.star.Star;

public class GameScreen extends Base2DScreen {

    private static final int STAR_COUNT = 56;
    private static final float STAR_HEIGHT = 0.01f;

    private Texture backgroundTexture;
    private Background background;

    private TextureAtlas atlas;

    private MainShip mainShip;

    private Star star[];
    private BulletPool bulletPool;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        backgroundTexture = new Texture("bg.png");
        background = new Background(new TextureRegion(backgroundTexture));

        atlas = new TextureAtlas("mainAtlas.tpack");


        bulletPool = new BulletPool(atlas);
        mainShip = new MainShip(atlas, bulletPool);

        star = new Star[STAR_COUNT];
        for (int i = 0; i < star.length; i++) {
            star[i] = new Star(atlas, Rnd.nextFloat(-0.005f, 0.005f), Rnd.nextFloat(-0.5f, -0.1f), STAR_HEIGHT);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    public void update(float delta) {
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
        mainShip.update(delta);
        bulletPool.freeAllDestroyedActiveObjects();
        bulletPool.updateActiveObjects(delta);
    }

    public void draw() {
        Gdx.gl.glClearColor(0.7f, 0.3f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < star.length; i++) {
            star[i].draw(batch);
        }
        bulletPool.drawActiveObjects(batch);
        mainShip.draw(batch);
        batch.end();
    }

    @Override
    protected void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
        bulletPool.resize(worldBounds);
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        atlas.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
    }
}
