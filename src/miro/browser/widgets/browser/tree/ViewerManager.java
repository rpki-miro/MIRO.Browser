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


import java.util.HashMap;

import miro.browser.widgets.browser.tree.ViewerContainer.ViewerType;
import miro.validator.types.ResourceCertificateTree;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class ViewerManager extends Composite {
	
	private HashMap<ViewerType, ViewerContainer> viewerMap;
	
	private StackLayout layout;
	
	private ViewerContainer selectedViewer;

	public ViewerManager(Composite parent, int style) {
		super(parent, style);
		init();
	}

	public void init() {
		layout = new StackLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout(layout);
		
		viewerMap = new HashMap<ViewerContainer.ViewerType, ViewerContainer>();
		initViewers();
		showViewer(ViewerType.TREE);
	}

	public void setViewerInput(ResourceCertificateTree tree){
		selectedViewer.setInput(tree);
	}
	
	private void initViewers() {
		ViewerContainer viewer = new TreeBrowser(this, SWT.NONE);
		viewerMap.put(viewer.getType(), viewer);

		viewer = new TableBrowser(this, SWT.NONE);
		viewerMap.put(viewer.getType(), viewer);
	}
	
	public void showViewer(ViewerContainer.ViewerType type){
		ViewerContainer viewer = viewerMap.get(type);
		if(selectedViewer != null){
			
			viewer.setInput(selectedViewer.getInput());
			

			if(selectedViewer.getSelection() != null){
				viewer.setSelection(selectedViewer.getSelection());
			}
			
			
			viewer.setFilters(selectedViewer.getFilters());
		}
		layout.topControl = (Control) viewer;
		selectedViewer = viewer;
		layout();
	}
	
	public ViewerContainer getViewer(ViewerType type){
		return viewerMap.get(type);
	}
	
	public void setViewerFilters(ViewerFilter[] viewerFilters) {
		selectedViewer.setFilters(viewerFilters);
	}

	public void resetViewerFilters() {
		selectedViewer.resetFilters();
	}

	public ResourceHoldingObject getSelectedObject() {
		return selectedViewer.getSelection();
	}

	public void setSelection(ResourceHoldingObject issuer) {
		selectedViewer.setSelection(issuer);
	}


}
