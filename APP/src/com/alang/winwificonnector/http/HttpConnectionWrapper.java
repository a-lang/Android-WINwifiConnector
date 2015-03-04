package com.alang.winwificonnector.http;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.alang.winwificonnector.util.Worker;

/**
 * @author Sasha Vasko
 *
 */
public abstract class HttpConnectionWrapper {
	private URL url;
	protected Map<String,String> headers = new HashMap<String,String>();
	protected String data;
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Map<String,String> getHeaders() {
		return headers;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	
	public void reset() {
		headers.clear();
		url = null;
		data = null;
	}

	public void showReceived (Worker context) {
		context.debug ("Page received:");
		for (String key : headers.keySet())
			context.debug("Field["+ key + "] = [" + headers.get(key) + "]");

		if (data != null) {
			context.debug ("Read " + data.length() + " bytes:");
			if (context.isSaveLogToFile()) {
				context.debug (data);
				context.debug ("#####################");
			}
		}
	}
	
	public abstract boolean post (Worker context, String postDataString, String cookies, String referer);
	public abstract boolean get (Worker context, String referer);
	
}
