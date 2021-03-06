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
package main.java.miro.browser.browser.widgets.browser.filter.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.miro.validator.types.CertificateObject;
import main.java.miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ResourceCertificateTreeFilter extends ViewerFilter {
	
	private List<ResourceHoldingObjectFilter> filters;
	
	public ResourceCertificateTreeFilter(boolean isTableView) {
		filters = new ArrayList<ResourceHoldingObjectFilter>();
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		ResourceHoldingObject obj = (ResourceHoldingObject) element;
		boolean selected = passesAll(obj);
		return getSelectResult(selected, viewer, obj);
	}
	
	public boolean passesAll(ResourceHoldingObject obj){
		boolean matches = true;
		for(ResourceHoldingObjectFilter filter : filters){
			matches &= filter.isMatch(obj);
		}
		return matches;
	}
	public void addFilter(ResourceHoldingObjectFilter f){
		filters.add(f);
	}

	public void addFilters(List<ResourceHoldingObjectFilter> fs) {
		filters.addAll(fs);
	}
	

	public boolean getSelectResult(boolean selected, Viewer viewer, ResourceHoldingObject obj) {
		if (selected) {
			if(viewer instanceof TreeViewer)
				markObject(obj, viewer);
			return selected;
		} else {
			if(viewer instanceof TableViewer)
				return false;
			else
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
	
	private void markObject(ResourceHoldingObject obj,Viewer v){
		HashMap<ResourceHoldingObject, Boolean> marked = (HashMap<ResourceHoldingObject, Boolean>)v.getData("MARKED");
		marked.put(obj, true);
	}
	
	
}
