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

import miro.browser.resources.MagicNumbers;
import miro.browser.updater.ModelUpdater;
import miro.browser.widgets.browser.RPKIBrowserView;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

public class BrowserCoolbar extends Composite {

	private RPKIBrowserView browser;
	
	private FilterControl filterControl;
	
	private RepoChooser repoChooser;
	
	private UpdateTimestamp updateTime;
	
	public BrowserCoolbar(Composite parent, int style) {
		super(parent, style);
		browser = (RPKIBrowserView) parent;
	}


	public void init(){
		setBackgroundMode(SWT.INHERIT_FORCE);
		
		setData(RWT.CUSTOM_VARIANT, "browserCoolbar");
		
		
		
		RowLayout layout = new RowLayout();
		layout.type = SWT.HORIZONTAL;
		layout.marginHeight = MagicNumbers.COOLBAR_MARGIN_HEIGHT;
		layout.marginWidth = MagicNumbers.COOLBAR_MARGIN_WIDTH;
		layout.marginBottom = 0;
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.spacing = MagicNumbers.COOLBAR_SPACING;
		setLayout(layout);

		initRepoChooser();
		initFilterControl();
		initUpdateTime();
		setDrawingOrder();
		
		/* See if names are loaded, if so display them */
    	String names[] = (String[]) RWT.getApplicationContext().getAttribute(ModelUpdater.MODEL_NAMES_KEY);
    	if(names != null){
    		repoChooser.updateDropDown(names);
    	}
		
	}
	
	private void initFilterControl(){
		filterControl = new FilterControl(this, SWT.NONE, browser.getTreeContainer());
	}
	
	
	private void initRepoChooser() {
		repoChooser = new RepoChooser(this, SWT.NONE, browser);
	
	}
	
	private void initUpdateTime() {
		updateTime = new UpdateTimestamp(this, SWT.NONE);
	}
	
	public UpdateTimestamp getUpdateTimestamp(){
		return updateTime;
	}
	
	private void setDrawingOrder(){
		filterControl.moveAbove(null);
		repoChooser.moveBelow(filterControl);
		updateTime.moveBelow(repoChooser);
		
	}

	public RepoChooser getRepoChooser() {
		return repoChooser;
	}
	
	public FilterControl getFilterControl(){
		return filterControl;
	}

	
	
	
}
