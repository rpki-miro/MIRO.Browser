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
import miro.browser.widgets.browser.filter.filters.ResourceCertificateTreeFilter;
import miro.browser.widgets.browser.filter.filters.ResourceHoldingObjectFilter;
import miro.browser.widgets.browser.views.ViewManager;

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

public class FilterWidget extends Composite{

	private AttributeFilterOption attributeOption;
	
	private FileTypeFilterOption filetypeOption;
	
	private ValidationStatusFilterOption validationStatusOption;
	
	private ViewManager viewerContainer;

	public FilterWidget(Composite parent, int style, ViewManager treeCont) {
		super(parent, style);
		viewerContainer = treeCont;
		createWidgets();
		createLayout();
	}
	
	private void createLayout() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		setLayout(layout);
		
		Label header = new Label(this, SWT.NONE);
		header.setText("Filter Options");
		header.setFont(Fonts.MEDIUM_HEADER_FONT);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0,0);
		layoutData.left = new FormAttachment(0,0);
		header.setLayoutData(layoutData);
		
		/*Attribute options*/
		Label attributeLabel = new Label(this, SWT.NONE);
		attributeLabel.setText("Choose filter attribute:");
		attributeLabel.setFont(Fonts.SMALL_HEADER_FONT);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(header,15);
		layoutData.left = new FormAttachment(0,0);
		attributeLabel.setLayoutData(layoutData);
		
		layoutData = new FormData();
		layoutData.top = new FormAttachment(attributeLabel);
		layoutData.left = new FormAttachment(0,0);
		attributeOption.setLayoutData(layoutData);
		
		/*Filetype Options*/
		Label filetypeLabel = new Label(this, SWT.NONE);
		filetypeLabel.setText("Select File Type:");
		filetypeLabel.setFont(Fonts.SMALL_HEADER_FONT);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(attributeOption,15);
		layoutData.left = new FormAttachment(0,0);
		filetypeLabel.setLayoutData(layoutData);
		
		layoutData = new FormData();
		layoutData.top = new FormAttachment(filetypeLabel);
		layoutData.left = new FormAttachment(0,0);
		filetypeOption.setLayoutData(layoutData);
	
		/*Validation Status Options*/
		Label validationStatusLabel = new Label(this, SWT.NONE);
		validationStatusLabel.setText("Select Validation Status:");
		validationStatusLabel.setFont(Fonts.SMALL_HEADER_FONT);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(filetypeOption,15);
		layoutData.left = new FormAttachment(0,0);
		validationStatusLabel.setLayoutData(layoutData);

		layoutData = new FormData();
		layoutData.top = new FormAttachment(validationStatusLabel);
		layoutData.left = new FormAttachment(0,0); 
		validationStatusOption.setLayoutData(layoutData);
		
		/*Buttons*/
		Button applyFilterBtn = new Button(this, SWT.PUSH);
		applyFilterBtn.setText("Apply Filter");
		layoutData = new FormData();
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.right = new FormAttachment(100,0);
		applyFilterBtn.setLayoutData(layoutData);
		applyFilterBtn.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				List<ResourceHoldingObjectFilter> filters = new ArrayList<ResourceHoldingObjectFilter>();
				filters.addAll(attributeOption.getFilters());
				filters.addAll(filetypeOption.getFilters());
				filters.addAll(validationStatusOption.getFilters());
				
				ResourceCertificateTreeFilter treeFilter = new ResourceCertificateTreeFilter(false);
				treeFilter.addFilters(filters);
				viewerContainer.setViewerFilters(new ViewerFilter[]{treeFilter});
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

	private void createWidgets() {
		filetypeOption = new FileTypeFilterOption(this, SWT.NONE);
		attributeOption = new AttributeFilterOption(this, SWT.NONE);
		validationStatusOption = new ValidationStatusFilterOption(this, SWT.NONE);
	}

	public void clearSelection(){
		attributeOption.clearSelection();
		filetypeOption.clearSelection();
		validationStatusOption.clearSelection();
		viewerContainer.resetViewerFilters();
	}
}
