package com.alang.winwificonnector.util;

import android.content.Context;
import android.content.Intent;

public class ContextHolder {

	private Context context = null;
	
	public ContextHolder (Context context) {
		this.context = context;
	}

	public ContextHolder (ContextHolder other) {
		this.context = other.context;
	}
	
	public Context getContext() {
		return context;
	}
	
	public String getResourceString (int id) {
		return (context != null) ? context.getString(id) : "";
	}
	
	public Object getSystemService (String name) {
		return (context != null)? context.getSystemService(name) : null;
		
	}
	
	public Intent makeIntent (Class<?> cls) {
		return new Intent(context, cls);
	}
	
	public void startActivity (Intent intent) {
		if (context != null)
			context.startActivity(intent);
	}

	
}
