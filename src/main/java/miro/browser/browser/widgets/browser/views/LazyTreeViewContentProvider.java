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

import java.util.ArrayList;
import java.util.HashMap;

import main.java.miro.validator.types.CertificateObject;
import main.java.miro.validator.types.ResourceCertificateTree;
import main.java.miro.validator.types.ResourceHoldingObject;
import main.java.miro.validator.types.RoaObject;

import org.apache.commons.lang3.ArrayUtils;
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
	
	private HashMap<ResourceHoldingObject, int[]> indexMap;

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
			int childCount = matchesFilters(null, ta, fs) ? getChildCount(ta,fs) : 0;
			viewer.replace(parent, index, ta);
			viewer.setChildCount(ta, childCount);
		}
		
		if( parent instanceof CertificateObject){
			ResourceHoldingObject child = ((CertificateObject) parent).getChildren().get(indexMap.get(parent)[index]);
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
		indexMap = new HashMap<ResourceHoldingObject, int[]>();
	}
	
	public int getChildCount(ResourceHoldingObject parent, ViewerFilter[] filters){
		if(parent instanceof RoaObject)
			return 0;
	
		int childCount = 0;
		CertificateObject ca = (CertificateObject) parent;
		ResourceHoldingObject obj;
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i<ca.getChildren().size();i++){
			obj = ca.getChildren().get(i);
			if (matchesFilters(ca, obj, filters)) {
				childCount++;
				list.add(i);
			}
		}
		int[] ar = ArrayUtils.toPrimitive(list.toArray(new Integer[list.size()]));
		indexMap.put(ca,ar);
		return childCount;
	}

	@Override
	public void updateChildCount(Object element, int currentChildCount) {
		
		ViewerFilter[] filters = viewer.getFilters();
		if(element instanceof CertificateObject){
			CertificateObject ca = (CertificateObject) element;
			if(ca.getIsRoot()){
				viewer.setChildCount(ca, getChildCount(ca, filters));
			}
			return;
		}
		
		if(element instanceof ResourceCertificateTree){
			ResourceCertificateTree tree = (ResourceCertificateTree) element;
			CertificateObject ta = tree.getTrustAnchor();
			int childCount = matchesFilters(null,ta,filters) ? 1 : 0;
			viewer.setChildCount(element, childCount);
			return;
		}
	}

	@Override
	public Object getParent(Object element) {
		if(!(element instanceof CertificateObject)){
			return ((ResourceHoldingObject)element).getParent();
		}
		
		CertificateObject cert = (CertificateObject) element;
		if(cert.getIsRoot())
			return viewer.getInput();
		
		return cert.getParent();
		
		
	}
	
	
//	public int getFlatIndex( ResourceHoldingObject element)
//	{
//		Stack<ResourceHoldingObject> parentHierarchy = new Stack<ResourceHoldingObject>();
//		ResourceHoldingObject parent = element;
//		while(parent != null)
//		{
//			parentHierarchy.push(parent);
//			parent = parent.getParent();
//		}
//		return getFlatIndex( null, parentHierarchy, 0);
//	}
//	
//	public int getFlatIndex(TreeItem element, Stack<ResourceHoldingObject> modelObjects, int previousCount)
//	{
//		TreeItem[] childTreeItems = null;
//		int currentCount = previousCount;
//		if( element == null )
//		{
//			//			an der wurzel angekommen
//			childTreeItems = viewer.getTree().getItems();
//		} else 
//		{
//			childTreeItems = element.getItems(); 
//		}
//		ResourceHoldingObject modelElement = modelObjects.pop();
//		
//		for( TreeItem item : childTreeItems)
//		{
//			if( item.getData() != modelElement)
//			{
//				currentCount += getRecursiveItemCount(item);
//			} else {
//				if( !item.getExpanded())
//				{
//					item.setExpanded(true);
//					if( element != null)
//					{
//						element.checkData(item,)
//					}
//				}
//				return getFlatIndex( item, modelObjects, currentCount);
//			}
//		}
//		return 0;
//	}
//
//	private int getRecursiveItemCount(TreeItem item) {
//		if( item.getExpanded())
//		{
//			int count = 1;
//			for( TreeItem child: item.getItems())
//				count+= getRecursiveItemCount(child);
//			return count;
//		} else {
//			return 1;
//		}
//	}
	
}
