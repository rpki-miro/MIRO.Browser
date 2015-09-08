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
import java.util.Iterator;

import main.java.miro.browser.browser.resources.ValidationTranslation;
import net.ripe.rpki.commons.validation.ValidationCheck;

import org.eclipse.core.databinding.conversion.IConverter;


public class ValidationCheckConverter implements IConverter {

	@Override
	public Object getFromType() {
		return ArrayList.class;
	}

	@Override
	public Object getToType() {
		return String.class;
	}

	@Override
	public Object convert(Object fromObject) {
		ArrayList<ValidationCheck> list = (ArrayList<ValidationCheck>) fromObject;
		String result = "";
		if(list == null){
			return result;
		}
		
		Iterator<ValidationCheck> iter = list.iterator();
		String c;
		while (iter.hasNext()) {
			ValidationCheck check = (ValidationCheck) iter.next();
			c = ValidationTranslation.getTranslation(check);
			
			
			
			result += c; 
			if(iter.hasNext()){
				result += "\n";
			}
		}
		return result;
	}
	
	
	public String ValidationCheckToString(ValidationCheck check) {
		return check.getKey();
	}

}
