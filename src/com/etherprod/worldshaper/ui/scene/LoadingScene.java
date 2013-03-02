package com.etherprod.worldshaper.ui.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.color.Color;

import com.etherprod.worldshaper.ui.scene.SceneManager.SceneType;

public class LoadingScene extends MyScene
{
	private Font font;

	@Override
	public void createScene()
	{
		FontFactory.setAssetBasePath("fonts/");
	    final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, 
	    		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	    
	    font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture,
	    		activity.getAssets(), "font.ttf", 50, false, Color.WHITE_ARGB_PACKED_INT, 2,
	    		Color.BLACK_ARGB_PACKED_INT);

	    font.load();

	    setBackground(new Background(Color.WHITE));
	    attachChild(new Text(40, 100, font, "Loading...", activity.getVertexBufferObjectManager()));
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
}
