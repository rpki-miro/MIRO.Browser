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

import java.util.ArrayList;
import java.util.Map;

import main.java.miro.browser.browser.resources.MagicNumbers;
import main.java.miro.browser.browser.widgets.browser.RPKIBrowser;
import main.java.miro.browser.util.ByteArrayPrinter;
import main.java.miro.validator.types.CertificateObject;
import main.java.miro.validator.types.ManifestObject;
import main.java.miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


/**
 * Wrapper class for a TableViewer that displays the filename:hash entries in a .mft file in a table.
 * @author ponken
 *
 */
public class ManifestFilesTable extends Composite {
	
	private TableViewer tableViewer;
	
	private RPKIBrowser browser;
	
	public ManifestFilesTable(Composite parent, int style, RPKIBrowser b) {
		super(parent, style);
		setData(RWT.CUSTOM_VARIANT, "displayContent");
		browser = b;
		setLayout(new FillLayout());	

		tableViewer = new TableViewer(this, SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL);
		tableViewer.setContentProvider(new ManifestFilesContentProvider());
		createColumns(tableViewer.getTable());
	}
	
	public void createColumns(Table table) {
		table.setHeaderVisible(true);
		TableViewerColumn newCol;
		newCol = new TableViewerColumn(tableViewer, new TableColumn(table,SWT.NONE));
		newCol.getColumn().setWidth(MagicNumbers.MFT_HASH_LIST_FILENAME_COLUMN_WIDTH);
		newCol.getColumn().setResizable(false);
		newCol.getColumn().setMoveable(false);
		newCol.getColumn().setText("Filename");
		newCol.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				FileHashPair pair = (FileHashPair)cell.getElement();
				cell.setText(pair.getFilename());
			}
		});
		
		newCol = new TableViewerColumn(tableViewer, new TableColumn(table, SWT.NONE));
		newCol.getColumn().setWidth(MagicNumbers.MFT_HASH_LIST_HASHVALUE_COLUMN_WIDTH);
		newCol.getColumn().setResizable(false);
		newCol.getColumn().setMoveable(false);
		newCol.getColumn().setText("Hash");
		newCol.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				FileHashPair pair = (FileHashPair)cell.getElement();
				cell.setText(ByteArrayPrinter.bytesToHex(pair.getHash()));
			}
		});
		
		table.addListener(SWT.DefaultSelection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				FileHashPair pair = (FileHashPair) ((TableItem)event.item).getData();
				if(pair.filename.endsWith(".crl")){
					for(CTabItem tab : browser.getDisplayContainer().getItems()) {
						if(tab.getText().equals("CRL")){
							CTabFolder folder = (CTabFolder) browser.getDisplayContainer();
							folder.setSelection(tab);
							return;
						}
					}
				} else {
					CertificateObject obj = (CertificateObject) browser.getViewerContainer().getSelectedObject();
					for(ResourceHoldingObject kid : obj.getChildren()){
						if(kid.getFilename().equals(pair.filename)){
							browser.getViewerContainer().setSelection(kid);
							return;
						}
					}
				}
			}
		});
	}

	public void setInput(ManifestObject manifest) {
		tableViewer.setInput(manifest);
	}
	
	private ArrayList<FileHashPair> createPairList(Map<String, byte[]> files) {
		ArrayList<FileHashPair> pairList = new ArrayList<FileHashPair>();
		for(String filename : files.keySet()) {
			pairList.add(new FileHashPair(filename, files.get(filename)));
		}
		return pairList;
	}
	
	private class ManifestFilesContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if(inputElement == null){
				return null;
			}
			ManifestObject mft = (ManifestObject)inputElement;
			return createPairList(mft.getFiles()).toArray();
		}
	}
	
	private class FileHashPair {
		
		private String filename;
		private byte[] hash;
		
		public FileHashPair(String f, byte[] h){
			filename = f;
			hash = h;
		}
		
		public String getFilename() {
			return filename;
		}
		
		public byte[] getHash() {
			return hash;
		}
		

	}
}
