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
package miro.browser.widgets.browser.display;

import java.util.Iterator;

import miro.browser.resources.MagicNumbers;
import net.ripe.ipresource.IpResource;
import net.ripe.ipresource.IpResourceSet;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.google.common.collect.Iterables;
/**
 * Wrapper class for a TableViewer that shows a IpResourceSet
 * @author ponken
 *
 */
public class ResourceSetTable extends Composite{
	
	private TableViewer tableViewer;

	public ResourceSetTable(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		
		tableViewer = new TableViewer(this, SWT.V_SCROLL);
		tableViewer.setContentProvider(new ResourceContentProvider());
		createColumns(tableViewer.getTable());
	}
	
	private void createColumns(Table table) {
		table.setHeaderVisible(true);
		TableViewerColumn newCol;
		newCol = new TableViewerColumn(tableViewer, new TableColumn(table,SWT.NONE));
		newCol.getColumn().setWidth(MagicNumbers.CDW_RESOURCE_LIST_COLUMN_WIDTH-10);
		newCol.getColumn().setMoveable(false);
		newCol.getColumn().setResizable(false);
		newCol.getColumn().setText("Resources");
		newCol.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				IpResource res = ( IpResource )cell.getElement();
				cell.setText(res.toString());
			}
		});
	}
	
	public void setInput(IpResourceSet res) {
		tableViewer.setInput(res);
	}
	
	
private class ResourceContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		IpResourceSet resources = (IpResourceSet) inputElement;
		
		int size = Iterables.size(resources);
		Iterator<IpResource> iter = resources.iterator();
		IpResource[] result = new IpResource[size];
		IpResource buf;
		int i = 0;
		while(iter.hasNext()){
			buf = (IpResource) iter.next();
			result[i] = buf;
			i++;
		}
		return result;
	}

}

}
