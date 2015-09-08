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

import main.java.miro.browser.browser.resources.MagicNumbers;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public abstract class InformationField extends Composite {

	protected Label label;

	protected int minLines;
	
	protected Class<?> type;
	
	protected Class<?> containerType;
	
	protected String propertyName;
	
	IConverter converter;
	public InformationField(Composite parent, int style, Class<?> t, Class<?> cont, String labelText, int mH, String name, IConverter conv) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "informationField");
		
		label = new Label(this, style | SWT.WRAP );
		label.setData(RWT.CUSTOM_VARIANT, "informationLabel");

		minLines = mH ;
		type = t;
		containerType = cont;
		propertyName = name;
		converter = conv;
		label.setText(labelText);

		FormLayout layout = new FormLayout();
		layout.marginHeight = MagicNumbers.INF_FIELD_MARGIN_HEIGHT;
		layout.marginWidth = MagicNumbers.INF_FIELD_MARGIN_WIDTH;
		layout.marginTop = 0;
		layout.spacing = 0;
		setLayout(layout);

		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0,0);
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.left = new FormAttachment(0,0);
		layoutData.width = MagicNumbers.INF_FIELD_LABEL_WIDTH;
		label.setLayoutData(layoutData);
	}
	
	public int getMinLines(){
		return minLines;
	}

	public abstract void bindField(IObservableValue selection, DataBindingContext dbc);

}
