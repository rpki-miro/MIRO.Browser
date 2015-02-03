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
package miro.browser.provider;


import miro.browser.resources.Images;
import miro.validator.types.ResourceHoldingObject;
import miro.validator.types.ValidationResults;
import net.ripe.rpki.commons.validation.ValidationStatus;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;


public class CertificateTreeLabelProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		ResourceHoldingObject obj = (ResourceHoldingObject) cell.getElement();
		cell.setText(obj.getFilename());
		
		ValidationStatus status = null;
		ValidationResults results = obj.getValidationResults();
	
		status = results.getValidationStatus();
		
		Image i = null;
		switch (status) {
		
		case PASSED:
			i = Images.VALID_ICON;
			break;
			
		case ERROR:
			i = Images.INVALID_ICON;
			break;
			
		case WARNING:
			i = Images.WARNING_ICON;
			break;

		default:
			break;
		}
		cell.setImage(i);
	}
	



}
