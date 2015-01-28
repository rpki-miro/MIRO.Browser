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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class TextField extends InformationField {

	Text text;
	RowData textRowData;
	
	//What to bind to 
	String propertyName;
	
	IConverter converter;
	UpdateValueStrategy modelToUi;
	UpdateValueStrategy UItoModel;
	
	Rectangle savedSize;
	
	
	public TextField(Composite parent, int style, Class type, Class cont, String labelText, int mH, String name, IConverter conv) {
		super(parent, style, type,cont, labelText, mH);
		propertyName = name;
		converter = conv;
		text = new Text(this, style | SWT.WRAP | SWT.READ_ONLY);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0,0);
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.left = new FormAttachment(label);
		layoutData.right = new FormAttachment(100,0);
		text.setLayoutData(layoutData);
	
		
		//This listener gets called if the input for the textField changes.
		//It calculates the new height this field should have and sets it
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				
				
				Text s = (Text) event.getSource();
				String textString = s.getText();
				
				if(textString == null | textString.length() == 0){
					textString = "None";
					s.setText(textString);
				} 
				
				int newLineCount = StringUtils.countMatches(textString, "\n");
				
				RowData rowData = (RowData) TextField.this.getLayoutData();
				GC gc = new GC(s);
				//Calculate new height, based on strinWidth
			    int stringWidth = gc.stringExtent(textString).x;
			    
			    if(rowData.width > 1000){
			    	
			    	System.out.println("Textfield width: "+rowData.width);
			    }
			    
			    
			    
			    //Divide by our width, factor is how many lines we expect from this input
				double factor = stringWidth/(double)(rowData.width - MagicNumbers.INF_FIELD_LABEL_WIDTH);
				
				//Calculate our new preferred height
				int height = (int) (Math.ceil(factor)*MagicNumbers.LINE_HEIGHT);
				for(int i = 0;i<newLineCount;i++){
					height += 20;
				}
				
				if(height == 0 | height >= minHeight){
					rowData.height = height;
				} else if(height < minHeight){
					rowData.height = minHeight;
				}
				TextField.this.setLayoutData(rowData);
				
				
				//Find browser
				Composite c = getParent();
				while(!(c instanceof DisplayWidget)){
					c = c.getParent();
				}
				
				DisplayWidget dw = (DisplayWidget) c;
				
				Point size = dw.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				
				
				
				
				
				ScrolledComposite scroller = (ScrolledComposite) dw.getParent();
				scroller.setMinHeight(size.y);
				dw.layout(true);
				
				
			}
		});
	}
	
	
	public void bindField(IObservableValue selection, DataBindingContext dbc){
		IObservableValue fieldObservable = SWTObservables.observeText(text);

		IObservableValue detailObservable = PojoProperties.value( (Class) selection.getValueType(), propertyName, type).observeDetail(selection);
		modelToUi = new UpdateValueStrategy();
		
		modelToUi.setConverter(converter);
		dbc.bindValue(fieldObservable, detailObservable, UItoModel, modelToUi);
	}
	
	
	

}
