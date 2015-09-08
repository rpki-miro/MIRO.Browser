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
package main.java.miro.browser.browser.widgets.browser.display;

import java.util.ArrayList;

import main.java.miro.browser.browser.download.DownloadHandler;
import main.java.miro.browser.browser.resources.MagicNumbers;
import main.java.miro.browser.browser.widgets.browser.RPKIBrowser;
import main.java.miro.validator.types.RepositoryObject;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

public abstract class DisplayWidget extends Composite {
	
	protected RPKIBrowser browser; 
	
	protected Composite titleBar;
	
	protected Composite informationContainer;
	
	protected ArrayList<InformationField> fields;

	public abstract void initFields(Composite parent, int style);
	
	public DisplayWidget(Composite parent, int style, RPKIBrowser b ) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "displayWidget");
		browser = b;
		fields = new ArrayList<InformationField>();
		createInformationContainer(this, style);
	}
	
	public void createInformationContainer(Composite parent, int style){
		informationContainer = new Composite(parent,style);
		informationContainer.setData(RWT.CUSTOM_VARIANT, "informationContainer");
		informationContainer.setData(RWT.CUSTOM_VARIANT, "displayContent");

		
		RowLayout layout = new RowLayout();
		layout.type = SWT.VERTICAL;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		
		layout.marginHeight = MagicNumbers.CDW_INFORMATION_CONTAINER_MARGIN_HEIGHT;
		layout.marginWidth =  MagicNumbers.CDW_INFORMATION_CONTAINER_MARGIN_WIDTH;
		layout.spacing = MagicNumbers.CDW_INFORMATION_CONTAINER_SPACING;
		informationContainer.setLayout(layout);
		
		initFields(informationContainer,style);
		Link download = new Link(informationContainer, SWT.NONE);
		download.setData(RWT.CUSTOM_VARIANT,"downloadLink");
		download.setText("<a>ASCII download</a>");
		download.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				DownloadHandler dlhand  = new DownloadHandler();
				CTabItem selectedTab = browser.getDisplayContainer().getSelection();
				RepositoryObject r = (RepositoryObject) selectedTab.getData();
				dlhand.sendDownload(r);
			}
		});
	}
	
	public void layoutFields(int width){
		RowData rowData;
		for(InformationField f : fields){
			rowData = new RowData();
			rowData.width = width;
			f.setLayoutData(rowData);
		}
	}
}
