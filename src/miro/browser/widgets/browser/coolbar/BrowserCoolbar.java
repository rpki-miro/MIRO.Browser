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
package miro.browser.widgets.browser.coolbar;

import java.io.OutputStreamWriter;

import miro.browser.download.DownloadHandler;
import miro.browser.updater.ModelUpdater;
import miro.browser.widgets.browser.RPKIBrowserView;
import miro.browser.widgets.browser.tree.ViewerManager;
import miro.validator.export.json.ManifestSerializer;
import miro.validator.types.ManifestObject;
import miro.validator.types.ResourceCertificateTree;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class BrowserCoolbar extends Composite {

	private RPKIBrowserView browser;
	
	private ToolBar toolbar;
	
	private RepoChooser repoChooser;
	
	private UpdateTimestamp updateTime;
	
	public BrowserCoolbar(Composite parent, int style) {
		super(parent, style);
		browser = (RPKIBrowserView) parent;
	}

	public void init(){
		setBackgroundMode(SWT.INHERIT_FORCE);
		setData(RWT.CUSTOM_VARIANT, "browserCoolbar");
		
		FormLayout layout = new FormLayout();
		setLayout(layout);

		
		initToolbar();
		initRepoChooser();
		initFilterControl();
		initUpdateTime();
		
		/* See if names are loaded, if so display them */
    	String names[] = (String[]) RWT.getApplicationContext().getAttribute(ModelUpdater.MODEL_NAMES_KEY);
    	if(names != null){
    		repoChooser.updateDropDown(names);
    	}
	}
	
	private void initToolbar(){
		toolbar = new ToolBar(this, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.left = new FormAttachment(0,0);
		toolbar.setLayoutData(layoutData);
	}
	
	private void initFilterControl(){
		ToolItem filterItem = new ToolItem(toolbar, SWT.PUSH);
		filterItem.setText("Show Filter");
		filterItem.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				browser.showFilter();
			}
		});
		
		ToolItem clearFilterItem = new ToolItem(toolbar, SWT.PUSH);
		clearFilterItem.setText("Clear Filter");
		clearFilterItem.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				browser.getFilterWidget().clearSelection();
				ViewerManager viewerContainer = browser.getViewerContainer();
				viewerContainer.resetViewerFilters();
			}
		});
		
		final ToolItem expandTreeItem = new ToolItem(toolbar, SWT.CHECK);
		expandTreeItem.setText("Expand Tree");
		expandTreeItem.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				TreeViewer treeViewer = browser.getViewerContainer().getTreeBrowser().getTreeViewer();
				ToolItem item = (ToolItem) event.widget;
				if(item.getSelection()){
					treeViewer.expandAll();
				} else {
					treeViewer.collapseAll();
				}
			}
		});
		
		ToolItem toggleViewerItem = new ToolItem(toolbar, SWT.CHECK);
		toggleViewerItem.setText("Show table viewer");
		toggleViewerItem.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				ToolItem item = (ToolItem) event.widget;
				if(item.getSelection()){
					browser.getViewerContainer().showTableBrowser();
					expandTreeItem.setEnabled(false);
				} else {
					browser.getViewerContainer().showTreeBrowser();
					expandTreeItem.setEnabled(true);
				}
				
			}
		});
		
		ToolItem sep = new ToolItem(toolbar, SWT.SEPARATOR);
		sep.setControl(repoChooser.getDropDown());
		sep.setWidth(200);
	}
	
	private void initRepoChooser() {
		repoChooser = new RepoChooser(toolbar, SWT.NONE, browser);
	}
	
	private void initUpdateTime() {
		updateTime = new UpdateTimestamp(this, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.right = new FormAttachment(100,0);
		layoutData.bottom = new FormAttachment(100,0);
		updateTime.setLayoutData(layoutData);
	}
	
	public UpdateTimestamp getUpdateTimestamp(){
		return updateTime;
	}
	
	public RepoChooser getRepoChooser() {
		return repoChooser;
	}
}
