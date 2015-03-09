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
package miro.browser.widgets.browser.views;


import java.util.HashMap;

import miro.browser.resources.Colors;
import miro.browser.resources.Images;
import miro.validator.types.ResourceHoldingObject;
import miro.validator.types.ValidationResults;
import net.ripe.rpki.commons.validation.ValidationStatus;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;


public class ViewLabelProvider extends CellLabelProvider{

	private Viewer viewer;
	
	public void setViewer(Viewer v){
		viewer = v;
	}
	
	public void update(ViewerCell cell) {
		ResourceHoldingObject obj = (ResourceHoldingObject) cell.getElement();
		cell.setText(obj.getFilename());
		Image i = getImage(obj);
		cell.setImage(i);
		setBackgroundColor(cell,obj);
	}
	
	private Image getImage(ResourceHoldingObject obj) {
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
		return i;
	}
	
	private void setBackgroundColor(ViewerCell cell, ResourceHoldingObject obj) {
		if(viewer != null){
			String variant = isMarked(obj) ? "filterMatch" : null;
			if (cell.getViewerRow().getItem() instanceof TreeItem) {
				TreeItem item = (TreeItem) cell.getViewerRow().getItem();
				item.setData(RWT.CUSTOM_VARIANT, variant);
			}
		}
	}
	
	public boolean isMarked(ResourceHoldingObject obj){
		HashMap<ResourceHoldingObject, Boolean> marked = (HashMap<ResourceHoldingObject, Boolean>) viewer.getData("MARKED");
		boolean isMarked;
		if (marked == null) {
			isMarked = false;
		} else {
			isMarked = marked.get(obj) == null ? false : marked.get(obj);
		}
		return isMarked;
	}
}
