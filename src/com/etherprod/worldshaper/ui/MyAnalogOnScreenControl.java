package com.etherprod.worldshaper.ui;
	
import org.andengine.engine.camera.Camera;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

public class MyAnalogOnScreenControl extends MyBaseOnScreenControl implements IClickDetectorListener
{
	private final ClickDetector mClickDetector = new ClickDetector(this);

	public MyAnalogOnScreenControl(final float pX, final float pY, final float borderX, final float borderY,
			final Camera pCamera, final ITextureRegion pControlBaseTextureRegion,
			final ITextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates,
			final VertexBufferObjectManager pVertexBufferObjectManager, 
			final IMyAnalogOnScreenControlListener pAnalogOnScreenControlListener)
	{
		super(pX, pY, borderX, borderY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion,
				pTimeBetweenUpdates, pVertexBufferObjectManager, pAnalogOnScreenControlListener);

		this.mClickDetector.setEnabled(false);
	}

	public MyAnalogOnScreenControl(final float pX, final float pY, final float borderX, final float borderY,
			final Camera pCamera, final ITextureRegion pControlBaseTextureRegion,
			final ITextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates,
			final long pOnControlClickMaximumMilliseconds, final VertexBufferObjectManager pVertexBufferObjectManager,
			final IMyAnalogOnScreenControlListener pAnalogOnScreenControlListener)
	{
		super(pX, pY, borderX, borderY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion,
				pTimeBetweenUpdates, pVertexBufferObjectManager, pAnalogOnScreenControlListener);

		this.mClickDetector.setTriggerClickMaximumMilliseconds(pOnControlClickMaximumMilliseconds);
	}


	public MyAnalogOnScreenControl(final float pX, final float pY, final Camera pCamera,
			final ITextureRegion pControlBaseTextureRegion, final ITextureRegion pControlKnobTextureRegion,
			final float pTimeBetweenUpdates, final VertexBufferObjectManager pVertexBufferObjectManager,
			final IMyAnalogOnScreenControlListener pAnalogOnScreenControlListener)
	{
		this(pX, pY, 0f, 0f, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates,
				pVertexBufferObjectManager, pAnalogOnScreenControlListener);
	}

	public MyAnalogOnScreenControl(final float pX, final float pY, final Camera pCamera,
			final ITextureRegion pControlBaseTextureRegion, final ITextureRegion pControlKnobTextureRegion,
			final float pTimeBetweenUpdates, final long pOnControlClickMaximumMilliseconds,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final IMyAnalogOnScreenControlListener pAnalogOnScreenControlListener)
	{
		this(pX, pY, 0f, 0f, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion,
				pTimeBetweenUpdates, pOnControlClickMaximumMilliseconds, pVertexBufferObjectManager,
				pAnalogOnScreenControlListener);
	}

	@Override
	public IMyAnalogOnScreenControlListener getOnScreenControlListener()
	{
		return (IMyAnalogOnScreenControlListener)super.getOnScreenControlListener();
	}

	public void setOnControlClickEnabled(final boolean pOnControlClickEnabled) {
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
		this.getOnScreenControlListener().onControlClick(this);
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
			super.onUpdateControlKnob(((float) Math.cos(angleRad)) * 0.5f, ((float) Math.sin(angleRad)) * 0.5f);
		}
	}

	public interface IMyAnalogOnScreenControlListener extends IMyOnScreenControlListener
	{
		public void onControlClick(final MyAnalogOnScreenControl pAnalogOnScreenControl);
	}
}
