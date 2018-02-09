package ru.geekbrains.stargame.bullet;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;

/**
 * Created by raultaylor on 10.02.18.
 */

public class Bullet extends Sprite {

    public static final Vector2 SPEED_BULLET = new Vector2(0,0.7f);
    public static final float HEIGHT_BULLET = 0.02f;
    private Rect worldBounds;

    public Bullet(TextureAtlas atlas) {
        super(atlas.findRegion("bulletMainShip"));
        setHeightProportion(HEIGHT_BULLET);
    }

    public void setStartPosition(Vector2 pos){
        this.pos.set(pos);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        super.resize(worldBounds);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(SPEED_BULLET,delta);
        if(getBottom()>worldBounds.getTop()){
            setDestroyed(true);
        }
    }
}
