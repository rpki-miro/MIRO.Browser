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
package miro.browser.widgets.browser.filter.filters;

import java.util.List;

import miro.validator.types.ResourceHoldingObject;
import net.ripe.rpki.commons.validation.ValidationStatus;

public class ValidationStatusFilter implements ResourceHoldingObjectFilter{
	
	private List<ValidationStatus> filteredStats;
	
	public ValidationStatusFilter(List<ValidationStatus> stats) {
		filteredStats = stats;
	}

	@Override
	public boolean isMatch(ResourceHoldingObject obj) {
		boolean selected = false;
		ValidationStatus status = obj.getValidationResults().getValidationStatus();
		if(filteredStats.contains(status)){
			selected = true;
		}
		return selected; 
	}

}
