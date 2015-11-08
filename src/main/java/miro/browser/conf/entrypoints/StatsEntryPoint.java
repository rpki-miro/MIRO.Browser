package main.java.miro.browser.conf.entrypoints;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import main.java.miro.browser.browser.updater.ModelUpdater;
import main.java.miro.browser.browser.widgets.stats.RPKIStats;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

public class StatsEntryPoint extends AbstractEntryPoint {

	@Override
	protected void createContents(Composite parent) {
		Realm.runWithDefault(SWTObservables.getRealm(parent.getDisplay()), new StatsRunnable(parent));
	}

	private class StatsRunnable implements Runnable {

		private Composite parent;

		public StatsRunnable(Composite p) {
			parent = p;
		}

		@Override
		public void run() {
			ScrolledComposite scroller = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
			scroller.setExpandHorizontal(true);
			scroller.setExpandVertical(true);

			RPKIStats statsContainer = new RPKIStats(scroller, SWT.NONE);
			scroller.setContent(statsContainer);
			scroller.setMinSize(statsContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

			HttpServletRequest request = RWT.getRequest();
			String taKey = getTrustAnchorKey(request);
			if(isKnownKey(taKey))
				statsContainer.selectTab(taKey);
		}

		private String getTrustAnchorKey(HttpServletRequest request) {
			return request.getParameter("trustAnchor");
		}

		private boolean isKnownKey(String taKey) {
			List<String> ta_names = (List<String>) RWT.getApplicationContext().getAttribute(ModelUpdater.STATS_NAMES_KEY);
			return ta_names.contains(ModelUpdater.STATS_NAME_PREFIX + taKey);
		}

	}

}
