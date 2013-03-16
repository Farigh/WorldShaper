package com.etherprod.worldshaper.util.loader;

import android.os.AsyncTask;

public class AsyncTaskRunner extends AsyncTask<IAsyncTask, Integer, Void>
{
    // instance
	private static final AsyncTaskRunner INSTANCE = new AsyncTaskRunner();

	private IAsyncTask[] task;
	
	public static AsyncTaskRunner getInstance()
    {
        return INSTANCE;
    }
	
	@Override
	protected Void doInBackground(IAsyncTask... tasks)
	{
		this.task = tasks;
		
		task[0].execute();
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{
		task[0].onTaskCompleted();
	}
}
