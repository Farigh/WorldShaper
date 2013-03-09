package com.etherprod.worldshaper.objects;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.objects.factories.PlayerFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory.TileType;
import com.etherprod.worldshaper.util.data.DataManager;
import com.etherprod.worldshaper.util.data.EntityData;
import com.etherprod.worldshaper.util.data.EntityData.EntityType;
import com.etherprod.worldshaper.util.data.MapData;
import com.etherprod.worldshaper.util.map.MapXMLParser;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is in charge of the game's map management.
 */
public class Map
{
	private final static int			TILE_SIZE = 32;
	private static ArrayList<Sprite>	_Tiles = new ArrayList<Sprite>();

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
	public static Player mapCreate(Scene scene, MainActivity activity,
			PhysicsWorld physicsWorld) 
	{
		MapXMLParser parser = new MapXMLParser();
		MapData mapData;

		// load map if exists
		if (activity.getFileStreamPath("map.dat").exists())
			mapData = DataManager.loadMap(activity);
		else
			mapData = parser.createMapFromFile(activity, "levels/level_test.xml");

		createMapFromData(scene, activity.getVertexBufferObjectManager(), physicsWorld, mapData);
		
		// set camera bounds
		activity.getCamera().setBounds(0, 0, mapData.getMapSize().y, mapData.getMapSize().x); 
		activity.getCamera().setBoundsEnabled(true);
		
		// top
		for (int i = 0; i < mapData.getMapSize().y; i += TILE_SIZE)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, i, -TILE_SIZE);

		// ground
		for (int i = 0; i < mapData.getMapSize().y; i += TILE_SIZE)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, i, 
					mapData.getMapSize().x);

		// left
		for (int i = 0; i < mapData.getMapSize().x; i += TILE_SIZE)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, -TILE_SIZE, i);

		// right
		for (int i = 0; i < mapData.getMapSize().x; i += TILE_SIZE)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, 
					mapData.getMapSize().y, i);

		 return PlayerFactory.getNewPlayer(scene, activity, physicsWorld, mapData.getMapSpawn().x,
				 mapData.getMapSpawn().y);
	}

	private static void createMapFromData(Scene scene,
			VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld, MapData mapData)
	{
		int i = 1;
		for (ArrayList<EntityData> list : mapData.getMap())
		{
			int j = 1;
			for (EntityData data : list)
			{
				if (data.getType() == EntityType.TILE)
					Map.addTile(scene, vertexBufferObjectManager, physicsWorld, i, j,
							TileType.valueOf(data.getTileType()));

				j++;
			}
			i++;
		}
	}

	public static void addTile(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld, int x, int y, TileType tileType)
	{
		_Tiles.add(TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, x, y,
				tileType));
	}
	
	public static void addBound(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld, int x, int y)
	{
		PhysicsFactory.createBoxBody(physicsWorld, x + (TILE_SIZE /2), y + (TILE_SIZE / 2), 
				TILE_SIZE, TILE_SIZE, BodyType.StaticBody, TileFactory.TILE_FIX);
	}
}
