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

import java.util.ArrayList;

import miro.browser.resources.Colors;
import miro.browser.resources.Fonts;
import miro.browser.resources.MagicNumbers;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import types.ResourceHolder;
import types.ValidationResults;

public abstract class DisplayWidget extends Composite {
	
	Composite titleBar;
	
	Composite content;
	
	TableViewer tableViewer;
	
	Composite informationContainer;
	
	
	ArrayList<InformationField> fields;
	public DisplayWidget(Composite parent, int style) {
		super(parent, style);
		setBackground(Colors.BROWSER_DISPLAY_WIDGETS_BACKGROUND);
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		fields = new ArrayList<InformationField>();
	}
	
	public void bindDisplayWidget(IObservableValue selection, DataBindingContext dbc) {
		IObservableValue validationResultObservable = PojoProperties.value((Class) selection.getValueType(), "validationResults", ValidationResults.class).observeDetail(selection);
		for(InformationField field : fields){

			if(field.containerType.equals(ResourceHolder.class)){
				field.bindField(selection, dbc);
			}
			
			if(field.containerType.equals(ValidationResults.class)){
				field.bindField(validationResultObservable, dbc);
			}
		}
		
	}
	
	public UpdateValueStrategy getUpdateStrategy() {
		//Override this
		return null;
	}
	

	private void setDisplayLayout() {
		RowLayout layout = new RowLayout();
		layout.marginTop = 0;
		layout.marginBottom = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.fill = true;
		layout.type = SWT.VERTICAL;
		layout.wrap = false;
		layout.marginHeight = MagicNumbers.DISPLAYWIDGET_MARGIN_HEIGHT;
		layout.marginWidth = MagicNumbers.DISPLAYWIDGET_MARGIN_WIDTH;
		layout.spacing = 0;
		setLayout(layout);
		
		
	}

	public  void createContent(int style) {
		content = new Composite(this,style);
		
		RowLayout layout  = new RowLayout();
		layout.wrap = true;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.fill = true;
		
		layout.marginHeight = MagicNumbers.DISPLAYWIDGET_MARGIN_HEIGHT;
		layout.marginWidth = MagicNumbers.DISPLAYWIDGET_MARGIN_WIDTH;
		layout.spacing = MagicNumbers.CDW_CONTENT_SPACING;
		
		content.setLayout(layout);
		createInformationContainer(content,style);
	}
	
//	public abstract void createResourceSetViewer(Composite parent,int style);
	
	public abstract void initFields(Composite parent, int style);
	
	public void createTitleBar(String heading,int style) {
		titleBar = new Composite(this,style);
		RowData layoutData = new RowData();
		layoutData.height = MagicNumbers.CDW_TITLE_BAR_HEIGHT;
		titleBar.setLayoutData(layoutData);
		
		
		RowLayout layout = new RowLayout();
		titleBar.setLayout(layout);
		
		
		Label title = new Label(titleBar, SWT.NONE);
		title.setText(heading);
		layoutData = new RowData();
//		layoutData.width = 250;
		title.setLayoutData(layoutData);
		title.setFont(Fonts.DISPLAY_WIDGET_TITLEBAR_FONT);
		
	}
	
	public void createInformationContainer(Composite parent, int style) {
		informationContainer = new Composite(parent,style);
		
		RowData rowData = new RowData();
		rowData.width = MagicNumbers.CDW_INFORMATION_CONTAINER_WIDTH;
		informationContainer.setLayoutData(rowData);
		
		
		RowLayout layout = new RowLayout();
		layout.type = SWT.VERTICAL;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		
		layout.marginHeight = MagicNumbers.CDW_INFORMATION_CONTAINER_MARGIN_HEIGHT;
		layout.marginWidth =  MagicNumbers.CDW_INFORMATION_CONTAINER_MARGIN_WIDTH;
		layout.spacing = MagicNumbers.CDW_INFORMATION_CONTAINER_SPACING;
		informationContainer.setLayout(layout);
		
		initFields(informationContainer,style);
		
		
	}
	public void layoutFields(int width){
		RowData rowData;
		Object buf;
		for(InformationField f : fields){
			buf = f.getLayoutData();
			
			if(buf == null){
				rowData = new RowData();
			} else {
				rowData = (RowData) buf;
			}
			rowData.width = width;
			f.setLayoutData(rowData);
		}
	}
}
