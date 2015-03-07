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

import miro.validator.types.ResourceCertificateTree;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;


public class TreeView extends Composite implements View{

	private TreeViewer treeViewer;
	
	public TreeView(Composite parent, int style) {
		super(parent, style);
		init();
		
	}

	private void init() {
		setLayout(new FillLayout());
		treeViewer = new TreeViewer(this, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL);	
		treeViewer.setUseHashlookup(true);
		treeViewer.setAutoExpandLevel(2);
		ViewLabelProvider label_provider = new ViewLabelProvider();
		LazyTreeViewContentProvider content_provider = new LazyTreeViewContentProvider();
		treeViewer.setContentProvider(content_provider);
		treeViewer.setLabelProvider(label_provider);
		label_provider.setViewer(treeViewer);
		
	}
	
	public void setSelection(ResourceHoldingObject obj) {
		treeViewer.setSelection(new StructuredSelection(obj));
		treeViewer.reveal(obj);
	}
	
	public TreeViewer getTreeViewer(){
		return treeViewer;
	}

	@Override
	public ResourceHoldingObject getSelection() {
		StructuredSelection selection = (StructuredSelection) treeViewer.getSelection();
		return (ResourceHoldingObject) selection.getFirstElement();
	}

	@Override
	public ViewType getType() {
		return ViewType.TREE;
	}

	@Override
	public void setInput(ResourceCertificateTree tree) {
		LazyTreeViewContentProvider cp = (LazyTreeViewContentProvider) treeViewer.getContentProvider();
		cp.resetIndexMap();
		treeViewer.setInput(tree);
		treeViewer.refresh();
	}

	@Override
	public ViewerFilter[] getFilters() {
		return treeViewer.getFilters();
	}

	@Override
	public void setFilters(ViewerFilter[] filters) {
		treeViewer.setData("MARKED", new HashMap<ResourceHoldingObject, Boolean>());
		treeViewer.setFilters(filters);
		treeViewer.expandToLevel(3);
	}

	@Override
	public void resetFilters() {
		treeViewer.setData("MARKED", new HashMap<ResourceHoldingObject, Boolean>());
		treeViewer.resetFilters();
	}

	@Override
	public StructuredViewer getViewer() {
		return treeViewer;
	}

	@Override
	public ResourceCertificateTree getInput() {
		return (ResourceCertificateTree) treeViewer.getInput();
	}

}
