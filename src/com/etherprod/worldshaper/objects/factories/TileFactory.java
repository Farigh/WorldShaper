package com.etherprod.worldshaper.objects.factories;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.etherprod.worldshaper.MainActivity;

public class TileFactory
{
	private static BitmapTextureAtlas	mTileTextureAtlas;
    private static TiledTextureRegion	mTileTextureRegion;
	
	public static void createResources(MainActivity mainActivity)
	{
		// create player texture
		mTileTextureAtlas = new BitmapTextureAtlas(mainActivity.getTextureManager(), 32, 32);
		mTileTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mTileTextureAtlas, mainActivity, "dirt.png", 0, 0, 1, 1);
		mTileTextureAtlas.load();	
	}
	
	public static Sprite addNewTile(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			int x, int y)
	{
		Sprite player = new Sprite(x, y, mTileTextureRegion, 
									vertexBufferObjectManager);

		scene.attachChild(player);

		return player;
	}
}
