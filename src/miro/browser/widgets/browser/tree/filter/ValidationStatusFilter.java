package miro.browser.widgets.browser.tree.filter;

import java.util.List;

import miro.browser.widgets.browser.tree.filter.FilterKeys.FilterKey;
import miro.validator.types.CertificateObject;
import miro.validator.types.ResourceHoldingObject;
import net.ripe.rpki.commons.validation.ValidationStatus;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ValidationStatusFilter extends ViewerFilter {
	
	private List<ValidationStatus> filteredStats;
	
	public ValidationStatusFilter(List<ValidationStatus> stats) {
		filteredStats = stats;
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		ResourceHoldingObject obj = (ResourceHoldingObject) element;
		boolean selected = false;
		
		ValidationStatus status = obj.getValidationResults().getValidationStatus();

		if(filteredStats.contains(status)){
			selected = true;
		}

		return getSelectResult(selected, viewer, obj);
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
