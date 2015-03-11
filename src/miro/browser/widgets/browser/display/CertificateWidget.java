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

import java.math.BigInteger;
import java.net.URI;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.security.auth.x500.X500Principal;

import json.deserializers.ValidationStatus;
import miro.browser.converters.ByteArrayConverter;
import miro.browser.converters.PublicKeyConverter;
import miro.browser.converters.URIConverter;
import miro.browser.converters.ValidationCheckConverter;
import miro.browser.converters.ValidityPeriodConverter;
import miro.browser.converters.X500PrincipalConverter;
import miro.browser.converters.X500PrincipalLinkConverter;
import miro.browser.resources.MagicNumbers;
import miro.browser.widgets.browser.RPKIBrowser;
import miro.validator.types.CertificateObject;
import miro.validator.types.ResourceHoldingObject;
import miro.validator.types.ValidationResults;
import net.ripe.rpki.commons.crypto.ValidityPeriod;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Widget that shows information about a CertificateObject
 * @author ponken
 *
 */
@SuppressWarnings("serial")
public class CertificateWidget extends DisplayWidget implements ResourceHolderObservableBinder {
	
	private ResourceSetTable resourceSetTable;
	
	public CertificateWidget(Composite parent, int style, RPKIBrowser b) {
		super(parent, style, b);
		resourceSetTable = new ResourceSetTable(this, style);
		setDisplayLayout();
		layout();
	}
	
	public void setDisplayLayout(){
//		GridLayout layout = new GridLayout();
//		layout.numColumns = 2;
//		layout.horizontalSpacing = 20;
//		layout.verticalSpacing = 0;
//		layout.marginHeight = MagicNumbers.DISPLAYWIDGET_MARGIN_HEIGHT;
//		layout.marginWidth = MagicNumbers.DISPLAYWIDGET_MARGIN_WIDTH;
//		setLayout(layout);
//
//		GridData gridData = new GridData();
//		gridData.widthHint = MagicNumbers.CDW_INFORMATION_CONTAINER_WIDTH;
//		informationContainer.setLayoutData(gridData);
//
//		gridData = new GridData();
//		gridData.heightHint = MagicNumbers.CDW_RESOURCE_LIST_HEIGHT;
//		gridData.widthHint = MagicNumbers.CDW_RESOURCE_LIST_WIDTH;
//		gridData.verticalAlignment = SWT.FILL;
//		resourceSetTable.setLayoutData(gridData);
//		
//		informationContainer.moveAbove(resourceSetTable);
		
		RowLayout layout = new RowLayout();
		layout.type = SWT.HORIZONTAL;
		layout.fill = true;
		layout.spacing = 20;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		layout.marginHeight = MagicNumbers.DISPLAYWIDGET_MARGIN_HEIGHT;
		layout.marginWidth = MagicNumbers.DISPLAYWIDGET_MARGIN_WIDTH;
		setLayout(layout);
		
		RowData rowData = new RowData();
		rowData.width = MagicNumbers.CDW_INFORMATION_CONTAINER_WIDTH;
		informationContainer.setLayoutData(rowData);
		
		rowData = new RowData();
		rowData.width = MagicNumbers.CDW_RESOURCE_LIST_WIDTH;
		rowData.height = MagicNumbers.CDW_RESOURCE_LIST_HEIGHT;
		resourceSetTable.setLayoutData(rowData);
		
		
		
	}
	
	public void initFields(Composite parent, int style) {
		
		ValidationCheckConverter checkToStringconv = new ValidationCheckConverter();
		ValidityPeriodConverter validityPeriodConv = new ValidityPeriodConverter();

		
		InformationField validationStatusField = new TextField(parent, style, ValidationStatus.class,ValidationResults.class,"Validation Status: ", 1, "validationStatus",null);
		fields.add(validationStatusField);

		InformationField validityPeriodField = new TextField(parent, style, ValidityPeriod.class,ResourceHoldingObject.class,"Validity Period: ", 1, "validityPeriod",validityPeriodConv);
		fields.add(validityPeriodField);

		InformationField invalidReasonsField = new TextField(parent, style, ArrayList.class,ValidationResults.class,"Errors: ", 1, "errors",checkToStringconv);
		fields.add(invalidReasonsField);
		
		InformationField warningsField = new TextField(parent,style, ArrayList.class, ValidationResults.class, "Warnings: ", 1,"warnings",checkToStringconv);
		fields.add(warningsField);

		InformationField subjectField = new TextField(parent, style, X500Principal.class, CertificateObject.class, "Subject: ", MagicNumbers.LINE_HEIGHT*2, "subject", new X500PrincipalConverter());
		fields.add(subjectField);
		
		InformationField skiField = new TextField(parent, style, byte[].class, CertificateObject.class,"SKI: ", MagicNumbers.LINE_HEIGHT, "subjectKeyIdentifier",new ByteArrayConverter());
		fields.add(skiField);

		LinkField issuerField = new LinkField(parent, style, X500Principal.class, CertificateObject.class,"Issuer: ", MagicNumbers.LINE_HEIGHT*2,"issuer", new X500PrincipalLinkConverter());
		issuerField.getLink().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ResourceHoldingObject selectedObj = browser.getViewerContainer().getSelectedObject();
				ResourceHoldingObject issuer = selectedObj.getParent();
				if (issuer != null) {
					browser.getViewerContainer().setSelection(issuer);
				}
			}
		});
		fields.add(issuerField);

		InformationField akiField = new TextField(parent, style, byte[].class, CertificateObject.class,"AKI: ", MagicNumbers.LINE_HEIGHT, "aki", new ByteArrayConverter());
		fields.add(akiField);

		InformationField serialField = new TextField(parent, style, BigInteger.class,ResourceHoldingObject.class,"SerialNr: ", MagicNumbers.LINE_HEIGHT,"serialNr",null);
		fields.add(serialField);

		InformationField filenameField = new TextField(parent, style, String.class,ResourceHoldingObject.class,"Filename: ", MagicNumbers.LINE_HEIGHT, "filename",null);
		fields.add(filenameField);
		
		InformationField locationField = new TextField(parent, style, URI.class, ResourceHoldingObject.class,"Location: ", MagicNumbers.LINE_HEIGHT*1, "remoteLocation",new URIConverter());
		fields.add(locationField);

		InformationField eeField = new TextField(parent, style, boolean.class,ResourceHoldingObject.class,"EE: ", MagicNumbers.LINE_HEIGHT, "isEE",null);
		fields.add(eeField);

		InformationField caField = new TextField(parent, style, boolean.class,ResourceHoldingObject.class,"CA: ", MagicNumbers.LINE_HEIGHT, "isCA",null);
		fields.add(caField);

		InformationField rootField = new TextField(parent, style, boolean.class,ResourceHoldingObject.class,"TA: ", MagicNumbers.LINE_HEIGHT, "isRoot",null);
		fields.add(rootField);
		
		PublicKeyConverter pkconv = new PublicKeyConverter();
		TextField keyField = new TextField(parent, style, PublicKey.class,ResourceHoldingObject.class,"Public Key: ", MagicNumbers.LINE_HEIGHT*2, "publicKey",pkconv);
		pkconv.setText(keyField.getText());
		fields.add(keyField);
		
		
		layoutFields(MagicNumbers.CDW_INFORMATION_CONTAINER_WIDTH - 20);
		parent.layout();
	}

	@Override
	public void bindToResourceHolder(IObservableValue resourceHolderObservable,
			DataBindingContext dbc) {
		
		IObservableValue validationResultObservable = PojoProperties.value((Class) resourceHolderObservable.getValueType(), "validationResults", ValidationResults.class).observeDetail(resourceHolderObservable);
		
		for(InformationField field : fields){
			if(field.containerType.equals(ValidationResults.class)){
				field.bindField(validationResultObservable, dbc);
			}
			
			if(field.containerType.equals(ResourceHoldingObject.class) | field.containerType.equals(CertificateObject.class)){
				field.bindField(resourceHolderObservable, dbc);
			}
		}
	}

	public ResourceSetTable getResourceSetTable() {
		return resourceSetTable;
	}
}
