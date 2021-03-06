package ru.acidsore.sprite.game;


import com.badlogic.gdx.Game;
=======

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.acidsore.base.Sprite;
import ru.acidsore.math.Rect;
import ru.acidsore.pool.BulletPool;

import ru.acidsore.pool.ExplosionPool;
import ru.acidsore.screen.GameOverScreen;
import ru.acidsore.screen.GameScreen;

public class MainShip extends Ship {

    private Rect worldBounds;

    private static final int INVALID_POINTER = -1;

public class MainShip extends Sprite {

    private Rect worldBounds;


    private final Vector2 v0 = new Vector2(0.5f, 0);
    private Vector2 v = new Vector2();

    private boolean isPressedLeft;
    private boolean isPressedRight;


    private float damageInterval = 0.1f;
    private float damageTimer = damageInterval;

   


    private BulletPool bulletPool;

    private TextureRegion bulletRegion;
    private Sound shot;


    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    private Game game;



    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.reloadInterval = 0.2f;
        setHeightProportion(0.15f);
        this.bulletV = new Vector2(0, 0.5f);
        this.bulletHeight = 0.01f;
        this.damage = 1;
        this.hp = 1;
        this.shootSound = Gdx.audio.newSound(Gdx.files.internal("music/shot.mp3"));
    }
    public MainShip(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletPool = bulletPool;
        setHeightProportion(0.15f);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);

        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }

    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                isPressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                isPressedRight = true;
                moveRight();
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                isPressedLeft = false;
                if (isPressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                isPressedRight = false;
                if (isPressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
        return false;
    }



    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) return false;
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) return false;
            rightPointer = pointer;
            moveRight();
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }
        return super.touchUp(touch, pointer);
    }
     @Override
    public void damage(int damage) {
        frame = 1;
        damageTimer = 0f;
        hp -= damage;
        if (hp <= 0) {
            destroy();
             bulletPool = null;
            game.setScreen(new GameScreen());
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom()
        );
    }

    private void moveRight() {
           v.set(v0);
    }

    private void moveLeft() {
            v.set(v0).rotate(180);

    private void moveRight() {
      //  while (pos.x < worldBounds.getRight() - 0.07f){
           v.set(v0);
//            if (pos.x  == worldBounds.getRight() - 0.07f) break;
//        }
    }

    private void moveLeft() {
       // while (pos.x > worldBounds.getLeft() + 0.07f) {
            v.set(v0).rotate(180);
//            if (pos.x == worldBounds.getLeft() + 0.07f) break;
//        }

    }

    private void stop() {
        v.setZero();
    }






    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, new Vector2(0, 0.5f), 0.01f, worldBounds, 1);
        shot = Gdx.audio.newSound(Gdx.files.internal("music/shot.mp3"));
        shot.play();
    }
      @Override
     public boolean touchDown(Vector2 touch, int pointer){
        isPressed = true;
        if (touch.x > (worldBounds.getHalfWidth()*0.3f))
        {
            moveRight();
            return true;
        }
        if ( touch.x < (-worldBounds.getHalfWidth()*0.03f))
         {
             moveLeft();
             return true;
         }
         isPressed = false;
        return true;
    }

    public boolean touchUp(Vector2 touch, int pointer){
        if (isPressed) {
            if (touch.x > (worldBounds.getHalfWidth() * 0.3f)) {
                moveRight();
                return true;
            }
            if (touch.x < (-worldBounds.getHalfWidth() * 0.03f)) {
                moveLeft();
                return true;
            }
        }
        return false;
    }

}
