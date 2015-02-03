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
import miro.browser.widgets.browser.filter.filters.FilterKeys;
import miro.browser.widgets.browser.filter.filters.FilterKeys.FilterKey;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ValidationStatusButtonContainer extends Composite implements FilterButtonContainer {
	
	private List<Button> checkButtons;
	
	public ValidationStatusButtonContainer(Composite parent, int style) {
		super(parent, style);
		init();
		
		initButtons();
	}

	private void initButtons() {
		Button button = new Button(this, SWT.CHECK);
		button.setText("Passed");
		button.setFont(Fonts.STANDARD_FONT);
		RowData rowData = new RowData();
		button.setLayoutData(rowData);
		button.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.PASSED_STATUS); 
		checkButtons.add(button);
		
		button = new Button(this, SWT.CHECK);
		button.setText("Warning");
		button.setFont(Fonts.STANDARD_FONT);
		rowData = new RowData();
		button.setLayoutData(rowData);
		button.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.WARNING_STATUS);
		checkButtons.add(button);

		button = new Button(this, SWT.CHECK);
		button.setText("Error");
		button.setFont(Fonts.STANDARD_FONT);
		rowData = new RowData();
		button.setLayoutData(rowData);
		button.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.ERROR_STATUS);
		checkButtons.add(button);

	}

	private void init() {
		checkButtons = new ArrayList<Button>();
		setLayout(new RowLayout());
		
	}

	public void clearSelection() {
		for(Button btn : checkButtons){
			btn.setSelection(false);
		}
	}

	@Override
	public Button[] getSelectedButtons() {
		List<Button> selected = new ArrayList<Button>();
		for(Button btn : checkButtons) {
			if(btn.getSelection()){
				selected.add(btn);
			}
		}
		return selected.toArray(new Button[]{});
	}

}
