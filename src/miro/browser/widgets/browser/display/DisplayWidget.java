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
package miro.browser.widgets.browser.display;

import java.util.ArrayList;

import miro.browser.download.DownloadHandler;
import miro.browser.resources.Fonts;
import miro.browser.resources.MagicNumbers;
import miro.browser.widgets.browser.RPKIBrowser;
import miro.validator.types.CertificateObject;
import miro.validator.types.RepositoryObject;
import miro.validator.types.ResourceHoldingObject;
import miro.validator.types.RoaObject;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabItem;

public abstract class DisplayWidget extends Composite {
	
	protected RPKIBrowser browser; 
	
	protected Composite titleBar;
	
	protected Composite informationContainer;
	
	protected ArrayList<InformationField> fields;

	public abstract void initFields(Composite parent, int style);
	
	public DisplayWidget(Composite parent, int style, RPKIBrowser b ) {
		super(parent, style);
		browser = b;
		fields = new ArrayList<InformationField>();
		createInformationContainer(this, style);
//		setBackground(Colors.BROWSER_DISPLAY_WIDGETS_BACKGROUND);
//		setBackgroundMode(SWT.INHERIT_DEFAULT);
	}
	
	public void initTitleBar(String heading) {
		titleBar = new Composite(this,SWT.NONE);
		
		RowLayout layout = new RowLayout();
		titleBar.setLayout(layout);
		
		Label title = new Label(titleBar, SWT.NONE);
		title.setText(heading);
		title.setFont(Fonts.DISPLAY_WIDGET_TITLEBAR_FONT);
		
		Link download = new Link(titleBar, SWT.NONE);
		download.setData(RWT.CUSTOM_VARIANT,"browserLink");
		download.setText("<a>ASCII download</a>");
		download.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				DownloadHandler dlhand  = new DownloadHandler();
				TabItem selectedTab = browser.getDisplayContainer().getSelection()[0];
				RepositoryObject r = (RepositoryObject) selectedTab.getData();
				dlhand.sendDownload(r);
			}
		});
	}
	
	public void createInformationContainer(Composite parent, int style){
		informationContainer = new Composite(parent,style);
		
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
	}
	
	public void layoutFields(int width){
		RowData rowData;
		Object buf;
		for(InformationField f : fields){
			buf = f.getLayoutData();
			if(buf == null){
				rowData = new RowData();
			} else {
				rowData = (RowData) buf;
			}
			rowData.width = width;
			f.setLayoutData(rowData);
		}
	}
}
