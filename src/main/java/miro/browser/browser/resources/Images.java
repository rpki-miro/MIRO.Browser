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
package main.java.miro.browser.browser.resources;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class Images {
	public static Display DISPLAY;
	
	public static Image VALID_ICON;
	
	public static Image INVALID_ICON;
	
	public static Image WARNING_ICON;
	
	public static Image TA_ICON;
	
	
	public static void init(Display d){
		DISPLAY = d;
		initImages();
	}
	
	private static void initImages(){
		String tmp_path = "/var/data/MIRO/MIRO.Browser/resources/images/";
		VALID_ICON = new Image(DISPLAY, tmp_path + "valid_icon.png");
		INVALID_ICON = new Image(DISPLAY, tmp_path + "error_icon.png");
		WARNING_ICON = new Image(DISPLAY, tmp_path + "warning_icon.png");
		TA_ICON = new Image(DISPLAY, tmp_path + "ta_icon.png");
		
	}
}
