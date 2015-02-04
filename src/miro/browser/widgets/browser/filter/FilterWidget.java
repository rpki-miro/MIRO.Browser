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

	private Label header;

	private Label attributeLabel;
	private AttributeButtonContainer attributeButtons;
	
	private Label filetypeLabel;
	private FileTypeButtonContainer filetypeButtons;
	
	private Label validationStatusLabel;
	private ValidationStatusButtonContainer validationStatusButtons;
	
	private SigningTimeButtonContainer signingTimeContainer; 

	private Button applyFilterBtn;
	
	private TreeContainer treeContainer;

	public FilterWidget(Composite parent, int style, TreeContainer treeCont) {
		super(parent, style);
		treeContainer = treeCont;
		init();
		initHeader();

		initAttributeLabel();
		initAttributeButtonContainer();

		initFileTypeLabel();
		initFileTypeButtonContainer();
		
		initValidationStatusLabel();
		initValidationStatusButtonContainer();
		
		initSigningTimeContainer();
		
		initApplyButton();
		

	}
	
	private void initSigningTimeContainer() {
		signingTimeContainer = new SigningTimeButtonContainer(this, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(validationStatusButtons);
		layoutData.left = new FormAttachment(0, 0);
		signingTimeContainer.setLayoutData(layoutData);
		
	}

	private void initValidationStatusLabel() {
		validationStatusLabel = new Label(this, SWT.NONE);
		validationStatusLabel.setText("Select Validation Status:");
		validationStatusLabel.setFont(Fonts.SMALL_HEADER_FONT);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(filetypeButtons,15);
		layoutData.left = new FormAttachment(0,0);
		validationStatusLabel.setLayoutData(layoutData);
		
	}

	private void initFileTypeLabel() {
		filetypeLabel = new Label(this, SWT.NONE);
		filetypeLabel.setText("Select File Type:");
		filetypeLabel.setFont(Fonts.SMALL_HEADER_FONT);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(attributeButtons,15);
		layoutData.left = new FormAttachment(0,0);
		filetypeLabel.setLayoutData(layoutData);
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
				
				treeFilter.addFilters(attributeButtons.getFilters());
				treeFilter.addFilters(filetypeButtons.getFilters());
				treeFilter.addFilters(validationStatusButtons.getFilters());
			
				// set new filter (this removes all old filters), refilter and
				// resort
				treeViewer.setFilters(new ViewerFilter[]{treeFilter});
				treeContainer.toggle();
			}
		});
	}

	public void clearSelection(){
		attributeButtons.clearSelection();
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

	private void initAttributeLabel() {
		attributeLabel = new Label(this, SWT.NONE);
		attributeLabel.setText("Choose filter attribute:");
		attributeLabel.setFont(Fonts.SMALL_HEADER_FONT);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(header,15);
		layoutData.left = new FormAttachment(0,0);
		attributeLabel.setLayoutData(layoutData);
		
	}
	
	private void initAttributeButtonContainer() {
		
		attributeButtons = new AttributeButtonContainer(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(attributeLabel);
		layoutData.left = new FormAttachment(0,0);
		
		attributeButtons.setLayoutData(layoutData);
		
	}

	private void initFileTypeButtonContainer() {
		filetypeButtons = new FileTypeButtonContainer(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(filetypeLabel);
		layoutData.left = new FormAttachment(0,0);
		
		filetypeButtons.setLayoutData(layoutData);
	}
	
	private void initValidationStatusButtonContainer() {
		validationStatusButtons = new ValidationStatusButtonContainer(this, SWT.NONE);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(validationStatusLabel);
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
