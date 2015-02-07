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

import miro.browser.resources.MagicNumbers;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public abstract class InformationField extends Composite {

	Label label;
	int minHeight;
	
	//What type to bind to
	Class type;
	Class containerType;
	
	String propertyName;
	
	IConverter converter;

	
	public InformationField(Composite parent, int style, Class t, Class cont, String labelText, int mH, String name, IConverter conv) {
		super(parent, style);
		minHeight = mH;
		type = t;
		containerType = cont;
		propertyName = name;
		converter = conv;

		
		FormLayout layout = new FormLayout();
		layout.marginHeight = MagicNumbers.INF_FIELD_MARGIN_HEIGHT;
		layout.marginWidth = MagicNumbers.INF_FIELD_MARGIN_WIDTH;
		layout.marginTop = 0;
		setLayout(layout);
		
		
		
		label = new Label(this, style | SWT.WRAP );
		label.setText(labelText);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0,0);
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.left = new FormAttachment(0,0);
		layoutData.width = MagicNumbers.INF_FIELD_LABEL_WIDTH;
		label.setLayoutData(layoutData);

		

	}
	
	public int getMinHeight(){
		return minHeight;
	}

	public abstract void bindField(IObservableValue selection, DataBindingContext dbc);

}
