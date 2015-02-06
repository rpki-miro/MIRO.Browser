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

import java.net.URI;
import java.util.ArrayList;

import json.deserializers.ValidationStatus;
import miro.browser.converters.URIConverter;
import miro.browser.converters.ValidationCheckConverter;
import miro.browser.resources.Colors;
import miro.browser.resources.Fonts;
import miro.browser.resources.MagicNumbers;
import miro.validator.types.RepositoryObject;
import miro.validator.types.ValidationResults;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;



public class CrlWidget extends DisplayWidget implements ResourceHolderObservableBinder{
	
	private RevokedCertificateViewer revokedCertViewer;
	
	public CrlWidget(Composite parent, int style) {
		super(parent, style);
		style = SWT.NONE;
		setDisplayLayout();
		initTitleBar("Certificate Revokation List", style);
		createInformationContainer(this, style);
		createRevokedCertificateViewer(this, style);
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
	
	public void initTitleBar(String heading,int style) {
		titleBar = new Composite(this,style);
		RowData layoutData = new RowData();
		layoutData.height = MagicNumbers.CDW_TITLE_BAR_HEIGHT;
		titleBar.setLayoutData(layoutData);
		
		
		RowLayout layout = new RowLayout();
		titleBar.setLayout(layout);
		
		
		Label title = new Label(titleBar, SWT.NONE);
		title.setText(heading);
		layoutData = new RowData();
		title.setLayoutData(layoutData);
		title.setFont(Fonts.DISPLAY_WIDGET_TITLEBAR_FONT);
		
	}

	public void createInformationContainer(Composite parent, int style) {
		super.createInformationContainer(this, style);
		RowData rowData = new RowData();
		rowData.width = MagicNumbers.CDW_INFORMATION_CONTAINER_WIDTH;
		informationContainer.setLayoutData(rowData);
	}
	
	public void createRevokedCertificateViewer(Composite parent, int style) {
		revokedCertViewer = new RevokedCertificateViewer(parent, style);
		
		RowData rowData = new RowData();
		rowData.height =  MagicNumbers.CRL_REVOKED_LIST_HEIGHT;
		rowData.width = MagicNumbers.CRL_REVOKED_LIST_WIDTH;
		revokedCertViewer.setLayoutData(rowData);	
	}
	
	
	public RevokedCertificateViewer getRevokedCertificateViewer(){
		return revokedCertViewer;
	}


	@Override
	public void initFields(Composite parent, int style) {
		ValidationCheckConverter checkToStringconv = new ValidationCheckConverter();
		
		InformationField filenameField = new TextField(parent, style, String.class,RepositoryObject.class,"Filename: ", MagicNumbers.LINE_HEIGHT, "filename",null);
		fields.add(filenameField);
		
		InformationField locationField = new TextField(parent, style, URI.class, RepositoryObject.class,"Location: ", MagicNumbers.LINE_HEIGHT, "remoteLocation",new URIConverter());
		fields.add(locationField);
		
		InformationField validationStatusField = new TextField(parent, style, ValidationStatus.class,ValidationResults.class,"Validation Status: ", MagicNumbers.LINE_HEIGHT*2, "validationStatus",null);
		fields.add(validationStatusField);
		
		InformationField invalidReasonsField = new TextField(parent, style, ArrayList.class,ValidationResults.class,"Errors: ", MagicNumbers.LINE_HEIGHT*2, "errors",checkToStringconv);
		fields.add(invalidReasonsField);
		
		InformationField warningsField = new TextField(parent,style,ArrayList.class, ValidationResults.class, "Warnings: ", MagicNumbers.LINE_HEIGHT*2,"warnings",checkToStringconv);
		fields.add(warningsField);
		
		layoutFields(MagicNumbers.CDW_INFORMATION_CONTAINER_WIDTH);
		parent.layout();
	}
	
	@Override
	public void bindToResourceHolder(IObservableValue crlObservable,
			DataBindingContext dbc) {
		
		//Bind viewer
		IObservableValue validationResultObservable = PojoProperties.value((Class) crlObservable.getValueType(), "validationResults", ValidationResults.class).observeDetail(crlObservable);
		for(InformationField field : fields){
			if(field.containerType.equals(ValidationResults.class)){
				field.bindField(validationResultObservable, dbc);
			}
			
			if(field.containerType.equals(RepositoryObject.class)){
				field.bindField(crlObservable, dbc);
			}

		}
		
		
	}


}
