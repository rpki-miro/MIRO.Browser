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

import miro.browser.resources.Colors;
import miro.browser.resources.Fonts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class FilterSearchField extends Composite {
	
	Label searchFor;
	Text searchText;

	public FilterSearchField(Composite parent, int style) {
		super(parent, style);
		init();
		initSearchFor();
		initSearchText();
		
	}

	private void init() {
		FormLayout layout = new FormLayout();
		setLayout(layout);
		
	}
	
	private void initSearchFor() {
		searchFor = new Label(this, SWT.NONE);
		searchFor.setText("Search for:");
		searchFor.setFont(Fonts.STANDARD_FONT);
		FormData formData = new FormData();
		formData.bottom = new FormAttachment(100,0);	
		formData.left = new FormAttachment(0,0);
		searchFor.setLayoutData(formData);
		
	}
	
	private void initSearchText() {
		searchText = new Text(this, SWT.BORDER);
		searchText.setBackground(Colors.WHITE);
		searchText.setFont(Fonts.STANDARD_FONT);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0,0);
		formData.left = new FormAttachment(searchFor,10);
		formData.right = new FormAttachment(100,0);
		formData.height = 10;
		
		searchText.setLayoutData(formData);
		
	}
	
	public Text getSearchText(){
		return searchText;
	}
	
	public void clearSelection(){
		searchText.setText("");
	}


}
