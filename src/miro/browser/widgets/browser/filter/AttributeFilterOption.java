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
package miro.browser.widgets.browser.filter;

import java.util.ArrayList;
import java.util.List;

import miro.browser.resources.Fonts;
import miro.browser.widgets.browser.filter.filters.AttributeFilter;
import miro.browser.widgets.browser.filter.filters.FilterKeys;
import miro.browser.widgets.browser.filter.filters.FilterKeys.FilterKey;
import miro.browser.widgets.browser.filter.filters.ResourceHoldingObjectFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class AttributeFilterOption extends RadioButtonContainer implements FilterOption{

	private FilterSearchField searchField;
	
	public AttributeFilterOption(Composite parent, int style) {
		super(parent, style);
		init();
		initButtons();
		searchField = new FilterSearchField(this, SWT.NONE);
	}

	private void init() {
		RowLayout layout = new RowLayout();
		setLayout(layout);
		
	}

	private void initButtons() {
		
		Button filenameButton = new Button(this, SWT.RADIO);
		filenameButton.setText("Filename");
		filenameButton.setFont(Fonts.STANDARD_FONT);
		RowData rowData = new RowData();
		filenameButton.setLayoutData(rowData);
		filenameButton.addListener(SWT.Selection,new SelectedButtonListener());
		filenameButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.FILENAME);
		
		
		Button locationButton = new Button(this, SWT.RADIO);
		locationButton.setText("Location");
		locationButton.setFont(Fonts.STANDARD_FONT);
		rowData = new RowData();
		locationButton.setLayoutData(rowData);
		locationButton.addListener(SWT.Selection,new SelectedButtonListener());
		locationButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.REMOTE_LOCATION);
		
		Button subjectButton = new Button(this, SWT.RADIO);
		subjectButton.setText("Subject");
		subjectButton.setFont(Fonts.STANDARD_FONT);
		rowData = new RowData();
		subjectButton.setLayoutData(rowData);
		subjectButton.addListener(SWT.Selection,new SelectedButtonListener());
		subjectButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.SUBJECT);
		
		
		Button issuerButton = new Button(this, SWT.RADIO);
		issuerButton.setText("Issuer");
		issuerButton.setFont(Fonts.STANDARD_FONT);
		rowData = new RowData();
		issuerButton.setLayoutData(rowData);
		issuerButton.addListener(SWT.Selection,new SelectedButtonListener());
		issuerButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.ISSUER);

		Button serialnrButton = new Button(this, SWT.RADIO);
		serialnrButton.setText("Serial Nr.");
		serialnrButton.setFont(Fonts.STANDARD_FONT);
		rowData = new RowData();
		serialnrButton.setLayoutData(rowData);
		serialnrButton.addListener(SWT.Selection,new SelectedButtonListener());
		serialnrButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.SERIAL_NUMBER);
		
		Button resourceButton = new Button(this, SWT.RADIO);
		resourceButton.setText("Resource");
		resourceButton.setFont(Fonts.STANDARD_FONT);
		rowData = new RowData();
		resourceButton.setLayoutData(rowData);
		resourceButton.addListener(SWT.Selection,new SelectedButtonListener());
		resourceButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.RESOURCE);
	}

	public void clearSelection() {
		//Clear button selection
		//Unselect
		if(selectedButton != null){
			selectedButton.setSelection(false);
		}
		//Dereference
		selectedButton = null;
		searchField.clearSelection();

	}

	@Override
	public List<ResourceHoldingObjectFilter> getFilters() {
		List<ResourceHoldingObjectFilter> filters = new ArrayList<ResourceHoldingObjectFilter>();

		String searchText = searchField.getSearchText().getText();
		if (selectedButton != null) {
			filters.add(new AttributeFilter(searchText,(FilterKey) selectedButton.getData(FilterKeys.FILTER_TYPE_KEY)));
		}
		return filters;
	}

}









