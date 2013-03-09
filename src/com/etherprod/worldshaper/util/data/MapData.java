package com.etherprod.worldshaper.util.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.etherprod.worldshaper.util.IntVector2;
import com.etherprod.worldshaper.util.data.EntityData.EntityType;

public class MapData implements Serializable
{
	private static final long					serialVersionUID = 1L;
	public static transient final int			TILE_SIZE = 32;
	public static transient final EntityData	empty = new EntityData(EntityType.EMPTY);

	private IntVector2							map_size;
	private IntVector2							map_spawn;
	private ArrayList<ArrayList<EntityData>> 	map;

	public MapData()
	{
		map_size = new IntVector2(0, 0);
		map_spawn = new IntVector2(0, 0);
	}

	public void setMapSize(int height, int width)
	{
		map_size.x = height;
		map_size.y = width;

		map = new ArrayList<ArrayList<EntityData>>();

		// Initialize with empty tiles
		for (int i = 0; i < height; i++)
		{
			ArrayList<EntityData> list = new ArrayList<EntityData>();
			
			for (int j = 0; j < width; j++)
				list.add(empty);
			
			map.add(list);
		}
	}

	public EntityData addEntity(EntityType type, int x, int y)
	{
		EntityData data = new EntityData(type);
		
		ArrayList<EntityData> list = map.get(x);
		
		list.set(y, data);
		
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

	public ArrayList<ArrayList<EntityData>> getMap()
	{
		return map;
	}
}
