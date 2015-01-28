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
package miro.browser.widgets.browser.tree.filter;

import miro.validator.types.CertificateObject;
import miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.Viewer;

public class FilenameTreeFilter extends TreeSearchBarFilter {


	public FilenameTreeFilter(String t) {
		super(t);
	}


	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if(text == null){
			return true;
		}


		ResourceHoldingObject obj = (ResourceHoldingObject) element;
		
		if(containsText(obj.getFilename())){
			return true;
		} else {
			
			if(obj instanceof CertificateObject){
				for(ResourceHoldingObject kid : ((CertificateObject) obj).getChildren()){
					if(select(viewer,obj,kid)){
						return true;
					}
				}
			}
		}
		return false;
		
	}
	
	
	public boolean containsText(String s){
		return s.contains(text);
	}

}
