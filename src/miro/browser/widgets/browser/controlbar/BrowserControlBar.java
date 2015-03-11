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
package miro.browser.widgets.browser.controlbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import miro.browser.updater.ModelObserver;
import miro.browser.updater.ModelUpdater;
import miro.browser.updater.ObserverType;
import miro.browser.widgets.browser.RPKIBrowser;
import miro.browser.widgets.browser.views.View.ViewType;
import miro.browser.widgets.browser.views.ViewManager;
import miro.validator.types.ResourceCertificateTree;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;



public class BrowserControlBar extends Composite implements ModelObserver {

	private RPKIBrowser browser;
	
	private ToolBar toolbar;
	
	private CCombo dropDown;
	
	private Label updateTimestamp;
	
	public BrowserControlBar(Composite parent, int style, RPKIBrowser b) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "browserControlbar");
		ModelUpdater.addObserver(this, ObserverType.MODEL);
		browser = b; 
		
		createWidgets();
		initToolbar();
		createLayout();
	}
	
	public void createWidgets(){
		toolbar = new ToolBar(this, SWT.NONE);
		dropDown = new CCombo(toolbar, SWT.READ_ONLY | SWT.FLAT);
		updateTimestamp = new Label(this, SWT.NONE);
		updateTimestamp.setData(RWT.CUSTOM_VARIANT, "updateTimestamp");
	}
	
	/**
	 * Creates the ToolItems contained by the ToolBar including their various handlers
	 */
	private void initToolbar(){
		ToolItem filterItem = new ToolItem(toolbar, SWT.PUSH);
		filterItem.setText("Filter");
		filterItem.setData(RWT.CUSTOM_VARIANT, "controlbarItem");
		filterItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				browser.showFilter();
			}
		});
		
		ToolItem clearFilterItem = new ToolItem(toolbar, SWT.PUSH);
		clearFilterItem.setText("Clear Filter");
		clearFilterItem.setData(RWT.CUSTOM_VARIANT, "controlbarItem");
		clearFilterItem.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				browser.getFilterWidget().clearSelection();
				ViewManager viewerContainer = browser.getViewerContainer();
				viewerContainer.resetViewerFilters();
			}
		});
		
		ToolItem toggleViewerItem = new ToolItem(toolbar, SWT.CHECK);
		toggleViewerItem.setText("List View");
		toggleViewerItem.setWidth(150);
		toggleViewerItem.setData(RWT.CUSTOM_VARIANT, "controlbarItem");
		toggleViewerItem.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				ToolItem item = (ToolItem) event.widget;
				if(item.getSelection()){
					browser.getViewerContainer().showView(ViewType.TABLE);
				} else {
					browser.getViewerContainer().showView(ViewType.TREE);
				}
				
			}
		});
		
		/* A separator item can hold a CCombo */
		ToolItem sep = new ToolItem(toolbar, SWT.SEPARATOR);
		sep.setControl(dropDown);
		sep.setWidth(130);
		dropDown.setData(RWT.CUSTOM_VARIANT, "controlbarDropdown");
		dropDown.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				
				/* Find the selected model name */
				CCombo combo = (CCombo) e.widget;
				int selection = combo.getSelectionIndex();
				if(selection == -1){
					return;
				}
				String selectedName = combo.getItem(selection);
				
				
				/* Get the corresponding model and set as input*/
				ResourceCertificateTree certTree = (ResourceCertificateTree) RWT.getApplicationContext().getAttribute(selectedName);
				browser.getViewerContainer().setViewerInput(certTree);
				browser.getViewerContainer().setSelection(certTree.getTrustAnchor());
				updateTimestamp.setText(certTree.getTimeStamp());
			}
		});

	}
	
	
	public void showCurrentRepos(){
		/* See if names are loaded, if so display them */
		String names[] = (String[]) RWT.getApplicationContext().getAttribute(ModelUpdater.MODEL_NAMES_KEY);
		if(names != null){
			updateDropDown(names);
		}
	}

	private void createLayout() {
		FormLayout layout = new FormLayout();
		setLayout(layout);

		FormData layoutData = new FormData();
		layoutData.left = new FormAttachment(0,0);
		toolbar.setLayoutData(layoutData);
		
		layoutData = new FormData();
		layoutData.right = new FormAttachment(100,0);
		layoutData.bottom = new FormAttachment(100,0);
		updateTimestamp.setLayoutData(layoutData);
	}

	/**
	 * Changes the entries displayed by dropDown to modelNames. Tries to maintain selection
	 * in case the old selection is contained within modelNames 
	 * @param modelNames The new items to display
	 */
	public void updateDropDown(String[] modelNames){
		String[] names = modelNames;
    	if(names.length == 0){
    		dropDown.setItems(names);
    		dropDown.notifyListeners(SWT.Selection, new Event());
    		return;
    	}
    	
    	int selectIndex = dropDown.getSelectionIndex();
    	String currentSelection = selectIndex != -1 ? dropDown.getItem(selectIndex) : names[0];
    	
    	Arrays.sort(names);
    	dropDown.setItems(names);
    	
    	for(int i = 0;i<dropDown.getItemCount();i++){
    		if(dropDown.getItem(i).equals(currentSelection)){
    			dropDown.select(i);
    			break;
    		}	
    	}
    	dropDown.notifyListeners(SWT.Selection, new Event());
	}
	

	@Override
	public void notifyModelChange() {
		if(isDisposed()){
			ModelUpdater.removeObserver(this, ObserverType.MODEL);
			return;
		}
		
		//We have to run this as an asyncExec, so the SWT UI thread can do the update.
		//We cannot do the update in this thread, since that breaks the SWT thread model
		//(only SWT UI thread is allowed to make UI changes)
		this.getDisplay().asyncExec(new Runnable() {
		    public void run() {
		    	String names[] = (String[]) RWT.getApplicationContext().getAttribute(ModelUpdater.MODEL_NAMES_KEY);
		    	updateDropDown(names);
		    	layout();
		    }
		});
	}
}
