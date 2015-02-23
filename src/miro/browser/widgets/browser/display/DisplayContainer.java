package miro.browser.widgets.browser.display;

import miro.browser.widgets.browser.RPKIBrowserView;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class DisplayContainer extends TabFolder{

	private CertificateDisplay certificateDisplay;
	
	private RoaDisplay roaDisplay;

	public DisplayContainer(Composite parent, int style) {
		super(parent, style);
		setLayout(new RowLayout());
	}
	
	
	public void initDisplays(RPKIBrowserView b){
		certificateDisplay = new CertificateDisplay(this,b);
		roaDisplay = new RoaDisplay(this, b);
	}
	
	public void bindDisplays(IObservableValue selection,
			DataBindingContext dbc){
		certificateDisplay.bindToResourceHolder(selection, dbc);
		roaDisplay.bindToResourceHolder(selection, dbc);
	}

	public CertificateDisplay getCertificateDisplay() {
		return certificateDisplay;
	}
	
	public RoaDisplay getRoaDisplay() {
		return roaDisplay;
	}

	public void clearTabs() {
		for(TabItem tab : getItems()){
			tab.dispose();
		}
	}
}
