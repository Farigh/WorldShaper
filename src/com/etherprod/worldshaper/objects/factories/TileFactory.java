package com.etherprod.worldshaper.objects.factories;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.etherprod.worldshaper.MainActivity;

public class TileFactory
{
	private static BitmapTextureAtlas	mTileTextureAtlas;
    private static TiledTextureRegion	mTileTextureRegion;
    private static FixtureDef TILE_FIX;
	
	public static void createResources(MainActivity mainActivity)
	{
		// Create physics FixtureDef
		TILE_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		
		// create player texture
		mTileTextureAtlas = new BitmapTextureAtlas(mainActivity.getTextureManager(), 32, 32);
		mTileTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mTileTextureAtlas, mainActivity, "dirt.png", 0, 0, 1, 1);
		mTileTextureAtlas.load();	
	}
	
	public static Sprite addNewTile(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld, int x, int y)
	{
		Sprite tile = new Sprite(x, y, mTileTextureRegion, 
									vertexBufferObjectManager);
		
		// set physics to not affect them
		PhysicsFactory.createBoxBody(physicsWorld, tile, BodyType.StaticBody,
				TILE_FIX);

		scene.attachChild(tile);

		return tile;
	}
}
