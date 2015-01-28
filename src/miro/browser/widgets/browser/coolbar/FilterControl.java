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
package miro.browser.widgets.browser.coolbar;

import miro.browser.widgets.browser.tree.TreeContainer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class FilterControl extends Group {
	
	FilterToggle filterToggle;
	FilterClear filterClear;
	
	TreeContainer treeContainer;

	public FilterControl(Composite parent, int style, TreeContainer treeCont) {
		super(parent, style);
		treeContainer = treeCont;
		init();
		initFilterToggle();
		initFilterClear();
	}
	
	private void init() {
		RowLayout layout = new RowLayout();
		layout.spacing = 5;
		layout.marginLeft = 0;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		layout.marginRight = 0;
		setLayout(layout);
		setText("Filter Control");	
		
		
	}

	private void initFilterToggle() {
		filterToggle = new FilterToggle(this, SWT.NONE, treeContainer);
		RowData rowData = new RowData();
		rowData.height = 25;
		
		filterToggle.setLayoutData(rowData);
	}
	
	private void initFilterClear() {
		filterClear = new FilterClear(this, SWT.NONE, treeContainer);
		RowData rowData = new RowData();
		rowData.height = 25;
		
		filterClear.setLayoutData(rowData);
	}

	public FilterToggle getFilterToggle() {
		return filterToggle;
	}

	public FilterClear getFilterClear() {
		return filterClear;
	}
	
	

}
