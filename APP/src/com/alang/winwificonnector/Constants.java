package com.alang.winwificonnector;

public interface Constants {

	public static final String TAG = "WINwifiConnector";
	
	public static final String URL_KERNEL_ORG_HTTPS = "https://mirrors.kernel.org/debian/pool/";
	public static final String URL_KERNEL_ORG_HTTP = "http://mirrors.kernel.org/debian/pool/";
	public static final String URL_GOOGLE_HTTP = "http://www.google.com";
	public static final String URL_GOOGLE_HTTPS = "https://www.google.com";
	
	// Google page is rather large, so let's try and pull a smaller page
	// kernel.org should be sufficiently fast and stable for our purposes
	public static final String URL_TO_CHECK_HTTP = URL_KERNEL_ORG_HTTP;
	public static final String URL_TO_CHECK_HTTPS = URL_KERNEL_ORG_HTTPS;
	
	public static final int SOCKET_CONNECT_TIMEOUT_MS = 0; //Use TCP timeout
	public static final int SOCKET_READ_TIMEOUT_MS = 220000; // this could be really slow in some cases
	
	
	public static final String [] PROTOCOLS = {"http","https"};

	public static final int MAX_AUTOMATED_REQUESTS = 10;

	// MAX timeout while handling <meta http-equiv="refresh" content="timeout; url=url">
	public static final int MAX_REFRESH_TIMEOUT = 15;


}
