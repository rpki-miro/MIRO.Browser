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
package miro.browser.widgets.browser.tree.filter;

import miro.browser.resources.Fonts;
import miro.browser.widgets.browser.tree.TreeContainer;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;




public class FilterWidget extends Composite {

	Label header;
	Label chooseFilter;
	private RadioButtonContainer radioButtons;
	FilterSearchField searchField;
	Button applyFilterBtn;
	
	TreeContainer treeContainer;

	public FilterWidget(Composite parent, int style, TreeContainer treeCont) {
		super(parent, style);
		treeContainer = treeCont;
		init();
		initHeader();
		initChooseFilter();
		initButtonContainer();
		initSearchField();
		initApplyButton();
	}
	
	private void initApplyButton() {
		applyFilterBtn = new Button(this, SWT.PUSH);
		applyFilterBtn.setText("Apply Filter");
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(searchField,20);
		layoutData.right = new FormAttachment(100,0);
		
		applyFilterBtn.setLayoutData(layoutData);
		
		
		
		applyFilterBtn.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				
				TreeViewer treeViewer = treeContainer.getTreeBrowser().getTreeViewer();
				Button selected = radioButtons.getSelected();
				if(selected == null){
					treeViewer.resetFilters();
					treeContainer.toggle();
					return;
				}
				
				String btnText = selected.getText();
				String searchText = searchField.getSearchText().getText();
				
				TreeSearchBarFilter filter;
				
				switch (btnText) {
					case "Filename":
						filter = new RadioButtonFilter(searchText, FilterAttribute.FILENAME);
						break;
					
					case "Subject":
						filter = new RadioButtonFilter(searchText, FilterAttribute.SUBJECT);
						break;
						
					case "Issuer":
						filter = new RadioButtonFilter(searchText, FilterAttribute.ISSUER);
						break;
						
					case "Serial Nr.":
						filter = new RadioButtonFilter(searchText, FilterAttribute.SERIAL_NUMBER);
						break;
						
					case "Location":
						filter = new RadioButtonFilter(searchText, FilterAttribute.REMOTE_LOCATION);
						break;

					case "Resource":
						filter = new RadioButtonFilter(searchText, FilterAttribute.RESOURCE);
						break;
						
					default:
						filter = null;
						break;
				}
				
				//If null, thats bad and should not happen. So reset
				if(filter == null){
					treeViewer.resetFilters();
					//temp log, need real logger
					System.out.println("FilterWidget: Null filter selected");
				} else {
					
					//set new filter (this removes all old filters), refilter and resort
					ViewerFilter[] filters = {filter};
					treeViewer.setFilters(filters);
				}
				
				treeContainer.toggle();
			}
		});
		
		
	}
	
	public void clearSelection(){
		radioButtons.clearSelection();
		searchField.clearSelection();
	}
	

	private void initSearchField() {
		searchField = new FilterSearchField(this, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(radioButtons,20);
		layoutData.left = new FormAttachment(0,0);
		layoutData.right = new FormAttachment(100,0);
		
		searchField.setLayoutData(layoutData);

	}

	private void initChooseFilter() {
		chooseFilter = new Label(this, SWT.NONE);
		chooseFilter.setText("Choose filter attribute:");
		chooseFilter.setFont(Fonts.SMALL_HEADER_FONT);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(header,15);
		layoutData.left = new FormAttachment(0,0);
		chooseFilter.setLayoutData(layoutData);
		
	}

	private void initButtonContainer() {
		radioButtons = new RadioButtonContainer(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(chooseFilter);
		layoutData.left = new FormAttachment(0,0);
		
		radioButtons.setLayoutData(layoutData);
		
	}

	private void init() {
//		RowLayout layout = new RowLayout();
//		layout.type = SWT.VERTICAL;
//		layout.fill = true;
		
		FormLayout layout = new FormLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		setLayout(layout);
	}

	
	private void initHeader() {
		header = new Label(this, SWT.NONE);
		header.setText("Filter Options");
		header.setFont(Fonts.MEDIUM_HEADER_FONT);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0,0);
		layoutData.left = new FormAttachment(0,0);
		
		header.setLayoutData(layoutData);
		
		
	}

}
