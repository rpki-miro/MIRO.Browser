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

import main.java.miro.validator.types.CertificateObject;
import main.java.miro.validator.types.ResourceHoldingObject;
import main.java.miro.validator.types.RoaObject;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;

/**
 * Listens for selection changes in a View and rearranges the Tabs of display 
 * in the appropriate manner
 * @author ponken
 *
 */
public class TabListener implements ISelectionChangedListener{

	private DetailViewContainer display;
	
	public TabListener(DetailViewContainer b) {
		display = b;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		ResourceHoldingObject obj = (ResourceHoldingObject) selection.getFirstElement();

		display.clearTabs();

		
		CTabItem tab;
		
		/* If the selection is a Certificate, init the appropriate tabs to display 
		 * CertificateDisplay
		 * */
		if(obj instanceof CertificateObject){
			CertificateView certificateDisplay = display.getCertificateDisplay();
			
			tab = new CTabItem(display, SWT.NONE);
			tab.setText("Certificate");
			tab.setData(obj);
			tab.setControl(certificateDisplay.getCertificateWidget().getParent());
			display.setSelection(tab);
			
			tab = new CTabItem(display, SWT.NONE);
			tab.setText("Manifest");
			tab.setData(((CertificateObject) obj).getManifest());
			tab.setControl(display.getCertificateDisplay().getManifestWidget().getParent());
			
			tab = new CTabItem(display, SWT.NONE);
			tab.setText("CRL");
			tab.setData(((CertificateObject) obj).getCrl());
			tab.setControl(display.getCertificateDisplay().getCrlWidget().getParent());
		}
		
		/* If the selection is a Roa, init the appropriate tabs to display 
		 * RoaDisplay
		 * */
		if(obj instanceof RoaObject){
			tab = new CTabItem(display, SWT.NONE);
			tab.setText("ROA");
			tab.setData(obj);
			tab.setControl(display.getRoaDisplay().getRoaWidget().getParent());
			display.setSelection(tab);
			
			tab = new CTabItem(display, SWT.NONE);
			tab.setText("EE Certificate");
			tab.setData(((RoaObject) obj).getEeCert());
			tab.setControl(display.getRoaDisplay().getCertificateWidget().getParent());
		}
		
		display.layout();
	}
}
