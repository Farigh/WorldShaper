package com.etherprod.worldshaper.util.loader;

import com.etherprod.worldshaper.ui.scene.MyScene;

public abstract class IAsyncTask
{
	protected MyScene scene;

	public IAsyncTask(MyScene scene)
	{
		this.scene = scene;
	}

	public abstract void execute();
	public void onTaskCompleted() {};
}
