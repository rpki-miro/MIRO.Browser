package main.java.miro.browser.browser.converters;

import net.ripe.ipresource.Asn;

import org.eclipse.core.databinding.conversion.IConverter;

public class ASNConverter implements IConverter {

	@Override
	public Object getFromType() {
		return Asn.class;
	}

	@Override
	public Object getToType() {
		return String.class;
	}

	@Override
	public Object convert(Object fromObject) {
		Asn as = (Asn) fromObject;
		
		if(as == null){
			return "";
		} else {
			return as.toString();
		}
	}

}
