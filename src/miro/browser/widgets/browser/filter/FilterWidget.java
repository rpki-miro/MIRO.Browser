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

import miro.browser.provider.CertificateTableLabelProvider;
import miro.browser.provider.CertificateTreeLabelProvider;
import miro.browser.resources.Fonts;
import miro.browser.widgets.browser.filter.filters.ResourceCertificateTreeFilter;
import miro.browser.widgets.browser.filter.filters.ResourceHoldingObjectFilter;
import miro.browser.widgets.browser.tree.ViewerManager;

import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.swt.widgets.Shell;

public class FilterWidget extends Composite{

	private Label header;

	private Label attributeLabel;
	private AttributeFilterOption attributeOption;
	
	private Label filetypeLabel;
	private FileTypeFilterOption filetypeOption;
	
	private Label validationStatusLabel;
	private ValidationStatusFilterOption validationStatusOption;
	
	private Button applyFilterBtn;
	
	private ViewerManager viewerContainer;

	public FilterWidget(Composite parent, int style, ViewerManager treeCont) {
		super(parent, style);
		viewerContainer = treeCont;
		init();
		initHeader();

		initAttributeLabel();
		initAttributeOption();

		initFileTypeLabel();
		initFileTypeOption();
		
		initValidationStatusLabel();
		initValidationStatusOption();
		
		initApplyButton();
	}
	
	private void initValidationStatusLabel() {
		validationStatusLabel = new Label(this, SWT.NONE);
		validationStatusLabel.setText("Select Validation Status:");
		validationStatusLabel.setFont(Fonts.SMALL_HEADER_FONT);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(filetypeOption,15);
		layoutData.left = new FormAttachment(0,0);
		validationStatusLabel.setLayoutData(layoutData);
		
	}

	private void initFileTypeLabel() {
		filetypeLabel = new Label(this, SWT.NONE);
		filetypeLabel.setText("Select File Type:");
		filetypeLabel.setFont(Fonts.SMALL_HEADER_FONT);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(attributeOption,15);
		layoutData.left = new FormAttachment(0,0);
		filetypeLabel.setLayoutData(layoutData);
	}

	private void initApplyButton() {
		applyFilterBtn = new Button(this, SWT.PUSH);
		applyFilterBtn.setText("Apply Filter");
		
		FormData layoutData = new FormData();
//		layoutData.top = new FormAttachment(validationStatusOption,20);
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.right = new FormAttachment(100,0);
		
		applyFilterBtn.setLayoutData(layoutData);
		applyFilterBtn.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				TreeViewer treeViewer = viewerContainer.getTreeBrowser().getTreeViewer();
				TableViewer tableViewer = viewerContainer.getTableBrowser().getTableViewer();
				
				List<ResourceHoldingObjectFilter> filters = new ArrayList<ResourceHoldingObjectFilter>();
				filters.addAll(attributeOption.getFilters());
				filters.addAll(filetypeOption.getFilters());
				filters.addAll(validationStatusOption.getFilters());
				

				ResourceCertificateTreeFilter treeFilter = new ResourceCertificateTreeFilter(false);
				treeFilter.addFilters(filters);
				CertificateTreeLabelProvider treeLabelProvider = (CertificateTreeLabelProvider) treeViewer.getLabelProvider();
				treeLabelProvider.setFilter(treeFilter);
				treeViewer.setFilters(new ViewerFilter[]{treeFilter});

				
				ResourceCertificateTreeFilter tableFilter = new ResourceCertificateTreeFilter(true);
//				CertificateTreeLabelProvider tableLabelProvider = (CertificateTreeLabelProvider) tableViewer.getLabelProvider();
//				tableLabelProvider.setFilter(tableFilter);
				tableFilter.addFilters(filters);
				tableViewer.setFilters(new ViewerFilter[]{tableFilter});
			}
		});
		
		
		Button clearFilter = new Button(this, SWT.PUSH);
		clearFilter.setText("Clear");
		layoutData = new FormData();
		layoutData.right = new FormAttachment(applyFilterBtn, -10);
		layoutData.bottom = new FormAttachment(100,0);
		clearFilter.setLayoutData(layoutData);
		
		clearFilter.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				clearSelection();
			}
		});
	}

	public void clearSelection(){
		attributeOption.clearSelection();
		filetypeOption.clearSelection();
		validationStatusOption.clearSelection();
		
		TreeViewer treeViewer = viewerContainer.getTreeBrowser().getTreeViewer();
		CertificateTreeLabelProvider labelProvider = (CertificateTreeLabelProvider) treeViewer.getLabelProvider();
		labelProvider.setFilter(null);
		
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

	private void initAttributeLabel() {
		attributeLabel = new Label(this, SWT.NONE);
		attributeLabel.setText("Choose filter attribute:");
		attributeLabel.setFont(Fonts.SMALL_HEADER_FONT);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(header,15);
		layoutData.left = new FormAttachment(0,0);
		attributeLabel.setLayoutData(layoutData);
		
	}
	
	private void initAttributeOption() {
		
		attributeOption = new AttributeFilterOption(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(attributeLabel);
		layoutData.left = new FormAttachment(0,0);
		
		attributeOption.setLayoutData(layoutData);
		
	}

	private void initFileTypeOption() {
		filetypeOption = new FileTypeFilterOption(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(filetypeLabel);
		layoutData.left = new FormAttachment(0,0);
		
		filetypeOption.setLayoutData(layoutData);
	}
	
	private void initValidationStatusOption() {
		validationStatusOption = new ValidationStatusFilterOption(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(validationStatusLabel);
		layoutData.left = new FormAttachment(0,0); 
		
		validationStatusOption.setLayoutData(layoutData);
	}
	
	private void init() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		setLayout(layout);
	}

	

}