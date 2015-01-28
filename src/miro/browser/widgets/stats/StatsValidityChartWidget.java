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
package miro.browser.widgets.stats;

import miro.browser.resources.Colors;
import miro.validator.stats.types.RPKIRepositoryStats;

import org.eclipse.rap.addons.d3chart.ChartItem;
import org.eclipse.rap.addons.d3chart.PieChart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class StatsValidityChartWidget extends Composite {
	
	private PieChart validityChart;

	public StatsValidityChartWidget(Composite parent, int style) {
		super(parent, style);
		
		validityChart = new PieChart( this, SWT.BORDER );
		GridData gridData = new GridData(400,300);
		validityChart.setLayoutData(gridData);
		
	}
	
	
	
	
	
	
	public void showRPKIRepositoryStats(RPKIRepositoryStats stats) {
		
		
		
		
		validityChart.setInnerRadius( 0.6f );
		ChartItem item1 = new ChartItem( validityChart );
		item1.setText( "Firefox" );
		item1.setColor(Colors.BLUE);
		item1.setValue( 23 );
	}

}
