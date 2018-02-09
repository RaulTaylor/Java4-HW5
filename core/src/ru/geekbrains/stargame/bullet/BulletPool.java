package ru.geekbrains.stargame.bullet;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.pool.SpritesPool;

/**
 * Created by raultaylor on 10.02.18.
 */

public class BulletPool extends SpritesPool<Bullet> {

    private Rect wolrdBounds;
    private TextureAtlas atlas;

    public BulletPool(TextureAtlas atlas){
        this.atlas = atlas;
    }

    @Override
    public Bullet obtain() {
        Bullet bullet = super.obtain();
        bullet.setDestroyed(false);
        bullet.resize(wolrdBounds);
        return bullet;
    }

    @Override
    protected Bullet newObject() {
        return new Bullet(atlas);
    }

    public void resize(Rect worldBounds){
        this.wolrdBounds = worldBounds;
        for(Bullet bullet: activeObjects){
            bullet.resize(worldBounds);
        }
    }

    public void info(){
        System.out.println("Size Active Objs = " + activeObjects.size());
    }
}
