package miro.browser.widgets.browser.tree.filter;

import java.util.List;

import miro.browser.widgets.browser.tree.filter.FilterKeys.FilterKey;
import miro.validator.types.CertificateObject;
import miro.validator.types.ResourceHoldingObject;
import net.ripe.rpki.commons.validation.ValidationStatus;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ValidationStatusFilter implements ResourceHoldingObjectFilter{
	
	private List<ValidationStatus> filteredStats;
	
	public ValidationStatusFilter(List<ValidationStatus> stats) {
		filteredStats = stats;
	}

	@Override
	public boolean isMatch(ResourceHoldingObject obj) {
		boolean selected = false;
		
		ValidationStatus status = obj.getValidationResults().getValidationStatus();

		if(filteredStats.contains(status)){
			selected = true;
		}

		return selected; 
	}

}
