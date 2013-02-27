package com.etherprod.worldshaper.ui;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.opengl.GLES20;
import android.util.DisplayMetrics;

public abstract class ControlsActivity extends SimpleBaseGameActivity
{
	// ===========================================================
	// Constants
	// ===========================================================
	private static int			CAMERA_WIDTH	= 480;
	private static int			CAMERA_HEIGHT	= 320;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera				mCamera;

	private BitmapTextureAtlas	mOnScreenControlTexture;
	private ITextureRegion		mOnScreenControlBaseTextureRegion;
	private ITextureRegion		mOnScreenControlKnobTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getCAMERA_WIDTH()
	{
		return CAMERA_WIDTH;
	}

	public int getCAMERA_HEIGHT()
	{
		return CAMERA_HEIGHT;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() 
	{
		// getting current screen dimensions
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		CAMERA_HEIGHT = metrics.heightPixels;
		CAMERA_WIDTH = metrics.widthPixels;

		// Sets the camera
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		// Sets the engine options to landscape
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		
		// multi-touch needed
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

		return engineOptions;
	}

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
	public Scene onCreateScene()
	{
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		/* Velocity control (left). */
		final float y = CAMERA_HEIGHT
				- this.mOnScreenControlBaseTextureRegion.getHeight();
		final AnalogOnScreenControl velocityOnScreenControl = new AnalogOnScreenControl(
				0, y, this.mCamera, this.mOnScreenControlBaseTextureRegion,
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

		scene.setChildScene(velocityOnScreenControl);

		/* Rotation control (right). */
		final float x = CAMERA_WIDTH
				- this.mOnScreenControlBaseTextureRegion.getWidth();
		final AnalogOnScreenControl rotationOnScreenControl = new AnalogOnScreenControl(
				x, y, this.mCamera, this.mOnScreenControlBaseTextureRegion,
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

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

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
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
