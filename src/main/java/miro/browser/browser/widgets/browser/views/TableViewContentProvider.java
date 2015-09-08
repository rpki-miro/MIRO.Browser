package main.java.miro.browser.browser.widgets.browser.views;

import java.util.ArrayList;
import java.util.List;

import main.java.miro.validator.types.CertificateObject;
import main.java.miro.validator.types.ResourceCertificateTree;
import main.java.miro.validator.types.ResourceHoldingObject;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TableViewContentProvider implements
		IStructuredContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		ResourceCertificateTree tree = (ResourceCertificateTree) inputElement;
		
		List<ResourceHoldingObject> objects = new ArrayList<ResourceHoldingObject>();
		List<ResourceHoldingObject> currentLevel = new ArrayList<ResourceHoldingObject>();
		List<ResourceHoldingObject> nextLevel = new ArrayList<ResourceHoldingObject>();
		
		currentLevel.add(tree.getTrustAnchor());
		
		while(!currentLevel.isEmpty()){
			for(ResourceHoldingObject obj : currentLevel){
				objects.add(obj);
				if(obj instanceof CertificateObject) {
					nextLevel.addAll(((CertificateObject)obj).getChildren());
				}
			}
		currentLevel = nextLevel;
		nextLevel = new ArrayList<ResourceHoldingObject>();
		}
		return objects.toArray(new ResourceHoldingObject[]{});
	}

}
