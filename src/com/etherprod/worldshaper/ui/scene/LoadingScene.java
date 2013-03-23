package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;

import com.etherprod.worldshaper.SceneManager.SceneType;
import com.etherprod.worldshaper.ui.ClippedAnimatedSprite;

public class LoadingScene extends MyScene
{
	private static BuildableBitmapTextureAtlas	loading_atlas;
	private static TiledTextureRegion			progressbar_region;
	private static TiledTextureRegion			progressbar_bg_region;
	private static ITextureRegion				loading_bg_region;
	private static ClippedAnimatedSprite		progressbar;
	private Text		 						loadingText;

	@Override
	public void createScene()
	{		
		createResources();

		// set color to #000033 (image background color)
		this.setBackground(new Background(0, 0, 51f / 255f));

		createBackground();
		createText();
		createLoadingBar();
	}

	@Override
	public void onBackKeyPressed()
	{
		// Can't leave while loading
	}

	@Override
	public SceneType getSceneType()
	{
		return null;
	}

	@Override
	public void disposeScene()
	{

	}

	private void createResources()
	{
		// create loading bar texture
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ui/");
		loading_atlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024,
				1024, TextureOptions.BILINEAR);
		loading_bg_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(loading_atlas, activity, "loading_bg.png");
		progressbar_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(loading_atlas, activity, "loadbar.png", 1, 7);
		progressbar_bg_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(loading_atlas, activity, "loadbar_bg.png", 1, 1);

		try
		{
			loading_atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, 
					BitmapTextureAtlas>(0, 1, 0));
			loading_atlas.load();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void createText()
	{
		// adding life text
		loadingText = new Text(220, 294, resourcesManager.getTextFont(),
				"messagedechargementsuperlongpourleprogress", 
				new TextOptions(HorizontalAlign.LEFT), activity.getVertexBufferObjectManager());
		loadingText.setText("");
		attachChild(loadingText);
	}
	
	private void createLoadingBar()
	{
		progressbar = new ClippedAnimatedSprite(300, 250, progressbar_region,
				activity.getVertexBufferObjectManager());

		final long[] progressbar_animate = new long[] { 200, 150, 150, 150, 150, 200, 150, 150, 150, 150 };
		final int[] frames = new int[] { 0, 1, 2, 3, 4, 5, 4, 3, 2, 1};
		progressbar.animate(progressbar_animate, frames, true);
		progressbar.setScale(2f);

		attachChild(new Sprite(180, 234, progressbar_bg_region, activity.getVertexBufferObjectManager()));
		attachChild(progressbar);
		setProgress(0, "");
	}
	
	private void createBackground()
	{
		this.attachChild(new Sprite(0, 0, loading_bg_region, activity.getVertexBufferObjectManager())
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});	
	}

	public void setProgress(int progress, String message)
	{
		progress = (int) (((float) progress / 100f) * 196f);

		progressbar.setClip(200, 207, (int) (progress * progressbar.getScaleX()), 30);
		loadingText.setText(message);
	}
}
