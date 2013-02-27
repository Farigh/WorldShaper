package com.etherprod.worldshaper.objects;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.etherprod.worldshaper.objects.Tile;

public class Map
{
	private static ArrayList<Tile>	_Tiles	= new ArrayList<Tile>();

	static public ArrayList<Tile> getTilesList()
	{
		return _Tiles;
	}

	static public void setTilesList(ArrayList<Tile> TilesList)
	{
		_Tiles = TilesList;
	}

	public static void mapCreate(Scene mMainScene,
			ArrayList<TiledTextureRegion> mTextureList,
			VertexBufferObjectManager vertexBufferObjectManager) 
	{
		_Tiles.add(new Tile(100, 500, mTextureList.get(0), vertexBufferObjectManager));
		_Tiles.add(new Tile(150, 550, mTextureList.get(0), vertexBufferObjectManager));
		_Tiles.add(new Tile(200, 600, mTextureList.get(0), vertexBufferObjectManager));
		_Tiles.add(new Tile(250, 650, mTextureList.get(0), vertexBufferObjectManager));

		for (Tile tile : _Tiles)
		{
			mMainScene.attachChild(tile);
		}
	}
}
