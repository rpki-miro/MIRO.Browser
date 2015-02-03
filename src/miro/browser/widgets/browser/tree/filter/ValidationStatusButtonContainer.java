package miro.browser.widgets.browser.tree.filter;

import java.util.ArrayList;
import java.util.List;

import miro.browser.resources.Fonts;
import miro.browser.widgets.browser.tree.filter.FilterKeys.FilterKey;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ValidationStatusButtonContainer extends Composite implements FilterButtonContainer {
	
	List<Button> checkButtons;
	
	public ValidationStatusButtonContainer(Composite parent, int style) {
		super(parent, style);
		init();
		
		initButtons();
		
	}

	private void initButtons() {
		Button button = new Button(this, SWT.CHECK);
		button.setText("Passed");
		button.setFont(Fonts.STANDARD_FONT);
		RowData rowData = new RowData();
		button.setLayoutData(rowData);
		button.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.PASSED_STATUS); 
		checkButtons.add(button);
		
		button = new Button(this, SWT.CHECK);
		button.setText("Warning");
		button.setFont(Fonts.STANDARD_FONT);
		rowData = new RowData();
		button.setLayoutData(rowData);
		button.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.WARNING_STATUS);
		checkButtons.add(button);

		button = new Button(this, SWT.CHECK);
		button.setText("Error");
		button.setFont(Fonts.STANDARD_FONT);
		rowData = new RowData();
		button.setLayoutData(rowData);
		button.setData(FilterKeys.FILTER_TYPE_KEY, FilterKey.ERROR_STATUS);
		checkButtons.add(button);

	}

	private void init() {
		checkButtons = new ArrayList<Button>();
		setLayout(new RowLayout());
		
	}

	public void clearSelection() {

		for(Button btn : checkButtons){
			btn.setSelection(false);
		}
	}

	@Override
	public Button[] getSelectedButtons() {
		List<Button> selected = new ArrayList<Button>();
		for(Button btn : checkButtons) {
			if(btn.getSelection()){
				selected.add(btn);
			}
		}
		return selected.toArray(new Button[]{});
	}

}
