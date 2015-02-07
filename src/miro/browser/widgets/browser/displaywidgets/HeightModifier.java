package miro.browser.widgets.browser.displaywidgets;

import miro.browser.resources.MagicNumbers;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class HeightModifier implements Listener{
	
	
	private InformationField field;

	public HeightModifier(InformationField f) {
		field = f;
	}
	public void modifyText(ModifyEvent event) {

	}
	@Override
	public void handleEvent(Event event) {
		
		GC gc;
		String textString = null;
		if(event.widget instanceof Text){
			Text s = (Text) event.widget;
			textString = s.getText();
			gc = new GC(s);
		} else if(event.widget instanceof Link) {
			Link l = (Link) event.widget;
			textString = l.getText();
			gc = new GC(l);
		} else {
			return;
		}
		

		if (textString == null | textString.length() == 0) {
			textString = "None";
			
			if(event.widget instanceof Text){
				((Text)event.widget).setText(textString);
			} else {
				((Link)event.widget).setText(textString);
			}
		}

		int newLineCount = StringUtils.countMatches(textString, "\n");

		RowData rowData = (RowData) field.getLayoutData();
		// Calculate new height, based on strinWidth
		int stringWidth = gc.stringExtent(textString).x;

		// Divide by our width, factor is how many lines we expect from this
		// input
		double factor = stringWidth
				/ (double) (rowData.width - MagicNumbers.INF_FIELD_LABEL_WIDTH);

		// Calculate our new preferred height
		int height = (int) (Math.ceil(factor) * MagicNumbers.LINE_HEIGHT);
		for (int i = 0; i < newLineCount; i++) {
			height += 20;
		}
		int minHeight = field.getMinHeight();
		if (height == 0 | height >= minHeight) {
			rowData.height = height;
		} else if (height < minHeight) {
			rowData.height = minHeight;
		}
		field.setLayoutData(rowData);

		Composite c = field.getParent();
		while (!(c instanceof DisplayWidget)) {
			c = c.getParent();
		}

		DisplayWidget dw = (DisplayWidget) c;

		Point size = dw.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		ScrolledComposite scroller = (ScrolledComposite) dw.getParent();
		scroller.setMinHeight(size.y);
		dw.layout(true);

		
	}

}
