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
import miro.browser.widgets.browser.displaywidgets.CertificateDisplay;
import miro.browser.widgets.browser.displaywidgets.CertificateWidget;
import miro.browser.widgets.browser.displaywidgets.CrlWidget;
import miro.browser.widgets.browser.displaywidgets.ManifestWidget;
import miro.browser.widgets.browser.displaywidgets.RoaDisplay;
import miro.browser.widgets.browser.displaywidgets.RoaWidget;
import miro.browser.widgets.browser.filter.FilterWidget;
import miro.browser.widgets.browser.tree.ViewerManager;
import miro.validator.types.ResourceCertificateTree;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import types.Certificate;
import types.RepositoryTree;
import types.ResourceHolder;




public class RPKIBrowserView extends Composite{
	
	private ViewerManager viewerContainer;
	
	private BrowserCoolbar coolBar;
	
	private CertificateDisplay certificateDisplay;
	
	private RoaDisplay roaDisplay;
	
	private Composite displayContainer;
	
	private ArrayList<TabItem> displayTabs;
	
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
	}
	
	private void initFilter() {
		filterShell = new Shell(getShell(),  SWT.TITLE | SWT.CLOSE);
		filterShell.setSize(380, 450);
		filterShell.setLayout(new FillLayout());
		filter = new FilterWidget(filterShell, SWT.NONE, viewerContainer);
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
		displayTabs = new ArrayList<TabItem>();
		FormLayout layout = new FormLayout();
 		layout.spacing = MagicNumbers.BROWSER_SPACING;
		setLayout(layout);
		setBackground(Colors.BROWSER_BACKGROUND);
	}

	private void createSash() {
		final Sash sash = new Sash(this, SWT.VERTICAL);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(coolBar);
		layoutData.left = new FormAttachment(viewerContainer,-5);
		layoutData.width = 5;
		layoutData.bottom = new FormAttachment(100,0);
		sash.setLayoutData(layoutData);
		sash.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
		        sash.setBounds(e.x, e.y, e.width, e.height);
		        FormData formData = (FormData) viewerContainer.getLayoutData();
		        formData.width = e.x;
		        viewerContainer.setLayoutData(formData);
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
		viewerContainer = new ViewerManager(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(coolBar);
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.left = new FormAttachment(0,0);
		layoutData.width = MagicNumbers.TREE_VIEWER_WIDTH;
		viewerContainer.setLayoutData(layoutData);
		viewerContainer.setBackground(Colors.BROWSER_TREE_BACKGROUND);
		viewerContainer.setBackgroundMode(SWT.INHERIT_FORCE);
		
		
		viewerContainer.init();
		viewerContainer.getTreeBrowser().getTree().addSelectionListener(new TabHideListener(this));
		viewerContainer.getTableBrowser().getTable().addSelectionListener(new TabHideListener(this));
	}

	private void createDisplayContainer() {
		displayContainer = new TabFolder(this, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(coolBar);	
		layoutData.left = new FormAttachment(viewerContainer);
		layoutData.right = new FormAttachment(100,0);
		layoutData.bottom = new FormAttachment(100,0);
		displayContainer.setLayoutData(layoutData);
		
		displayContainer.setBackground(Colors.BROWSER_DISPLAY_CONTAINER_BACKGROUND);
		displayContainer.setLayout(new RowLayout());
		
		certificateDisplay = new CertificateDisplay(displayContainer,this);
		roaDisplay = new RoaDisplay(displayContainer, this);
		
	}
	
	private void initDatabindings() {
		TreeViewer treeViewer = viewerContainer.getTreeBrowser().getTreeViewer();
		IViewerObservableValue selection = ViewersObservables.observeSingleSelection(treeViewer);
		DataBindingContext dbc = new DataBindingContext();
		certificateDisplay.bindToResourceHolder(selection, dbc);
		roaDisplay.bindToResourceHolder(selection, dbc);
		treeViewer.getTree().addSelectionListener(new ViewerListener(this));
		
		TableViewer tableViewer = viewerContainer.getTableBrowser().getTableViewer();
		selection = ViewersObservables.observeSingleSelection(tableViewer);
		dbc = new DataBindingContext();
		certificateDisplay.bindToResourceHolder(selection, dbc);
		roaDisplay.bindToResourceHolder(selection, dbc);
		tableViewer.getTable().addSelectionListener(new ViewerListener(this));
		
	}
	
	
	public Composite getDisplayContainer() {
		return displayContainer;
	}
	
	public void addTab(TabItem tab){
		displayTabs.add(tab);
	}
	
	public BrowserCoolbar getBrowserCoolbar(){
		return coolBar;
	}
	
	public ViewerManager getViewerContainer(){
		return viewerContainer;
	}
	
	public ArrayList<TabItem> getTabs(){
		return displayTabs;
	}

	public CertificateDisplay getCertificateDisplay() {
		return certificateDisplay;
	}
	
	public RoaDisplay getRoaDisplay() {
		return roaDisplay;
	}

	public FilterWidget getFilterWidget() {
		return filter;
	}

	public void showFilter() {
		filterShell.open();
	}

	public void clearTabs() {
		for(TabItem tab : displayTabs){
			tab.dispose();
		}
		displayTabs.clear();
		
	}
}
