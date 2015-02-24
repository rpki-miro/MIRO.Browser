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
import java.util.LinkedList;

import miro.validator.types.CertificateObject;
import miro.validator.types.ResourceCertificateTree;
import miro.validator.types.ResourceHoldingObject;
import miro.validator.types.RoaObject;

import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
/**
 * Provides objects for our TreeView in a lazy way. This means only objects currently on the screen
 * will be actually loaded, everything else will be loaded dynamically when needed.
 * @author ponken
 *
 */
public class LazyTreeViewContentProvider implements ILazyTreeContentProvider {
	
	private TreeViewer viewer;
	
	private HashMap<ResourceHoldingObject, LinkedList<Integer>> indexMap;

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
	}

	@Override
	public void updateElement(Object parent, int index) {
		ViewerFilter[] fs = viewer.getFilters();
		
		if( parent instanceof ResourceCertificateTree){
			CertificateObject ta = ((ResourceCertificateTree) parent).getTrustAnchor();
			viewer.replace(parent, index, ta);
			viewer.setChildCount(ta, getChildCount(ta, fs));
		}
		
		if( parent instanceof CertificateObject){
			ResourceHoldingObject child = ((CertificateObject) parent).getChildren().get(indexMap.get(parent).removeFirst());
			viewer.replace(parent, index,child); 
			viewer.setChildCount(child, getChildCount(child, fs));
		}
	}
	
	public boolean matchesFilters(ResourceHoldingObject parent, ResourceHoldingObject obj, ViewerFilter[] filters){
		for(ViewerFilter f : filters){
			if(!f.select(viewer,parent,obj)){
				return false;
			}
		}
		return true;
	}
	
	public void resetIndexMap(){
		indexMap = new HashMap<ResourceHoldingObject, LinkedList<Integer>>();
	}
	
	public int getChildCount(ResourceHoldingObject parent, ViewerFilter[] filters){
		if(parent instanceof RoaObject)
			return 0;
	
		int childCount = 0;
		CertificateObject ca = (CertificateObject) parent;
		ResourceHoldingObject obj;
		LinkedList<Integer> list = new LinkedList<Integer>();
		for(int i = 0; i<ca.getChildren().size();i++){
			obj = ca.getChildren().get(i);
			if (matchesFilters(ca, obj, filters)) {
				childCount++;
				list.add(i);
			}
		}
		indexMap.put(ca,list);
		return childCount;
	}

	@Override
	public void updateChildCount(Object element, int currentChildCount) {
		
		if(element instanceof CertificateObject){
			CertificateObject ca = (CertificateObject) element;
			if(ca.getIsRoot()){
				ViewerFilter[] filters = viewer.getFilters();
				viewer.setChildCount(ca, getChildCount(ca, filters));
			}
			return;
		}
		
		if(element instanceof ResourceCertificateTree){
			ResourceCertificateTree tree = (ResourceCertificateTree) element;
			CertificateObject ta = tree.getTrustAnchor();
			viewer.setChildCount(element, 1);
			return;
		}
	}

	@Override
	public Object getParent(Object element) {
		return ((ResourceHoldingObject)element).getParent();
	}

}
