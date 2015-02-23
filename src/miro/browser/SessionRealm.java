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
import miro.browser.widgets.ContentContainer;
import miro.browser.widgets.browser.RPKIBrowser;
import miro.browser.widgets.header.HeaderBar;
import miro.browser.widgets.header.LinkContainer;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
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
		
		FormLayout layout = new FormLayout();
		parent.setLayout(layout);
		
		HeaderBar header = new HeaderBar(parent, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.left = new FormAttachment(0, MagicNumbers.SHELL_OUTER_GAPS);
		layoutData.right = new FormAttachment(100, - MagicNumbers.SHELL_OUTER_GAPS);
		layoutData.top = new FormAttachment(0,  MagicNumbers.SHELL_OUTER_GAPS);
		header.setLayoutData(layoutData);
		
		LinkContainer linkContainer = new LinkContainer(header, SWT.BORDER);
		
		
		ContentContainer content = new ContentContainer(parent, SWT.NONE);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(header,0);
		layoutData.left = new FormAttachment(0, MagicNumbers.SHELL_OUTER_GAPS);
		layoutData.bottom = new FormAttachment(100, -MagicNumbers.SHELL_OUTER_GAPS);
		layoutData.right = new FormAttachment(100,-MagicNumbers.SHELL_OUTER_GAPS);
		content.setLayoutData(layoutData);

		//init browser
		RPKIBrowser browser = new RPKIBrowser(content,SWT.BORDER);
		content.setBrowser(browser);
		
		
		//Show browser default
		content.showBrowser();
		
		linkContainer.setContentContainer(content);
		
		parent.layout();
	}
}
