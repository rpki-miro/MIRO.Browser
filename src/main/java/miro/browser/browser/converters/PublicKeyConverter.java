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

import java.security.PublicKey;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Text;

public class PublicKeyConverter implements IConverter {

	private Text text;
	
	@Override
	public Object getFromType() {
		return PublicKey.class;
	}

	@Override
	public Object getToType() {
		return String.class;
	}

	@Override
	public Object convert(Object fromObject) {
		if(fromObject == null){
			return "";
		}
		if(text == null)
			return "";

		/* -10 for VScrollbar that appears for some reason when trying to use the whole width */
		int width = text.getClientArea().width - 10;
		GC gc = new GC(text);
		int charWidth = gc.getFontMetrics().getAverageCharWidth();
		charWidth = gc.getCharWidth('0');
		int charsPerLine = width/charWidth;
		
		/*Minus one for the newline char we will append to each line*/
		charsPerLine--;
		
		
		PublicKey key = (PublicKey) fromObject;
		String result = "";
		String[] lines = key.toString().split("\n");
		String line;
		String seg;
		for(int i = 0;i<lines.length;i++){
			line = lines[i];
			while(line.length() > charsPerLine){
				seg = line.substring(0,charsPerLine) + "\n";
				result += seg;
				line = line.substring(charsPerLine);
			}
			result += line;
			if( i != lines.length -1)
				result += "\n";
		}
		
		return result ;
	}
	
	public void setText(Text s){
		text = s;
	}
}
