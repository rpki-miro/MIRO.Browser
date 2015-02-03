package miro.browser.widgets.browser.tree.filter;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public abstract class RadioButtonContainer extends Composite {

	protected Button selectedButton;
	
	public RadioButtonContainer(Composite parent, int style) {
		super(parent, style);
	}

	protected class SelectedButtonListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			selectedButton = (Button) event.widget;
		}
	}

}
