package com.etherprod.worldshaper.objects;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class SelfDetachableSpriteWithBoby extends Sprite
{
	private Body			body;
	private PhysicsWorld	physicsWorld;

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram)
	{
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType)
	{
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pDrawType);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType,
			final ShaderProgram pShaderProgram)
	{
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final ISpriteVertexBufferObject pVertexBufferObject)
	{
		super(pX, pY, pTextureRegion, pVertexBufferObject);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final ISpriteVertexBufferObject pVertexBufferObject, final ShaderProgram pShaderProgram)
	{
		super(pX, pY, pTextureRegion, pVertexBufferObject, pShaderProgram);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final float pWidth, final float pHeight,
			final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final float pWidth, final float pHeight,
			final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager,
			final ShaderProgram pShaderProgram)
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final float pWidth, final float pHeight,
			final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType)
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pDrawType);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final float pWidth, final float pHeight,
			final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType, final ShaderProgram pShaderProgram)
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final float pWidth, final float pHeight,
			final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pSpriteVertexBufferObject)
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject);
	}

	public SelfDetachableSpriteWithBoby(final float pX, final float pY, final float pWidth, final float pHeight,
			final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pSpriteVertexBufferObject,
			final ShaderProgram pShaderProgram)
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject, pShaderProgram);
	}

	public void safeDetachSelfAndChildren(BaseGameActivity activity)
	{
		activity.runOnUpdateThread(new Runnable()
		{
			@Override
			public void run()
			{
				physicsWorld.destroyBody(SelfDetachableSpriteWithBoby.this.body);
				SelfDetachableSpriteWithBoby.this.detachChildren();
				SelfDetachableSpriteWithBoby.this.detachSelf();
			}
		});
	}

	public void setBody(PhysicsWorld physicsWorld, BodyType bodyType, FixtureDef fixtureDef)
	{
		this.physicsWorld = physicsWorld;
		this.body = PhysicsFactory.createBoxBody(physicsWorld, this, bodyType, fixtureDef);
	}
}
