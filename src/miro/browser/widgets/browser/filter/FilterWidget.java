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
package miro.browser.widgets.browser.filter;

import java.util.ArrayList;
import java.util.List;

import miro.browser.resources.Fonts;
import miro.browser.widgets.browser.filter.filters.AttributeFilter;
import miro.browser.widgets.browser.filter.filters.FileTypeFilter;
import miro.browser.widgets.browser.filter.filters.FilterKeys;
import miro.browser.widgets.browser.filter.filters.ResourceCertificateTreeFilter;
import miro.browser.widgets.browser.filter.filters.ResourceHoldingObjectFilter;
import miro.browser.widgets.browser.filter.filters.ValidationStatusFilter;
import miro.browser.widgets.browser.filter.filters.FilterKeys.FilterKey;
import miro.browser.widgets.browser.tree.TreeContainer;
import net.ripe.rpki.commons.validation.ValidationStatus;

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
	
	private ValidationStatusButtonContainer validationStatusButtons;
	
	FilterSearchField searchField;
	Button applyFilterBtn;
	
	TreeContainer treeContainer;

	public FilterWidget(Composite parent, int style, TreeContainer treeCont) {
		super(parent, style);
		treeContainer = treeCont;
		init();
		initHeader();
		initChooseFilter();
		initAttributeButtonContainer();
		initSearchField();
		initFileTypeButtonContainer();
		initValidationStatusButtonContainer();
		initApplyButton();
	}
	
	private void initApplyButton() {
		applyFilterBtn = new Button(this, SWT.PUSH);
		applyFilterBtn.setText("Apply Filter");
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(validationStatusButtons,20);
		layoutData.right = new FormAttachment(100,0);
		
		applyFilterBtn.setLayoutData(layoutData);
		applyFilterBtn.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				TreeViewer treeViewer = treeContainer.getTreeBrowser().getTreeViewer();
				ResourceCertificateTreeFilter treeFilter = new ResourceCertificateTreeFilter();
				
				ResourceHoldingObjectFilter f = getAttributeFilter();
				if(f != null){
					treeFilter.addFilter((ResourceHoldingObjectFilter) f);
				}
				
				f = getFileTypeFilter();
				if(f != null){
					treeFilter.addFilter((ResourceHoldingObjectFilter) f);
				}
			
				f = getValidationStatusFilter();
				if(f != null){
					treeFilter.addFilter((ResourceHoldingObjectFilter) f);
				}
			
				// set new filter (this removes all old filters), refilter and
				// resort
				treeViewer.setFilters(new ViewerFilter[]{treeFilter});
				treeContainer.toggle();
			}
		});
		
		
	}
	
	public ValidationStatusFilter getValidationStatusFilter() {
		Button[] selected = validationStatusButtons.getSelectedButtons();
		List<ValidationStatus> stats = new ArrayList<ValidationStatus>();
		
		ValidationStatus status;
		FilterKey key;
		for(Button btn : selected) {
			key = (FilterKey) btn.getData(FilterKeys.FILTER_TYPE_KEY);
			switch(key){
			case PASSED_STATUS:
				stats.add(ValidationStatus.PASSED);
				break;
			case ERROR_STATUS:
				stats.add(ValidationStatus.ERROR);
				break;
			case WARNING_STATUS:
				stats.add(ValidationStatus.WARNING);
				break;
			default:
				break;
			}
		}
		
		if(!stats.isEmpty()){
			return new ValidationStatusFilter(stats);
		} else {
			return null;
		}
	}
	
	public FileTypeFilter getFileTypeFilter() {
		Button selectedFileTypeButton = filetypeButtons.getSelectedButtons()[0];
		if (selectedFileTypeButton != null) {
			return new FileTypeFilter((FilterKey) selectedFileTypeButton .getData(FilterKeys.FILTER_TYPE_KEY));
		}
		return null;
	}
	
	public AttributeFilter getAttributeFilter() {
		Button selectedAttributeButton = attributeButtons.getSelectedButtons()[0];
		String searchText = searchField.getSearchText().getText();
		if (selectedAttributeButton != null) {
			return new AttributeFilter(searchText,(FilterKey) selectedAttributeButton.getData(FilterKeys.FILTER_TYPE_KEY));
		}
		return null;
	}
	
	public void clearSelection(){
		attributeButtons.clearSelection();
		searchField.clearSelection();
		filetypeButtons.clearSelection();
		validationStatusButtons.clearSelection();
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

	private void initChooseFilter() {
		chooseFilter = new Label(this, SWT.NONE);
		chooseFilter.setText("Choose filter attribute:");
		chooseFilter.setFont(Fonts.SMALL_HEADER_FONT);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(header,15);
		layoutData.left = new FormAttachment(0,0);
		chooseFilter.setLayoutData(layoutData);
		
	}
	
	private void initAttributeButtonContainer() {
		
		attributeButtons = new AttributeButtonContainer(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(chooseFilter);
		layoutData.left = new FormAttachment(0,0);
		
		attributeButtons.setLayoutData(layoutData);
		
	}

	private void initSearchField() {
		searchField = new FilterSearchField(this, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(attributeButtons,20);
		layoutData.left = new FormAttachment(0,0);
		layoutData.right = new FormAttachment(100,0);
		
		searchField.setLayoutData(layoutData);

	}

	private void initFileTypeButtonContainer() {
		filetypeButtons = new FileTypeButtonContainer(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(searchField);
		layoutData.left = new FormAttachment(0,0);
		
		filetypeButtons.setLayoutData(layoutData);
	}
	
	private void initValidationStatusButtonContainer() {
		validationStatusButtons = new ValidationStatusButtonContainer(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(filetypeButtons);
		layoutData.left = new FormAttachment(0,0); 
		
		validationStatusButtons.setLayoutData(layoutData);
	}
	
	private void init() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		setLayout(layout);
	}

	

}
