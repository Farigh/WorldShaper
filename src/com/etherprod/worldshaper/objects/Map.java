package com.etherprod.worldshaper.objects;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.objects.factories.TileFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory.TileType;
import com.etherprod.worldshaper.util.IntVector2;
import com.etherprod.worldshaper.util.map.MapXMLParser;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is in charge of the game's map management.
 */
public class Map
{
	private final static int TILE_SIZE = 32;
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
	public static void mapCreate(Scene scene, MainActivity activity,
			PhysicsWorld physicsWorld) 
	{
		MapXMLParser parser = new MapXMLParser(scene, activity.getVertexBufferObjectManager(), physicsWorld);
		
		IntVector2 bound = parser.createMapFromFile(activity.getAssets(), "levels/level_test.xml");

		// top
		for (int i = 0; i < bound.y; i += TILE_SIZE)
			_Tiles.add(TileFactory.addNewTile(scene, activity.getVertexBufferObjectManager(), 
					physicsWorld, i, 0, TileType.DIRT));

		// ground
		for (int i = 0; i < bound.y; i += TILE_SIZE)
			_Tiles.add(TileFactory.addNewTile(scene, activity.getVertexBufferObjectManager(), 
					physicsWorld, i, bound.x, TileType.DIRT));

		// left
		for (int i = 0; i < bound.x; i += TILE_SIZE)
			_Tiles.add(TileFactory.addNewTile(scene, activity.getVertexBufferObjectManager(), 
					physicsWorld, 0, i, TileType.DIRT));

		// right
		for (int i = 0; i < bound.x; i += TILE_SIZE)
			_Tiles.add(TileFactory.addNewTile(scene, activity.getVertexBufferObjectManager(), 
					physicsWorld, bound.y, i, TileType.DIRT));
	}

	public static void addTile(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld, int x, int y, TileType tileType)
	{
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, x, y,
				tileType));
	}
}
