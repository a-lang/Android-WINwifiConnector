package com.alang.winwificonnector.http;

public enum HttpConnectionFactory {
	INSTANCE;
	
	private String connectionClass;
	private boolean singleton = false;
	private HttpConnectionWrapper singletonInstance;
	
	public HttpConnectionWrapper getConnection() {
		if (connectionClass == null)
			return new HttpURLConnectionWrapper();
		if (singletonInstance != null) {
			singletonInstance.reset();
			return singletonInstance;
		}
		HttpConnectionWrapper conn = null;
		try {
			conn = (HttpConnectionWrapper) Class.forName(connectionClass).newInstance(); 
		}catch (InstantiationException e) {
			
		}catch (IllegalAccessException e) {
			
		}catch (ClassNotFoundException e) {
			
		}
		if (singleton)
			singletonInstance = conn;
		
		return conn;
	}
	
	public void setConnectionClass (String name, boolean singleton) {
		if (singletonInstance != null) {
			if (!singleton || name == null || !name.equals(connectionClass))
				singletonInstance = null;
		}
		connectionClass = name;
		this.singleton = singleton;
	}

	public void setConnectionInstance (HttpConnectionWrapper instance) {
		singletonInstance = instance;
		connectionClass = instance.getClass().getName();
		this.singleton = true;
	}
}
