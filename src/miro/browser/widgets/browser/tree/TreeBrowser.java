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

import java.lang.reflect.Method;
import java.util.HashMap;

import miro.browser.provider.CertificateTreeContentProvider;
import miro.browser.provider.CertificateTreeLabelProvider;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


public class TreeBrowser extends Composite {
	
	private Tree tree;

	private TreeViewer treeViewer;
	
	private HashMap<ResourceHoldingObject, TreeItem> itemMap;

	public TreeBrowser(Composite parent, int style) {
		super(parent, style);
		init();
		
	}

	private void init() {
		setLayout(new FillLayout());

		tree = new Tree(this, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL |  SWT.BORDER);
		treeViewer = new TreeViewer(tree);	
		
		CertificateTreeLabelProvider label_provider = new CertificateTreeLabelProvider();
		CertificateTreeContentProvider content_provider = new CertificateTreeContentProvider();
		treeViewer.setContentProvider(content_provider);
		treeViewer.setLabelProvider(label_provider);
	}
	
	public void setSelection(ResourceHoldingObject obj) {
		Method findItem;
		try {
			findItem = StructuredViewer.class.getDeclaredMethod("doFindItem", Object.class);
			findItem.setAccessible(true);
			TreeItem item = (TreeItem) findItem.invoke(treeViewer, obj);
			if(item == null){
				return;
			}
			treeViewer.getTree().setSelection(item);
			Event ev = new Event();
			ev.item = item;
			treeViewer.getTree().notifyListeners(SWT.Selection, ev);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Tree getTree(){
		return tree;
	}
	
	public TreeViewer getTreeViewer(){
		return treeViewer;
	}

}
