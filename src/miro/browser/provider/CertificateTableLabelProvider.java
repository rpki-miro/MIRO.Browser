package miro.browser.provider;

import miro.browser.widgets.browser.filter.filters.ResourceCertificateTreeFilter;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class CertificateTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ResourceHoldingObject obj = (ResourceHoldingObject) element;
		return obj.getFilename();
	}

	public void setFilter(ResourceCertificateTreeFilter treeFilter) {
		// TODO Auto-generated method stub
		
	}

}
