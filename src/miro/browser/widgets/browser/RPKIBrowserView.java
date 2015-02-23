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
package miro.browser.widgets.browser;


import java.util.ArrayList;

import miro.browser.resources.Colors;
import miro.browser.resources.MagicNumbers;
import miro.browser.widgets.browser.coolbar.BrowserCoolbar;
import miro.browser.widgets.browser.display.CertificateDisplay;
import miro.browser.widgets.browser.display.DisplayContainer;
import miro.browser.widgets.browser.display.RoaDisplay;
import miro.browser.widgets.browser.filter.FilterWidget;
import miro.browser.widgets.browser.tree.ViewerManager;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class RPKIBrowserView extends Composite{
	
	private ViewerManager viewerManager;
	
	private DisplayContainer displayContainer;
	
//	private ArrayList<TabItem> displayTabs;
	
	private BrowserCoolbar coolBar;
	private FilterWidget filter;

	private Shell filterShell;
	
	public RPKIBrowserView(Composite parent, int style) {
		super(parent, style);
		
		init();
		
		createCoolBar();
		
		createViewerContainer();
		
		createDisplayContainer();
		
		createSash();
		
		initDatabindings();
		
		initFilter();
		
		coolBar.init();
		viewerManager.getTreeBrowser().getTreeViewer().addSelectionChangedListener(new TabHideListener(displayContainer));
		viewerManager.getTableBrowser().getTableViewer().addSelectionChangedListener(new TabHideListener(displayContainer));
	}
	
	private void initFilter() {
		filterShell = new Shell(getShell(),  SWT.TITLE | SWT.CLOSE);
		filterShell.setSize(380, 450);
		filterShell.setLayout(new FillLayout());
		filter = new FilterWidget(filterShell, SWT.NONE, viewerManager);
		filterShell.layout();
		filterShell.addListener(SWT.Close, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Shell s = (Shell) event.widget;
				s.setVisible(false);
				event.doit = false;
				
			}
		});
	}

	private void init() {
//		displayTabs = new ArrayList<TabItem>();
		FormLayout layout = new FormLayout();
 		layout.spacing = MagicNumbers.BROWSER_SPACING;
		setLayout(layout);
	}

	private void createSash() {
		final Sash sash = new Sash(this, SWT.VERTICAL);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(coolBar);
		layoutData.left = new FormAttachment(viewerManager,-5);
		layoutData.width = 5;
		layoutData.bottom = new FormAttachment(100,0);
		sash.setLayoutData(layoutData);
		sash.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
		        sash.setBounds(e.x, e.y, e.width, e.height);
		        FormData formData = (FormData) viewerManager.getLayoutData();
		        formData.width = e.x;
		        viewerManager.setLayoutData(formData);
		        layout(true);
			}
		});
	}

	private void createCoolBar() {
		coolBar = new BrowserCoolbar(this, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, 0);
		layoutData.left = new FormAttachment(0,0);
		layoutData.right = new FormAttachment(100, 0);
		coolBar.setLayoutData(layoutData);
	}
	
	private void createViewerContainer(){
		viewerManager = new ViewerManager(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(coolBar);
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.left = new FormAttachment(0,0);
		layoutData.width = MagicNumbers.TREE_VIEWER_WIDTH;
		viewerManager.setLayoutData(layoutData);
		
	}

	private void createDisplayContainer() {
		displayContainer = new DisplayContainer(this, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(coolBar);	
		layoutData.left = new FormAttachment(viewerManager);
		layoutData.right = new FormAttachment(100,0);
		layoutData.bottom = new FormAttachment(100,0);
		displayContainer.setLayoutData(layoutData);
		displayContainer.initDisplays(this);
	}
	
	private void initDatabindings() {
		TreeViewer treeViewer = viewerManager.getTreeBrowser().getTreeViewer();
		IViewerObservableValue selection = ViewersObservables.observeSingleSelection(treeViewer);
		DataBindingContext dbc = new DataBindingContext();
		treeViewer.addSelectionChangedListener(new ViewerListener(displayContainer));
		displayContainer.bindDisplays(selection, dbc);
		
		
		TableViewer tableViewer = viewerManager.getTableBrowser().getTableViewer();
		selection = ViewersObservables.observeSingleSelection(tableViewer);
		dbc = new DataBindingContext();
		tableViewer.addSelectionChangedListener(new ViewerListener(displayContainer));
		displayContainer.bindDisplays(selection, dbc);
	}
	
	public DisplayContainer getDisplayContainer() {
		return displayContainer;
	}
	
	
	public BrowserCoolbar getBrowserCoolbar(){
		return coolBar;
	}
	
	public ViewerManager getViewerContainer(){
		return viewerManager;
	}
	

	public FilterWidget getFilterWidget() {
		return filter;
	}

	public void showFilter() {
		filterShell.open();
	}

}
