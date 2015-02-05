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
package miro.browser.widgets.browser;

import miro.browser.widgets.browser.displaywidgets.CertificateDisplay;
import miro.validator.types.CertificateObject;
import miro.validator.types.ResourceHoldingObject;
import miro.validator.types.RoaObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class TabHideListener implements SelectionListener {

	RPKIBrowserView browser;
	
	public TabHideListener(RPKIBrowserView b) {
		browser = b;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		

		ResourceHoldingObject obj = (ResourceHoldingObject) e.item.getData();
		
		//Dispose all
		for(TabItem tab : browser.getTabs()){
			tab.dispose();
		}
		
		TabItem tab;
		
		if(obj instanceof CertificateObject){
			CertificateDisplay certificateDisplay = browser.getCertificateDisplay();
			
			
			tab = new TabItem((TabFolder) browser.getDisplayContainer(), SWT.NONE);
			browser.addTab(tab);
			tab.setText("Certificate");
			tab.setControl(certificateDisplay.getCertificateWidget().getParent());
			
			tab = new TabItem((TabFolder) browser.getDisplayContainer(), SWT.NONE);
			browser.addTab(tab);
			tab.setText("Manifest");
			tab.setControl(browser.getCertificateDisplay().getManifestWidget().getParent());
			
			tab = new TabItem((TabFolder) browser.getDisplayContainer(), SWT.NONE);
			browser.addTab(tab);
			tab.setText("CRL");
			tab.setControl(browser.getCertificateDisplay().getCrlWidget().getParent());
			
		}
		
		if(obj instanceof RoaObject){
			tab = new TabItem((TabFolder) browser.getDisplayContainer(), SWT.NONE);
			browser.addTab(tab);
			tab.setText("ROA");
			tab.setControl(browser.getRoaDisplay().getRoaWidget().getParent());
			
			tab = new TabItem((TabFolder) browser.getDisplayContainer(), SWT.NONE);
			browser.addTab(tab);
			tab.setText("EE Certificate");
			tab.setControl(browser.getRoaDisplay().getCertificateWidget().getParent());
		}
		
		browser.layout();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}
}
