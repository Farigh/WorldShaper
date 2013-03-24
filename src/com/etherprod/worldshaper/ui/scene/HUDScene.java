package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.HorizontalAlign;

import com.etherprod.worldshaper.ui.ClippedAnimatedSprite;

import android.opengl.GLES20;

public abstract class HUDScene extends MyScene
{
	private BitmapTextureAtlas			mOnScreenControlTexture;
	private BuildableBitmapTextureAtlas	uiTextureAtlas;
	private ITextureRegion				mOnScreenControlBaseTextureRegion;
	private ITextureRegion				mOnScreenControlKnobTextureRegion;
	protected HUD 						gameHUD;
	
	// life
	private Text 							lifeText;
	private static TiledTextureRegion		lifebar_region;
	private static TiledTextureRegion		lifebar_bg_region;
	private static ClippedAnimatedSprite	lifebar;
	private final static float				LIFEBAR_WIDTH = 96f;

	private ITextureRegion				jump_texture;
	
	@Override
	public void createScene()
	{

	}
	
	public void loadResouces()
	{
		onCreateResources();
		createHUD();

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

		/* Rotation control (right). *//*
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

		velocityOnScreenControl.setChildScene(rotationOnScreenControl);*/
	}

	private void onCreateResources()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/hud/");
		this.mOnScreenControlTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, activity,
						"onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, activity,
						"onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();

		// jump button
		uiTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024,
				1024, TextureOptions.BILINEAR);

		jump_texture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(uiTextureAtlas, activity, "jump_button.png");

		// life bar
		lifebar_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(uiTextureAtlas, activity, "lifebar.png", 1, 21);
		lifebar_bg_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(uiTextureAtlas, activity, "lifebar_bg.png", 1, 1);

		try
		{
			uiTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, 
					BitmapTextureAtlas>(0, 1, 0));
			uiTextureAtlas.load();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void createHUD()
	{
		gameHUD = new HUD();

		// adding life text
		createLife();

		// adding jump button
		final float x = activity.getCAMERA_WIDTH() - jump_texture.getWidth() - 100;
		final float y = activity.getCAMERA_HEIGHT() - jump_texture.getHeight() - 50;

		final Sprite jump_button = new Sprite(x, y, jump_texture, 
				activity.getVertexBufferObjectManager())
		{
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
			{
				if (touchEvent.isActionUp())
				{
					onJumpButtonClick();
				}
				return true;
			};
		};

		jump_button.setScale(2.0f);

		gameHUD.registerTouchArea(jump_button);
		gameHUD.attachChild(jump_button);
	}

	private void createLife()
	{
		lifeText = new Text(0, 0, resourcesManager.getTitleFont(), "Life : 9999/9999", 
				new TextOptions(HorizontalAlign.LEFT), activity.getVertexBufferObjectManager());
		lifeText.setSkewCenter(0, 0);    
		lifeText.setText("Life: 50/50");
		lifeText.setScale(0.8f);
		gameHUD.attachChild(lifeText);

		// life bar
		lifebar = new ClippedAnimatedSprite(634, 21, lifebar_region,
				activity.getVertexBufferObjectManager());

		final long[] progressbar_animate = new long[] { 20000, 300, 300, 300, 300, 300, 300, 
				300, 300, 300, 300, 300, 20000, 300, 300, 300, 300, 300, 300, 300, 300 };
		lifebar.animate(progressbar_animate, true);
		lifebar.setScale(2f);

		gameHUD.attachChild(new Sprite(584, 14, lifebar_bg_region, activity.getVertexBufferObjectManager()));
		gameHUD.attachChild(lifebar);

		// set life to max
		setLife(50, 50);
	}

	@Override
	public void disposeScene()
	{
		// remove HUD on scene leave
		activity.getCamera().setHUD(null);
	}

	public void setLife(int life, int max)
	{
		lifeText.setText("Life: " + Integer.toString(life) + "/" + Integer.toString(max));

		int life_width = (int) (((float)life / (float) max) * LIFEBAR_WIDTH);
		lifebar.setClip(587, 440, (int) ((life_width + 1) * lifebar.getScaleX()), 24);
	}

	//=====================================
	//         Abstract functions
	//=====================================

	protected abstract void onLeftControlChange(BaseOnScreenControl pBaseOnScreenControl,
			float pValueX, float pValueY);

	protected abstract void onLeftControlClick(final AnalogOnScreenControl pAnalogOnScreenControl);

	protected abstract void onRightControlChange(BaseOnScreenControl pBaseOnScreenControl,
			float pValueX, float pValueY);

	protected abstract void onRightControlClick(final AnalogOnScreenControl pAnalogOnScreenControl);

	protected abstract void onJumpButtonClick();
}
