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
package main.java.miro.browser.browser.converters;

import java.util.ArrayList;

import net.ripe.rpki.commons.crypto.cms.roa.RoaPrefix;

import org.eclipse.core.databinding.conversion.IConverter;


public class RoaPrefixToStringConverter implements IConverter {

	@Override
	public Object getToType() {
		// TODO Auto-generated method stub
		return String.class;
	}
	
	@Override
	public Object getFromType() {
		// TODO Auto-generated method stub
		return ArrayList.class;
	}


	@Override
	public Object convert(Object fromObject) {
		
		ArrayList<RoaPrefix> prefixes = (ArrayList<RoaPrefix>) fromObject;
		
		String result = "";
		if(prefixes == null){
			return result;
		}

		for(RoaPrefix prefix : prefixes){
			result += prefix.getPrefix();
			result += " - ";
			result += prefix.getMaximumLength();
			result += "\n";
		}
		return result;
	}

}
