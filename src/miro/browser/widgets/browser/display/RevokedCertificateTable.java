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

import main.java.miro.validator.types.CRLObject;
import miro.browser.resources.MagicNumbers;
import net.ripe.rpki.commons.crypto.crl.X509Crl;
import net.ripe.rpki.commons.crypto.crl.X509Crl.Entry;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class RevokedCertificateTable extends Composite {
	private TableViewer tableViewer;

	public RevokedCertificateTable(Composite parent, int style) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "displayContent");
		setLayout(new FillLayout());
		tableViewer = new TableViewer(this, SWT.V_SCROLL);
		tableViewer.setContentProvider(new CRLEntryContentProvider());
		createColumns(tableViewer.getTable());
	}

	private void createColumns(Table table) {
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		
		TableViewerColumn newCol;
		newCol = new TableViewerColumn(tableViewer, new TableColumn(table,SWT.VIRTUAL));
		newCol.getColumn().setWidth(MagicNumbers.CRL_REVOKED_LIST_SERIAL_COLUMN_WIDTH);
		newCol.getColumn().setResizable(false);
		newCol.getColumn().setMoveable(false);
		newCol.getColumn().setText("Serial Nr.");
		newCol.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Entry entry = (Entry)cell.getElement();
				cell.setText(entry.getSerialNumber().toString());
			}
		});
		
		
		newCol = new TableViewerColumn(tableViewer, new TableColumn(table, SWT.NONE));
		newCol.getColumn().setWidth(MagicNumbers.CRL_REVOKED_LIST_TIME_COLUMN_WIDTH);
		newCol.getColumn().setResizable(false);
		newCol.getColumn().setMoveable(false);
		newCol.getColumn().setText("Revocation time");
		newCol.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Entry entry = (Entry)cell.getElement();
				cell.setText(entry.getRevocationDateTime().toString());
			}
		});
	}
	
	public void setInput(CRLObject obj) {
		tableViewer.setInput(obj);
	}
	
	
	private class CRLEntryContentProvider implements IStructuredContentProvider {

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
			X509Crl crl = ((CRLObject)inputElement).getCrl();
			return crl.getRevokedCertificates().toArray();
		}
		
	}
	
	

}
