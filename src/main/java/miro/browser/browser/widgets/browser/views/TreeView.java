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
package main.java.miro.browser.browser.widgets.browser.views;

import java.util.HashMap;

import main.java.miro.browser.util.DownloadHandler;
import main.java.miro.validator.types.RepositoryObject;
import main.java.miro.validator.types.ResourceCertificateTree;
import main.java.miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;


public class TreeView extends Composite implements RepositoryView{

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
		treeViewer.getTree().addListener(SWT.DefaultSelection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				TreeItem treeItem = (TreeItem) event.item;
				boolean expand = !treeItem.getExpanded();
				treeItem.setExpanded(expand);
			}
		});
		createContextMenu(treeViewer);
		
	}
	
	
	protected void createContextMenu(Viewer viewer) {
	    MenuManager contextMenu = new MenuManager("#ViewerMenu"); //$NON-NLS-1$
	    contextMenu.setRemoveAllWhenShown(true);
	    contextMenu.addMenuListener(new IMenuListener() {
	        @Override
	        public void menuAboutToShow(IMenuManager mgr) {
	            fillContextMenu(mgr);
	        }
	    });

	    Menu menu = contextMenu.createContextMenu(viewer.getControl());
	    viewer.getControl().setMenu(menu);
	}
	
	protected void fillContextMenu(IMenuManager contextMenu) {

	    contextMenu.add(new Action("Download object") {
	        @Override
	        public void run() {
	        	DownloadHandler dlHandler = new DownloadHandler();
	        	RepositoryObject obj = getSelection();
	        	dlHandler.sendDownload(obj, false);
	        }
	    });
	    contextMenu.add(new Action("Download object with subtree") {
	        @Override
	        public void run() {
	        	DownloadHandler dlHandler = new DownloadHandler();
	        	RepositoryObject obj = getSelection();
	        	dlHandler.sendDownload(obj, true);
	        }
	    });
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
	public RepositoryViewType getType() {
		return RepositoryViewType.TREE;
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
