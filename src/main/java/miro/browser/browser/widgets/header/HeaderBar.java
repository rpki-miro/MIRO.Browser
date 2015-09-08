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
package main.java.miro.browser.browser.widgets.header;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class HeaderBar extends Composite {
	
	public HeaderBar(Composite parent, int style) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "header");
		initLayout();
		initHeading();
	}
	
	private void initHeading(){
		
		Composite wrap = new Composite(this, SWT.NONE);
		wrap.setData(RWT.CUSTOM_VARIANT, "heading_wrap");
		
		
		FillLayout layout = new FillLayout();
		layout.marginHeight = 15;
		layout.marginWidth = 15;
		wrap.setLayout(layout);

		
		Label heading = new Label(wrap, SWT.NONE);
		heading.setText("RPKI Repository Browser");
		heading.setData(RWT.CUSTOM_VARIANT, "heading");
	}
	
	private void initLayout(){
		RowLayout layout = new RowLayout();
		layout.fill = true;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.marginBottom = 0;
		layout.marginLeft = 0;
		layout.marginTop = 0;
		layout.marginRight = 0;
		layout.spacing = 0;
		setLayout(layout);
	}

}
