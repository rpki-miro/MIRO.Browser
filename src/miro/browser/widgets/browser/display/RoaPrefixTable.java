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

import miro.browser.resources.MagicNumbers;
import miro.validator.types.RoaObject;
import net.ripe.rpki.commons.crypto.cms.roa.RoaPrefix;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class RoaPrefixTable extends Composite {
	
	
	private TableViewer tableViewer;

	public RoaPrefixTable(Composite parent, int style) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "displayContent");
		setLayout(new FormLayout());
		createTableViewer();
	}

	public void createTableViewer() {
		tableViewer = new TableViewer(this, SWT.V_SCROLL);
		tableViewer.setContentProvider(new RoaPrefixContentProvider());
		Table table = tableViewer.getTable();
		createColumns(table);
		
		FormData formData = new FormData();
		formData.top = new FormAttachment(0,0);
		formData.left = new FormAttachment(0,0);
		formData.right = new FormAttachment(100,0);
		formData.bottom = new FormAttachment(100,0);
		table.setLayoutData(formData);
		table.setHeaderVisible(true);
	}

	public void createColumns(Table table) {
		
		TableViewerColumn newCol;
		newCol = new TableViewerColumn(tableViewer, new TableColumn(table,SWT.NONE));
		newCol.getColumn().setWidth(MagicNumbers.RDW_PREFIX_LIST_PREFIX_COLUMN_WIDTH);
		newCol.getColumn().setResizable(false);
		newCol.getColumn().setMoveable(false);
		newCol.getColumn().setText("Prefix");
		newCol.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				RoaPrefix prefix = (RoaPrefix)cell.getElement();
				cell.setText(prefix.getPrefix().toString());
			}
		});
		
		
		newCol = new TableViewerColumn(tableViewer, new TableColumn(table,SWT.NONE));
		newCol.getColumn().setWidth(MagicNumbers.RDW_PREFIX_LIST_MAX_LENGTH_COLUMN_WIDTH);
		newCol.getColumn().setResizable(false);
		newCol.getColumn().setMoveable(false);
		newCol.getColumn().setText("Max. Length");
		newCol.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				RoaPrefix prefix = (RoaPrefix)cell.getElement();
				cell.setText(String.valueOf(prefix.getEffectiveMaximumLength()));
			}
		});
	}
	
	public void setInput(RoaObject roa) {
		tableViewer.setInput(roa);
	}
	
private class RoaPrefixContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement == null){
			return null;
		}
		
		RoaObject roaObj = (RoaObject)inputElement;
		return roaObj.getRoa().getPrefixes().toArray();
	}
	
}

	

}
