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

import miro.browser.widgets.browser.RPKIBrowser;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

/**
 * Widget container for the different Object Displays, in this case RoaDisplay and CertificateDisplay.
 * Shows Displays in Tabs, see Tab
 * 
 * @author ponken
 *
 */
public class DetailViewContainer extends CTabFolder{

	private CertificateView certificateDisplay;
	
	private RoaView roaDisplay;

	public DetailViewContainer(Composite parent, int style) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "displayContainer");
	}
	
	
	public void initDisplays(RPKIBrowser b){
		certificateDisplay = new CertificateView(this,b);
		roaDisplay = new RoaView(this, b);
	}
	
	public void bindViews(IObservableValue selection,
			DataBindingContext dbc){
		certificateDisplay.bindToResourceHolder(selection, dbc);
		roaDisplay.bindToResourceHolder(selection, dbc);
	}

	public CertificateView getCertificateDisplay() {
		return certificateDisplay;
	}
	
	public RoaView getRoaDisplay() {
		return roaDisplay;
	}

	public void clearTabs() {
		for(CTabItem tab : getItems()){
			tab.dispose();
		}
	}
}
