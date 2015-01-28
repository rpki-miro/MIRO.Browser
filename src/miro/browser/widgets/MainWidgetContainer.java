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
package miro.browser.widgets;

import miro.browser.widgets.browser.RPKIBrowserView;
import miro.browser.widgets.query.QueryWidget;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

public class MainWidgetContainer extends Composite {

	private RPKIBrowserView browser;
	
	private QueryWidget queryPage;
	
	private Composite statsContainer;
	
	private StackLayout layout;
	
	public MainWidgetContainer(Composite parent, int style) {
		super(parent, style);
		setMainLayout();
	}
	
	public void setMainLayout() {
		layout = new StackLayout();
		this.setLayout(layout);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
	}
	
	public void setBrowser(RPKIBrowserView b) {
		browser = b;
	}
	
	public void setQuery(QueryWidget q){
		queryPage = q;
	}
	
	public void setStatsContainer(Composite s){
		statsContainer = s;
	}
	
	public void showQuery(){
		layout.topControl = queryPage;
		this.layout();
	}
	
	public void showBrowser(){
		layout.topControl = browser;
		this.layout();
	}
	
	public void showStats(){
		layout.topControl = statsContainer;
		this.layout();
	}
	
	public boolean browserShowing(){
		return layout.topControl.equals(browser);
	}
	
	public boolean queryShowing(){
		return layout.topControl.equals(queryPage);
	}

	public RPKIBrowserView getBrowser() {
		return browser;
	}
	
	

}
