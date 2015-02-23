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

import miro.browser.widgets.browser.RPKIBrowserView;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

public class LinkField extends InformationField {
	
	private RPKIBrowserView browser;
	
	private Link link;

	public LinkField(Composite parent, int style, Class t, Class cont,
			String labelText, int mH, String name, IConverter conv, RPKIBrowserView b) {
		super(parent, style, t, cont, labelText, mH, name, conv);
		link = new Link(this, style );
		browser = b;
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0,0);
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.left = new FormAttachment(label);
		layoutData.right = new FormAttachment(100,0);
		link.setLayoutData(layoutData);
		link.addListener(SWT.CHANGED, new HeightModifier(this));
		link.setData(RWT.CUSTOM_VARIANT, "browserLink");
	}
	
	public Link getLink(){
		return link;
	}

	@Override
	public void bindField(IObservableValue selection, DataBindingContext dbc) {
		IObservableValue fieldObservable = SWTObservables.observeText(link);

		IObservableValue detailObservable = PojoProperties.value( (Class) selection.getValueType(), propertyName, type).observeDetail(selection);
		UpdateValueStrategy modelToUi = new UpdateValueStrategy();
		
		modelToUi.setConverter(converter);
		dbc.bindValue(fieldObservable, detailObservable, null, modelToUi);
	}
}
