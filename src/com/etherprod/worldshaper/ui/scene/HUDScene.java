package com.etherprod.worldshaper.ui.scene;

import org.andengine.engine.camera.hud.HUD;
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
import com.etherprod.worldshaper.ui.MouseOnScreenControl;
import com.etherprod.worldshaper.ui.MyAnalogOnScreenControl;
import com.etherprod.worldshaper.ui.MyAnalogOnScreenControl.IMyAnalogOnScreenControlListener;
import com.etherprod.worldshaper.ui.MyBaseOnScreenControl;

import android.opengl.GLES20;

public abstract class HUDScene extends MyScene
{
	private BuildableBitmapTextureAtlas	uiTextureAtlas;
	private BitmapTextureAtlas			leftPadTexture;
	private ITextureRegion				leftPadBaseRegion;
	private ITextureRegion				leftPadPointerRegion;
	private BitmapTextureAtlas			mouseTexture;
	private ITextureRegion				mouseBaseRegion;
	private ITextureRegion				mousePointerRegion;

	private ITextureRegion				jump_texture;

	protected HUD 						gameHUD;

	// life
	private Text							lifeText;
	private static TiledTextureRegion		lifebar_region;
	private static ITextureRegion			lifebar_bg_region;
	private static ClippedAnimatedSprite	lifebar;
	private static Sprite					lifebar_bg;
	private final static float				LIFEBAR_WIDTH = 96f;

	// constants
	private final static int LIFEBAR_BG_POSITION_X = 584;
	private final static int LIFEBAR_BG_POSITION_Y = 14;

	@Override
	public void createScene()
	{

	}

	public void loadResouces()
	{
		onCreateResources();
		createHUD();
	}

	private void onCreateResources()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/hud/");

		// left pad
		this.leftPadTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.leftPadBaseRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.leftPadTexture, activity,
						"onscreen_control_base.png", 0, 0);
		this.leftPadPointerRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.leftPadTexture, activity,
						"onscreen_control_knob.png", 0, 60);
		this.leftPadTexture.load();

		// mouse
		this.mouseTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
		this.mouseBaseRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mouseTexture, activity,
						"mouse_base.png", 0, 0);
		this.mousePointerRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mouseTexture, activity,
						"mouse_pointer.png", 256, 0);
		this.mouseTexture.load();

		// jump button
		uiTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024,
				1024, TextureOptions.BILINEAR);

		jump_texture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(uiTextureAtlas, activity, "jump_button.png");

		// life bar
		lifebar_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(uiTextureAtlas, activity, "lifebar.png", 1, 21);
		lifebar_bg_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(uiTextureAtlas, activity, "lifebar_bg.png");

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

		// adding controls
		createJump();
		MyAnalogOnScreenControl leftPad = createVelocityPad();
		createMouse(leftPad);
	}

	private void createJump()
	{
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
		// life bar
		lifebar = new ClippedAnimatedSprite(634, 21, lifebar_region,
				activity.getVertexBufferObjectManager());

		final long[] progressbar_animate = new long[] { 20000, 300, 300, 300, 300, 300, 300, 
				300, 300, 300, 300, 300, 20000, 300, 300, 300, 300, 300, 300, 300, 300 };
		lifebar.animate(progressbar_animate, true);
		lifebar.setScale(2f);

		lifebar_bg = new Sprite(LIFEBAR_BG_POSITION_X, LIFEBAR_BG_POSITION_Y, lifebar_bg_region,
				activity.getVertexBufferObjectManager());
		gameHUD.attachChild(lifebar_bg);
		gameHUD.attachChild(lifebar);

		lifeText = new Text(0, 0, resourcesManager.getLifeTextFont(), "99999/99999", 
				new TextOptions(HorizontalAlign.LEFT), activity.getVertexBufferObjectManager());
		gameHUD.attachChild(lifeText);

		// set life to max
		setLife(50, 50);
	}

	private MyAnalogOnScreenControl createVelocityPad()
	{
		final float y = activity.getCAMERA_HEIGHT()
				- this.leftPadBaseRegion.getHeight();

		/* Velocity control (left). */
		final MyAnalogOnScreenControl velocityPad = new MyAnalogOnScreenControl(10f, y - 40, 0f, 60f,
				activity.getCamera(), this.leftPadBaseRegion, this.leftPadPointerRegion, 0.1f,
				activity.getVertexBufferObjectManager(),
				new IMyAnalogOnScreenControlListener()
				{
					@Override
					public void onControlChange(final MyBaseOnScreenControl control,
							final float pValueX, final float pValueY)
					{
						onLeftControlChange(control, pValueX, pValueY);
					}

					@Override
					public void onControlClick(final MyAnalogOnScreenControl control)
					{
						onLeftControlClick(control);
					}
				});
		velocityPad.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		velocityPad.getControlBase().setAlpha(0.5f);

		gameHUD.setChildScene(velocityPad);

		return velocityPad;
	}

	private void createMouse(MyAnalogOnScreenControl leftPad)
	{
		final float x = (activity.getCAMERA_WIDTH() - this.mouseBaseRegion.getWidth()) / 2;
		final float y = (activity.getCAMERA_HEIGHT() - this.mouseBaseRegion.getHeight()) / 2;

		/* Mouse control */
		final MouseOnScreenControl mouseOnScreenControl = new MouseOnScreenControl(x, y, 80,
				80, activity.getCamera(), this.mouseBaseRegion,
				this.mousePointerRegion, 0.1f, activity.getVertexBufferObjectManager());

		mouseOnScreenControl.getControlKnob().setBlendFunction(
				GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		mouseOnScreenControl.getControlKnob().setAlpha(0.5f);

		leftPad.setChildScene(mouseOnScreenControl);
	}

	@Override
	public void disposeScene()
	{
		// remove HUD on scene leave
		activity.getCamera().setHUD(null);
	}

	public void setLife(int life, int max)
	{
		lifeText.setText(Integer.toString(life) + "/" + Integer.toString(max));
		// center it to life bar
		float pX = LIFEBAR_BG_POSITION_X + (lifebar_bg.getWidth() / 2) - (lifeText.getWidth() / 2);
		float pY = LIFEBAR_BG_POSITION_Y + (lifebar_bg.getHeight() / 2) - (lifeText.getHeight() / 2) + 2;
		lifeText.setPosition(pX, pY);

		int life_width = (int) (((float)life / (float) max) * LIFEBAR_WIDTH);
		lifebar.setClip(587, 440, (int) ((life_width + 1) * lifebar.getScaleX()), 24);
	}

	//=====================================
	//         Abstract functions
	//=====================================

	protected abstract void onLeftControlChange(MyBaseOnScreenControl pBaseOnScreenControl,
			float pValueX, float pValueY);

	protected abstract void onLeftControlClick(final MyAnalogOnScreenControl pAnalogOnScreenControl);

	protected abstract void onJumpButtonClick();
}
