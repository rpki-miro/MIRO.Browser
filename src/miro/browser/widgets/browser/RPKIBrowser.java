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


import miro.browser.resources.MagicNumbers;
import miro.browser.widgets.browser.controlbar.BrowserControlBar;
import miro.browser.widgets.browser.display.DetailViewContainer;
import miro.browser.widgets.browser.display.TabListener;
import miro.browser.widgets.browser.display.TableListener;
import miro.browser.widgets.browser.filter.FilterWidget;
import miro.browser.widgets.browser.views.RepositoryView.RepositoryViewType;
import miro.browser.widgets.browser.views.RepositoryViewContainer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;

public class RPKIBrowser extends Composite{
	
	private RepositoryViewContainer repositoryViewContainer;
	
	private DetailViewContainer detailViewContainer;
	
	private BrowserControlBar controlBar;
	
	private FilterWidget filter;

	private Shell filterShell;
	
	public RPKIBrowser(Composite parent, int style) {
		super(parent, style);
		createWidgets();
		createLayout();
		controlBar.showCurrentRepos();
	}
	
	public void createWidgets(){
		repositoryViewContainer = new RepositoryViewContainer(this, SWT.NONE);
		
		detailViewContainer = new DetailViewContainer(this, SWT.NONE);
		detailViewContainer.initDisplays(this);
		initDatabindings();
		
		repositoryViewContainer.getView(RepositoryViewType.TREE).getViewer().addSelectionChangedListener(new TabListener(detailViewContainer));
		repositoryViewContainer.getView(RepositoryViewType.TREE).getViewer().addSelectionChangedListener(new TableListener(detailViewContainer));
		repositoryViewContainer.getView(RepositoryViewType.TABLE).getViewer().addSelectionChangedListener(new TabListener(detailViewContainer));
		repositoryViewContainer.getView(RepositoryViewType.TABLE).getViewer().addSelectionChangedListener(new TableListener(detailViewContainer));
		controlBar = new BrowserControlBar(this, SWT.NONE,this);
		initFilter();
	}
	
	private void createLayout() {
		FormLayout layout = new FormLayout();
 		layout.spacing = MagicNumbers.BROWSER_SPACING;
		setLayout(layout);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, 0);
		layoutData.left = new FormAttachment(0,0);
		layoutData.right = new FormAttachment(100, 0);
		controlBar.setLayoutData(layoutData);
		
		
		layoutData = new FormData();
		layoutData.top = new FormAttachment(controlBar);
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.left = new FormAttachment(0,0);
		layoutData.width = MagicNumbers.TREE_VIEWER_WIDTH;
		repositoryViewContainer.setLayoutData(layoutData);

		layoutData = new FormData();
		layoutData.top = new FormAttachment(controlBar);	
		layoutData.left = new FormAttachment(repositoryViewContainer,0);
		layoutData.right = new FormAttachment(100,0);
		layoutData.bottom = new FormAttachment(100,0);
		detailViewContainer.setLayoutData(layoutData);
		
		final Sash sash = new Sash(this, SWT.VERTICAL);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(controlBar);
		layoutData.left = new FormAttachment(repositoryViewContainer,-5);
		layoutData.width = 5;
		layoutData.bottom = new FormAttachment(100,0);
		sash.setLayoutData(layoutData);
		sash.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
		        sash.setBounds(e.x, e.y, e.width, e.height);
		        FormData formData = (FormData) repositoryViewContainer.getLayoutData();
		        formData.width = e.x;
		        repositoryViewContainer.setLayoutData(formData);
		        layout(true);
			}
		});
	}

	private void initFilter() {
		filterShell = new Shell(getShell(),  SWT.CLOSE | SWT.TITLE );
		filterShell.setData(RWT.CUSTOM_VARIANT, "filterShell");
		filterShell.setSize(380, 450);
		filterShell.setText("Filter Options");
		FillLayout layout = new FillLayout();
		filterShell.setLayout(layout);
		filter = new FilterWidget(filterShell, SWT.NONE, repositoryViewContainer);
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
	
	/**
	 * Binds the selection of viewers held by viewerManager to the displays held by displayContainer
	 */
	private void initDatabindings() {
		StructuredViewer viewer = repositoryViewContainer.getView(RepositoryViewType.TREE).getViewer();
		IViewerObservableValue selection = ViewersObservables.observeSingleSelection(viewer);
		DataBindingContext dbc = new DataBindingContext();
		detailViewContainer.bindViews(selection, dbc);
		
		
		viewer = repositoryViewContainer.getView(RepositoryViewType.TABLE).getViewer();
		selection = ViewersObservables.observeSingleSelection(viewer);
		dbc = new DataBindingContext();
		detailViewContainer.bindViews(selection, dbc);
	}
	
	public DetailViewContainer getDisplayContainer() {
		return detailViewContainer;
	}
	
	
	public BrowserControlBar getBrowserControlBar(){
		return controlBar;
	}
	
	public RepositoryViewContainer getViewerContainer(){
		return repositoryViewContainer;
	}
	

	public FilterWidget getFilterWidget() {
		return filter;
	}

	public void showFilter() {
		filterShell.open();
	}

}
