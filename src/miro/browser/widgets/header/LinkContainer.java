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
package miro.browser.widgets.header;

import miro.browser.widgets.ContentContainer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

public class LinkContainer extends Composite {
	
	private ContentContainer content;

	public LinkContainer(Composite parent, int style) {
		super(parent, style);
		initLayout();
		initLinks();
	}

	private void initLinks() {
		RowData rowData;
		
		Link browserLink = new Link(this, SWT.NONE);
		rowData = new RowData();
		browserLink.setLayoutData(rowData);
		browserLink.setText("<a>RPKI Browser</a>");
		browserLink.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(content != null){
					content.showBrowser();
				}
			}
		});
		
		Link statsLink = new Link(this,SWT.NONE);
		rowData = new RowData();
		statsLink.setLayoutData(rowData);
		statsLink.setText("<a>Statistics</a>");
		statsLink.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(content != null){
					content.showStats();
				}
			}
		});
	}
	
	public void setContentContainer(ContentContainer contentCont){
		content = contentCont;
	}

	private void initLayout() {
		RowLayout layout = new RowLayout();
		layout.marginBottom = 0;
		layout.marginTop = 3;
		setLayout(layout);
	}

}
