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
package miro.browser.widgets.browser.coolbar;

import miro.browser.resources.Fonts;
import miro.browser.widgets.browser.tree.ViewerContainer;
import miro.browser.widgets.browser.tree.TreeToggleObserver;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class FilterToggle extends Composite implements TreeToggleObserver{

	Button toggleButton;
	boolean toggle;
	ViewerContainer treeContainer;
	
	public FilterToggle(Composite parent, int style, ViewerContainer treeCont) {
		super(parent, style);
		treeContainer = treeCont;
		init();
	}

	private void init() {
		setLayout(new FillLayout());
		toggleButton = new Button(this, SWT.PUSH);
		toggleButton.setText("Show Filter");
		toggleButton.setFont(Fonts.STANDARD_FONT);
		
		toggleButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
//				treeContainer.toggle();
			}
		});
	}
	
	

	@Override
	public void notifyTreeToggle(boolean toggle) {
		if(!toggle){
			toggleButton.setText("Show Tree");

		} else {
			toggleButton.setText("Show Filter");
		}
		layout();
	}
	

}
