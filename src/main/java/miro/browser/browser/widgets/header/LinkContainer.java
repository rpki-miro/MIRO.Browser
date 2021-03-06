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
package main.java.miro.browser.browser.widgets.header;


import main.java.miro.browser.browser.widgets.ContentContainer;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class LinkContainer extends Composite {
	
	private ContentContainer content;

	public LinkContainer(Composite parent, int style) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "linkContainer");
		initLayout();
		initLinks();
	}

	private void initLinks() {
		Button browserLink = new Button(this,SWT.PUSH);
		browserLink.setText("RPKI Browser");
		browserLink.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				content.showBrowser();
			}
		});
		browserLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		browserLink.setData(RWT.CUSTOM_VARIANT, "navButton");
		
		Button statsLink = new Button(this,SWT.NONE);
		statsLink.setText("Statistics");
		statsLink.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(content != null){
					content.showStats();
				}
			}
		});
		statsLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		statsLink.setData(RWT.CUSTOM_VARIANT, "navButton");
		
		Button aboutLink = new Button(this,SWT.NONE);
		aboutLink.setText("About");
		aboutLink.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				RWT.getClient().getService(JavaScriptExecutor.class).execute("window.location=\"" +"http://rpki-miro.realmv6.org/" +  "\";" );
				
			}
		});
		aboutLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		aboutLink.setData(RWT.CUSTOM_VARIANT, "navButton");
	}
	
	public void setContentContainer(ContentContainer contentCont){
		content = contentCont;
	}

	private void initLayout() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
	}

}
