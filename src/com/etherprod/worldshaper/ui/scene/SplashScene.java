package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;

import com.etherprod.worldshaper.ui.scene.SceneManager.SceneType;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This is the splash screen scene
 */
public class SplashScene extends MyScene
{
	private ITextureRegion		splash_region;
	private BitmapTextureAtlas	splashTextureAtlas;
	private Sprite 				splash;

	@Override
	public void createScene()
	{
		onCreateResources();
		
		this.setBackground(new Background(0, 0, 0));

		splash = new Sprite(0, 0, splash_region, activity.getVertexBufferObjectManager())
		{
		    @Override
		    protected void preDraw(GLState pGLState, Camera pCamera) 
		    {
		       super.preDraw(pGLState, pCamera);
		       pGLState.enableDither();
		    }
		};

		splash.setPosition((activity.getCAMERA_WIDTH() - 400) / 2,
						   (activity.getCAMERA_HEIGHT() - 300) / 2);
		attachChild(splash);
	}

	@Override
	public void onBackKeyPressed()
	{
		
	}

	@Override
	public SceneType getSceneType()
	{
	    return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene()
	{
		onDestroyResources();
		
	    splash.detachSelf();
	    splash.dispose();
	    this.detachSelf();
	    this.dispose();
	}

	private void onCreateResources()
	{
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 400, 300,
				TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, 
				activity, "logo.png", 0, 0);
		splashTextureAtlas.load();
	}
	
	private void onDestroyResources()
	{
		splashTextureAtlas.unload();
		splash_region = null;
	}
}
