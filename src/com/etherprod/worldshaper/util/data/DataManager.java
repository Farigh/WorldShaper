package com.etherprod.worldshaper.util.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.andengine.util.debug.Debug;

import android.content.Context;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.objects.Map;

public class DataManager
{
	private static String mapFile = "map.dat";
	
	public static void saveMap(MainActivity activity, MapData data)
	{
		try
		{
			OutputStream file = activity.openFileOutput(mapFile, Context.MODE_PRIVATE);
			OutputStream base64 = new Base64OutputStream(file, Base64.DEFAULT);
			OutputStream compress = new GZIPOutputStream(base64);
			OutputStream buffer = new BufferedOutputStream(compress);
			ObjectOutput output = new ObjectOutputStream(buffer);

			try
			{
				output.writeObject(data);
			}
			finally
			{
				output.close();
			}
		}
		catch (Exception e)
		{
			Debug.e(e);
		}
	}
	
	public static Map loadMap(MainActivity activity)
	{
		Map map = null;

		try
		{
			InputStream file = activity.openFileInput(mapFile);
			InputStream base64 = new Base64InputStream(file, Base64.DEFAULT);
			InputStream compress = new GZIPInputStream(base64);
			InputStream buffer = new BufferedInputStream(compress);
			ObjectInput input = new ObjectInputStream (buffer);

			try
			{
				map = (Map) input.readObject();
			}
			finally
			{
				input.close();
			}
		}
		catch(Exception e)
		{
			Debug.e(e);
		}
		
		return map;
	}
}
