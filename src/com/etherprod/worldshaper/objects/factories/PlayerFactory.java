package com.etherprod.worldshaper.objects.factories;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.objects.Player;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a factory pattern based one in charge of creating
 * the player object and its related resources
 */
public class PlayerFactory
{
	private static BitmapTextureAtlas	mPlayerTextureAtlas;
    private static TiledTextureRegion	mPlayerTiledTextureRegion;
	
    /**
     * This function MUST be used at the beginning of game loading.
     * It will be called by the @ResourcesManager.
     * 
     * @param mainActivity The main activity of the game
     */
	public static void createResources(MainActivity mainActivity)
	{
		// create player texture
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");
		mPlayerTextureAtlas = new BitmapTextureAtlas(mainActivity.getTextureManager(), 32, 32);
        mPlayerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
        		.createTiledFromAsset(mPlayerTextureAtlas, mainActivity, "face_box.png", 0, 0, 1, 1);
        mPlayerTextureAtlas.load();	
	}
	
	/**
	 * Creates a new player
	 * 
	 * @param scene The game's scene
	 * @param mainActivity The main activity
	 * @param physicsWorld The physics handler
	 * 
	 * @return The player object created
	 */
	public static Player getNewPlayer(Scene scene, MainActivity mainActivity, PhysicsWorld physicsWorld)
	{
		float centerX = (mainActivity.getCAMERA_WIDTH() - 
				mPlayerTiledTextureRegion.getWidth()) / 2;
		float centerY = (mainActivity.getCAMERA_HEIGHT() - 
				mPlayerTiledTextureRegion.getHeight()) / 2;

		Player player = new Player(centerX, centerY, mPlayerTiledTextureRegion, 
								   mainActivity.getVertexBufferObjectManager(), physicsWorld);
		
		scene.attachChild(player);
		
		return player;
	}
}
