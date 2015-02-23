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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * Widget container for the different Object Displays, in this case RoaDisplay and CertificateDisplay.
 * Shows Displays in Tabs, see Tab
 * 
 * @author ponken
 *
 */
public class DisplayContainer extends TabFolder{

	private CertificateDisplay certificateDisplay;
	
	private RoaDisplay roaDisplay;

	public DisplayContainer(Composite parent, int style) {
		super(parent, style);
	}
	
	
	public void initDisplays(RPKIBrowser b){
		certificateDisplay = new CertificateDisplay(this,b);
		roaDisplay = new RoaDisplay(this, b);
	}
	
	public void bindDisplays(IObservableValue selection,
			DataBindingContext dbc){
		certificateDisplay.bindToResourceHolder(selection, dbc);
		roaDisplay.bindToResourceHolder(selection, dbc);
	}

	public CertificateDisplay getCertificateDisplay() {
		return certificateDisplay;
	}
	
	public RoaDisplay getRoaDisplay() {
		return roaDisplay;
	}

	public void clearTabs() {
		for(TabItem tab : getItems()){
			tab.dispose();
		}
	}
}
