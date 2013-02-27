package com.etherprod.worldshaper;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathUtils;

import com.etherprod.worldshaper.ui.ControlsActivity;

public class MainActivity extends ControlsActivity 
{
	// ===========================================================
    // Constants
    // ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas	mBitmapTextureAtlas;
	private ITextureRegion		mFaceTextureRegion;
	private PhysicsHandler 		physicsHandler;
	private Sprite 				face;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public EngineOptions onCreateEngineOptions()
	{
		// TODO Auto-generated method stub
		return super.onCreateEngineOptions();
	}

	@Override
	public void onCreateResources()
	{
		super.onCreateResources();
		
		// create player texture
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"face_box.png", 0, 0);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene()
	{
		Scene scene = super.onCreateScene();
		
		final float centerX = (this.getCAMERA_WIDTH() - this.mFaceTextureRegion
				.getWidth()) / 2;
		final float centerY = (this.getCAMERA_HEIGHT() - this.mFaceTextureRegion
				.getHeight()) / 2;
		
		face = new Sprite(centerX, centerY,
				this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		
		physicsHandler = new PhysicsHandler(face);
		face.registerUpdateHandler(physicsHandler);

		scene.attachChild(face);

		return scene;
	}

	@Override
	protected void onLeftControlChange(
			BaseOnScreenControl pBaseOnScreenControl, float pValueX, 
			float pValueY) 
	{
		physicsHandler.setVelocity(pValueX * 100, pValueY * 100);		
	}

	@Override
	protected void onLeftControlClick(
			AnalogOnScreenControl pAnalogOnScreenControl)
	{
		/* Nothing. */
	}

	@Override
	protected void onRightControlChange(
			BaseOnScreenControl pBaseOnScreenControl, float pValueX, 
			float pValueY) 
	{
		if (pValueX == 0 && pValueY == 0)
		{
			face.setRotation(0);
		}
		else
		{
			face.setRotation(MathUtils.radToDeg((float) Math
					.atan2(pValueX, -pValueY)));
		}
	}

	@Override
	protected void onRightControlClick(
			AnalogOnScreenControl pAnalogOnScreenControl)
	{
		/* Nothing. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
