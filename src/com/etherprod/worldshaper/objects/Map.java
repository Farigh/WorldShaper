package com.etherprod.worldshaper.objects;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.SceneManager;
import com.etherprod.worldshaper.objects.factories.PlayerFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory;
import com.etherprod.worldshaper.objects.factories.TileFactory.TileType;
import com.etherprod.worldshaper.util.data.EntityData;
import com.etherprod.worldshaper.util.data.MapXMLManager;
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
		if (activity.getFileStreamPath("map.dat").exists())
		{
			SceneManager.getInstance().setProgress(5, "Loading world map");
			mapData = MapXMLManager.loadMapFromFile(activity, "map.dat");
		}
		else
		{
			SceneManager.getInstance().setProgress(5, "Creating world map");
			mapData = MapGenerator.generateHome();
			MapXMLManager.saveMapToFile(activity, "map.dat", mapData);
		}

		SceneManager.getInstance().setProgress(80, "Rendering world");
		createMapFromData(scene, activity, physicsWorld, mapData);

		// set camera bounds
		activity.getCamera().setBounds(0, 0, mapData.getMapSize().y * TILE_SIZE,
				mapData.getMapSize().x * TILE_SIZE);
		activity.getCamera().setBoundsEnabled(true);

		SceneManager.getInstance().setProgress(85, "Setting bounderies 0%");
		// top
		for (int i = 0; i <= mapData.getMapSize().y; i++)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, i * TILE_SIZE,
					-TILE_SIZE);

		SceneManager.getInstance().setProgress(88, "Setting bounderies 25%");
		// ground
		for (int i = 0; i <= mapData.getMapSize().y; i++)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, i * TILE_SIZE,
					mapData.getMapSize().x * TILE_SIZE);

		SceneManager.getInstance().setProgress(91, "Setting bounderies 50%");
		// left
		for (int i = 0; i <= mapData.getMapSize().x; i++)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld, -TILE_SIZE,
					i * TILE_SIZE);

		SceneManager.getInstance().setProgress(94, "Setting bounderies 75%");
		// right
		for (int i = 0; i <= mapData.getMapSize().x; i++)
			addBound(scene, activity.getVertexBufferObjectManager(), physicsWorld,
					mapData.getMapSize().y * TILE_SIZE, i * TILE_SIZE);

		SceneManager.getInstance().setProgress(97, "Adding player");
		Player player = PlayerFactory.getNewPlayer(scene, activity, physicsWorld,
				mapData.getMapSpawn().x * TILE_SIZE, mapData.getMapSpawn().y * TILE_SIZE);

		SceneManager.getInstance().setProgress(100, "");
		return player;
	}

	private static void createMapFromData(Scene scene, MainActivity activity,
			PhysicsWorld physicsWorld, MapData mapData)
	{
		EntityData[][] map = mapData.getMap();

		// only load on screen tiles
		int maxWidth = mapData.getMapSpawn().x + ((activity.getCAMERA_WIDTH() / TILE_SIZE) / 2) + 2;
		for (int i = mapData.getMapSpawn().x - ((activity.getCAMERA_WIDTH() / TILE_SIZE) / 2) - 1;
			 i < maxWidth; i++)
		{
			int maxHeight = mapData.getMapSpawn().y + ((activity.getCAMERA_HEIGHT() / TILE_SIZE) / 2) + 2;
			for (int j = mapData.getMapSpawn().y - ((activity.getCAMERA_HEIGHT() / TILE_SIZE) / 2 - 1);
				 j < maxHeight; j++)
			{
				EntityData data = map[i][j];
				if ((data != null) && (data.getType() == EntityType.TILE))
				{
					Map.addTile(scene, activity.getVertexBufferObjectManager(),
							physicsWorld, i * TILE_SIZE, j * TILE_SIZE,
							TileType.valueOf(data.getTileType()));
				}
			}
		}
	}

	public static void addTile(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld, int x, int y, TileType tileType)
	{
		TileFactory.addNewTile(scene, vertexBufferObjectManager, physicsWorld, x, y,
				tileType);
	}

	public static void addBound(Scene scene, VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld physicsWorld, int x, int y)
	{
		PhysicsFactory.createBoxBody(physicsWorld, x + (TILE_SIZE /2), y + (TILE_SIZE / 2), 
				TILE_SIZE, TILE_SIZE, BodyType.StaticBody, TileFactory.TILE_FIX);
	}
}
