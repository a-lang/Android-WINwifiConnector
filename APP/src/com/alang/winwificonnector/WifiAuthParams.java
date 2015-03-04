package com.alang.winwificonnector;

import java.util.Collection;
import java.util.HashMap;

import android.util.Log;
import com.alang.winwificonnector.WifiAuthenticator.AuthAction;
import com.alang.winwificonnector.html.HtmlInput;
import com.alang.winwificonnector.util.WifiTools;

public class WifiAuthParams {
	// Some standard input names used almost universally:
	public static final String PASSWORD = "password";
	public static final String USERNAME = "username";
	public static final String EMAIL = "email";
	
	
	public AuthAction authAction = AuthAction.DEFAULT;
	public WifiTools.Action wifiAction = WifiTools.Action.DEFAULT;
	
	boolean savePassword = false;
	
	protected HashMap<String,HtmlInput> fields = new HashMap<String,HtmlInput>();
	
	public Collection<HtmlInput> getFields() {
		return fields.values();
	}

	public void add (HtmlInput i) {
		Log.d(Constants.TAG, "Adding param ["+i.getName() + "], type = [" + i.getType()+"]");
		if (i.isValid() && !i.isHidden() && isSupportedParamType(i)){
			Log.d(Constants.TAG, "Added");
			fields.put(i.getName(), i);
		}
		else
			Log.d(Constants.TAG, "Not Added");
	}
	
	public boolean hasParam (final String name) {
		HtmlInput i = fields.get (name);
		return (i != null && !i.getValue().isEmpty());
	}
	
	public static boolean isSupportedParamType (HtmlInput input) {
		return (input.getAndroidInputType() != 0);
		
	}

	public HtmlInput getField(String tag) {
		return fields.get(tag);
	}

	public HtmlInput getFieldByType(String type) {
  	   	for (HtmlInput i : fields.values()) {
   			if (i.matchType(HtmlInput.TYPE_PASSWORD))
   					return i;
  	   	}
  	   	return null;
	}

	@Override
	public String toString() {
		String s = "<WifiAuthParams authAction=\"" + authAction + 
				"\" wifiAction=\"" + wifiAction + 
				"\" savePassword=\"" + savePassword +
				"\">";
		for (HtmlInput i : fields.values())
			s += i;
		s += "</WifiAuthParams>";
		return s;
	}
	
	public static String toString (WifiAuthParams params) {
		return params == null ? "(null)": params.toString();
	}
}
