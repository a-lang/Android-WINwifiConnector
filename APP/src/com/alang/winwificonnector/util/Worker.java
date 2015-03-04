package com.alang.winwificonnector.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.alang.winwificonnector.Constants;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Worker extends ContextHolder {
	private Logger logger;
	protected Preferences prefs;
	
	public Worker (Logger logger, Context context) {
		super (context);
		this.logger = logger;
		this.prefs = new Preferences (context);
	}
	
	public Worker (Worker other) {
		super (other);
		this.logger = other.logger;
		this.prefs = new Preferences (other.getContext());
	}
	
	public void exception (Throwable e) {
		if (logger!= null)
			logger.exception(e);
		else
			e.printStackTrace();
	}
	
	public void debug (String msg) {
		if (logger != null)
			logger.debug(msg);
	}
	
	public void error (String msg) {
		if (logger != null)
			logger.error(msg);
	}
	
	public boolean isSaveLogToFile() {
		return prefs.getSaveLogToFile();
	}
	
	public void setLogFileName (String filename) {
		if (prefs.getSaveLogToFile()) {
			File saveDir = prefs.getSaveLogLocation();
			Log.d(Constants.TAG, "Save location = " + saveDir);
			try {
				logger.setLogFile(new File (saveDir, filename));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void toIntent (Intent intent) {
		if (logger != null)
			logger.toIntent(intent);
	}

}
