package com.etherprod.worldshaper.objects;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.etherprod.worldshaper.objects.factories.TileFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory.TileType;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is in charge of the game's map management.
 */
public class Map
{
	private static ArrayList<Sprite> _Tiles	= new ArrayList<Sprite>();

	/**
	 * This function returns the map's objects list (tiles)
	 * 
	 * @return The map's tile array
	 */
	static public ArrayList<Sprite> getTilesList()
	{
		return _Tiles;
	}

	/**
	 * Creates the map adding each tiles
	 * 
	 * @param scene The games scene
	 * @param vertexBufferObjectManager The vertex buffer manager
	 * @param physicsWorld the game physics object
	 */
	public static void mapCreate(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld) 
	{
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, 100, 50,
				TileType.DIRT));
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, 150, 100,
				TileType.DIRT));
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, 200, 150,
				TileType.DIRT));
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, 250, 200,
				TileType.DIRT));

		// ground
		for (int i = 0; i < 800; i += 16)
			_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, i, 400,
					TileType.DIRT));
	}
}
