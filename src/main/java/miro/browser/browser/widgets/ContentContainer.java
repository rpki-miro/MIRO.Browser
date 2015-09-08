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
package main.java.miro.browser.browser.widgets;

import main.java.miro.browser.browser.widgets.browser.RPKIBrowser;
import main.java.miro.browser.browser.widgets.stats.RPKIStats;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

public class ContentContainer extends Composite {

	private Composite browser;
	
	private Composite statsContainer;
	
	private StackLayout layout;
	
	public ContentContainer(Composite parent, int style) {
		super(parent, style);
		layout = new StackLayout();
		this.setLayout(layout);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		
		setData(RWT.CUSTOM_VARIANT, "contentContainer");
	}
	
	public void setBrowser(RPKIBrowser b) {
		browser = b;
	}
	
	public void setStatsContainer(Composite s){
		statsContainer = s;
	}
	
	public void showBrowser(){
		layout.topControl = browser;
		this.layout();
	}
	
	public void showStats(){
		if(statsContainer == null){
			initStats();
		}
		layout.topControl = statsContainer;
		this.layout();
	}
	
	private void initStats() {
		ScrolledComposite scroller = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);

		RPKIStats statsContainer = new RPKIStats(scroller, SWT.NONE);
		setStatsContainer(scroller);
		scroller.setContent(statsContainer);
		scroller.setMinSize(statsContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
}
