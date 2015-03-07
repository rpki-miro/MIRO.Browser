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
package miro.browser.widgets.stats;

import miro.validator.stats.types.RPKIRepositoryStats;
import miro.validator.stats.types.Result;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

public class StatsWidget extends Composite {
	
	private StatsWidgetHeader header;
	
	private HostnameHeader hostnameHeader;	
	
	public StatsWidget(Composite parent, int style) {
		super(parent, style);
		
		setData(RWT.CUSTOM_VARIANT,"statsWidget");
		
		initLayout();
		
		initHeader();
		
	}
	
	public void initLayout() {
		RowLayout layout = new RowLayout();
		layout.type = SWT.VERTICAL;
		layout.fill = true;
		setLayout(layout);
	}
	
	private void initHeader() {
		header = new StatsWidgetHeader(this, SWT.BORDER);
		RowData rowData = new RowData();
		header.setLayoutData(rowData);
	}
	

	public void showRPKIRepositoryStats(RPKIRepositoryStats stats) {
		header.showRPKIRepositoryStats(stats);
		
		Result result = stats.getResult();
		ResultWidget resultWidget = new ResultWidget(this, SWT.BORDER);
		RowData rowData = new RowData();
		resultWidget.setLayoutData(rowData);
		resultWidget.showResult(result);
		
		hostnameHeader = new HostnameHeader(this, SWT.BORDER);
		hostnameHeader.getHeader().setText("Object distribution by host");
		
		for(Result hostResult : stats.getHostResults()){
				rowData = new RowData();
				resultWidget = new ResultWidget(this, SWT.BORDER);
				resultWidget.setLayoutData(rowData);
				resultWidget.showResult(hostResult);
		}
		
		
		layout();
	}
}
