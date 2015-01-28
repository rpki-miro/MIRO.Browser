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
package miro.browser.widgets.navigation;

import miro.browser.widgets.MainWidgetContainer;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class HeaderBar extends Composite {
	
	private Label heading;
	
	private MainWidgetContainer mainContainer;

	public HeaderBar(Composite parent, int style) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "header");
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		initLayout();
		initHeading();
	}
	
	private void initHeading(){
		heading = new Label(this, SWT.NONE);
		heading.setText("RPKI Repository Browser");
		heading.setData(RWT.CUSTOM_VARIANT, "heading");
		RowData layoutData = new RowData();
		heading.setLayoutData(layoutData);
	}
	
	private void initLayout(){
		RowLayout layout = new RowLayout();
		layout.fill = true;
		setLayout(layout);
	}

}
