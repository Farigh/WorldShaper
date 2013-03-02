package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.debug.Debug;

import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.ResourcesManager;
import com.etherprod.worldshaper.ui.scene.SceneManager.SceneType;

public class MainMenuScene extends MyScene implements IOnMenuItemClickListener
{
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	
	private static ITextureRegion menu_background_region;
	private static ITextureRegion play_region;
	private static ITextureRegion options_region;
	    
	private static BuildableBitmapTextureAtlas menuTextureAtlas;
	
	public static void onCreateRessources()
	{
		MainActivity activity = ResourcesManager.getInstance().getActivity();	
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "main_menu_bg.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play_bt.png");
		options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options_bt.png");

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
		createBackground();
		createButtons();
	}

	@Override
	public void onBackKeyPressed()
	{
	    System.exit(0);
	}

	@Override
	public SceneType getSceneType()
	{
	    return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene()
	{

	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY)
	{
		switch(pMenuItem.getID())
        {
        	case MENU_PLAY:
        		return true;
        	case MENU_OPTIONS:
        		return true;
        	default:
        		return false;
        }
	}

	private void createBackground()
	{
		this.attachChild(new Sprite(0, 0, menu_background_region,
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
		menuChildScene = new MenuScene(activity.getCamera());
	    menuChildScene.setPosition(0, 0);
	    
	    // zoom on click
	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, 
	    		play_region, activity.getVertexBufferObjectManager()), 1.2f, 1);
	    final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, 
	    		options_region, activity.getVertexBufferObjectManager()), 1.2f, 1);
	    
	    menuChildScene.addMenuItem(playMenuItem);
	    menuChildScene.addMenuItem(optionsMenuItem);
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
	    playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() + 26);
	    optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() + 74);
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(menuChildScene);
	}
}
