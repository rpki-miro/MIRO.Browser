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

import java.util.logging.Level;
import java.util.logging.Logger;

import miro.logging.LoggerFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class UpdateTimestamp extends Composite{
	
	private static final Logger log = LoggerFactory.getLogger(UpdateTimestamp.class, Level.FINEST);
	
	private Label updateTime;

	public UpdateTimestamp(Composite parent, int style) {
		super(parent, style);
		init();
		initTimeText();
	}

	private void init() {
		setLayout(new FillLayout());
//		setText("Last updated");
		
	}
	
	private void initTimeText() {
		updateTime = new Label(this, SWT.NONE);
//		updateTime.setFont(Fonts.MEDIUM_HEADER_FONT);
	}
	
	public void updateTimestamp(String newTimestamp) {
		updateTime.setText(newTimestamp);
		getParent().layout();
	}
}
