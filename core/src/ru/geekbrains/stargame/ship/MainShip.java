package ru.geekbrains.stargame.ship;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.LinkedList;

import ru.geekbrains.stargame.bullet.Bullet;
import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;

public class MainShip extends Sprite {

    private static final float SHIP_HEIGHT = 0.15f;
    private static final float BOTTOM_MARGIN = 0.05f;
    private static final float COOLDOWN_FIRE = 0.2f;

    private final Vector2 v0 = new Vector2(0.5f, 0.0f);
    private final Vector2 v = new Vector2();

    private boolean pressedLeft;
    private boolean pressedRight;
    private boolean presedFire;
    private float time;

    private LinkedList<Integer> currentPointers;
    private HashMap<Integer, Vector2> mapPointers;

    private Rect worldBounds;

    private BulletPool bulletPool;


    public MainShip(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        setHeightProportion(SHIP_HEIGHT);
        this.bulletPool = bulletPool;
        currentPointers = new LinkedList<Integer>();
        mapPointers = new HashMap<Integer, Vector2>();
    }

    @Override
    public void update(float delta) {

        time+=delta;
        if(time>COOLDOWN_FIRE && presedFire){
            time = 0;
            fire();
        }

        if(!(pressedLeft || pressedRight)) {
            if (currentPointers.isEmpty()) {
                stop();
            } else if (mapPointers.get(currentPointers.getLast()).x > worldBounds.pos.x) {
                moveRight();
            } else {
                moveLeft();
            }
        }

        pos.mulAdd(v, delta);
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    public void keyDown(int keycode) {


        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;

            case Input.Keys.UP:
            case Input.Keys.W:
                presedFire = true;
                break;
        }
    }

    public void keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
            case Input.Keys.UP:
            case Input.Keys.W:
                presedFire = false;
                break;
        }
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        currentPointers.add(pointer);
        mapPointers.put(pointer, touch);
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        for(Integer point: currentPointers){
            if(point.equals(pointer)) {
                currentPointers.remove(point);
                break;
            }
        }
    }

    @Override
    protected void touchDragged(Vector2 touch, int pointer) {
        mapPointers.put(pointer, touch);
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }

    private void fire(){
        Bullet bullet = bulletPool.obtain();
        bullet.pos.set(this.pos);
        bullet.setTop(getTop());

    }
}
