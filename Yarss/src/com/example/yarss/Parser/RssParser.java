package com.example.yarss.Parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.net.ParseException;
import android.util.Xml;

public class RssParser implements IParser {

	private static final String dateFormat = "EEE, d MMM yyyy HH:mm:ss Z";

	public ArrayList<Item> parse(String urlString) {
		XmlPullParser parser = Xml.newPullParser();
		ArrayList<Item> items = new ArrayList<Item>();
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(150000);
			connection.setConnectTimeout(10000);
			connection.setRequestMethod("GET");
			connection.connect();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(connection.getInputStream(), null);
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
					items.add(readItem(parser));
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return items;
	}

	private Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException,
			ParseException {
		Item item = new Item();
		int eventType = parser.getEventType();
		while (true) {
			if (eventType == XmlPullParser.END_TAG && parser.getName().equals("item")) {
				return item;
			} else if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("title")) {
					eventType = parser.next();
					item.setTitle(parser.getText());
				} else if (parser.getName().equals("link")) {
					eventType = parser.next();
					item.setLink(parser.getText());
				} else if (parser.getName().equals("description")) {
					eventType = parser.next();
					item.setDescription(parser.getText());
				} else if (parser.getName().equals("guid")) {
					eventType = parser.next();
					item.setGuid(parser.getText());
				} else if (parser.getName().equals("pubDate")) {
					eventType = parser.next();
					SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
					Date date;
					try {
						date = format.parse(parser.getText());
						item.setDate(date.getTime() / 1000L);
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
				}
			}
			
			eventType = parser.next();
		}
	}
}
