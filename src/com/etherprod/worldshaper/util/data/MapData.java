package com.etherprod.worldshaper.util.data;

import java.io.Serializable;

import com.etherprod.worldshaper.util.IntVector2;
import com.etherprod.worldshaper.util.data.EntityData.EntityType;

public class MapData implements Serializable
{
	private static final long					serialVersionUID = 1L;
	public static transient final int			TILE_SIZE = 32;

	private IntVector2							map_size;
	private IntVector2							map_spawn;
	private int									tile_number = 0;

	private EntityData[][]						map;

	public MapData()
	{
		map_size = new IntVector2(0, 0);
		map_spawn = new IntVector2(0, 0);
		tile_number = 0;
	}

	public void setMapSize(int height, int width)
	{
		map_size.x = height;
		map_size.y = width;

		//TODO: display this to load screen
		//"Big bang in progress ..."
		map = new EntityData[width][height];
	}

	public EntityData addEntity(EntityType type, int x, int y)
	{
		EntityData data = new EntityData(type);

		tile_number++;
		map[x][y] = data;

		return data;
	}

	public void setMapSpawn(int x, int y)
	{
		map_spawn.x = x;
		map_spawn.y = y;
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

	public int getTileNumber()
	{
		return tile_number;
	}
}
