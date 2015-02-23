package miro.browser.widgets.browser.filter;

import java.util.List;

import miro.browser.widgets.browser.filter.filters.ResourceHoldingObjectFilter;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

public class SigningTimeButtonContainer extends Composite implements FilterOption{
	
	private DateTime dateSelect;

	public SigningTimeButtonContainer(Composite parent, int style) {
		super(parent, style);
		init();
		initDateSelect();
	}
	
	
	private void initDateSelect() {
	}


	public void init() {
		GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        setLayout(gridLayout);	
	}


	@Override
	public void clearSelection() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<ResourceHoldingObjectFilter> getFilters() {
		// TODO Auto-generated method stub
		return null;
	}

}
