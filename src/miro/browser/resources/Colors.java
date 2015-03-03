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
package miro.browser.resources;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Colors {
	
	public static Display DISPLAY;
	
	public static Color SHELL_BACKGROUND;
	
	//Header
	public static Color HEADER_BACKGROUND;
	
	//BrowserWidget
	public static Color BROWSER_BACKGROUND;
	
	public static Color BROWSER_DISPLAY_CONTAINER_BACKGROUND;
	
	public static Color BROWSER_DISPLAY_WIDGETS_BACKGROUND;
	
	public static Color BROWSER_DISPLAY_WIDGETS_TITLE_BAR_BACKGROUND;
	
	public static Color BROWSER_DISPLAY_WIDGETS_TABLE_BACKGROUND;
	
	public static Color BROWSER_COOLBAR_BACKGROUND;
	
	public static Color BROWSER_TREE_BACKGROUND;
	
	public static Color GREEN;
	
	public static Color BLUE;
	
	public static Color WHITE;
	
	public static Color RED;
	
	public static Color DARK_GREY;
	
	public static Color VALID_OBJECT_COLOR;
	
	public static Color INVALID_OBJECT_COLOR;
	
	public static Color CER_OBJECT_COLOR;
	
	public static Color MFT_OBJECT_COLOR;
	
	public static Color CRL_OBJECT_COLOR;
	
	public static Color ROA_OBJECT_COLOR;
	
	public static Color FILTER_MATCH;

	
	
	public static void init(Display d){
		DISPLAY = d;
		initColors();
	}
	
	private static void initColors(){
		SHELL_BACKGROUND = new Color(DISPLAY, 255,255,255);
		
		HEADER_BACKGROUND = new Color(DISPLAY, 49, 97, 156);
		
		BROWSER_BACKGROUND = SHELL_BACKGROUND;
		BROWSER_DISPLAY_CONTAINER_BACKGROUND = new Color(DISPLAY,210, 210, 210);
		BROWSER_DISPLAY_WIDGETS_BACKGROUND = new Color(DISPLAY, 230, 230, 230);
		BROWSER_DISPLAY_WIDGETS_TITLE_BAR_BACKGROUND = new Color(DISPLAY, 230, 230, 230);
		BROWSER_DISPLAY_WIDGETS_TABLE_BACKGROUND = new Color(DISPLAY,200,200,200);
		BROWSER_COOLBAR_BACKGROUND = new Color(DISPLAY,200,200,200);
		BROWSER_TREE_BACKGROUND = new Color(DISPLAY,230, 230, 230);
		
		WHITE = new Color(DISPLAY, 255,255,255);
		RED = new Color(DISPLAY, 255,0,0);
		BLUE= new Color(DISPLAY, 0,0,255);
		GREEN = new Color(DISPLAY, 0,255,0);
		DARK_GREY = new Color(DISPLAY, 170, 170, 170);

		VALID_OBJECT_COLOR = new Color(DISPLAY, 44,160,44);
		INVALID_OBJECT_COLOR = new Color(DISPLAY, 214, 39, 40);
		CER_OBJECT_COLOR = new Color(DISPLAY, 31,119,180);
		MFT_OBJECT_COLOR = new Color(DISPLAY, 100,0,100);
		CRL_OBJECT_COLOR = new Color(DISPLAY, 0, 100, 100);
		ROA_OBJECT_COLOR = new Color(DISPLAY, 148,103, 189);
		
		
		FILTER_MATCH = new Color(DISPLAY, 208, 233, 198);
		
		
		
		
	}
	
}
