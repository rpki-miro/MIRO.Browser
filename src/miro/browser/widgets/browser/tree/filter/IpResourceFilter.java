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
import miro.validator.types.RoaObject;
import net.ripe.ipresource.IpResource;
import net.ripe.rpki.commons.crypto.cms.roa.RoaPrefix;

import org.eclipse.jface.viewers.Viewer;

public class IpResourceFilter extends TreeSearchBarFilter {

	public IpResourceFilter(String t) {
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		if(searchQuery == null){
			return true;
		}
		
		try {
			ResourceHoldingObject obj = (ResourceHoldingObject) element;
			IpResource queryRes = IpResource.parse(searchQuery);
			if (ownsResource(obj, queryRes)) {
				return true;
			} else if (obj instanceof CertificateObject) {
				for (ResourceHoldingObject kid : ((CertificateObject) obj).getChildren()) {
					if (select(viewer, obj, kid)) {
						return true;
					}
				}
			}

		} catch (IllegalArgumentException e) {
			System.out.println("break");
		}
		return false;
	}
	
	public boolean ownsResource(ResourceHoldingObject obj, IpResource res){
		
		if(obj instanceof CertificateObject){
			CertificateObject cert = (CertificateObject) obj;
			return cert.getResources().contains(res);
		} else if(obj instanceof RoaObject){
			RoaObject roa = (RoaObject) obj;
			if(res.equals(roa.getRoa().getAsn().toString())){
				return true;
			}
			
			for(RoaPrefix prefix : roa.getRoa().getPrefixes()){
				if(prefix.getPrefix().contains(res) | res.contains(prefix.getPrefix())){
					return true;
				}
			}
		}
		

		return false;
	}

}
