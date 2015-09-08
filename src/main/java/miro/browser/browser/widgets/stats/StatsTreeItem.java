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

import java.util.ArrayList;
import java.util.List;

import main.java.miro.validator.stats.StatsKeys;
import main.java.miro.validator.stats.types.Result;

public class StatsTreeItem {

	private String key;
	
	private int amount;
	
	private float fraction;
	
	private List<StatsTreeItem> objectTypeBreakdown;
	
	private List<StatsTreeItem> statusBreakdown;
	
	private StatsTreeItem(){
		
	}
	
	public String getKey() {
		return key;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public float getFraction() {
		return fraction;
	}
	
	public List<StatsTreeItem> getObjectTypeBreakdown() {
		return objectTypeBreakdown;
	}

	public List<StatsTreeItem> getStatusBreakdown() {
		return statusBreakdown;
	}

	public static StatsTreeItem toStatsTreeItem(Result result) {
		StatsTreeItem root = new StatsTreeItem();
		
		root.key = StatsKeys.TOTAL_OBJECTS;
		root.amount = result.getObjectCount(StatsKeys.TOTAL_OBJECTS);
		root.fraction = 1f;
		
		root.objectTypeBreakdown = new ArrayList<StatsTreeItem>();
		
		root.objectTypeBreakdown.add(createChild(root,StatsKeys.TOTAL_CER_OBJECTS,
				result.getObjectCount(StatsKeys.TOTAL_CER_OBJECTS)));
		
		root.objectTypeBreakdown.add(createChild(root,StatsKeys.TOTAL_MFT_OBJECTS,
				result.getObjectCount(StatsKeys.TOTAL_MFT_OBJECTS)));
		
		root.objectTypeBreakdown.add(createChild(root,StatsKeys.TOTAL_CRL_OBJECTS,
				result.getObjectCount(StatsKeys.TOTAL_CRL_OBJECTS)));
		
		root.objectTypeBreakdown.add(createChild(root,StatsKeys.TOTAL_ROA_OBJECTS,
				result.getObjectCount(StatsKeys.TOTAL_ROA_OBJECTS)));
		
		
		root.statusBreakdown = new ArrayList<StatsTreeItem>();
		
		StatsTreeItem valids = createChild(root, StatsKeys.TOTAL_VALID_OBJECTS, result.getObjectCount(StatsKeys.TOTAL_VALID_OBJECTS));
		valids.objectTypeBreakdown = new ArrayList<StatsTreeItem>();
		valids.objectTypeBreakdown.add(createChild(valids,StatsKeys.VALID_CER_OBJECTS, result.getObjectCount(StatsKeys.VALID_CER_OBJECTS)));
		valids.objectTypeBreakdown.add(createChild(valids,StatsKeys.VALID_MFT_OBJECTS, result.getObjectCount(StatsKeys.VALID_MFT_OBJECTS)));
		valids.objectTypeBreakdown.add(createChild(valids,StatsKeys.VALID_CRL_OBJECTS, result.getObjectCount(StatsKeys.VALID_CRL_OBJECTS)));
		valids.objectTypeBreakdown.add(createChild(valids,StatsKeys.VALID_ROA_OBJECTS, result.getObjectCount(StatsKeys.VALID_ROA_OBJECTS)));
		
		StatsTreeItem invalids = createChild(root, StatsKeys.TOTAL_INVALID_OBJECTS, result.getObjectCount(StatsKeys.TOTAL_INVALID_OBJECTS));
		invalids.objectTypeBreakdown = new ArrayList<StatsTreeItem>();
		invalids.objectTypeBreakdown.add(createChild(invalids,StatsKeys.INVALID_CER_OBJECTS, result.getObjectCount(StatsKeys.INVALID_CER_OBJECTS)));
		invalids.objectTypeBreakdown.add(createChild(invalids,StatsKeys.INVALID_MFT_OBJECTS, result.getObjectCount(StatsKeys.INVALID_MFT_OBJECTS)));
		invalids.objectTypeBreakdown.add(createChild(invalids,StatsKeys.INVALID_CRL_OBJECTS, result.getObjectCount(StatsKeys.INVALID_CRL_OBJECTS)));
		invalids.objectTypeBreakdown.add(createChild(invalids,StatsKeys.INVALID_ROA_OBJECTS, result.getObjectCount(StatsKeys.INVALID_ROA_OBJECTS)));
		
		StatsTreeItem warnings = createChild(root, StatsKeys.TOTAL_WARNING_OBJECTS, result.getObjectCount(StatsKeys.TOTAL_WARNING_OBJECTS));
		warnings.objectTypeBreakdown = new ArrayList<StatsTreeItem>();
		warnings.objectTypeBreakdown.add(createChild(warnings,StatsKeys.WARNING_CER_OBJECTS, result.getObjectCount(StatsKeys.WARNING_CER_OBJECTS)));
		warnings.objectTypeBreakdown.add(createChild(warnings,StatsKeys.WARNING_MFT_OBJECTS, result.getObjectCount(StatsKeys.WARNING_MFT_OBJECTS)));
		warnings.objectTypeBreakdown.add(createChild(warnings,StatsKeys.WARNING_CRL_OBJECTS, result.getObjectCount(StatsKeys.WARNING_CRL_OBJECTS)));
		warnings.objectTypeBreakdown.add(createChild(warnings,StatsKeys.WARNING_ROA_OBJECTS, result.getObjectCount(StatsKeys.WARNING_ROA_OBJECTS)));
		
		
		root.statusBreakdown.add(valids);
		root.statusBreakdown.add(invalids);
//		root.statusBreakdown.add(warnings);
		
		
		return root;
	}
	
	private static float calculateFraction(float total, float partial) {

		if(total == 0){
			return 0f;
		}
		float fraction = partial/total;
		
		return fraction;
	}
	
	public static StatsTreeItem getFakeRoot(StatsTreeItem realRoot) {
		StatsTreeItem fake = new StatsTreeItem();
		
		fake.objectTypeBreakdown = new ArrayList<StatsTreeItem>();
		fake.objectTypeBreakdown.add(realRoot);
		
		fake.statusBreakdown = new ArrayList<StatsTreeItem>();
		fake.statusBreakdown.add(realRoot);
		
		return fake;
	}
	
	private static StatsTreeItem createChild(StatsTreeItem parent, String childKey, int childAmount) {
		StatsTreeItem child = new StatsTreeItem();
		child.key = childKey;
		child.amount = childAmount;
		child.fraction = calculateFraction(parent.amount, child.amount);
		return child;
		
	}

	public void setFraction(float f) {
		fraction = f;
	}
	
	
	
}
