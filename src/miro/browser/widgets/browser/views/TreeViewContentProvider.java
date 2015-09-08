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

import main.java.miro.validator.types.CertificateObject;
import main.java.miro.validator.types.ResourceCertificateTree;
import main.java.miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;




@SuppressWarnings("serial")
public class TreeViewContentProvider implements ITreeContentProvider {
	

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getElements(Object inputElement) {
		ResourceCertificateTree tree = (ResourceCertificateTree) inputElement;
		return new Object[]{tree.getTrustAnchor()};
		
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof CertificateObject) {
			return ((CertificateObject) parentElement).getChildren().toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		return ((ResourceHoldingObject) element).getParent();
	}

	public boolean hasChildren(Object element) {

		if (element instanceof CertificateObject) {
			return !((CertificateObject)element).getChildren().isEmpty();
		}
		return false;
	}

}
