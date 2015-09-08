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
package miro.browser.widgets.browser.display;

import main.java.miro.validator.types.CertificateObject;
import main.java.miro.validator.types.ResourceHoldingObject;
import main.java.miro.validator.types.RoaObject;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;


/**
 * Listens for selection changes in a View and populates the tables in CertificateDisplay
 * or RoaDisplay
 * @author ponken
 *
 */
@SuppressWarnings("serial")
public class TableListener implements ISelectionChangedListener {

	private DetailViewContainer display;
	
	public TableListener(DetailViewContainer b) {
		display = b;
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		
		/*Get selected object*/
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		ResourceHoldingObject obj  = (ResourceHoldingObject) selection.getFirstElement();
		
		/*Populate tables in appropriate Display*/
		if(obj instanceof CertificateObject){
			display.getCertificateDisplay().populateTables((CertificateObject)obj);
		}
		if(obj instanceof RoaObject){
			display.getRoaDisplay().populateTables((RoaObject)obj);
		}
	}

}
