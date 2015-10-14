package main.java.miro.browser.conf.entrypoints.downloads;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.widgets.Composite;

public class RepositoryEntryPoint extends AbstractEntryPoint {

	@Override
	protected void createContents(Composite parent) {
		Realm.runWithDefault(SWTObservables.getRealm(parent.getDisplay()), new DownloadRunnable());
	}

	private class DownloadRunnable implements Runnable {

		@Override
		public void run() {
			HttpServletRequest request = RWT.getRequest();
			String foo = request.getParameter( "foo" );
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
