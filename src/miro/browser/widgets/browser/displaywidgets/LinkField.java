package miro.browser.widgets.browser.displaywidgets;

import miro.browser.widgets.browser.RPKIBrowserView;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class LinkField extends InformationField {
	
	private RPKIBrowserView browser;
	
	Link link;

	public LinkField(Composite parent, int style, Class t, Class cont,
			String labelText, int mH, String name, IConverter conv, RPKIBrowserView b) {
		super(parent, style, t, cont, labelText, mH, name, conv);
		link = new Link(this, style | SWT.READ_ONLY);
		browser = b;
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0,0);
		layoutData.bottom = new FormAttachment(100,0);
		layoutData.left = new FormAttachment(label);
		layoutData.right = new FormAttachment(100,0);
		link.setLayoutData(layoutData);
		link.addListener(SWT.CHANGED, new HeightModifier(this));
		
		link.setData(RWT.CUSTOM_VARIANT, "browserLink");
		
		link.addListener(SWT.MouseUp, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Tree tree = browser.getViewerContainer().getTreeBrowser().getTree();
				TreeItem item = tree.getSelection()[0];
				TreeItem parent = item.getParentItem();
				
				if(parent != null){
					tree.setSelection(item.getParentItem());
					Event ev = new Event();
					ev.item = item.getParentItem();
					tree.notifyListeners(SWT.Selection, ev);
				}
			}
		});
	}

	@Override
	public void bindField(IObservableValue selection, DataBindingContext dbc) {
		IObservableValue fieldObservable = SWTObservables.observeText(link);

		IObservableValue detailObservable = PojoProperties.value( (Class) selection.getValueType(), propertyName, type).observeDetail(selection);
		UpdateValueStrategy modelToUi = new UpdateValueStrategy();
		
		modelToUi.setConverter(converter);
		dbc.bindValue(fieldObservable, detailObservable, null, modelToUi);
	}
	
	
}
