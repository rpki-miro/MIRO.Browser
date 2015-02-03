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
package miro.browser.widgets.browser.tree;


import java.util.ArrayList;

import miro.browser.widgets.browser.filter.FilterWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

public class TreeContainer extends Composite {
	
	ArrayList<TreeToggleObserver> toggleObservers;
	
	TreeBrowser treeBrowser;
	FilterWidget filter;
	StackLayout layout;
	private boolean toggle;

	public TreeContainer(Composite parent, int style) {
		super(parent, style);
		toggleObservers = new ArrayList<TreeToggleObserver>();
	}

	public void init() {
		layout = new StackLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout(layout);
		initTreeBrowser();
		initFilter();
		showTree();
		
	}
	
	private void initTreeBrowser() {
		treeBrowser = new TreeBrowser(this, SWT.NONE);
	}

	private void initFilter() {
		filter = new FilterWidget(this, SWT.NONE, this);
		
	}
	
	private void showTree(){
		layout.topControl = treeBrowser;
		layout();
	}
	
	private void showFilter(){
		layout.topControl = filter;
		layout();
	}
	
	public TreeBrowser getTreeBrowser(){
		return treeBrowser;
	}
	
	public FilterWidget getFilterWidget(){
		return filter;
	}
	
	public void addTreeToggleObserver(TreeToggleObserver obs){
		toggleObservers.add(obs);
	}
	
	public void notifyTreeToggleObservers(){
		for(TreeToggleObserver obs : toggleObservers){
			obs.notifyTreeToggle(toggle);
		}
	}
	
	public void toggle(){
		/*
		 * toggle is false at the start, and we want tree as our start widget
		 * So !toggle should lead to filter being shown, and toggle to tree being shown
		 */
		if(!toggle){
			showFilter();
		} else {
			showTree();
		}
		notifyTreeToggleObservers();
		toggle = !toggle;
	}


}
