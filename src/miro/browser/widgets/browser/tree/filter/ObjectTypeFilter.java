package miro.browser.widgets.browser.tree.filter;

import miro.validator.types.CertificateObject;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ObjectTypeFilter extends ViewerFilter {
	
	
	

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		
		if(element instanceof CertificateObject){
			return true;
		} {
			return false;
		}
	}

}
