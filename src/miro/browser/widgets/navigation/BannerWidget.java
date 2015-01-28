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

import java.util.ArrayList;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

public class BannerWidget extends Composite {
	ArrayList<Link> repoLinks;
	Link queryTest;

	public BannerWidget(Composite parent, int style) {
		super(parent, style);
		setBannerLayoutData();
		setBannerLayout();
	}
	
//	public void initLinks(BannerLinkListener listener) {
//		repoLinks = new ArrayList<Link>();
//		Link buf;
//		
//
//		for(ResourceCertificateRepositoryModel model : ResourceCertificateRepositoryModel.getAllModels()){
//			buf = new Link(this, SWT.NONE);
//			buf.setText("<a>"+model.getName()+"</a>");
//			buf.addListener(SWT.Selection, listener);
//			repoLinks.add(buf);
//		}
//		
//		queryTest = new Link(this, SWT.NONE);
//		queryTest.setText("<a>Query</a>");
//		queryTest.addListener(SWT.Selection, listener);
//		
//	}
	
	public void setBannerLayout() {
		this.setLayout(new FillLayout());
	}
	
	public void setBannerLayoutData() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.grabExcessHorizontalSpace = true;
		this.setLayoutData(gridData);
		
		
	}

}
