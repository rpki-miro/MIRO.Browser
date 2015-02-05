package miro.browser.widgets.browser.tree;

import miro.browser.provider.CertificateTableContentProvider;
import miro.browser.provider.CertificateTableLabelProvider;
import miro.browser.provider.CertificateTreeLabelProvider;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Table;

public class TableBrowser extends Composite {
	
	private TableViewer tableViewer;

	public TableBrowser(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		tableViewer = new TableViewer(this,SWT.VIRTUAL);
		tableViewer.setLabelProvider(new CertificateTreeLabelProvider());
		tableViewer.setContentProvider(new CertificateTableContentProvider());
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public Table getTable() {
		return tableViewer.getTable();
	}

	
	

}
