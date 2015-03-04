package com.alang.winwificonnector.handlers;

import java.net.URL;
import java.util.HashMap;

import com.alang.winwificonnector.Constants;
import com.alang.winwificonnector.ParsedHttpInput;
import com.alang.winwificonnector.WifiAuthParams;

import android.util.Log;

import com.alang.winwificonnector.html.HtmlForm;
import com.alang.winwificonnector.html.HtmlInput;
import com.alang.winwificonnector.html.HtmlPage;
import com.alang.winwificonnector.util.HttpInput;

public abstract class CaptivePageHandler {

	public interface Detection {
		public Boolean detect(HttpInput input); 
	}
	
	// Strictly speaking we don't really need a map here for now
	// May add methods for listing available handlers in future
	private static HashMap <String,Class<? extends Detection>> registeredHandlers = null;
	
	// Using Reflection and package listing is way too much trouble
	private static final String[] standardHandlers = new String[] {
		"CiscoHandler",
		"UniFiHandler",
		"WanderingWifiHandler",
		"SwitchURLHandler",
//		"AttHandler", Can be handled by GenericHandler
		"WiNGHandler",
		"NNUHandler",
		"HiltonHandler"
	};

	@SuppressWarnings("unchecked")
	private static void registerStandardHandlers() {
		if (registeredHandlers != null)
			return;
		
		registeredHandlers = new HashMap<String,Class<? extends Detection>>();
		for (String handlerName : standardHandlers) {
			Log.d(Constants.TAG, "registering standard handler " + handlerName);
			try {
				Log.d(Constants.TAG, "Class " +  (Class<? extends Detection>)Class.forName(CaptivePageHandler.class.getPackage().getName() + '.' + handlerName));
				registerHandler ((Class<? extends Detection>)Class.forName(CaptivePageHandler.class.getPackage().getName() + '.' + handlerName));
			} catch (ClassNotFoundException e) {// don't care
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Register new Captive Portal handler at runtime.
	 * 
	 * @param handler - class of the handler to be registered. 
	 * Must extend CaptivePageHandler and implement CaptivePageHandler.Detection
	 */
	public static void registerHandler (Class<? extends Detection> handler) {
		if (registeredHandlers == null)
			registerStandardHandlers();
			
		registeredHandlers.put (handler.getName(), handler);
	}
	
	// THE FACTORY
	public static CaptivePageHandler autodetect (HttpInput input) {
		
		if (input == null)
			return null;
		
		if (registeredHandlers == null)
			registerStandardHandlers();

		CaptivePageHandler handler = null;
		
		for (Class<? extends Detection> handlerClass : registeredHandlers.values()) {
			try {
				Log.d(Constants.TAG, "detecting " + handlerClass.getName());
				handler = (CaptivePageHandler) handlerClass.newInstance();
				//Method m = handlerClass.getMethod("detect", HtmlPage.class);
				//Boolean result = (Boolean)m.invoke(handler, page);
				Detection d = (Detection)handler;
				Boolean result = d.detect(input);
				Log.d(Constants.TAG, " result = " + result);
				if (result)
					break;
				handler = null;
//			} catch (NoSuchMethodException e) { e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
//			} catch (InvocationTargetException e) {		e.printStackTrace();
			} catch (NullPointerException e) { // ignore
				e.printStackTrace();
			}
		}
		if (handler == null)
			handler = new GenericHandler();

		handler.setPage(input);
		return handler;
	}
	
	
	public enum States {
		Normal,	HandleRedirects, HandleRedirectsAll, Failed, Success;  
	}
	
	protected HttpInput page = null;
	protected States state = States.Normal;
	
	protected void setPage (HttpInput page) {
		this.page = page;
	}
	
	protected void setState (States state) {
		this.state = state;
	}
	
	public States getState () {
		return state;
	}
	
	public boolean checkParamsMissing (WifiAuthParams params){
		WifiAuthParams allParams = addMissingParams(params);
		if (allParams != null) {
			for (HtmlInput i : allParams.getFields()) {
				if (i.getValue().isEmpty())
					return true;
			}
		}
		return false;
	}
	
	
	public void validateLoginForm (WifiAuthParams params, HtmlForm form){
		for (HtmlInput i : form.getInputs()) {
			if (!i.isHidden() && i.matchType(HtmlInput.TYPE_CHECKBOX) && i.getValue().isEmpty())
				i.setValue("yes");
		}
	}
	
	public String getPostData (WifiAuthParams params) {
		HtmlForm form = getLoginForm();
		Log.d (Constants.TAG, "LoginForm = " + form);
		if (form != null) {
			form.fillInputs(params);
			validateLoginForm (params, form);
			return form.formatPostData();
		}
		return null;
	}

	protected HtmlPage getHtmlPage() {
		return (page != null && page instanceof HtmlPage) ? (HtmlPage)page : null;
	}
	
	public HtmlForm getLoginForm () {
		Log.d (Constants.TAG, "Page = " + page);
		return HtmlPage.getForm(page);
	}
	
	/* 
	 * Possibly to be overriden in subclasses
	 */
	public URL getPostURL () 
	{ 
		HtmlForm form = getLoginForm();
		if (form != null)
			return form.formatActionURL (page.getURL());
		return page.getURL(); 
	};
	
	public boolean checkUsernamePasswordMissing (WifiAuthParams params){
		HtmlForm form = getLoginForm();
		Log.d(Constants.TAG, "Checking for missing params. Form = " + form);
		return (form != null && form.isParamMissing(params, WifiAuthParams.USERNAME)||form.isParamMissing(params, WifiAuthParams.PASSWORD)); 
	}
	
	public WifiAuthParams addMissingParams (WifiAuthParams params) {
		HtmlForm form = getLoginForm();
		Log.d(Constants.TAG, "Adding missing params. Form = " + form);
		if (params == null)
			params = new WifiAuthParams();

		if (form != null) 
			params = form.fillParams (params);
		return params;
	}

	public ParsedHttpInput authenticate(ParsedHttpInput parsedPage,	WifiAuthParams authParams) {
		// this works for most captive portals. The weird ones should override this method.
		ParsedHttpInput result = parsedPage.postForm (authParams);
		setState (result!=null ? States.Success : States.Failed);
		return result;
	}
	
}
