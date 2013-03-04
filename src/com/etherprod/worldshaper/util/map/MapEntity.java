package com.etherprod.worldshaper.util.map;

public class MapEntity
{
	private int x;			public int getX() { return x; }
	private int y;			public int getY() { return y; }
	private String type;	public String getType() { return type; }

	public MapEntity(int x, int y, String type)
	{
		this.x = x;
		this.y = y;
		this.type = type;
	}
}
