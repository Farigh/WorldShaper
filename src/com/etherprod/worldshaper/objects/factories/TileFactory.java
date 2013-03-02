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

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a factory pattern based one in charge of creating
 * the maps tiles and its related resources
 */
public class TileFactory
{
	private static BitmapTextureAtlas	mTileTextureAtlas;
    private static TiledTextureRegion	mTileTextureRegion;
    private static FixtureDef TILE_FIX;
	
    /**
     * This function MUST be used at the beginning of game loading.
     * It will be called by the @ResourcesManager.
     * 
     * @param mainActivity The main activity of the game
     */
	public static void createResources(MainActivity mainActivity)
	{
		// Create physics FixtureDef
		TILE_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		
		// create player texture
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");
		mTileTextureAtlas = new BitmapTextureAtlas(mainActivity.getTextureManager(), 32, 32);
		mTileTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mTileTextureAtlas, mainActivity, "dirt.png", 0, 0, 1, 1);
		mTileTextureAtlas.load();	
	}
	
	/**
	 * Creates a new tile
	 * 
	 * @param scene The game's scene
	 * @param mainActivity The main activity
	 * @param physicsWorld The physics handler
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * 
	 * @return Sprite created
	 */
	public static Sprite addNewTile(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld, int x, int y)
	{
		Sprite tile = new Sprite(x, y, mTileTextureRegion, vertexBufferObjectManager);
		
		// set physics to not affect them
		PhysicsFactory.createBoxBody(physicsWorld, tile, BodyType.StaticBody, TILE_FIX);

		scene.attachChild(tile);

		return tile;
	}
}
