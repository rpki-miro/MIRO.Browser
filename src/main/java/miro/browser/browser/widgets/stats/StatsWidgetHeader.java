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
package main.java.miro.browser.browser.widgets.stats;


import main.java.miro.validator.stats.types.RPKIRepositoryStats;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class StatsWidgetHeader extends Composite {
	
	private Label filenameLabel;
	
	private Label timestampLabel;
	
	public StatsWidgetHeader(Composite parent, int style) {
		super(parent, style);
		
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		setLayout(layout);
		

		filenameLabel = new Label(this, SWT.NONE);
//		filenameLabel.setFont(Fonts.SMALL_HEADER_FONT);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0,0);
		filenameLabel.setLayoutData(formData);
		
		timestampLabel = new Label(this, SWT.NONE);
//		timestampLabel.setFont(Fonts.SMALL_HEADER_FONT);
		formData = new FormData();
		formData.right = new FormAttachment(100,0);
		timestampLabel.setLayoutData(formData);
	}
	
	public void showRPKIRepositoryStats(RPKIRepositoryStats stats) {
		timestampLabel.setText("Last Updated: " +stats.getTimestamp());
		
		filenameLabel.setText("Trust Anchor: "+stats.getTrustAnchor());
	}

}
