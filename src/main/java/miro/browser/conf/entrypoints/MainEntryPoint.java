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
package main.java.miro.browser.conf.entrypoints;

import main.java.miro.browser.browser.resources.Colors;
import main.java.miro.browser.browser.resources.Images;
import main.java.miro.browser.browser.resources.MagicNumbers;
import main.java.miro.browser.browser.widgets.ContentContainer;
import main.java.miro.browser.browser.widgets.browser.RPKIBrowser;
import main.java.miro.browser.browser.widgets.header.HeaderBar;
import main.java.miro.browser.browser.widgets.header.LinkContainer;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

public class MainEntryPoint extends AbstractEntryPoint{
	/**
	 * @wbp.parser.entryPoint
	 */
	protected void createContents(Composite parent) {
		Realm.runWithDefault(SWTObservables.getRealm(parent.getDisplay()), new MainRunnable(parent));
	}
	
	
	private class MainRunnable implements Runnable {

		private Composite parent;

		public MainRunnable(Composite p) {
			parent = p;
		}

		public void run() {
			// Enable push, for async. events
			final ServerPushSession pushSession = new ServerPushSession();
			pushSession.start();

			// Init resources
			Colors.init(parent.getDisplay());
			Images.init(parent.getDisplay());

			FormLayout layout = new FormLayout();
			parent.setLayout(layout);
			parent.setData(RWT.CUSTOM_VARIANT, "mainShell");

			HeaderBar header = new HeaderBar(parent, SWT.NONE);
			FormData layoutData = new FormData();
			layoutData.left = new FormAttachment(0, MagicNumbers.SHELL_OUTER_GAPS);
			layoutData.right = new FormAttachment(100, -MagicNumbers.SHELL_OUTER_GAPS);
			layoutData.top = new FormAttachment(0, MagicNumbers.SHELL_OUTER_GAPS);
			header.setLayoutData(layoutData);

			LinkContainer linkContainer = new LinkContainer(header, SWT.NONE);

			ContentContainer content = new ContentContainer(parent, SWT.NONE);
			layoutData = new FormData();
			layoutData.top = new FormAttachment(header, 0);
			layoutData.left = new FormAttachment(0, MagicNumbers.SHELL_OUTER_GAPS);
			layoutData.bottom = new FormAttachment(100, -MagicNumbers.SHELL_OUTER_GAPS);
			layoutData.right = new FormAttachment(100, -MagicNumbers.SHELL_OUTER_GAPS);
			content.setLayoutData(layoutData);

			// init browser
			RPKIBrowser browser = new RPKIBrowser(content, SWT.NONE);
			content.setBrowser(browser);

			// Show browser default
			content.showBrowser();

			linkContainer.setContentContainer(content);

			parent.layout();
		}
	}
}
