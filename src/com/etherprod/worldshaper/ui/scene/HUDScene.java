package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.HorizontalAlign;

import android.opengl.GLES20;

public abstract class HUDScene extends MyScene
{
	private BitmapTextureAtlas	mOnScreenControlTexture;
	private ITextureRegion		mOnScreenControlBaseTextureRegion;
	private ITextureRegion		mOnScreenControlKnobTextureRegion;
	private HUD 				gameHUD;
	private Text 				scoreText;
	
	@Override
    public void createScene()
    {
		createHUD();
		onCreateResources();

		/* Velocity control (left). */
		final float y = activity.getCAMERA_HEIGHT()
				- this.mOnScreenControlBaseTextureRegion.getHeight();
		final AnalogOnScreenControl velocityOnScreenControl = new AnalogOnScreenControl(
				0, y, activity.getCamera(), this.mOnScreenControlBaseTextureRegion,
				this.mOnScreenControlKnobTextureRegion, 0.1f,
				activity.getVertexBufferObjectManager(),
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

		gameHUD.setChildScene(velocityOnScreenControl);

		/* Rotation control (right). */
		final float x = activity.getCAMERA_WIDTH()
				- this.mOnScreenControlBaseTextureRegion.getWidth();
		final AnalogOnScreenControl rotationOnScreenControl = new AnalogOnScreenControl(
				x, y, activity.getCamera(), this.mOnScreenControlBaseTextureRegion,
				this.mOnScreenControlKnobTextureRegion, 0.1f,
				activity.getVertexBufferObjectManager(),
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

	private void onCreateResources()
	{
		this.mOnScreenControlTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, activity,
						"onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, activity,
						"onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();
	}
	
	private void createHUD()
	{
	    gameHUD = new HUD();

	    // adding life text
	    scoreText = new Text(0, 0, resourcesManager.getFont(), "Life : 9999/9999", 
	    		new TextOptions(HorizontalAlign.LEFT), activity.getVertexBufferObjectManager());
	    scoreText.setSkewCenter(0, 0);    
	    scoreText.setText("Life: 50/50");
	    scoreText.setScale(0.6f);
	    gameHUD.attachChild(scoreText);

	    activity.getCamera().setHUD(gameHUD);
	}

	@Override
	public void disposeScene()
	{
		// remove HUD on scene leave
	    activity.getCamera().setHUD(null);
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
