package com.etherprod.worldshaper.ui;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import android.opengl.GLES20;

public class ClippedAnimatedSprite extends AnimatedSprite
{
	private int clipX = 0;
	private int clipY = 0;
	private int clipHeight = 0;
	private int clipWidth = 0;

	public ClippedAnimatedSprite(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, 
			VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
	}

	@Override
	protected void onManagedDraw(final GLState pGLState, final Camera pCamera)
	{
		// enable ScissorTest and save previous state
		final boolean wasScissorTestEnabled = pGLState.enableScissorTest();

		// apply clipping
		GLES20.glScissor(clipX, clipY, clipHeight, clipWidth);

		// call parent class function
		super.onManagedDraw(pGLState, pCamera);

		// restore ScissorTest state
		pGLState.setScissorTestEnabled(wasScissorTestEnabled);
	}
	
	public void setClip(int clipX, int clipY, int clipHeight, int clipWidth)
	{
		this.clipX = clipX;
		this.clipY = clipY;
		this.clipHeight = clipHeight;
		this.clipWidth = clipWidth;
	}
}
