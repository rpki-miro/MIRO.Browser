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
package miro.browser.widgets.browser.filter.filters;

import java.util.ArrayList;
import java.util.List;

import miro.validator.types.CertificateObject;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ResourceCertificateTreeFilter extends ViewerFilter {
	
	private List<ResourceHoldingObjectFilter> filters;
	
	public ResourceCertificateTreeFilter() {
		filters = new ArrayList<ResourceHoldingObjectFilter>();
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		ResourceHoldingObject obj = (ResourceHoldingObject) element;
		boolean selected = matchesAll(obj);
		return getSelectResult(selected, viewer, obj);
	}
	
	public void addFilter(ResourceHoldingObjectFilter f){
		filters.add(f);
	}

	public void addFilters(List<ResourceHoldingObjectFilter> fs) {
		filters.addAll(fs);
	}
	
	public boolean matchesAll(ResourceHoldingObject obj){
		boolean matches = true;
		for(ResourceHoldingObjectFilter filter : filters){
			matches &= filter.isMatch(obj);
		}
		return matches;
	}

	public boolean getSelectResult(boolean selected, Viewer viewer, ResourceHoldingObject obj) {
		if (selected) {
			return selected;
		} else {
			return selectChildren(viewer, obj);
		}
	}
	
	public boolean selectChildren(Viewer viewer, ResourceHoldingObject obj) {
		if (obj instanceof CertificateObject) {
			for (ResourceHoldingObject kid : ((CertificateObject) obj)
					.getChildren()) {
				if (select(viewer, obj, kid)) {
					return true;
				}
			}
		}
		return false;
	}
}
