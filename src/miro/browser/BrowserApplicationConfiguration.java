/*
 * Copyright (c) 2015, Andreas Reuter, Freie Universität Berlin 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 * 
 * */

package miro.browser;


import java.util.HashMap;
import java.util.Map;

import miro.browser.resources.ValidationTranslation;
import miro.browser.updater.ModelUpdater;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.Application.OperationMode;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.eclipse.rap.rwt.service.ApplicationContext;

public class BrowserApplicationConfiguration implements
		ApplicationConfiguration {

	public void configure(Application application) {
		/*
		 * TODO:
		 * statische Resourcen (immoment nur images) im ResourceManager speichern	
		 */

		@SuppressWarnings("restriction")
		ApplicationContext context = ((ApplicationImpl)application).getApplicationContext();
		
		application.addStyleSheet( "example", "theme/example.css" );
	
		ValidationTranslation.readTranslation();
		
		
		
		
		
		new Thread(new ModelUpdater(context)).start();
		Map<String, String> properties = new HashMap<String, String>();
		
		properties.put( WebClient.THEME_ID, "example" );
		properties.put(WebClient.PAGE_TITLE, "Resource Certificate Monitor");
		application.addEntryPoint("/", ClientEntryPoint.class,
				properties);
		application.setOperationMode(OperationMode.JEE_COMPATIBILITY);
	}

}
