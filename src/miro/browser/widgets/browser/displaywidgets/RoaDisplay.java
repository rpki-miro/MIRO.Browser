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
package miro.browser.widgets.browser.displaywidgets;

import miro.browser.resources.Colors;
import miro.browser.widgets.browser.RPKIBrowserView;
import miro.validator.types.CertificateObject;
import miro.validator.types.RoaObject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

public class RoaDisplay implements ResourceHolderObservableBinder{
	
	private RoaWidget roaWidget;
	
	private CertificateWidget certificateWidget;
	
	public RoaDisplay(Composite parent, RPKIBrowserView b) {
		ScrolledComposite scroller = createScrollingContainer(parent);
		roaWidget = new RoaWidget(scroller, SWT.NONE);
		scroller.setContent(roaWidget);
		
		scroller = createScrollingContainer(parent);
		certificateWidget = new CertificateWidget(scroller, SWT.NONE, b);
		scroller.setContent(certificateWidget);
	}

	@Override
	public void bindToResourceHolder(IObservableValue selection,
			DataBindingContext dbc) {
		
		roaWidget.bindToResourceHolder(selection, dbc);
		
		IObservableValue eeCertObsValue = PojoProperties.value((Class) selection.getValueType(), "eeCert", CertificateObject.class).observeDetail(selection);
		certificateWidget.bindToResourceHolder(eeCertObsValue, dbc);

		
	}
	
	private ScrolledComposite createScrollingContainer(Composite parent){
		ScrolledComposite scrollingContainer = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrollingContainer.setExpandHorizontal(true);
		scrollingContainer.setExpandVertical(true);
		scrollingContainer.setBackground(Colors.BROWSER_DISPLAY_WIDGETS_BACKGROUND);
		return scrollingContainer;
	
	}

	public RoaWidget getRoaWidget() {
		return roaWidget;
	}

	public CertificateWidget getCertificateWidget() {
		return certificateWidget;
	}

	public void showResources(RoaObject obj) {
		roaWidget.getRoaPrefixViewer().setInput(obj);
		certificateWidget.getResourceSetViewer().setInput(obj.getEeCert().getResources());
	}
	
}
