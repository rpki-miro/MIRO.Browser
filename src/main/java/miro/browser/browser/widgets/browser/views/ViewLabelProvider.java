/*
 * Copyright (c) 2015, Andreas Reuter, Freie Universität Berlin 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 * 
 * */
package main.java.miro.browser.browser.widgets.browser.views;


import main.java.miro.validator.types.ResourceHoldingObject;
import net.ripe.rpki.commons.validation.ValidationStatus;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


public class ViewLabelProvider extends CellLabelProvider{

	public void update(ViewerCell cell) {
		ResourceHoldingObject obj = (ResourceHoldingObject) cell.getElement();
		cell.setText(obj.getFilename());
		Image i = getImage(obj);
		cell.setImage(i);
	}
	
	private Image getImage(ResourceHoldingObject obj) {
		ValidationStatus status = obj.getValidationResults().getValidationStatus();
		Display display = Display.getDefault();
		ResourceManager rm = RWT.getApplicationContext().getResourceManager();
		Image i = null;
		switch (status) {
		case PASSED:
			i = new Image(display,rm.getRegisteredContent("resources/images/valid_icon.png"));
			break;
			
		case ERROR:
			i = new Image(display,rm.getRegisteredContent("resources/images/error_icon.png"));
			break;
			
		case WARNING:
			i = new Image(display,rm.getRegisteredContent("resources/images/warning_icon.png"));
			break;

		default:
			break;
		}
		return i;
	}
}
