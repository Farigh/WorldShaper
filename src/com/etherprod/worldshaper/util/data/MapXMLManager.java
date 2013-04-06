package com.etherprod.worldshaper.util.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import com.etherprod.worldshaper.MainActivity;
import com.etherprod.worldshaper.SceneManager;
import com.etherprod.worldshaper.util.data.EntityData;
import com.etherprod.worldshaper.util.data.MapData;
import com.etherprod.worldshaper.util.data.EntityData.EntityType;

public class MapXMLManager extends DefaultHandler
{
	private String			tmpVal;
	private MapData			mapData;
	private static int		tile_number = 0;
	private static int		current_parsed = 0;

	public MapData load(MainActivity activity, String filename)
	{
		mapData = new MapData();
		tile_number = 0;
		current_parsed = 0;

		//get a factory
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try
		{
			SAXParser parser = factory.newSAXParser();

			InputStream file = activity.openFileInput(filename);
			InputStream base64 = new Base64InputStream(file, Base64.DEFAULT);
			InputStream compress = new GZIPInputStream(base64);
			InputStream stream = new BufferedInputStream(compress);

			parser.parse(stream, this);
			
			stream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return mapData;
	}

	public static MapData loadMapFromFile(MainActivity activity, String filename)
	{
		MapXMLManager parser = new MapXMLManager();
		MapData data = parser.load(activity, filename);
		
		parser = null;
		
		return data;
	}

	/**
	 * Saves the map to the following format :
	 *   <?xml version=\"1.0\" encoding=\"utf-8\"?>
	 *   <level tiles="${tile-number}" width="${width}" height="${height}">
	 *   	<spawn x="${spawn_x}" y="${spawn_y}" />
	 *		<entities>
	 *			tile_type1;x1;y1@[...]@tile_typeN;xN;yN
	 * 		</entities>
	 *   </level>
	 * The file is a compressed base64 encoded one
	 *
	 * @param activity The game activity
	 * @param filename The filename to save the map to
	 * @param data     The map data to be saved
	 */
	public static void saveMapToFile(MainActivity activity, String filename, MapData data)
	{
		tile_number = data.getTileNumber();
		current_parsed = 0;

		try
		{
			OutputStream file = activity.openFileOutput(filename, Context.MODE_PRIVATE);
			OutputStream buffer = new BufferedOutputStream(file);
			OutputStream base64 = new Base64OutputStream(buffer, Base64.DEFAULT);
			OutputStream compress = new GZIPOutputStream(base64);
			OutputStreamWriter stream = new OutputStreamWriter(compress);

			try
			{
				int height = data.getMapSize().x;
				int width = data.getMapSize().y;
				EntityData[][] map = data.getMap();

				stream.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
				stream.write("<level tiles=\"" + data.getTileNumber() + "\" width=\"" + width 
						+ "\" height=\"" + height + "\">\n");
				stream.write("<spawn x=\"" + data.getMapSpawn().x
						+ "\" y=\"" + data.getMapSpawn().y + "\" />\n"
						+ "<entities>");

				for (int i = 0; i < width; i++)
				{
					for (int j = 0; j < height; j++)
					{
						if (map[i][j] != null)
						{
							int previous = (int)(((float)current_parsed / (float)tile_number) * 50f);
							int now = (int)(((float)(current_parsed + 1) / (float)tile_number) * 50f);
							int percent = (int)(((float)(current_parsed + 1) / (float)tile_number) * 100f);
							current_parsed++;

							if (previous != now)
								SceneManager.getInstance().setProgress(30 + now, "Saving world map "
										+ percent + "%");

							stream.write(map[i][j].getTileType() + ";" + (i + 1) + ";" + (j + 1) + "@");
						}
					}
				}

				stream.write("</entities>\n</level>");

				stream.flush();
			}
			finally
			{
				stream.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	//Event Handlers
	public void startElement(String uri, String localName, String qName,
		Attributes attributes) throws SAXException 
	{
		if (qName.equalsIgnoreCase("entities"))
			tmpVal = "";

		if (qName.equalsIgnoreCase("level"))
		{
			mapData.setMapSize(Integer.parseInt(attributes.getValue("height")),
					Integer.parseInt(attributes.getValue("width")));

			tile_number = Integer.parseInt(attributes.getValue("tiles"));
		}

		if (qName.equalsIgnoreCase("spawn"))
		{
			mapData.setMapSpawn(Integer.parseInt(attributes.getValue("x")),
					Integer.parseInt(attributes.getValue("y")));
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		tmpVal += new String(ch, start, length);
		
		boolean ends_with_at = tmpVal.endsWith("@");
		
		String entities[] = tmpVal.split("@");
		for (int i = 0; i < (entities.length - 1); i++)
		{
			int previous = (int)(((float)current_parsed / (float)tile_number) * 75f);
			int now = (int)(((float)(current_parsed + 1) / (float)tile_number) * 75f);
			int percent = (int)(((float)(current_parsed + 1) / (float)tile_number) * 100f);

			if (previous != now)
				SceneManager.getInstance().setProgress(5 + now, "Loading world map " + percent + "%");

			parseEntityInfo(entities[i]);

			current_parsed++;
		}

		tmpVal = entities[entities.length - 1];

		// put back @ so it wont merge 2 entities
		if (ends_with_at)
			tmpVal += "@";
	}

	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if(qName.equalsIgnoreCase("entities") && (tmpVal != ""))
		{
			// don't forget the last entity 
			parseEntityInfo(tmpVal);
		}
	}
	
	/**
	 * Handles entity lines of the file (tile_type;x;y)
	 *
	 * @param buf The fist line
	 */
	private void parseEntityInfo(String buf)
	{
		String fields[] = buf.split(";");

		EntityData entity = mapData.addEntity(EntityType.TILE, Integer.parseInt(fields[1]) - 1,
				Integer.parseInt(fields[2]) - 1);
		entity.setTileType(fields[0]);
	}
}
