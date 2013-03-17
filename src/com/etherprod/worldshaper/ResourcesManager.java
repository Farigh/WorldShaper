package com.etherprod.worldshaper;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.color.Color;

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

	public Font text_font;
	public Font title_font;

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

	//=====================================
	//         Private functions
	//=====================================

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

		title_font = FontFactory.createStrokeFromAsset(activity.getFontManager(), titleFontTexture,
				activity.getAssets(), "title.ttf", 50, false, Color.WHITE_ARGB_PACKED_INT, 2,
				Color.BLACK_ARGB_PACKED_INT);

		text_font = FontFactory.createFromAsset(activity.getFontManager(), textFontTexture,
				activity.getAssets(), "text.ttf", 20, false, Color.WHITE_ARGB_PACKED_INT);

		titleFontTexture.load();
		textFontTexture.load();
		title_font.load();
		text_font.load();
	}
}
