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

import miro.browser.resources.Fonts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class FilterButtonContainer extends Composite {
	
	Button selectedButton;

	public FilterButtonContainer(Composite parent, int style) {
		super(parent, style);
		init();
		initButtons();
	}

	private void init() {
		RowLayout layout = new RowLayout();
		setLayout(layout);
		
	}

	private void initButtons() {
		
		Button filenameButton = new Button(this, SWT.RADIO);
		filenameButton.setText("Filename");
		filenameButton.setFont(Fonts.STANDARD_FONT);
		RowData rowData = new RowData();
		filenameButton.setLayoutData(rowData);
		filenameButton.addListener(SWT.Selection,new SelectedButtonListener());
		
		
		
		Button resourceButton = new Button(this, SWT.RADIO);
		resourceButton.setText("Resource");
		resourceButton.setFont(Fonts.STANDARD_FONT);
		rowData = new RowData();
		resourceButton.setLayoutData(rowData);
		resourceButton.addListener(SWT.Selection,new SelectedButtonListener());
		
	}
	
	
	
	public Button getSelected(){
		return selectedButton;
	}
	
	private class SelectedButtonListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			selectedButton = (Button) event.widget;
		}
		
	}

	public void clearSelection() {

		//Clear button selection
		//Unselect
		if(selectedButton != null){
			selectedButton.setSelection(false);
		}
		//Dereference
		selectedButton = null;

	}

}









