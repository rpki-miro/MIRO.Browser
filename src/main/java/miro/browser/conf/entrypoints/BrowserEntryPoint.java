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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import main.java.miro.browser.browser.updater.ModelUpdater;
import main.java.miro.browser.browser.widgets.browser.RPKIBrowser;
import main.java.miro.browser.browser.widgets.browser.filter.filters.AttributeFilter;
import main.java.miro.browser.browser.widgets.browser.filter.filters.FileTypeFilter;
import main.java.miro.browser.browser.widgets.browser.filter.filters.FilterKeys.FilterKey;
import main.java.miro.browser.browser.widgets.browser.filter.filters.ResourceCertificateTreeFilter;
import main.java.miro.browser.browser.widgets.browser.filter.filters.ResourceHoldingObjectFilter;
import main.java.miro.browser.browser.widgets.browser.filter.filters.ValidationStatusFilter;
import main.java.miro.browser.util.DownloadHandler;
import main.java.miro.validator.types.CertificateObject;
import main.java.miro.validator.types.RepositoryObject;
import main.java.miro.validator.types.ResourceCertificateTree;
import main.java.miro.validator.types.ResourceHoldingObject;
import net.ripe.rpki.commons.validation.ValidationStatus;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
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
			HttpServletRequest request = RWT.getRequest();
			String taKey = getTrustAnchorKey(request);
			List<ResourceHoldingObjectFilter> filters = getFilters(request);
			if(isDownloadRequest(request)){
				handleDownloadRequest(taKey,filters);
			} else {
				handleGUIRequest(taKey,filters);
			}
		}

		private String getTrustAnchorKey(HttpServletRequest request) {
			return request.getParameter("trustAnchor");
		}

		private boolean isKnownKey(String taKey) {
			List<String> ta_names = (List<String>) RWT.getApplicationContext().getAttribute(ModelUpdater.MODEL_NAMES_KEY);
			return ta_names.contains(taKey);
		}

		private List<ResourceHoldingObjectFilter> getFilters(HttpServletRequest request) {
			List<ResourceHoldingObjectFilter> filters = new ArrayList<ResourceHoldingObjectFilter>();
			ResourceHoldingObjectFilter filter;
			String filetype = request.getParameter("filetype");
			filter = getFileTypeFilter(filetype);
			if(filter != null)
				filters.add(filter);

			String attributeKey = request.getParameter("attributeKey");
			String attributeValue = request.getParameter("attributeValue");
			filter = getAttributeFilter(attributeKey, attributeValue);
			if(filter != null)
				filters.add(filter);

			String validationStatus = request.getParameter("validationStatus");
			filter = getValidationStatusFilter(validationStatus);
			if(filter != null)
				filters.add(filter);

			return filters;
		}

		private boolean isDownloadRequest(HttpServletRequest request) {
			return request.getParameter("dl") != null;
		}

		private void handleGUIRequest(String taKey, List<ResourceHoldingObjectFilter> filters) {
			RPKIBrowser browser = new RPKIBrowser(parent, SWT.NONE);
			if(isKnownKey(taKey)){
				ResourceCertificateTreeFilter treeFilter = new ResourceCertificateTreeFilter(false);
				treeFilter.addFilters(filters);
				browser.getBrowserControlBar().selectTrustAnchor(taKey);
				browser.getFilterWidget().setFilter(treeFilter);
			}
		}

		private void handleDownloadRequest(String taKey, List<ResourceHoldingObjectFilter> filters) {
			if(isKnownKey(taKey)){
				ResourceCertificateTree certTree = (ResourceCertificateTree) RWT
						.getApplicationContext().getAttribute(taKey);
				DownloadHandler dlHandler = new DownloadHandler();
				if (filters.isEmpty()) {
					dlHandler.sendDownload(ModelUpdater.EXPORT_DIRECTORY + certTree.getName());
				} else {
					List<RepositoryObject> results = filterObjects(certTree, filters);
					dlHandler.sendDownload(results);
				}
			}
		}

		private List<RepositoryObject> filterObjects(ResourceCertificateTree certTree,
				List<ResourceHoldingObjectFilter> filters) {
			List<RepositoryObject> results = new ArrayList<RepositoryObject>();
			List<ResourceHoldingObject> current = new ArrayList<ResourceHoldingObject>();
			List<ResourceHoldingObject> next = new ArrayList<ResourceHoldingObject>();
			current.add(certTree.getTrustAnchor());
			while(!current.isEmpty()){
				for (ResourceHoldingObject obj : current) {
					if(matchesAllFilters(filters, obj))
						results.add(obj);
					if(obj instanceof CertificateObject)
						next.addAll(((CertificateObject)obj).getChildren());
				}
				current = next;
				next = new ArrayList<ResourceHoldingObject>();
			}
			return results;
		}
		
		private boolean matchesAllFilters(List<ResourceHoldingObjectFilter> filters, ResourceHoldingObject obj) {
			for (ResourceHoldingObjectFilter filter : filters) {
				if (!filter.isMatch(obj))
					return false;
			}
			return true;
		}

		private ResourceHoldingObjectFilter getValidationStatusFilter(String validationStatus) {
			if(validationStatus == null)
				return null;
			String[] statuses = validationStatus.split(",");
			List<ValidationStatus> statusList = new ArrayList<ValidationStatus>();
			for(String status : statuses) {
				switch(status) {
				case "passed":
					statusList.add(ValidationStatus.PASSED);
					break;
				case "error":
					statusList.add(ValidationStatus.ERROR);
					break;
				case "warning":
					statusList.add(ValidationStatus.WARNING);
					break;
				}
			}

			if(!statusList.isEmpty())
				return new ValidationStatusFilter(statusList);
			else
				return null;
		}

		private ResourceHoldingObjectFilter getAttributeFilter(String attributeKey,
				String attributeValue) {
			if(attributeKey == null | attributeValue == null)
				return null;
			switch(attributeKey) {
			case "filename":
				return new AttributeFilter(attributeValue,FilterKey.FILENAME);
			case "location":
				return new AttributeFilter(attributeValue,FilterKey.REMOTE_LOCATION);
			case "subject":
				return new AttributeFilter(attributeValue,FilterKey.SUBJECT);
			case "issuer":
				return new AttributeFilter(attributeValue,FilterKey.ISSUER);
			case "serial_nr":
				return new AttributeFilter(attributeValue,FilterKey.SERIAL_NUMBER);
			case "resource":
				return new AttributeFilter(attributeValue,FilterKey.RESOURCE);
			default:
				return null;
			}
		}

		private FileTypeFilter getFileTypeFilter(String filetype) {
			if(filetype == null)
					return null;
			switch(filetype) {
			case "cer":
				return new FileTypeFilter(FilterKey.CER_FILES);
			case "roa":
				return new FileTypeFilter(FilterKey.ROA_FILES);
			case "all":
				return new FileTypeFilter(FilterKey.ALL_FILES);
			default:
				return null;
			}
		}
	}
}
