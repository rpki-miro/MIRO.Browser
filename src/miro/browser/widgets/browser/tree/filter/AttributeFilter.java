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

import java.math.BigInteger;
import java.net.URI;

import miro.browser.widgets.browser.tree.filter.FilterKeys.FilterKey;
import miro.validator.types.CertificateObject;
import miro.validator.types.ResourceHoldingObject;
import miro.validator.types.RoaObject;
import net.ripe.ipresource.IpResource;
import net.ripe.rpki.commons.crypto.cms.roa.RoaPrefix;

import org.eclipse.jface.viewers.Viewer;

public class AttributeFilter implements ResourceHoldingObjectFilter {

	private FilterKey filterAttribute;
	
	private String searchQuery;
	
	public AttributeFilter(String query, FilterKey attr) {
		searchQuery = query;
		filterAttribute = attr;
	}
	
	public boolean uriMatches(URI remoteLocation) {
		return remoteLocation.toString().contains((searchQuery));
	}


	public boolean stringMatches(String s){
		return s.contains(searchQuery);
	}
	
	public String getSearchQuery(){
		return searchQuery;
	}
	
	public boolean bigIntegerMatches(BigInteger nr) {
		BigInteger query;
		try {
			 query = new BigInteger(searchQuery); 
		} catch (NumberFormatException e) {
			return false;
		}
		return query.compareTo(nr) == 0; 
	}
	
	public boolean ownsResource(ResourceHoldingObject obj, IpResource res){
		
		if(obj instanceof CertificateObject){
			CertificateObject cert = (CertificateObject) obj;
			return cert.getResources().contains(res);
		} else if(obj instanceof RoaObject){
			RoaObject roa = (RoaObject) obj;
			if(res.equals(roa.getRoa().getAsn())){
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

	@Override
	public boolean isMatch(ResourceHoldingObject obj) {
		if(searchQuery == null){
			return true;
		}
	
		boolean selected = false;
		switch (filterAttribute) {
		
		case FILENAME:
			selected = stringMatches(obj.getFilename());
			break;
			
		case SUBJECT:
			selected = stringMatches(obj.getSubject().toString());
			break;

		case ISSUER:
			selected = stringMatches(obj.getIssuer().toString());
			break;
		
		case SERIAL_NUMBER:
			selected = bigIntegerMatches(obj.getSerialNr());
			break;
		
		case REMOTE_LOCATION:
			selected = uriMatches(obj.getRemoteLocation());
			break;
			
		case RESOURCE:
			IpResource queryRes; 
			try {
				queryRes = IpResource.parse(searchQuery);
			} catch (Exception e) {
				return false;
			}
			selected = ownsResource(obj, queryRes);
			break;
			
		default:
			break;
		}

		return selected;
	}
}
