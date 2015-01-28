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



import miro.browser.resources.Colors;
import miro.browser.resources.Fonts;
import miro.browser.resources.Images;
import miro.browser.resources.MagicNumbers;
import miro.browser.widgets.MainWidgetContainer;
import miro.browser.widgets.browser.RPKIBrowserView;
import miro.browser.widgets.navigation.HeaderBar;
import miro.browser.widgets.navigation.LinkContainer;
import miro.browser.widgets.query.QueryWidget;
import miro.browser.widgets.stats.StatsView;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;

public class SessionRealm implements Runnable {
	private Composite parent;
	
	public SessionRealm(Composite p){
		parent = p;
	}

	public void run() {
		//Enable push, for async. events
		final ServerPushSession pushSession = new ServerPushSession();
		pushSession.start();
		
		//Init resources
		Colors.init(parent.getDisplay());
		Fonts.init(parent.getDisplay());
		Images.init(parent.getDisplay());
		
		initParent(parent);
		
		//header init
		Composite header = new HeaderBar(parent, SWT.NONE);
		initHeader(header);
		
		//main init
		MainWidgetContainer mainWidget = new MainWidgetContainer(parent, SWT.NONE);
		initMain(mainWidget,header);

		//init browser
		RPKIBrowserView browser = new RPKIBrowserView(mainWidget,SWT.BORDER);
		mainWidget.setBrowser(browser);
		

		
		ScrolledComposite scroller = new ScrolledComposite(mainWidget, SWT.V_SCROLL | SWT.H_SCROLL);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);
		StatsView statsContainer = new StatsView(scroller, SWT.BORDER);
		mainWidget.setStatsContainer(scroller);
		scroller.setContent(statsContainer);
		Point size = statsContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scroller.setMinSize(statsContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		
		//Query
		QueryWidget queryPage = new QueryWidget(mainWidget, SWT.NONE);
		mainWidget.setQuery(queryPage);

		//Show browser default
		mainWidget.showBrowser();
		
		LinkContainer linkContainer = new LinkContainer(header, SWT.BORDER, mainWidget);
		initLinkContainer(linkContainer);
		
		parent.layout();
	}
	
	
	
	private void initLinkContainer(Composite linkContainer) {
		RowData rowData = new RowData();
		linkContainer.setLayoutData(rowData);
		
	}

	private void initParent(Composite parent){
		FormLayout layout = new FormLayout();
		parent.setLayout(layout);
		parent.setBackground(Colors.SHELL_BACKGROUND);
	}
	
	private void initHeader(Composite header){
		FormData layoutData = new FormData();
		layoutData.left = new FormAttachment(0, MagicNumbers.SHELL_OUTER_GAPS);
		layoutData.right = new FormAttachment(100, - MagicNumbers.SHELL_OUTER_GAPS);
		layoutData.top = new FormAttachment(0,  MagicNumbers.SHELL_OUTER_GAPS);
//		layoutData.height = MagicNumbers.HEADER_HEIGHT;
		header.setLayoutData(layoutData);
	}
	
	private void initMain(Composite mainWidget, Composite header){
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(header,0);
		layoutData.left = new FormAttachment(0, MagicNumbers.SHELL_OUTER_GAPS);
		layoutData.bottom = new FormAttachment(100, -MagicNumbers.SHELL_OUTER_GAPS);
		layoutData.right = new FormAttachment(100,-MagicNumbers.SHELL_OUTER_GAPS);
		mainWidget.setLayoutData(layoutData);
	}


}
