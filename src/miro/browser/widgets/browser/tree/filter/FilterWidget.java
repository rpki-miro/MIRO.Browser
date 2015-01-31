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

import java.util.ArrayList;
import java.util.List;

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
	private AttributeButtonContainer attributeButtons;
	
	private FileTypeButtonContainer filetypeButtons;
	
	FilterSearchField searchField;
	Button applyFilterBtn;
	
	TreeContainer treeContainer;

	public FilterWidget(Composite parent, int style, TreeContainer treeCont) {
		super(parent, style);
		treeContainer = treeCont;
		init();
		initHeader();
		initChooseFilter();
		initRadioButtonContainer();
		initSearchField();
		initCheckButtonContainer();
		initApplyButton();
	}
	
	private void initApplyButton() {
		applyFilterBtn = new Button(this, SWT.PUSH);
		applyFilterBtn.setText("Apply Filter");
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(filetypeButtons,20);
		layoutData.right = new FormAttachment(100,0);
		
		applyFilterBtn.setLayoutData(layoutData);
		applyFilterBtn.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				List<ViewerFilter> filters = new ArrayList<ViewerFilter>();
				TreeViewer treeViewer = treeContainer.getTreeBrowser().getTreeViewer();

				Button selectedAttributeButton = attributeButtons.getSelected();
				String searchText = searchField.getSearchText().getText();
				if(selectedAttributeButton != null){
					filters.add(getAttributeFilter(selectedAttributeButton, searchText));
				}

				
				Button selectedFileTypeButton = filetypeButtons.getSelected();
				if(selectedFileTypeButton != null) {
					filters.add(getFileTypeFilter(selectedFileTypeButton));
				}
				
				
				// set new filter (this removes all old filters), refilter and
				// resort
				treeViewer.setFilters(filters.toArray(new ViewerFilter[]{}));

				treeContainer.toggle();
			}
		});
		
		
	}
	
	
	public FileTypeFilter getFileTypeFilter(Button selectedBtn) {
		return new FileTypeFilter((FilterAttribute) selectedBtn.getData(RadioButtonContainer.FILTER_TYPE_KEY));
	}
	
	public AttributeFilter getAttributeFilter(Button selectedBtn, String searchText) {
		AttributeFilter filter = new AttributeFilter(searchText, (FilterAttribute) selectedBtn.getData(RadioButtonContainer.FILTER_TYPE_KEY));
		return filter;
	}
	
	public void clearSelection(){
		attributeButtons.clearSelection();
		searchField.clearSelection();
		filetypeButtons.clearSelection();
	}
	

	private void initSearchField() {
		searchField = new FilterSearchField(this, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(attributeButtons,20);
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
	
	private void initCheckButtonContainer() {
		filetypeButtons = new FileTypeButtonContainer(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(searchField);
		layoutData.left = new FormAttachment(0,0);
		
		filetypeButtons.setLayoutData(layoutData);
	}

	private void initRadioButtonContainer() {
		attributeButtons = new AttributeButtonContainer(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(chooseFilter);
		layoutData.left = new FormAttachment(0,0);
		
		attributeButtons.setLayoutData(layoutData);
		
	}

	private void init() {
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
