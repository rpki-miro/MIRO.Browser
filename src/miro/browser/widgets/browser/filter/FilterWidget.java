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

import miro.browser.widgets.browser.filter.filters.ResourceCertificateTreeFilter;
import miro.browser.widgets.browser.filter.filters.ResourceHoldingObjectFilter;
import miro.browser.widgets.browser.views.RepositoryViewContainer;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
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
	
	private RepositoryViewContainer viewerContainer;

	public FilterWidget(Composite parent, int style, RepositoryViewContainer treeCont) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "filterWidget");
		viewerContainer = treeCont;
		createWidgets();
		createLayout();
	}
	
	private void createLayout() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		setLayout(layout);
		
		FormData layoutData;
		
		/*Attribute options*/
		Composite labelHolder = new Composite(this, SWT.NONE);
		labelHolder.setData(RWT.CUSTOM_VARIANT, "filterHeadingHolder");
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 5;
		labelHolder.setLayout(fillLayout);
		Label attributeLabel = new Label(labelHolder, SWT.NONE);
		attributeLabel.setData(RWT.CUSTOM_VARIANT, "filterHeading");
		attributeLabel.setText("Choose filter attribute:");
		layoutData = new FormData();
		layoutData.top = new FormAttachment(0,0);
		layoutData.left = new FormAttachment(0,0);
		layoutData.right = new FormAttachment(100,0);
		labelHolder.setLayoutData(layoutData);
		
		layoutData = new FormData();
		layoutData.top = new FormAttachment(attributeLabel.getParent());
		layoutData.left = new FormAttachment(0,0);
		layoutData.right = new FormAttachment(100,0);
		attributeOption.setLayoutData(layoutData);
		
		/*Filetype Options*/
		labelHolder = new Composite(this, SWT.NONE);
		labelHolder.setData(RWT.CUSTOM_VARIANT, "filterHeadingHolder");
		fillLayout = new FillLayout();
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 5;
		labelHolder.setLayout(fillLayout);
		Label filetypeLabel = new Label(labelHolder, SWT.NONE);
		filetypeLabel.setData(RWT.CUSTOM_VARIANT, "filterHeading");
		filetypeLabel.setText("Select file type:");
		layoutData = new FormData();
		layoutData.top = new FormAttachment(attributeOption,15);
		layoutData.left = new FormAttachment(0,0);
		layoutData.right = new FormAttachment(100,0);
		labelHolder.setLayoutData(layoutData);
		
		layoutData = new FormData();
		layoutData.top = new FormAttachment(filetypeLabel.getParent());
		layoutData.left = new FormAttachment(0,0);
		layoutData.right = new FormAttachment(100,0);
		filetypeOption.setLayoutData(layoutData);
	
		/*Validation Status Options*/
		labelHolder = new Composite(this, SWT.NONE);
		labelHolder.setData(RWT.CUSTOM_VARIANT, "filterHeadingHolder");
		fillLayout = new FillLayout();
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 5;
		labelHolder.setLayout(fillLayout);
		Label validationStatusLabel = new Label(labelHolder, SWT.NONE);
		validationStatusLabel.setData(RWT.CUSTOM_VARIANT, "filterHeading");
		validationStatusLabel.setText("Select validation status:");
		layoutData = new FormData();
		layoutData.top = new FormAttachment(filetypeOption,15);
		layoutData.left = new FormAttachment(0,0);
		layoutData.right = new FormAttachment(100,0);
		labelHolder.setLayoutData(layoutData);

		layoutData = new FormData();
		layoutData.top = new FormAttachment(validationStatusLabel.getParent());
		layoutData.left = new FormAttachment(0,0); 
		layoutData.right = new FormAttachment(100,0);
		validationStatusOption.setLayoutData(layoutData);
		
		/*Buttons*/
		Button okButton = new Button(this,SWT.PUSH);
		okButton.setData(RWT.CUSTOM_VARIANT, "filterButton");
		okButton.setText("OK");
		layoutData = new FormData();
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.right = new FormAttachment(100,0);
		okButton.setLayoutData(layoutData);
		okButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				applyAndClose();
			}
		});
		
		Button applyFilterBtn = new Button(this, SWT.PUSH);
		applyFilterBtn.setData(RWT.CUSTOM_VARIANT, "filterButton");
		applyFilterBtn.setText("Apply Filter");
		layoutData = new FormData();
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.right = new FormAttachment(okButton,-10);
		applyFilterBtn.setLayoutData(layoutData);
		applyFilterBtn.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				applyFilter();
			}
		});
		
		Button clearFilter = new Button(this, SWT.PUSH);
		clearFilter.setData(RWT.CUSTOM_VARIANT, "filterButton");
		clearFilter.setText("Clear");
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0,0);
		layoutData.bottom = new FormAttachment(100,0);
		clearFilter.setLayoutData(layoutData);
		clearFilter.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				clearSelection();
			}
		});

		
		/*Key listener for convenience*/
		getShell().getDisplay().setData(RWT.ACTIVE_KEYS, new String[]{"ENTER"});
		getShell().getDisplay().addFilter(SWT.KeyDown, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				applyAndClose();
			}
		});
	}
	
	private void applyAndClose(){
		applyFilter();
		getShell().setVisible(false);
	}
	
	private void applyFilter(){
		List<ResourceHoldingObjectFilter> filters = new ArrayList<ResourceHoldingObjectFilter>();
		filters.addAll(attributeOption.getFilters());
		filters.addAll(filetypeOption.getFilters());
		filters.addAll(validationStatusOption.getFilters());

		ResourceCertificateTreeFilter treeFilter = new ResourceCertificateTreeFilter(
				false);
		treeFilter.addFilters(filters);
		viewerContainer.setViewerFilters(new ViewerFilter[] { treeFilter });
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
