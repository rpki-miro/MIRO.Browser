package main.java.miro.browser.conf.entrypoints.widgets;

import main.java.miro.browser.browser.widgets.browser.RPKIBrowser;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

public class BrowserEntryPoint extends AbstractEntryPoint {

	@Override
	protected void createContents(Composite parent) {
		Realm.runWithDefault(SWTObservables.getRealm(parent.getDisplay()), new BrowserRunnable(parent));
	}
	
	private class BrowserRunnable implements Runnable {
		
		private Composite parent;
		
		public BrowserRunnable(Composite p) {
			parent = p;
		}

		@Override
		public void run() {
			parent.setLayout(new FillLayout());
			RPKIBrowser browser = new RPKIBrowser(parent, SWT.NONE);
		}
		
	}

}
