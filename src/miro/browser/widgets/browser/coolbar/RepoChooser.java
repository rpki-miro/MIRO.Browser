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

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import miro.browser.resources.Colors;
import miro.browser.resources.Fonts;
import miro.browser.updater.ModelObserver;
import miro.browser.updater.ModelUpdater;
import miro.browser.updater.ObserverType;
import miro.browser.widgets.browser.RPKIBrowserView;
import miro.logging.LoggerFactory;
import miro.validator.types.ResourceCertificateTree;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

public class RepoChooser extends Composite implements ModelObserver{
	
	private static final Logger log = LoggerFactory.getLogger(RepoChooser.class, Level.FINEST);
	
	private CCombo dropDown;
	
	private RPKIBrowserView browser;

	public RepoChooser(Composite parent, int style, RPKIBrowserView b) {
		super(parent, style);
		browser = b;
		ModelUpdater.addObserver(this, ObserverType.MODEL);
		init();
		initDropDown();
	}


	private void init() {
		RowLayout layout = new RowLayout();
		layout.marginBottom = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.spacing = 0;
		setLayout(layout);
//		setText("Repository Menu");
	}

	private void initDropDown() {
		dropDown = new CCombo(this, SWT.READ_ONLY | SWT.FLAT);
		RowData rowData = new RowData();
		rowData.height = 25;
		dropDown.setLayoutData(rowData);
		dropDown.setFont(Fonts.STANDARD_FONT);
		dropDown.setBackground(Colors.WHITE);
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
				//RepositoryTree repoTree = (RepositoryTree) RWT.getApplicationContext().getAttribute(selectedName);
//				browser.getBrowserCoolbar().getUpdateTimestamp().updateTimestamp(repoTree.getTimeStamp());
//				browser.setTreeViewerInput(repoTree);
				
				
				ResourceCertificateTree certTree = (ResourceCertificateTree) RWT.getApplicationContext().getAttribute(selectedName);

				browser.getBrowserCoolbar().getUpdateTimestamp().updateTimestamp(certTree.getTimeStamp());
				browser.getViewerContainer().setViewerInput(certTree);
			}
		});
	}
	
	public void updateDropDown(String[] modelNames){
		String[] names = modelNames;
    	if(names.length == 0){
    		dropDown.setItems(names);
    		dropDown.notifyListeners(SWT.Selection, new Event());
    		return;
    	}
    	
    	//Get index of current selection
    	int selectIndex = dropDown.getSelectionIndex();
    	String currentSelection;
    	
    	//If something is selected, get it. Else just use the first one in names
    	if(selectIndex != -1){
    		currentSelection = dropDown.getItem(selectIndex);
    	}else {
    		currentSelection = names[0];
    	}
    	
    	//Set the itemlist
    	Arrays.sort(names);
    	dropDown.setItems(names);
    	
    	//Select "currentSelection", by finding its index first, then selecting it
    	for(int i = 0;i<dropDown.getItemCount();i++){
    		if(dropDown.getItem(i).equals(currentSelection)){
    			dropDown.select(i);
    			break;
    		}	
    	}
    	dropDown.getParent().getParent().layout();
    	//Notify our selection listener, so the browser tree can update
    	dropDown.notifyListeners(SWT.Selection, new Event());
	}
	
	public CCombo getDropDown(){
		return dropDown;
	}
	
	public void setDefaultRepo(){
		String[] items = dropDown.getItems();
		
		/* If some model(s) exists, select the first one.*/
		if(items.length != 0){
			dropDown.select(0);
			/*Notify our selection listener, so the browser tree can update*/
	    	dropDown.notifyListeners(SWT.Selection, new Event());
		}
	}


	@Override
	public void notifyModelChange() {
		
		if(isDisposed()){
			log.log(Level.FINE,"Widget is disposed");
			ModelUpdater.removeObserver(this, ObserverType.MODEL);
			return;
		}
		
		//We have to run this as an asyncExec, so the SWT UI thread can do the update.
		//We cannot do the update in this thread, since that breaks the SWT thread model
		//(only SWT UI thread is allowed to make UI changes)
		this.getDisplay().asyncExec(new Runnable() {
		    public void run() {
		    	log.log(Level.FINE,"Updating DropDown");
		    	String names[] = (String[]) RWT.getApplicationContext().getAttribute(ModelUpdater.MODEL_NAMES_KEY);
		    	updateDropDown(names);
		    	getParent().layout();
		    }
		});
		
	}
}
