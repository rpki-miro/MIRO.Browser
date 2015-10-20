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

package main.java.miro.browser.conf;


import java.util.HashMap;
import java.util.Map;

import main.java.miro.browser.browser.resources.ValidationTranslation;
import main.java.miro.browser.browser.updater.ModelUpdater;
import main.java.miro.browser.conf.entrypoints.MainEntryPoint;
import main.java.miro.browser.conf.entrypoints.downloads.RepositoryDLEntryPoint;
import main.java.miro.browser.conf.entrypoints.downloads.StatsDLEntryPoint;
import main.java.miro.browser.conf.entrypoints.widgets.BrowserEntryPoint;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.Application.OperationMode;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.eclipse.rap.rwt.service.ApplicationContext;

public class BrowserApplicationConfiguration implements
		ApplicationConfiguration {

	public void configure(Application application) {
		addStyleSheets(application);
		addMainEntryPoint(application);
		addDownloadEntryPoints(application);
		addBrowserEntryPoint(application);
		initModelUpdater(application);
		ValidationTranslation.readTranslation();
		application.setOperationMode(OperationMode.JEE_COMPATIBILITY);
	}
	
	public void addStyleSheets(Application application) {
		application.addStyleSheet( "miro", "theme/miro_theme.css" );
	}
	
	public void addMainEntryPoint(Application application) {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(WebClient.THEME_ID, "miro" );
		properties.put(WebClient.PAGE_TITLE, "RPKI MIRO");
		application.addEntryPoint("/", MainEntryPoint.class,properties);
	}
	
	public void initModelUpdater(Application application) {
		@SuppressWarnings("restriction")
		ApplicationContext context = ((ApplicationImpl)application).getApplicationContext();
		new Thread(new ModelUpdater(context)).start();
	}
	
	public void addDownloadEntryPoints(Application application) {
		addRepositoryDLEntryPoint(application);
		addStatsDLEntryPoint(application);
	}
	
	public void addRepositoryDLEntryPoint(Application application) {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(WebClient.PAGE_TITLE, "RPKI MIRO - Repository Download");
		application.addEntryPoint("/repository", RepositoryDLEntryPoint.class,properties);
	}

	public void addStatsDLEntryPoint(Application application) {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(WebClient.PAGE_TITLE, "RPKI MIRO - Stats Download");
		application.addEntryPoint("/stats", StatsDLEntryPoint.class,properties);
	}
	
	public void addBrowserEntryPoint(Application application) {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(WebClient.THEME_ID, "miro" );
		properties.put(WebClient.PAGE_TITLE, "RPKI MIRO - Browser");
		application.addEntryPoint("/browser", BrowserEntryPoint.class,properties);
	}
}
