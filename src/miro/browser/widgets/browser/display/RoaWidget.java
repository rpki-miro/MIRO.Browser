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
import miro.browser.converters.ASNConverter;
import miro.browser.converters.DateTimeConverter;
import miro.browser.converters.URIConverter;
import miro.browser.converters.ValidationCheckConverter;
import miro.browser.converters.ValidityPeriodConverter;
import miro.browser.resources.MagicNumbers;
import miro.browser.widgets.browser.RPKIBrowser;
import miro.validator.types.RepositoryObject;
import miro.validator.types.ResourceHoldingObject;
import miro.validator.types.RoaObject;
import miro.validator.types.ValidationResults;
import net.ripe.ipresource.Asn;
import net.ripe.rpki.commons.crypto.ValidityPeriod;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.joda.time.DateTime;

/**
 * Widget that shows information about a RoaObject
 * @author ponken
 *
 */
public class RoaWidget extends DisplayWidget implements ResourceHolderObservableBinder {
	
	private RoaPrefixTable prefixTable;
	
	public RoaWidget(Composite parent, int style,RPKIBrowser b) {
		super(parent, style,b);
		prefixTable = new RoaPrefixTable(this, style);
		setDisplayLayout();
		layout();
		
		informationContainer.moveAbove(null);
		prefixTable.moveBelow(informationContainer);
		
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
		layout.spacing = 20;
		setLayout(layout);

		RowData rowData = new RowData();
		rowData.width = MagicNumbers.CDW_INFORMATION_CONTAINER_WIDTH;
		informationContainer.setLayoutData(rowData);

		rowData = new RowData();
		rowData.height =  MagicNumbers.RDW_PREFIX_LIST_HEIGHT;
		rowData.width = MagicNumbers.RDW_PREFIX_LIST_WIDTH;
		prefixTable.setLayoutData(rowData);
	}

	@Override
	public void initFields(Composite parent, int style) {
		ValidationCheckConverter checkToStringconv = new ValidationCheckConverter();
		ValidityPeriodConverter validityPeriodConv = new ValidityPeriodConverter();
		
		InformationField filenameField = new TextField(parent, style, String.class,RepositoryObject.class,"Filename: ", MagicNumbers.LINE_HEIGHT, "filename",null);
		fields.add(filenameField);
		
		InformationField locationField = new TextField(parent, style, URI.class, RepositoryObject.class,"Location: ", MagicNumbers.LINE_HEIGHT, "remoteLocation",new URIConverter());
		fields.add(locationField);

		fields.add(new TextField(parent, style, ValidationStatus.class, ValidationResults.class,"Validation Status: ", MagicNumbers.LINE_HEIGHT*2, "validationStatus",null));

		fields.add(new TextField(parent, style, ArrayList.class, ValidationResults.class,"Errors: ", MagicNumbers.LINE_HEIGHT*2, "errors", checkToStringconv));
		
		fields.add(new TextField(parent,style,ArrayList.class, ValidationResults.class, "Warnings: ", MagicNumbers.LINE_HEIGHT*2,"warnings",checkToStringconv));
		
		fields.add(new TextField(parent, style, ValidityPeriod.class, RoaObject.class ,"Validity Period: ", MagicNumbers.LINE_HEIGHT*2, "validityPeriod",validityPeriodConv));
		
		fields.add(new TextField(parent, style, DateTime.class, ResourceHoldingObject.class,"Signing Time: ", MagicNumbers.LINE_HEIGHT*2, "signingTime", new DateTimeConverter()));

		fields.add(new TextField(parent,style,Asn.class, RoaObject.class, "ASN: ", MagicNumbers.LINE_HEIGHT,"asn", new ASNConverter()));
		
		layoutFields(MagicNumbers.CDW_INFORMATION_CONTAINER_WIDTH - 20);
		parent.layout();
	}

	public RoaPrefixTable getRoaPrefixTable() {
		return prefixTable;
	}

	@Override
	public void bindToResourceHolder(IObservableValue resourceHolderObservable,
			DataBindingContext dbc) {
		
		IObservableValue validationResultObservable = PojoProperties.value((Class) resourceHolderObservable.getValueType(), "validationResults", ValidationResults.class).observeDetail(resourceHolderObservable);
		for(InformationField field : fields){
			if(field.containerType.equals(ResourceHoldingObject.class) | field.containerType.equals(RoaObject.class) | field.containerType.equals(RepositoryObject.class)){
				field.bindField(resourceHolderObservable, dbc);
			}
			
			if(field.containerType.equals(ValidationResults.class)){
				field.bindField(validationResultObservable, dbc);
			}
		}
	}
	
	
}
