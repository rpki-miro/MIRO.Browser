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
package miro.browser.widgets.browser.tree;

import java.lang.reflect.Method;

import miro.browser.provider.CertificateTableContentProvider;
import miro.browser.provider.CertificateTreeLabelProvider;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class TableBrowser extends Composite implements ViewerContainer{
	
	private TableViewer tableViewer;
	
	private Table table;

	public TableBrowser(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		tableViewer = new TableViewer(this,SWT.VIRTUAL);
		tableViewer.setLabelProvider(new CertificateTreeLabelProvider());
		tableViewer.setContentProvider(new CertificateTableContentProvider());
		table = tableViewer.getTable();
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public Table getTable() {
		return table;
	}

	public void setSelection(ResourceHoldingObject obj) {
		Method findItem;
		try {
			findItem = TableViewer.class.getDeclaredMethod("doFindItem", Object.class);
			findItem.setAccessible(true);
			TableItem item = (TableItem) findItem.invoke(tableViewer, obj);
			if(item == null){
				return;
			}
			tableViewer.getTable().setSelection(item);
			Event ev = new Event();
			ev.item = item;
			tableViewer.getTable().notifyListeners(SWT.Selection, ev);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public ResourceHoldingObject getSelection() {
		if(table.getSelectionCount() > 0) {
			TableItem item = table.getSelection()[0];
			return (ResourceHoldingObject) item.getData();
		}
		return null;
	}
	

	
	

}