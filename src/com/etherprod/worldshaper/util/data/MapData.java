package com.etherprod.worldshaper.util.data;

import java.io.Serializable;

import org.andengine.util.debug.Debug;

import com.etherprod.worldshaper.util.IntVector2;
import com.etherprod.worldshaper.util.data.EntityData.EntityType;

public class MapData implements Serializable
{
	private static final long					serialVersionUID = 1L;
	public static transient final int			TILE_SIZE = 32;

	private IntVector2							map_size;
	private IntVector2							map_spawn;
	private EntityData[][]						map;

	public MapData()
	{
		map_size = new IntVector2(0, 0);
		map_spawn = new IntVector2(0, 0);
	}

	public void setMapSize(int height, int width)
	{
		map_size.x = height;
		map_size.y = width;

		//TODO: display this to loadscreen
		String progresstext = "Big bang in progress ...";
		Debug.e("Setting size to " + width + "," + height);
		map = new EntityData[width][height];
	}

	public EntityData addEntity(EntityType type, int x, int y)
	{
		EntityData data = new EntityData(type);

		map[x][y] = data;

		return data;
	}

	public void setMapSpawn(int x, int y)
	{
		map_spawn.x = x;
		map_spawn.y = y;
		Debug.e("Setting spawn to " + x + "," + y);
	}

	public IntVector2 getMapSize()
	{
		return map_size;
	}

	public IntVector2 getMapSpawn()
	{
		return map_spawn;
	}

	public EntityData[][] getMap()
	{
		return map;
	}
}
