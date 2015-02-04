package miro.browser.widgets.browser.filter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

public class SigningTimeButtonContainer extends Composite {
	
	private DateTime dateSelect;

	public SigningTimeButtonContainer(Composite parent, int style) {
		super(parent, style);
		init();
		initDateSelect();
	}
	
	
	private void initDateSelect() {
        dateSelect = new DateTime(this, SWT.DATE | SWT.DROP_DOWN | SWT.MEDIUM);	
	}


	public void init() {
		GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        setLayout(gridLayout);	
	}

}
