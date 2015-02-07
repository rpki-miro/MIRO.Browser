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


import miro.validator.types.ResourceCertificateTree;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class ViewerManager extends Composite {
	
	TreeBrowser treeBrowser;
	
	TableBrowser tableBrowser;
	
	StackLayout layout;
	
	ViewerContainer selectedBrowser;

	public ViewerManager(Composite parent, int style) {
		super(parent, style);
	}

	public void init() {
		layout = new StackLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout(layout);
		initTreeBrowser();
		showTreeBrowser();
		
	}

	public void setViewerInput(ResourceCertificateTree tree){
		TreeViewer treeViewer = treeBrowser.getTreeViewer();
		treeViewer.setInput(tree);
		treeViewer.refresh();

		TableViewer tableViewer = tableBrowser.getTableViewer();
		tableViewer.setInput(tree);
		tableViewer.refresh();
	}
	
	private void initTreeBrowser() {
		treeBrowser = new TreeBrowser(this, SWT.NONE);
		tableBrowser = new TableBrowser(this, SWT.NONE);
	}
	
	public void showTreeBrowser(){
		if(layout.topControl == tableBrowser){
			Table table = tableBrowser.getTable();
			if(table.getSelectionCount() > 0) {
				TableItem item = table.getSelection()[0];
				ResourceHoldingObject obj = (ResourceHoldingObject) item.getData();
				treeBrowser.setSelection(obj);
			}
		}
		selectedBrowser = treeBrowser;
		layout.topControl = treeBrowser;
		layout();
	}
	
	public TreeBrowser getTreeBrowser(){
		return treeBrowser;
	}
	
	public void showTableBrowser() {
		if(layout.topControl == treeBrowser){
			Tree tree = treeBrowser.getTree();
			if(tree.getSelectionCount() > 0) {
				TreeItem item = tree.getSelection()[0];
				ResourceHoldingObject obj = (ResourceHoldingObject) item.getData();
				tableBrowser.setSelection(obj);
			}
		}
		layout.topControl = tableBrowser;
		selectedBrowser = tableBrowser;
		layout();
	}
	
	public TableBrowser getTableBrowser() {
		return tableBrowser;
	}

	public void setViewerFilters(ViewerFilter[] viewerFilters) {
		treeBrowser.getTreeViewer().setFilters(viewerFilters);
		tableBrowser.getTableViewer().setFilters(viewerFilters);
	}

	public void resetViewerFilters() {
		treeBrowser.getTreeViewer().resetFilters();
		tableBrowser.getTableViewer().resetFilters();
	}

	public ResourceHoldingObject getSelectedObject() {
		return selectedBrowser.getSelection();
	}

	public void setSelection(ResourceHoldingObject issuer) {
		selectedBrowser.setSelection(issuer);
	}


}