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
import miro.validator.types.CRLObject;
import miro.validator.types.CertificateObject;
import miro.validator.types.ManifestObject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

public class CertificateDisplay implements ResourceHolderObservableBinder{
	
	private CertificateWidget certWidget;
	
	private ManifestWidget manifestWidget;
	
	private CrlWidget crlWidget;
	
	public CertificateDisplay(Composite parent, RPKIBrowserView b) {
		ScrolledComposite scroller = createScrollingContainer(parent);
		certWidget = new CertificateWidget(scroller, SWT.NONE,b);
		scroller.setContent(certWidget);
		
		scroller = createScrollingContainer(parent);
		manifestWidget = new ManifestWidget(scroller, SWT.NONE,b);
		scroller.setContent(manifestWidget);
		
		scroller = createScrollingContainer(parent);
		crlWidget = new CrlWidget(scroller, SWT.NONE,b);
		scroller.setContent(crlWidget);
	}
	
	private ScrolledComposite createScrollingContainer(Composite parent){
		ScrolledComposite scrollingContainer = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrollingContainer.setExpandHorizontal(true);
		scrollingContainer.setExpandVertical(true);
		scrollingContainer.setBackground(Colors.BROWSER_DISPLAY_WIDGETS_BACKGROUND);
		return scrollingContainer;
	}

	@Override
	public void bindToResourceHolder(IObservableValue selection,
			DataBindingContext dbc) {
		certWidget.bindToResourceHolder(selection, dbc);
		
		IObservableValue mftObservable = PojoProperties.value((Class) selection.getValueType(), "manifest", ManifestObject.class).observeDetail(selection);
		manifestWidget.bindToResourceHolder(mftObservable, dbc);
		
		IObservableValue crlObservable = PojoProperties.value((Class) selection.getValueType(), "crl", CRLObject.class).observeDetail(selection);
		crlWidget.bindToResourceHolder(crlObservable, dbc);
		
	}
	
	public void showResources(CertificateObject obj) {
		certWidget.getResourceSetViewer().setInput(obj.getResources());
		manifestWidget.getManifestFilesViewer().setInput(obj.getManifest());
		crlWidget.getRevokedCertificateViewer().setInput(obj.getCrl());
	}

	public CertificateWidget getCertificateWidget() {
		return certWidget;
	}

	public CertificateWidget getCertWidget() {
		return certWidget;
	}

	public ManifestWidget getManifestWidget() {
		return manifestWidget;
	}

	public CrlWidget getCrlWidget() {
		return crlWidget;
	}
}
