package com.etherprod.worldshaper.objects;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.etherprod.worldshaper.objects.factories.TileFactory;

public class Map
{
	private static ArrayList<Sprite> _Tiles	= new ArrayList<Sprite>();

	static public ArrayList<Sprite> getTilesList()
	{
		return _Tiles;
	}

	public static void mapCreate(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld) 
	{
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, 100, 50));
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, 150, 100));
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, 200, 150));
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, 250, 200));
		
		// ground
		for (int i = 0; i < 800; i += 16)
			_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, i, 400));
			
	}
}
