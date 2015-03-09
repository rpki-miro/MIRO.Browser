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

import miro.browser.widgets.browser.filter.filters.AttributeFilter;
import miro.browser.widgets.browser.filter.filters.FilterKeys;
import miro.browser.widgets.browser.filter.filters.FilterKeys.FilterKey;
import miro.browser.widgets.browser.filter.filters.ResourceHoldingObjectFilter;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class AttributeFilterOption extends RadioButtonContainer implements FilterOption{

	private FilterSearchField searchField;
	
	public AttributeFilterOption(Composite parent, int style) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "filterOption");
		init();
		initButtons();
		initSearchField();
	}

	private void init() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		setLayout(layout);
	}
	
	private void initSearchField() {
		searchField = new FilterSearchField(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		searchField.setLayoutData(gridData);
	}

	private void initButtons() {
		
		Button filenameButton = new Button(this, SWT.RADIO);
		filenameButton.setText("Filename");
		filenameButton.addListener(SWT.Selection,new SelectedButtonListener());
		filenameButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.FILENAME);
		GridData gridData = new GridData();
		filenameButton.setLayoutData(gridData);
		
		Button locationButton = new Button(this, SWT.RADIO);
		locationButton.setText("Location");
		locationButton.addListener(SWT.Selection,new SelectedButtonListener());
		locationButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.REMOTE_LOCATION);
		gridData = new GridData();
		locationButton.setLayoutData(gridData);
		
		Button subjectButton = new Button(this, SWT.RADIO);
		subjectButton.setText("Subject");
		subjectButton.addListener(SWT.Selection,new SelectedButtonListener());
		subjectButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.SUBJECT);
		gridData = new GridData();
		subjectButton.setLayoutData(gridData);
		
		
		Button issuerButton = new Button(this, SWT.RADIO);
		issuerButton.setText("Issuer");
		issuerButton.addListener(SWT.Selection,new SelectedButtonListener());
		issuerButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.ISSUER);
		gridData = new GridData();
		issuerButton.setLayoutData(gridData);

		Button serialnrButton = new Button(this, SWT.RADIO);
		serialnrButton.setText("Serial Nr.");
		serialnrButton.addListener(SWT.Selection,new SelectedButtonListener());
		serialnrButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.SERIAL_NUMBER);
		gridData = new GridData();
		serialnrButton.setLayoutData(gridData);
		
		Button resourceButton = new Button(this, SWT.RADIO);
		resourceButton.setText("Resource");
		resourceButton.addListener(SWT.Selection,new SelectedButtonListener());
		resourceButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.RESOURCE);
		gridData = new GridData();
		resourceButton.setLayoutData(gridData);
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









