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

import java.net.URI;
import java.util.ArrayList;

import json.deserializers.ValidationStatus;
import miro.browser.converters.URIConverter;
import miro.browser.converters.ValidationCheckConverter;
import miro.browser.resources.MagicNumbers;
import miro.browser.widgets.browser.RPKIBrowserView;
import miro.validator.types.RepositoryObject;
import miro.validator.types.ValidationResults;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;


public class ManifestWidget extends DisplayWidget implements ResourceHolderObservableBinder {

	private ManifestFilesViewer filesViewer;
	
	public ManifestWidget(Composite parent, int style, RPKIBrowserView b) {
		super(parent, style,b);
		style = SWT.NONE;
		setDisplayLayout();
		initTitleBar("Manifest");
		createInformationContainer(this,style);
		createFilesViewer(this,style,b);
		this.layout();
	}
	
	public void setDisplayLayout(){
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
	
	public void initTitleBar(String heading) {
		super.initTitleBar(heading);
		RowData layoutData = new RowData();
		layoutData.height = MagicNumbers.CDW_TITLE_BAR_HEIGHT;
		titleBar.setLayoutData(layoutData);
	}
	public void initFields(Composite parent,int style){
		ValidationCheckConverter checkToStringconv = new ValidationCheckConverter();
		
		InformationField filenameField = new TextField(parent, style, String.class,RepositoryObject.class,"Filename: ", MagicNumbers.LINE_HEIGHT, "filename",null);
		fields.add(filenameField);
		
		InformationField locationField = new TextField(parent, style, URI.class, RepositoryObject.class,"Location: ", MagicNumbers.LINE_HEIGHT, "remoteLocation",new URIConverter());
		fields.add(locationField);
		
		InformationField validationStatusField = new TextField(parent, style, ValidationStatus.class,ValidationResults.class,"Validation Status: ", MagicNumbers.LINE_HEIGHT*2, "validationStatus",null);
		fields.add(validationStatusField);

		InformationField invalidReasonsField = new TextField(parent, style, ArrayList.class,ValidationResults.class,"Errors: ", MagicNumbers.LINE_HEIGHT*2, "errors", checkToStringconv);
		fields.add(invalidReasonsField);
		
		InformationField warningsField = new TextField(parent,style,ArrayList.class, ValidationResults.class, "Warnings: ", MagicNumbers.LINE_HEIGHT*2,"warnings",checkToStringconv);
		fields.add(warningsField);
		
		layoutFields(MagicNumbers.CDW_INFORMATION_CONTAINER_WIDTH);
		parent.layout();
	}
	
	public void createInformationContainer(Composite parent, int style) {
		super.createInformationContainer(this, style);
		RowData rowData = new RowData();
		rowData.width = MagicNumbers.CDW_INFORMATION_CONTAINER_WIDTH;
		informationContainer.setLayoutData(rowData);
	}
	public void createFilesViewer(Composite parent, int style, RPKIBrowserView b) {
		filesViewer = new ManifestFilesViewer(this, style,b);
		RowData rowData = new RowData();
		rowData.height =  MagicNumbers.MFT_HASH_LIST_HEIGHT;
		filesViewer.setLayoutData(rowData);	
	}


	@Override
	public void bindToResourceHolder(IObservableValue manifestObservable,
			DataBindingContext dbc) {
		
		IObservableValue validationResultObservable = PojoProperties.value(
				(Class) manifestObservable.getValueType(), "validationResults",
				ValidationResults.class).observeDetail(manifestObservable);

		for (InformationField field : fields) {
			if (field.containerType.equals(ValidationResults.class)) {
				field.bindField(validationResultObservable, dbc);
			}
			
			if(field.containerType.equals(RepositoryObject.class)){
				field.bindField(manifestObservable, dbc);
			}
		}
	}


	public ManifestFilesViewer getManifestFilesViewer() {
		return filesViewer;
	}

}
