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

import miro.browser.widgets.browser.filter.filters.FileTypeFilter;
import miro.browser.widgets.browser.filter.filters.FilterKeys;
import miro.browser.widgets.browser.filter.filters.FilterKeys.FilterKey;
import miro.browser.widgets.browser.filter.filters.ResourceHoldingObjectFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class FileTypeFilterOption extends RadioButtonContainer implements FilterOption{
	
	private Button allButton;
	
	private Button cerButton;
	
	private Button roaButton;
	
	public FileTypeFilterOption(Composite parent, int style) {
		super(parent, style);
		init();
		initButtons();
	}

	private void initButtons() {
		allButton = new Button(this, SWT.RADIO);
		allButton.setText("All files");
		RowData rowData = new RowData();
		allButton.setLayoutData(rowData);
		allButton.addListener(SWT.Selection, new SelectedButtonListener());
		allButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.ALL_FILES);
		allButton.setSelection(true);
		selectedButton = allButton;
		
		cerButton = new Button(this, SWT.RADIO);
		cerButton.setText("Only .cer files");
		rowData = new RowData();
		cerButton.setLayoutData(rowData);
		cerButton.addListener(SWT.Selection, new SelectedButtonListener());
		cerButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.CER_FILES);

		roaButton = new Button(this, SWT.RADIO);
		roaButton.setText("Only .roa files");
		rowData = new RowData();
		roaButton.setLayoutData(rowData);
		roaButton.addListener(SWT.Selection, new SelectedButtonListener());
		roaButton.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.ROA_FILES);
	}

	private void init() {
		setLayout(new RowLayout());
	}
	
	public void clearSelection() {
		selectedButton.setSelection(false);
		allButton.setSelection(true);
		selectedButton = allButton;
	}

	@Override
	public List<ResourceHoldingObjectFilter> getFilters() {
		List<ResourceHoldingObjectFilter> filters = new ArrayList<ResourceHoldingObjectFilter>();
		if (selectedButton != null) {
			filters.add(new FileTypeFilter((FilterKey) selectedButton.getData(FilterKeys.FILTER_TYPE_KEY)));
		}
		return filters;
	}
}
