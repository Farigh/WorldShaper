package com.etherprod.worldshaper;

import java.util.ArrayList;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.etherprod.worldshaper.objects.Texture;

public class Loader
{
	public static ArrayList<TiledTextureRegion> TextureLoad(MainActivity mainActivity)
	{
	    BitmapTextureAtlas tBitmapTextureAtlas;

	    ArrayList<TiledTextureRegion> AtlasList = new ArrayList<TiledTextureRegion>();
	    ArrayList<Texture> TextureList = new ArrayList<Texture>();
		
		// filling list with all textures
		TextureList.add(new Texture("durt.png", 16, 16));
		
		// Load all the textures this game needs.
		for (Texture t : TextureList)
		{
	        tBitmapTextureAtlas = new BitmapTextureAtlas(mainActivity.getTextureManager(), 
	        											 t.height, t.width);
	        AtlasList.add(BitmapTextureAtlasTextureRegionFactory
	        		.createTiledFromAsset(tBitmapTextureAtlas, mainActivity, t.filename, 0, 0, 1, 1));
	        tBitmapTextureAtlas.load();
		}
		return AtlasList;
	}
}
