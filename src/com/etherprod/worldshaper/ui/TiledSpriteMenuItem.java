package com.etherprod.worldshaper.ui;

import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class TiledSpriteMenuItem extends TiledSprite implements IMenuItem
{
	private final int mID;

	public TiledSpriteMenuItem(final int pID, final ITiledTextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(0, 0, pTextureRegion, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final ITiledTextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram)
	{
		super(0, 0, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final ITiledTextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType)
	{
		super(0, 0, pTextureRegion, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final ITiledTextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType,
			final ShaderProgram pShaderProgram)
	{
		super(0, 0, pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight,
			final ITiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(0, 0, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight,
			final ITiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager,
			final ShaderProgram pShaderProgram)
	{
		super(0, 0, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight,
			final ITiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType)
	{
		super(0, 0, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight,
			final ITiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType, final ShaderProgram pShaderProgram)
	{
		super(0, 0, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	@Override
	public int getID()
	{
		return this.mID;
	}

	@Override
	public void onSelected()
	{

	}

	@Override
	public void onUnselected()
	{

	}
}
