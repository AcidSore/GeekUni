package ru.acidsore.stargame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class StarGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("maxresdefault.jpg");
		Vector2 v1 = new Vector2(1,3);
		Vector2 v2 = new Vector2(0,-1);
		v1.add(v2);
		System.out.println("v1+v2 = "+ v1.x+ v1.y);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 5, 4, 4);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, -250, -150);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
