package miro.browser.widgets.browser.tree.filter;

import java.util.ArrayList;
import java.util.List;

import miro.validator.types.CertificateObject;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ResourceCertificateTreeFilter extends ViewerFilter {
	
	private List<ResourceHoldingObjectFilter> filters;
	
	public ResourceCertificateTreeFilter() {
		filters = new ArrayList<ResourceHoldingObjectFilter>();
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		ResourceHoldingObject obj = (ResourceHoldingObject) element;
		boolean selected = matchesAll(obj);
		return getSelectResult(selected, viewer, obj);
	}
	
	public void addFilter(ResourceHoldingObjectFilter f){
		filters.add(f);
	}
	
	
	public boolean matchesAll(ResourceHoldingObject obj){
		boolean matches = true;
		for(ResourceHoldingObjectFilter filter : filters){
			matches &= filter.isMatch(obj);
		}
		return matches;
	}

	public boolean getSelectResult(boolean selected, Viewer viewer, ResourceHoldingObject obj) {
		if (selected) {
			return selected;
		} else {
			return selectChildren(viewer, obj);
		}
	}
	
	public boolean selectChildren(Viewer viewer, ResourceHoldingObject obj) {
		if (obj instanceof CertificateObject) {
			for (ResourceHoldingObject kid : ((CertificateObject) obj)
					.getChildren()) {
				if (select(viewer, obj, kid)) {
					return true;
				}
			}
		}
		return false;
	}
}
