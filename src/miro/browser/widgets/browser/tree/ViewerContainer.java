package miro.browser.widgets.browser.tree;

import miro.validator.types.ResourceHoldingObject;

public interface ViewerContainer {
	
	public void setSelection(ResourceHoldingObject obj);
	
	public ResourceHoldingObject getSelection();

}
