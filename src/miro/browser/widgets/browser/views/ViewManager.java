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
package miro.browser.widgets.browser.views;


import java.util.HashMap;

import miro.browser.widgets.browser.views.View.ViewType;
import miro.validator.types.ResourceCertificateTree;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Widget container for the different views available. Manages input, selection, filters of View Objects and shows the current View.
 * @author ponken
 *
 */
public class ViewManager extends Composite {
	
	private HashMap<ViewType, View> viewMap;
	
	private StackLayout layout;
	
	private View currentView;

	public ViewManager(Composite parent, int style) {
		super(parent, style);
		viewMap = new HashMap<View.ViewType, View>();

		layout = new StackLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout(layout);

		initViewers();
		showView(ViewType.TREE);
	}
	
	/**
	 * Shows the View with the corresponding ViewType. 
	 * Tries to preserve selection, input and filters from the old View.
	 * @param type Type of the View to display
	 */
	public void showView(ViewType type){
		View viewer = viewMap.get(type);
		if(currentView != null){
			viewer.setInput(currentView.getInput());
			if(currentView.getSelection() != null){
				viewer.setSelection(currentView.getSelection());
			}
			viewer.setFilters(currentView.getFilters());
		}
		layout.topControl = (Control) viewer;
		currentView = viewer;
		layout();
	}

	private void initViewers() {
		View viewer = new TreeView(this, SWT.NONE);
		viewMap.put(viewer.getType(), viewer);

		viewer = new TableView(this, SWT.NONE);
		viewMap.put(viewer.getType(), viewer);
	}
	
	public View getView(ViewType type){
		return viewMap.get(type);
	}
	
	public void setViewerFilters(ViewerFilter[] viewerFilters) {
		currentView.setFilters(viewerFilters);
	}

	public void resetViewerFilters() {
		currentView.resetFilters();
		currentView.getViewer().refresh();
	}

	public ResourceHoldingObject getSelectedObject() {
		return currentView.getSelection();
	}

	public void setSelection(ResourceHoldingObject issuer) {
		currentView.setSelection(issuer);
	}

	public void setViewerInput(ResourceCertificateTree tree){
		currentView.setInput(tree);
	}
}
