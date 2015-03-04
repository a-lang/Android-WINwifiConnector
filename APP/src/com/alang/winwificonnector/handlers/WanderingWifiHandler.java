package com.alang.winwificonnector.handlers;

import com.alang.winwificonnector.Constants;
import com.alang.winwificonnector.ParsedHttpInput;
import com.alang.winwificonnector.WifiAuthParams;
import com.alang.winwificonnector.html.HtmlForm;
import com.alang.winwificonnector.html.HtmlPage;
import com.alang.winwificonnector.util.HttpInput;

/**
 * @author sasha
 *
 */
public class WanderingWifiHandler extends CaptivePageHandler implements CaptivePageHandler.Detection{

	/* (non-Javadoc)
	 * @see com.wifiafterconnect.handlers.CaptivePageHandler#checkParamsMissing(com.wifiafterconnect.WifiAuthenticator.WifiAuthParams)
	 */
	@Override
	public boolean checkParamsMissing(WifiAuthParams params) {
		return checkUsernamePasswordMissing (params);
	}

	@Override
	public Boolean detect(HttpInput input) {
		return (input.getURL().getHost().contains("wanderingwifi") && HtmlPage.getForm(input) != null);
	}

	@Override
	public void validateLoginForm(WifiAuthParams params, HtmlForm form) {
		// TODO need to check onClick script for the Login Button
		form.setInputValue ("Action", "Login"); 
	}

	@Override
	public ParsedHttpInput authenticate(ParsedHttpInput parsedPage,
			WifiAuthParams authParams) {
		ParsedHttpInput result = super.authenticate(parsedPage, authParams);

		// Wandering WiFi is nuts. It has Several!!! automated pages at the end of authentication, 
		// using a mix of redirecting methods, allow meta http-equiv=refresh here - we can't rely on JS.
		if (result != null)
			result = result.handleAutoRedirects(Constants.MAX_AUTOMATED_REQUESTS, true);

		setState (result!=null ? States.Success : States.Failed);
		return result;
	}

}
