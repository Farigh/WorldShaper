package com.etherprod.worldshaper.objects.factories;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.objects.Player;

public class PlayerFactory
{
	private static BitmapTextureAtlas	mPlayerTextureAtlas;
    private static TiledTextureRegion	mPlayerTiledTextureRegion;
	
	public static void createResources(MainActivity mainActivity)
	{
		// create player texture
		mPlayerTextureAtlas = new BitmapTextureAtlas(mainActivity.getTextureManager(), 32, 32);
        mPlayerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
        		.createTiledFromAsset(mPlayerTextureAtlas, mainActivity, "face_box.png", 0, 0, 1, 1);
        mPlayerTextureAtlas.load();	
	}
	
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
