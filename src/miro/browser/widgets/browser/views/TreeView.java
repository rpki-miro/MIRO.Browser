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
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


public class TreeView extends Composite implements View{
	
	private Tree tree;

	private TreeViewer treeViewer;
	
	public TreeView(Composite parent, int style) {
		super(parent, style);
		init();
		
	}

	private void init() {
		setLayout(new FillLayout());

		tree = new Tree(this, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL |  SWT.BORDER);
		treeViewer = new TreeViewer(tree);	
		
		ViewLabelProvider label_provider = new ViewLabelProvider();
		TreeViewContentProvider content_provider = new TreeViewContentProvider();
		treeViewer.setContentProvider(content_provider);
		treeViewer.setLabelProvider(label_provider);
		label_provider.setViewer(treeViewer);
		
		tree.setData(RWT.MARKUP_ENABLED,Boolean.TRUE);
	}
	
	public void setSelection(ResourceHoldingObject obj) {
		treeViewer.setSelection(new StructuredSelection(obj));
		treeViewer.reveal(obj);
		
	}
	
	public Tree getTree(){
		return tree;
	}
	
	public TreeViewer getTreeViewer(){
		return treeViewer;
	}

	@Override
	public ResourceHoldingObject getSelection() {
		if(tree.getSelectionCount() > 0){
			TreeItem item = tree.getSelection()[0];
			return (ResourceHoldingObject) item.getData();
		}
		return null;
	}

	@Override
	public ViewType getType() {
		return ViewType.TREE;
	}

	@Override
	public void setInput(ResourceCertificateTree tree) {
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