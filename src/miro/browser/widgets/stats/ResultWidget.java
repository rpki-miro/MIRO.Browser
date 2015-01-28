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
package miro.browser.widgets.stats;

import java.util.List;

import miro.browser.resources.Colors;
import miro.browser.resources.Fonts;
import miro.validator.stats.types.Result;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rap.addons.d3chart.ChartItem;
import org.eclipse.rap.addons.d3chart.PieChart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;

public class ResultWidget extends Composite {
	
	private Label descriptor;

	private TreeViewer validationStatusViewer;
	
	private TreeViewer objectTypeTreeViewer;
	
	private PieChart validationStatusChart;
	
	private PieChart objectBreakDownChart;
	
	public ResultWidget(Composite parent, int style) {
		super(parent, style);
		
		initLayout();
		
		initDescriptor();
		
		initValidationStatusViewer();
		
		initValidationStatusChart();
		
		initObjectBreakdownViewer();
		
		initObjectBreakdownChart();
		
		
	}

	private void initDescriptor() {
		descriptor = new Label(this, SWT.NONE);
		descriptor.setFont(Fonts.SMALL_HEADER_FONT);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = SWT.BEGINNING;
		descriptor.setLayoutData(gridData);
		
	}

	public void initLayout() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		setLayout(layout);
		
	}

	public void initValidationStatusViewer() {
		final Tree validityTree = new Tree(this, SWT.FULL_SELECTION | SWT.BORDER);
		validationStatusViewer = new TreeViewer(validityTree);
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = SWT.FILL;
		validityTree.setLayoutData(gridData);
		
		validityTree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				StatsTreeItem selected = (StatsTreeItem) e.item.getData();
				
				objectTypeTreeViewer.setInput(StatsTreeItem.getFakeRoot(selected));
				objectTypeTreeViewer.expandAll();
				objectTypeTreeViewer.refresh();
				setObjectBreakdownItems(selected.getObjectTypeBreakdown());
			}

		});
		
		TreeViewerColumn colorColumn = new TreeViewerColumn(validationStatusViewer, SWT.BORDER);
		colorColumn.getColumn().setWidth(30);
		colorColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				StatsTreeItem item = (StatsTreeItem) cell.getElement();
				if(item.getKey().endsWith("valid.objects")){
					cell.setBackground(Colors.VALID_OBJECT_COLOR);
				}
				if(item.getKey().endsWith("invalid.objects")){
					cell.setBackground(Colors.INVALID_OBJECT_COLOR);
				}
			}
		});
		
		TreeViewerColumn textColumn = new TreeViewerColumn(validationStatusViewer, SWT.BORDER);
		textColumn.getColumn().setWidth(250);
		textColumn.setLabelProvider(new KeyColumnLabelProvider());
		
		TreeViewerColumn amountColumn = new TreeViewerColumn(validationStatusViewer, SWT.BORDER);
		amountColumn.getColumn().setWidth(200);
		amountColumn.setLabelProvider(new AmountColumnLabelProvider());
		
		TreeViewerColumn percentColumn = new TreeViewerColumn(validationStatusViewer, SWT.BORDER);
		percentColumn.getColumn().setWidth(100);
		percentColumn.setLabelProvider(new PercentageColumnLabelProvider());
		
		validationStatusViewer.setContentProvider(new StatsTreeContentProvider(StatsViewerType.VALIDITY));
		validationStatusViewer.setLabelProvider(new StatsTreeLabelProvider(StatsViewerType.VALIDITY));
	}
	
	private void initObjectBreakdownViewer() {
		Tree objectTree = new Tree(this,SWT.BORDER);
		objectTypeTreeViewer = new TreeViewer(objectTree);
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = SWT.FILL;
		objectTree.setLayoutData(gridData);
		
		TreeViewerColumn colorColumn = new TreeViewerColumn(objectTypeTreeViewer, SWT.BORDER);
		colorColumn.getColumn().setWidth(30);
		colorColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				StatsTreeItem item = (StatsTreeItem) cell.getElement();
				if(item.getKey().endsWith(".cer.objects")){
					cell.setBackground(Colors.CER_OBJECT_COLOR);
				}
				if(item.getKey().endsWith(".mft.objects")){
					cell.setBackground(Colors.MFT_OBJECT_COLOR);
				}
				if(item.getKey().endsWith(".crl.objects")){
					cell.setBackground(Colors.CRL_OBJECT_COLOR);
				}
				if(item.getKey().endsWith(".roa.objects")){
					cell.setBackground(Colors.ROA_OBJECT_COLOR);
				}
				
			}
		});
		
		TreeViewerColumn textColumn = new TreeViewerColumn(objectTypeTreeViewer, SWT.NONE);
		textColumn.getColumn().setWidth(250);
		textColumn.setLabelProvider(new KeyColumnLabelProvider());
		
		TreeViewerColumn amountColumn = new TreeViewerColumn(objectTypeTreeViewer, SWT.NONE);
		amountColumn.getColumn().setWidth(200);
		amountColumn.setLabelProvider(new AmountColumnLabelProvider());
		
		TreeViewerColumn percentColumn = new TreeViewerColumn(objectTypeTreeViewer, SWT.NONE);
		percentColumn.getColumn().setWidth(100);
		percentColumn.setLabelProvider(new PercentageColumnLabelProvider());
		
		objectTypeTreeViewer.setContentProvider(new StatsTreeContentProvider(StatsViewerType.OBJECT_TYPE));
		objectTypeTreeViewer.setLabelProvider(new StatsTreeLabelProvider(StatsViewerType.OBJECT_TYPE));
	}
	
	private void setObjectBreakdownItems(List<StatsTreeItem> items) {
		
		for(ChartItem item : objectBreakDownChart.getItems()){
			item.dispose();
		}
		
		ChartItem cItem;
		for(StatsTreeItem item : items){
			if(item.getFraction() == 0){
				continue;
			}
			
			cItem = new ChartItem(objectBreakDownChart);
			cItem.setValue(item.getFraction());
			if(item.getKey().endsWith(".cer.objects")){
				cItem.setColor(Colors.CER_OBJECT_COLOR);
				cItem.setText(".cer");
			}
			if(item.getKey().endsWith(".mft.objects")){
				cItem.setColor(Colors.MFT_OBJECT_COLOR);
				cItem.setText(".mft");
			}
			if(item.getKey().endsWith(".crl.objects")){
				cItem.setColor(Colors.CRL_OBJECT_COLOR);
				cItem.setText(".crl");
			}
			if(item.getKey().endsWith(".roa.objects")){
				cItem.setText(".roa");
				cItem.setColor(Colors.ROA_OBJECT_COLOR);
			}
		}
	}

	private void initObjectBreakdownChart() {
		objectBreakDownChart = new PieChart( this, SWT.BORDER );
		GridData gridData = new GridData(400,300);
		objectBreakDownChart.setLayoutData(gridData);
	}

	private void initValidationStatusChart() {
		validationStatusChart = new PieChart( this, SWT.BORDER );
		GridData gridData = new GridData(400,300);
//		gridData.verticalSpan = 2;
		validationStatusChart.setLayoutData(gridData);
	}
	
	public void showResult(Result result) {
		StatsTreeItem root = StatsTreeItem.toStatsTreeItem(result);
		validationStatusViewer.setInput(StatsTreeItem.getFakeRoot(root));
		validationStatusViewer.expandAll();
		setValidationStatusItems(root.getStatusBreakdown());
		
		objectTypeTreeViewer.setInput(StatsTreeItem.getFakeRoot(root));
		objectTypeTreeViewer.expandAll();
		setObjectBreakdownItems(root.getObjectTypeBreakdown());
		
		
		descriptor.setText(result.getDescriptor());
		
		layout();
	}

	private void setValidationStatusItems(List<StatsTreeItem> items) {
		for(ChartItem item : validationStatusChart.getItems()){
			item.dispose();
		}
		ChartItem cItem;
		for(StatsTreeItem item : items){
			if(item.getFraction() == 0){
				continue;
			}
			cItem = new ChartItem(validationStatusChart);
			cItem.setValue(item.getFraction());
			String percentage = Integer.toString((int) (item.getFraction()*100)) + "%";
			cItem.setText(percentage);
			
			if(item.getKey().equals("invalid.objects")){
				cItem.setColor(Colors.INVALID_OBJECT_COLOR);
			}
			
			if(item.getKey().equals("valid.objects")){
				cItem.setColor(Colors.VALID_OBJECT_COLOR);
			}
		}
	}
	
	private class KeyColumnLabelProvider extends CellLabelProvider{
		@Override
		public void update(ViewerCell cell) {
			StatsTreeItem item = (StatsTreeItem) cell.getElement();
			String result = item.getKey();
			cell.setText(item.getKey());
		}
	}
	
	private class AmountColumnLabelProvider extends CellLabelProvider{
		@Override
		public void update(ViewerCell cell) {
			StatsTreeItem item = (StatsTreeItem) cell.getElement();
			cell.setText(Integer.toString(item.getAmount()));
		}
	}
	
	private class PercentageColumnLabelProvider extends CellLabelProvider{
		@Override
		public void update(ViewerCell cell) {
			StatsTreeItem item = (StatsTreeItem) cell.getElement();
			cell.setText(Float.toString(item.getFraction() * 100));
		}
	}

}
