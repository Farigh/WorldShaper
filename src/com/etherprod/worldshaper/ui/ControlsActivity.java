package com.etherprod.worldshaper.ui;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.opengl.GLES20;

/**
 * @author GARCIN David <david.garcin.pro@gmail.com>
 *
 * This class is a abstract one used to easily create game activities
 * with controls on it
 */
public abstract class ControlsActivity extends MyGameActivity
{
	private BitmapTextureAtlas	mOnScreenControlTexture;
	private ITextureRegion		mOnScreenControlBaseTextureRegion;
	private ITextureRegion		mOnScreenControlKnobTextureRegion;

	@Override
	public void onCreateResources() 
	{
		this.mOnScreenControlTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();
	}

	@Override
	public void onCreateScene()
	{

		/* Velocity control (left). */
		final float y = CAMERA_HEIGHT
				- this.mOnScreenControlBaseTextureRegion.getHeight();
		final AnalogOnScreenControl velocityOnScreenControl = new AnalogOnScreenControl(
				0, y, this.camera, this.mOnScreenControlBaseTextureRegion,
				this.mOnScreenControlKnobTextureRegion, 0.1f,
				this.getVertexBufferObjectManager(),
				new IAnalogOnScreenControlListener() {
					@Override
					public void onControlChange(
							final BaseOnScreenControl pBaseOnScreenControl,
							final float pValueX, final float pValueY)
					{
						onLeftControlChange(pBaseOnScreenControl, pValueX, pValueY);
					}

					@Override
					public void onControlClick(
							final AnalogOnScreenControl pAnalogOnScreenControl)
					{
						onLeftControlClick(pAnalogOnScreenControl);
					}
				});
		velocityOnScreenControl.getControlBase().setBlendFunction(
				GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		velocityOnScreenControl.getControlBase().setAlpha(0.5f);

		this.scene.setChildScene(velocityOnScreenControl);

		/* Rotation control (right). */
		final float x = CAMERA_WIDTH
				- this.mOnScreenControlBaseTextureRegion.getWidth();
		final AnalogOnScreenControl rotationOnScreenControl = new AnalogOnScreenControl(
				x, y, this.camera, this.mOnScreenControlBaseTextureRegion,
				this.mOnScreenControlKnobTextureRegion, 0.1f,
				this.getVertexBufferObjectManager(),
				new IAnalogOnScreenControlListener() {
					@Override
					public void onControlChange(
							final BaseOnScreenControl pBaseOnScreenControl,
							final float pValueX, final float pValueY)
					{
						onRightControlChange(pBaseOnScreenControl, pValueX, pValueY);
					}

					@Override
					public void onControlClick(
							final AnalogOnScreenControl pAnalogOnScreenControl)
					{
						onRightControlClick(pAnalogOnScreenControl);
					}
				});
		rotationOnScreenControl.getControlBase().setBlendFunction(
				GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		rotationOnScreenControl.getControlBase().setAlpha(0.5f);

		velocityOnScreenControl.setChildScene(rotationOnScreenControl);
	}

    //=====================================
    //         Abstract functions
    //=====================================

	protected abstract void onLeftControlChange(
			BaseOnScreenControl pBaseOnScreenControl,
			float pValueX, float pValueY);
	
	protected abstract void onLeftControlClick(
			final AnalogOnScreenControl pAnalogOnScreenControl);
	
	protected abstract void onRightControlChange(
			BaseOnScreenControl pBaseOnScreenControl,
			float pValueX, float pValueY);
	
	protected abstract void onRightControlClick(
			final AnalogOnScreenControl pAnalogOnScreenControl);
}
