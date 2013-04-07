package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.debug.Debug;

import android.content.SharedPreferences;

import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.ResourcesManager;
import com.etherprod.worldshaper.SceneManager;
import com.etherprod.worldshaper.SceneManager.SceneType;
import com.etherprod.worldshaper.ui.TiledSpriteMenuItem;

public class SettingsScene extends MyScene implements IOnMenuItemClickListener
{
	private MenuScene			settingsChildScene;
	private final int			SETTINGS_MUTE_SOUNDS	= 0;
	private final int			SETTINGS_MUTE_MUSIC		= 1;
	private TiledSpriteMenuItem	soundsMenuItem;
	private TiledSpriteMenuItem	musicMenuItem;

	private static final String	PREFS_NAME			= "Settings";
	private static final String PREFS_SOUNDS_NAME	= "sounds";
	private static final String PREFS_MUSIC_NAME	= "music";

	private static ITextureRegion settings_background_region;
	private static TiledTextureRegion checkbox_region;

	private static BuildableBitmapTextureAtlas menuTextureAtlas;

	public static void onCreateRessources()
	{
		ResourcesManager.getInstance().loadMenuResources();

		MainActivity activity = ResourcesManager.getInstance().getActivity();	

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/ui/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		settings_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity,
				"settings_bg.png");
		checkbox_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity,
				"checkbox.png", 2, 1);

		try 
		{
			menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, 
					BitmapTextureAtlas>(0, 1, 0));
			menuTextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

	@Override
	public void createScene()
	{
		// set color to #000033 (image background color)
		this.setBackground(new Background(0, 0, 51f / 255f));

		createBackground();
		createButtons();
	}

	@Override
	public void onBackKeyPressed()
	{
		// go back to main menu
		SceneManager.getInstance().setScene(SceneType.SCENE_MENU);

		// save settings
		this.disposeScene();
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_SETTINGS;
	}

	@Override
	public void disposeScene()
	{
		// Save settings
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(PREFS_MUSIC_NAME, activity.isMusicActive());
		editor.putBoolean(PREFS_SOUNDS_NAME, activity.isSoundsActive());

		// Commit modifications
		editor.commit();
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY)
	{
		switch(pMenuItem.getID())
		{
			case SETTINGS_MUTE_SOUNDS:
				soundsMenuItem.setCurrentTileIndex((soundsMenuItem.getCurrentTileIndex() + 1)
						% soundsMenuItem.getTileCount());
				activity.computeSounds();
				return true;
			case SETTINGS_MUTE_MUSIC:
				musicMenuItem.setCurrentTileIndex((musicMenuItem.getCurrentTileIndex() + 1)
						% musicMenuItem.getTileCount());
				activity.computeMusic();
				return true;
			default:
				return false;
		}
	}

	public void unloadTextures()
	{
		menuTextureAtlas.unload();
	}

	public void loadTextures()
	{
		menuTextureAtlas.load();
	}

	private void createBackground()
	{
		this.attachChild(new Sprite(0, 0, settings_background_region,
				activity.getVertexBufferObjectManager())
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});	
	}

	private void createButtons()
	{
		settingsChildScene = new MenuScene(activity.getCamera());
		settingsChildScene.setPosition(0, 0);

		// zoom on click
		soundsMenuItem = new TiledSpriteMenuItem(SETTINGS_MUTE_SOUNDS,
				checkbox_region, activity.getVertexBufferObjectManager());
		musicMenuItem = new TiledSpriteMenuItem(SETTINGS_MUTE_MUSIC, 
				checkbox_region, activity.getVertexBufferObjectManager());

		settingsChildScene.addMenuItem(musicMenuItem);
		settingsChildScene.addMenuItem(soundsMenuItem);

		// initialize check-box
		musicMenuItem.setCurrentTileIndex(activity.isMusicActive() ? 1 : 0);
		soundsMenuItem.setCurrentTileIndex(activity.isSoundsActive() ? 1 : 0);

		settingsChildScene.buildAnimations();
		settingsChildScene.setBackgroundEnabled(false);

		musicMenuItem.setPosition(musicMenuItem.getX() + 100, musicMenuItem.getY() + 30);
		soundsMenuItem.setPosition(soundsMenuItem.getX() + 100, soundsMenuItem.getY() + 53);

		settingsChildScene.setOnMenuItemClickListener(this);

		setChildScene(settingsChildScene);
	}

	public static void restoreSettings(MainActivity activity)
	{
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);

		if (!settings.getBoolean(PREFS_MUSIC_NAME, true))
			activity.computeMusic();

		if (!settings.getBoolean(PREFS_SOUNDS_NAME, true))
			activity.computeSounds();
	}
}
