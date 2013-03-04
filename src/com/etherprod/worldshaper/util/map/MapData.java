package com.etherprod.worldshaper.util.map;

import com.etherprod.worldshaper.util.IntVector2;

public class MapData
{
	public static final int TILE_SIZE = 32;
	private IntVector2 map_size;
	private IntVector2 map_spawn;
	
	public MapData()
	{
		map_size = new IntVector2(0, 0);
		map_spawn = new IntVector2(0, 0);
	}

	public void setMapSize(int height, int width)
	{
		map_size.x = height;
		map_size.y = width;
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
	
}
