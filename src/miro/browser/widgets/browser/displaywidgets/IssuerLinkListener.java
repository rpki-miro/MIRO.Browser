package miro.browser.widgets.browser.displaywidgets;

import miro.browser.widgets.browser.tree.ViewerManager;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class IssuerLinkListener implements Listener {

	private ViewerManager viewerContainer;
	
	public IssuerLinkListener(ViewerManager c) {
		viewerContainer = c;
	}

	@Override
	public void handleEvent(Event event) {
		ResourceHoldingObject selectedObj = viewerContainer.getSelectedObject();
		ResourceHoldingObject issuer = selectedObj.getParent();
		if(issuer != null){
			viewerContainer.setSelection(issuer);
		}
	}

}
