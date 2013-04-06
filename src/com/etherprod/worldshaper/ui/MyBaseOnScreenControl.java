package com.etherprod.worldshaper.ui;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import android.view.MotionEvent;

public abstract class MyBaseOnScreenControl extends HUD implements IOnSceneTouchListener
{
	private static final int INVALID_POINTER_ID = -1;

	private final Sprite mControlBase;
	private final Sprite mControlKnob;

	private float mControlValueX;
	private float mControlValueY;
	
	private float controlBorderX = 0;
	private float controlBorderY = 0;

	private final IMyOnScreenControlListener mOnScreenControlListener;

	private int mActivePointerID = INVALID_POINTER_ID;

	public MyBaseOnScreenControl(final float pX, final float pY, final float borderX, final float borderY,
			final Camera pCamera, final ITextureRegion pControlBaseTextureRegion, 
			final ITextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final IMyOnScreenControlListener pOnScreenControlListener)
	{
		this(pX, pY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates,
				pVertexBufferObjectManager, pOnScreenControlListener);
		this.controlBorderX = borderX;
		this.controlBorderY = borderY;
	}
	
	public MyBaseOnScreenControl(final float pX, final float pY, final Camera pCamera,
			final ITextureRegion pControlBaseTextureRegion, final ITextureRegion pControlKnobTextureRegion,
			final float pTimeBetweenUpdates, final VertexBufferObjectManager pVertexBufferObjectManager,
			final IMyOnScreenControlListener pOnScreenControlListener)
	{
		this.setCamera(pCamera);

		this.mOnScreenControlListener = pOnScreenControlListener;
		/* Create the control base. */
		this.mControlBase = new Sprite(pX, pY, pControlBaseTextureRegion, pVertexBufferObjectManager)
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
					final float pTouchAreaLocalY)
			{
				return MyBaseOnScreenControl.this.onHandleControlBaseTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};

		/* Create the control knob. */
		this.mControlKnob = new Sprite(0, 0, pControlKnobTextureRegion, pVertexBufferObjectManager);
		this.onHandleControlKnobReleased();

		/* Register listeners and add objects to this HUD. */
		this.setOnSceneTouchListener(this);
		this.registerTouchArea(this.mControlBase);
		this.registerUpdateHandler(new TimerHandler(pTimeBetweenUpdates, true, new ITimerCallback()
		{
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler)
			{
				MyBaseOnScreenControl.this.mOnScreenControlListener.onControlChange(MyBaseOnScreenControl.this,
						MyBaseOnScreenControl.this.mControlValueX, MyBaseOnScreenControl.this.mControlValueY);
			}
		}));

		this.attachChild(this.mControlBase);
		this.attachChild(this.mControlKnob);

		this.setTouchAreaBindingOnActionDownEnabled(true);
	}

	public Sprite getControlBase()
	{
		return this.mControlBase;
	}

	public Sprite getControlKnob()
	{
		return this.mControlKnob;
	}

	public IMyOnScreenControlListener getOnScreenControlListener()
	{
		return this.mOnScreenControlListener;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent)
	{
		final int pointerID = pSceneTouchEvent.getPointerID();
		if(pointerID == this.mActivePointerID) {
			this.onHandleControlBaseLeft();

			switch(pSceneTouchEvent.getAction()) {
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					this.mActivePointerID = INVALID_POINTER_ID;
			}
		}
		return false;
	}

	public void refreshControlKnobPosition()
	{
		this.onUpdateControlKnob(this.mControlValueX * 0.5f, this.mControlValueY * 0.5f);
	}

	/**
	 *  When the touch happened outside of the bounds of this OnScreenControl.
	 * */
	protected void onHandleControlBaseLeft()
	{
		this.onUpdateControlKnob(0, 0);
	}

	/**
	 * When the OnScreenControl was released.
	 */
	protected void onHandleControlKnobReleased()
	{
		this.onUpdateControlKnob(0, 0);
	}

	protected boolean onHandleControlBaseTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
			final float pTouchAreaLocalY)
	{
		final int pointerID = pSceneTouchEvent.getPointerID();

		switch(pSceneTouchEvent.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if(this.mActivePointerID == INVALID_POINTER_ID)
				{
					this.mActivePointerID = pointerID;
					this.updateControlKnob(pTouchAreaLocalX, pTouchAreaLocalY);
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if(this.mActivePointerID == pointerID)
				{
					this.mActivePointerID = INVALID_POINTER_ID;
					this.onHandleControlKnobReleased();
					return true;
				}
				break;
			default:
				if(this.mActivePointerID == pointerID)
				{
					this.updateControlKnob(pTouchAreaLocalX, pTouchAreaLocalY);
					return true;
				}
				break;
		}
		return true;
	}

	private void updateControlKnob(final float pTouchAreaLocalX, final float pTouchAreaLocalY)
	{
		final Sprite controlBase = this.mControlBase;

		final float relativeX = MathUtils.bringToBounds(0, controlBase.getWidth(),
				pTouchAreaLocalX) / controlBase.getWidth() - 0.5f;
		final float relativeY = MathUtils.bringToBounds(0, controlBase.getHeight(),
				pTouchAreaLocalY) / controlBase.getHeight() - 0.5f;

		this.onUpdateControlKnob(relativeX, relativeY);
	}

	/**
	 * @param pRelativeX from <code>-0.5</code> (left) to <code>0.5</code> (right).
	 * @param pRelativeY from <code>-0.5</code> (top) to <code>0.5</code> (bottom).
	 */
	protected void onUpdateControlKnob(final float pRelativeX, final float pRelativeY)
	{
		final Sprite controlBase = this.mControlBase;
		final Sprite controlKnob = this.mControlKnob;

		this.mControlValueX = 2 * pRelativeX;
		this.mControlValueY = 2 * pRelativeY;

		final float[] controlBaseSceneCenterCoordinates = controlBase.getSceneCenterCoordinates();
		final float x = controlBaseSceneCenterCoordinates[VERTEX_INDEX_X] - controlKnob.getWidth() * 0.5f
				+ pRelativeX * (controlBase.getWidthScaled() - controlBorderX);
		final float y = controlBaseSceneCenterCoordinates[VERTEX_INDEX_Y] - controlKnob.getHeight() * 0.5f
				+ pRelativeY * (controlBase.getHeightScaled() - controlBorderY);

		controlKnob.setPosition(x, y);
	}

	public static interface IMyOnScreenControlListener
	{
		/**
		 * @param pBaseOnScreenControl
		 * @param pValueX between <code>-1</code> (left) to <code>1</code> (right).
		 * @param pValueY between <code>-1</code> (up) to <code>1</code> (down).
		 */
		public void onControlChange(final MyBaseOnScreenControl pBaseOnScreenControl, final float pValueX,
				final float pValueY);
	}
}
