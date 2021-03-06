package com.etherprod.worldshaper.util.map;

import java.util.Random;

import com.etherprod.worldshaper.SceneManager;
import com.etherprod.worldshaper.objects.factories.TileFactory.TileType;
import com.etherprod.worldshaper.util.data.EntityData;
import com.etherprod.worldshaper.util.data.MapData;
import com.etherprod.worldshaper.util.data.EntityData.EntityType;

public class MapGenerator
{
	private static final float MIN_SKY_LIMIT = 0.15f; // dirt can't be generated over it
	private static final float DEFAULT_DIRT_LIMIT = 0.2f;
	private static final float MIN_DIRT_LIMIT = 0.25f; // dirt can't start under it
	private static final float DEFAULT_ROCK_LIMIT = 0.5f;

	private static MapData	mapData;
	private static Random 	randomizer = new Random();
	private static int 		dirtStart;
	private static int 		rockStart;

	private enum WorldSettings
	{
		HOME(400,800);

		private int height; public int getHeight() { return height; }
		private int width;  public int getWidth() { return width; }

		private WorldSettings(int height, int width)
		{
			this.height = height;
			this.width = width;
		}
	}

	public static MapData generateHome()
	{
		mapData = new MapData();
		mapData.setMapSize(WorldSettings.HOME.getHeight(), WorldSettings.HOME.getWidth());

		dirtStart = (int) (WorldSettings.HOME.getHeight() * 0.2); // not that much sky for home
		rockStart = (int) (WorldSettings.HOME.getHeight() * 0.5); // not that much sky for home

		generateWorld(WorldSettings.HOME);

		return mapData;
	}

	private static void generateWorld(WorldSettings settings)
	{ 
		int height = settings.getHeight();
		int width = settings.getWidth();

		// randomly increase/decrease layout start a little
		dirtStart += ((double) height * 0.001f * (double) randomizer.nextInt(30));
		rockStart += ((double) height * 0.005f * (double) randomizer.nextInt(30));

		int current_progress = 0;
		for (int i = 0; i < width; i++)
		{
			float progress = (float)i / (float) width;
			int now = (int)(progress * 25f);

			if (current_progress != now)
			{
				SceneManager.getInstance().setProgress(5 + now, "Terraforming in progress "
						+ (int)(progress * 100f) + "%");
				current_progress++;
			}

			for (int j = dirtStart; j < height; j++)
			{
				EntityData entity = mapData.addEntity(EntityType.TILE, i, j);

				if (j < rockStart)
					entity.setTileType(TileType.DIRT.toString());
				else
					entity.setTileType(TileType.ROCK.toString());
			}
		}

		// set spawn point
		mapData.setMapSpawn(width / 2, dirtStart - 1);
	}
}
