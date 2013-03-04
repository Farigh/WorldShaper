package com.etherprod.worldshaper.objects.factories;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
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
	private static BuildableBitmapTextureAtlas	player_atlas;
    private static TiledTextureRegion	player_region;
	
    /**
     * This function MUST be used at the beginning of game loading.
     * It will be called by the @ResourcesManager.
     * 
     * @param activity The main activity of the game
     */
	public static void createResources(MainActivity activity)
	{
		// create player texture
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/player/");
		player_atlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024,
	    		1024, TextureOptions.BILINEAR);
        player_region = BitmapTextureAtlasTextureRegionFactory
        		.createTiledFromAsset(player_atlas, activity, "player.png", 3, 1);
        
        try
		{
			player_atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, 
					BitmapTextureAtlas>(0, 1, 0));
	        player_atlas.load();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new player
	 * 
	 * @param scene The game's scene
	 * @param activity The main activity
	 * @param physicsWorld The physics handler
	 * @param y 
	 * @param x 
	 * 
	 * @return The player object created
	 */
	public static Player getNewPlayer(Scene scene, MainActivity activity, PhysicsWorld physicsWorld,
			int x, int y)
	{
		Player player = new Player(x, y, player_region, 
								   activity, physicsWorld);
		
		scene.attachChild(player);
		
		return player;
	}
}
