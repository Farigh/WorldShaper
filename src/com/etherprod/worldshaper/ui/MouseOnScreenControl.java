package com.etherprod.worldshaper.ui;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

public class MouseOnScreenControl extends MyBaseOnScreenControl implements IClickDetectorListener
{
	private final ClickDetector mClickDetector = new ClickDetector(this);

	public MouseOnScreenControl(final float pX, final float pY, final float borderX, final float borderY,
			final Camera pCamera, final ITextureRegion pControlBaseTextureRegion,
			final ITextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates,
			final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(pX, pY, borderX, borderY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion,
				pTimeBetweenUpdates, pVertexBufferObjectManager, new MouseOnScreenControlListener());

		this.getOnScreenControlListener().setPointer(getControlKnob());

		this.init();

		this.mClickDetector.setEnabled(false);
	}

	public MouseOnScreenControl(final float pX, final float pY, final float borderX, final float borderY,
			final Camera pCamera, final ITextureRegion pControlBaseTextureRegion,
			final ITextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates,
			final long pOnControlClickMaximumMilliseconds, final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(pX, pY, borderX, borderY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion,
				pTimeBetweenUpdates, pVertexBufferObjectManager, new MouseOnScreenControlListener());

		this.getOnScreenControlListener().setPointer(getControlKnob());

		this.init();

		this.mClickDetector.setTriggerClickMaximumMilliseconds(pOnControlClickMaximumMilliseconds);
	}

	public MouseOnScreenControl(final float pX, final float pY, final Camera pCamera,
			final ITextureRegion pControlBaseTextureRegion, final ITextureRegion pControlKnobTextureRegion,
			final float pTimeBetweenUpdates, final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		this(pX, pY, 0f, 0f, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates,
				pVertexBufferObjectManager);
	}

	public MouseOnScreenControl(final float pX, final float pY, final Camera pCamera,
			final ITextureRegion pControlBaseTextureRegion, final ITextureRegion pControlKnobTextureRegion,
			final float pTimeBetweenUpdates, final long pOnControlClickMaximumMilliseconds,
			final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		this(pX, pY, 0f, 0f, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates,
				pOnControlClickMaximumMilliseconds, pVertexBufferObjectManager);
	}

	@Override
	public MouseOnScreenControlListener getOnScreenControlListener()
	{
		return (MouseOnScreenControlListener) super.getOnScreenControlListener();
	}

	public void setOnControlClickEnabled(final boolean pOnControlClickEnabled)
	{
		this.mClickDetector.setEnabled(pOnControlClickEnabled);
	}

	public void setOnControlClickMaximumMilliseconds(final long pOnControlClickMaximumMilliseconds)
	{
		this.mClickDetector.setTriggerClickMaximumMilliseconds(pOnControlClickMaximumMilliseconds);
	}

	@Override
	public void onClick(final ClickDetector pClickDetector, final int pPointerID, final float pSceneX,
			final float pSceneY)
	{
		// Nothing to do here
	}

	@Override
	protected boolean onHandleControlBaseTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
			final float pTouchAreaLocalY)
	{
		this.mClickDetector.onSceneTouchEvent(null, pSceneTouchEvent);
		return super.onHandleControlBaseTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}

	@Override
	protected void onUpdateControlKnob(final float pRelativeX, final float pRelativeY)
	{
		if(pRelativeX * pRelativeX + pRelativeY * pRelativeY <= 0.25f)
		{
			super.onUpdateControlKnob(pRelativeX, pRelativeY);
		}
		else
		{
			final float angleRad = MathUtils.atan2(pRelativeY, pRelativeX);
			super.onUpdateControlKnob((float) Math.cos(angleRad) * 0.5f, (float) Math.sin(angleRad) * 0.5f);
		}
	}

	public static class MouseOnScreenControlListener implements IMyOnScreenControlListener
	{
		private Sprite pointer = null;

		public void setPointer(Sprite pointer)
		{
			this.pointer = pointer;
		}

		@Override
		public void onControlChange(MyBaseOnScreenControl pBaseOnScreenControl, float pValueX, float pValueY)
		{
			if (pointer == null)
				return;

			if (pValueX == 0 && pValueY == 0)
				pointer.setRotation(0);
			else
				pointer.setRotation(MathUtils.radToDeg((float) Math.atan2(pValueX, -pValueY)));
		}
	}

	@Override
	protected void onHandleControlKnobReleased()
	{
		// Do not reset position on release
	}

	private void init()
	{
		// initialize to the right
		this.onUpdateControlKnob(1, 0);
	}
}
