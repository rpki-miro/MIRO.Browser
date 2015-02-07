package miro.browser.widgets.browser.displaywidgets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import miro.browser.widgets.browser.RPKIBrowserView;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.TableViewer;
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
	
	private Link link;

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
	}
	
	public Link getLink(){
		return link;
	}

	@Override
	public void bindField(IObservableValue selection, DataBindingContext dbc) {
		IObservableValue fieldObservable = SWTObservables.observeText(link);

		IObservableValue detailObservable = PojoProperties.value( (Class) selection.getValueType(), propertyName, type).observeDetail(selection);
		UpdateValueStrategy modelToUi = new UpdateValueStrategy();
		
		modelToUi.setConverter(converter);
		dbc.bindValue(fieldObservable, detailObservable, null, modelToUi);
	}
	
	private class IssuerLinkListener implements Listener {

		@Override
		public void handleEvent(Event event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
