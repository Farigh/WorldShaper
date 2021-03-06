package com.etherprod.worldshaper;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import com.etherprod.worldshaper.objects.factories.PlayerFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a singleton pattern based one in charge of creating and
 * managing all resources used within our game
 */
public class ResourcesManager
{
	private static final ResourcesManager INSTANCE = new ResourcesManager();

	private MainActivity 	activity;

	//=====================================
	//              Resources
	//=====================================

	private Font	text_font;
	private Font	life_text_font;
	private Font	title_font;
	private Music	mainMusic;

	/**
	 * This function MUST be used at the beginning of game loading.
	 * It will prepare Resources Manager properly, setting all needed parameters, 
	 * so we can latter access them from different classes
	 * 
	 * @param engine The game engine
	 * @param activity The main activity
	 * @param camera The scene camera
	 * @param vbom The vertex buffer object manager
	 */
	public static void prepareManager(MainActivity activity)
	{
		getInstance().activity = activity;
	}

	/**
	 * This function return the ResourcesManager instance
	 * 
	 * @return The ResourcesManager instance
	 */
	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}

	/**
	 * This function is in charge of loading game resources
	 */
	public void loadMenuResources()
	{
		loadMenuFonts();
	}

	/**
	 * This function is in charge of loading game resources
	 */
	public void loadGameResources()
	{
		loadGameAudio();
		loadGameGraphics();
	}

	/**
	 * This function is in charge of unloading game resources
	 */
	public void unloadGameResources()
	{
		//TODO: unload game's texture
	}

	//=====================================
	//      Getters/setters functions
	//=====================================

	public MainActivity getActivity() 
	{
		return activity;
	}

	public Font getTitleFont()
	{
		return title_font;
	}

	public Font getTextFont()
	{
		return text_font;
	}

	public IFont getLifeTextFont()
	{
		return life_text_font;
	}
	
	public Music getMainMusic()
	{
		return mainMusic;
	}

	//=====================================
	//         Private functions
	//=====================================

	private void loadGameAudio()
	{
		try
		{
			MusicFactory.setAssetBasePath("sounds/");
			mainMusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "main_theme.wav");
			mainMusic.setLooping(true);
		}
		catch (final Exception e)
		{
			Debug.e(e);
		}
	}

	private void loadGameGraphics()
	{
		TileFactory.createResources(activity);
		PlayerFactory.createResources(activity);
	}

	private void loadMenuFonts()
	{
		FontFactory.setAssetBasePath("fonts/");
		final ITexture titleFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture textFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture lifeTextFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		title_font = FontFactory.createStrokeFromAsset(activity.getFontManager(), titleFontTexture,
				activity.getAssets(), "title.ttf", 50, false, Color.WHITE_ARGB_PACKED_INT, 2,
				Color.BLACK_ARGB_PACKED_INT);

		text_font = FontFactory.createFromAsset(activity.getFontManager(), textFontTexture,
				activity.getAssets(), "text.ttf", 20, false, Color.WHITE_ARGB_PACKED_INT);

		life_text_font = FontFactory.createFromAsset(activity.getFontManager(), lifeTextFontTexture,
				activity.getAssets(), "text.ttf", 22, false, Color.WHITE_ARGB_PACKED_INT);

		titleFontTexture.load();
		textFontTexture.load();
		lifeTextFontTexture.load();
		title_font.load();
		text_font.load();
		life_text_font.load();
	}
}
