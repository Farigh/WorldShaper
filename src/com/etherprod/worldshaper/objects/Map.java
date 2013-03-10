package com.etherprod.worldshaper.objects;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.objects.factories.PlayerFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory.TileType;
import com.etherprod.worldshaper.util.data.DataManager;
import com.etherprod.worldshaper.util.data.EntityData;
import com.etherprod.worldshaper.util.data.EntityData.EntityType;
import com.etherprod.worldshaper.util.data.MapData;
import com.etherprod.worldshaper.util.map.MapGenerator;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is in charge of the game's map management.
 */
public class Map
{
	private final static int			TILE_SIZE = 31; // tile size - 1 to avoid spaces
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
		MapData mapData;

		// load map if exists
		if (activity.getFileStreamPath("mp.dat").exists())
			mapData = DataManager.loadMap(activity);
		else
		{
			mapData = MapGenerator.generateHome();
			DataManager.saveMap(activity, mapData);
		}

		createMapFromData(scene, activity, physicsWorld, mapData);

		// set camera bounds
		activity.getCamera().setBounds(0, 0, mapData.getMapSize().y * TILE_SIZE,
				mapData.getMapSize().x * TILE_SIZE); 
		activity.getCamera().setBoundsEnabled(true);

		// top
		for (int i = 0; i <= mapData.getMapSize().y; i++)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, i * TILE_SIZE,
					-TILE_SIZE);

		// ground
		for (int i = 0; i <= mapData.getMapSize().y; i++)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, i * TILE_SIZE, 
					mapData.getMapSize().x * TILE_SIZE);

		// left
		for (int i = 0; i <= mapData.getMapSize().x; i++)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, -TILE_SIZE, 
					i * TILE_SIZE);

		// right
		for (int i = 0; i <= mapData.getMapSize().x; i++)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, 
					mapData.getMapSize().y * TILE_SIZE, i * TILE_SIZE);

		Debug.e("Spawn player at " + mapData.getMapSpawn().x + "," + mapData.getMapSpawn().y);
		 return PlayerFactory.getNewPlayer(scene, activity, physicsWorld, 
				 mapData.getMapSpawn().y * TILE_SIZE,
				 mapData.getMapSpawn().x * TILE_SIZE);
	}

	private static void createMapFromData(Scene scene, MainActivity activity,
			PhysicsWorld physicsWorld, MapData mapData)
	{
		// only load on screen tiles
		int maxHeight = mapData.getMapSpawn().x + ((activity.getCAMERA_HEIGHT() / TILE_SIZE) / 2) + 2;
		for (int i = mapData.getMapSpawn().x - ((activity.getCAMERA_HEIGHT() / TILE_SIZE) / 2 - 1);
			 i < maxHeight; i++)
		{
			ArrayList<EntityData> list = mapData.getMap().get(i);

			int maxWidth = mapData.getMapSpawn().y + ((activity.getCAMERA_WIDTH() / TILE_SIZE) / 2) + 2;
			for (int j = mapData.getMapSpawn().y - ((activity.getCAMERA_WIDTH() / TILE_SIZE) / 2) - 1;
				 j < maxWidth; j++)
			{
				EntityData data = list.get(j);
				if (data.getType() == EntityType.TILE)
				{
					Debug.e("Spawning tile at " + j + "," + i);
					Map.addTile(scene, activity.getVertexBufferObjectManager(), 
							physicsWorld, j * TILE_SIZE, i * TILE_SIZE, 
							TileType.valueOf(data.getTileType()));
				}
			}
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
