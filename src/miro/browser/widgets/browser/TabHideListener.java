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

import java.util.ArrayList;

import miro.browser.widgets.browser.display.CertificateDisplay;
import miro.browser.widgets.browser.display.DisplayContainer;
import miro.validator.types.CertificateObject;
import miro.validator.types.ResourceHoldingObject;
import miro.validator.types.RoaObject;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class TabHideListener implements ISelectionChangedListener{

	private DisplayContainer display;
	
	public TabHideListener(DisplayContainer b) {
		display = b;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		ResourceHoldingObject obj = (ResourceHoldingObject) selection.getFirstElement();

		display.clearTabs();

		TabItem tab;
		if(obj instanceof CertificateObject){
			CertificateDisplay certificateDisplay = display.getCertificateDisplay();
			
			tab = new TabItem(display, SWT.NONE);
			tab.setText("Certificate");
			tab.setControl(certificateDisplay.getCertificateWidget().getParent());

			tab = new TabItem(display, SWT.NONE);
			tab.setText("Manifest");
			tab.setControl(display.getCertificateDisplay().getManifestWidget().getParent());
			
			tab = new TabItem(display, SWT.NONE);
			tab.setText("CRL");
			tab.setControl(display.getCertificateDisplay().getCrlWidget().getParent());
		}
		
		if(obj instanceof RoaObject){
			tab = new TabItem(display, SWT.NONE);
			tab.setText("ROA");
			tab.setControl(display.getRoaDisplay().getRoaWidget().getParent());
			
			tab = new TabItem(display, SWT.NONE);
			tab.setText("EE Certificate");
			tab.setControl(display.getRoaDisplay().getCertificateWidget().getParent());
		}
		
		display.layout();
	}
}
