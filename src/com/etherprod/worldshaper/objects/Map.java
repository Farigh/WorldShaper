package com.etherprod.worldshaper.objects;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.etherprod.worldshaper.objects.factories.TileFactory;

public class Map
{
	private static ArrayList<Sprite> _Tiles	= new ArrayList<Sprite>();

	static public ArrayList<Sprite> getTilesList()
	{
		return _Tiles;
	}

	public static void mapCreate(Scene scene, VertexBufferObjectManager vertexBufferObjectManager) 
	{
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, 100, 50));
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, 150, 100));
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, 200, 150));
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, 250, 200));
	}
}
