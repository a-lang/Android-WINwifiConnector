package com.alang.winwificonnector.handlers;

import com.alang.winwificonnector.ParsedHttpInput;
import com.alang.winwificonnector.WifiAuthParams;
import com.alang.winwificonnector.html.HtmlPage;

/**
 * @author sasha
 *
 */
public class GenericHandler extends CaptivePageHandler {

	@Override
	public ParsedHttpInput authenticate(ParsedHttpInput parsedPage,
			WifiAuthParams authParams) {
		ParsedHttpInput result = super.authenticate(parsedPage, authParams);

		// some portals throw up a page while authenticating user - 
		// must wait for the final page with the 0 timeout:
		
		HtmlPage.MetaRefresh metaRefresh;
		int count = 0 ;
		while (result != null 
				&& (metaRefresh = result.getMetaRefresh()) != null 
				&& metaRefresh.getTimeout() > 0) {
			result = result.getRefresh(null);
			count++;
		}
		// One last refresh to get to the actual location's landing place 
		if (count > 0 && result != null && (metaRefresh = result.getMetaRefresh()) != null)
			result = result.getRefresh(null);

		setState (result!=null ? States.Success : States.Failed);
		return result;
	}
	
	
}
