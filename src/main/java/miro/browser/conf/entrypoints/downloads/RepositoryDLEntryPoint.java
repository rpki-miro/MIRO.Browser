package main.java.miro.browser.conf.entrypoints.downloads;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import main.java.miro.browser.browser.updater.ModelUpdater;
import main.java.miro.browser.util.DownloadHandler;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.widgets.Composite;

public class RepositoryDLEntryPoint extends AbstractEntryPoint {

	@Override
	protected void createContents(Composite parent) {
		Realm.runWithDefault(SWTObservables.getRealm(parent.getDisplay()), new RepositoryDLRunnable());
	}

	private class RepositoryDLRunnable implements Runnable {

		@Override
		public void run() {
			HttpServletRequest request = RWT.getRequest();
			String key = request.getParameter("key");
			String filepath = getModelFilepath(key);
			if(isFail(filepath))
				handleNullCase();
			else {
				DownloadHandler dlHandler = new DownloadHandler();
				dlHandler.sendDownload(filepath);
			}
		}

		private String getModelFilepath(String key) {
			if(key == null)
				return "";
			return ModelUpdater.EXPORT_DIRECTORY + key;
		}

		private boolean isFail(String filepath) {
			if(filepath == "")
				return true;
			if(!(new File(filepath)).exists()){
				return true;
			}
			return false;
		}

		private void handleNullCase() {
			//TODO figure out how to handle 404 case. Probably have to look into DownloadHandler and ClientServices in general to find a 
			//good solution
		}
	}
}
