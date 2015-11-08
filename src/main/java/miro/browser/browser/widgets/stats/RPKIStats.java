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
package main.java.miro.browser.browser.widgets.stats;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.miro.browser.browser.resources.RGBs;
import main.java.miro.browser.browser.updater.ModelUpdater;
import main.java.miro.browser.browser.updater.ObserverType;
import main.java.miro.browser.browser.updater.StatsObserver;
import main.java.miro.browser.logging.LoggerFactory;
import main.java.miro.validator.stats.types.RPKIRepositoryStats;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class RPKIStats extends Composite implements StatsObserver{
	
	private static final Logger log = LoggerFactory.getLogger(RPKIStats.class, Level.FINEST);
	
	private CTabFolder statsTabFolder;
	
	public RPKIStats(Composite parent, int style) {
		super(parent, style);
		ModelUpdater.addObserver(this, ObserverType.STATS);
		setLayout(new GridLayout());
		statsTabFolder = new CTabFolder(this, SWT.NONE);
		showNewestStats();
	}

	private void showRPKIRepositoryStats(List<String> statsNames) {
		for(CTabItem tab : statsTabFolder.getItems()) {
			tab.dispose();
		}

		CTabItem newTab;
		StatsWidget statsWidget;
		RPKIRepositoryStats stats;
		for(String statName : statsNames) {
			stats = (RPKIRepositoryStats) RWT.getApplicationContext().getAttribute(statName);
			newTab = new CTabItem(statsTabFolder, SWT.NONE);
			newTab.setText(stats.getName());
			
			statsWidget = new StatsWidget(statsTabFolder, SWT.NONE);
			statsWidget.showRPKIRepositoryStats(stats);
			newTab.setControl(statsWidget);
			
		}
		layout();
	}
	
	public void showNewestStats() {
		List<String> names = (List<String>) RWT.getApplicationContext().getAttribute(ModelUpdater.STATS_NAMES_KEY);
		if(names != null){
			showRPKIRepositoryStats(names);
		}
	}
	
	@Override
	public void notifyStatsChange() {
		if(isDisposed()){
			log.log(Level.FINE,"Widget is disposed");
			ModelUpdater.removeObserver(this, ObserverType.STATS);
			return;
		}
		
		//We have to run this as an asyncExec, so the SWT UI thread can do the update.
		//We cannot do the update in this thread, since that breaks the SWT thread model
		//(only SWT UI thread is allowed to make UI changes)
		this.getDisplay().asyncExec(new Runnable() {
		    public void run() {
		    	showNewestStats();
		    }
		});
	}
	
	public void selectTab(String key) {
		for(CTabItem tab : statsTabFolder.getItems()){
			if(tab.getText().equals(key)){
				statsTabFolder.setSelection(tab);
				break;
			}
		}
	}
}
