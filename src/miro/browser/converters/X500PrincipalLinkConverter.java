package miro.browser.converters;

import javax.security.auth.x500.X500Principal;

import org.eclipse.core.databinding.conversion.IConverter;

public class X500PrincipalLinkConverter implements IConverter {

	@Override
	public Object getFromType() {
		return X500Principal.class;
	}

	@Override
	public Object getToType() {
		return String.class;
	}

	@Override
	public Object convert(Object fromObject) {
		if(fromObject == null) {
			return "";
		}
		
		X500Principal princ = (X500Principal)fromObject;
		return "<a>" + princ.toString() + "</a>";
	}

}
