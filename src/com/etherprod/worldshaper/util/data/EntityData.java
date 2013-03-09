package com.etherprod.worldshaper.util.data;

import java.io.Serializable;

public class EntityData implements Serializable
{
	private static final long	serialVersionUID	= 1L;
	public EntityType			type;
	public String				tileType;

	public enum EntityType
	{
		TILE,
		EMPTY
	}

	public EntityData(EntityType type)
	{
		super();
		this.type = type;
	}

	public EntityType getType()
	{
		return type;
	}

	public String getTileType()
	{
		return tileType;
	}

	public void setTileType(String type)
	{
		this.tileType = type;
	}
}
