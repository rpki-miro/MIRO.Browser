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
package main.java.miro.browser.browser.widgets.browser.display;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
/**
 * This listener modifies the height of DisplayWidgets and their containing InformationFields.
 * This is necessary because the DisplayWidgets have dynamic String inputs, which can vary in length.
 * A longer String requires a taller Widget to display it.
 * @author ponken
 *
 */
public class HeightModifier implements Listener{
	
	private InformationField field;

	public HeightModifier(InformationField f) {
		field = f;
	}
	public void modifyText(ModifyEvent event) {

	}
	@Override
	public void handleEvent(Event event) {
		
		GC gc;
		String textString = null;
		Text s = (Text) event.widget;
		int lineHeight = s.getLineHeight();
		
		Rectangle cd = s.getClientArea();
		int lineWidth = s.getClientArea().width;
		if(event.widget instanceof Text){
			textString = s.getText();
			gc = new GC(s);
		} else if(event.widget instanceof Link) {
			Link l = (Link) event.widget;
			textString = l.getText();
			gc = new GC(l);
		} else {
			return;
		}
		

		int newLineCount = StringUtils.countMatches(textString, "\n");
		if(newLineCount > 0){
//			textString = textString.replace("\n", " ");
//			s.setText(textString);
		}
		
		newLineCount = StringUtils.countMatches(textString, "\n");
		if (textString == null | textString.length() == 0) {
			textString = "None";
			
			if(event.widget instanceof Text){
				((Text)event.widget).setText(textString);
			} else {
				((Link)event.widget).setText(textString);
			}
		}
		
		
		
		
		RowData rowData = (RowData) field.getLayoutData();
		
		// Calculate new height, based on strinWidth
		int stringWidth = gc.stringExtent(textString).x;

		// Divide by our width, factor is how many lines we expect from this
		// input
		double factor = stringWidth
				/ (double) (lineWidth);

		int minLines = field.getMinLines();
		int lines = Math.max((int)factor, minLines);
		
		// Calculate our new preferred height
		int height = (int) (Math.ceil(lines) * lineHeight);
		for (int i = 0; i < newLineCount; i++) {
			height += lineHeight;
		}
//		rowData.height = height;
		
		
//		if (height == 0 | height >= minLines) {
//			rowData.height = height;
//		} else if (height < minLines) {
//			rowData.height = minLines;
//		}
//		field.setLayoutData(rowData);

		Composite c = field.getParent();
		while (!(c instanceof DisplayWidget)) {
			c = c.getParent();
		}

		DisplayWidget dw = (DisplayWidget) c;
		dw.getParent().layout();
		dw.getParent().getParent().layout();
		Point size = dw.computeSize(dw.getSize().x, SWT.DEFAULT);
//		Point size = dw.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point wat = dw.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT);

		ScrolledComposite scroller = (ScrolledComposite) dw.getParent();
		scroller.setMinHeight(size.y);
		dw.layout(true);
		dw.pack();
	}

}
