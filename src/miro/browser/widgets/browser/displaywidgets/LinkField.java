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
					Method method = null;
					try {
						method = TableViewer.class.getDeclaredMethod("doFindItem", Object.class);
						method.setAccessible(true);
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						Object result = method.invoke(browser.getViewerContainer().getTableBrowser().getTableViewer(), item.getData());
						System.out.println(result.toString());
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Event ev = new Event();
					ev.item = item.getParentItem();
					tree.notifyListeners(SWT.Selection, ev);
					browser.getViewerContainer().showTreeBrowser();
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
