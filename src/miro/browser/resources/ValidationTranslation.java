package miro.browser.resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;

import net.ripe.rpki.commons.validation.ValidationCheck;

public class ValidationTranslation {
	
	private static HashMap<String, String> translations;
	
	
	public static void readTranslation(){
		translations = new HashMap<String, String>();
		try { 
			InputStream is = ValidationTranslation.class.getClassLoader().getResourceAsStream("resources/validation_en.properties");
			BufferedReader bufferedReader  = new BufferedReader(new InputStreamReader(is));
			String line = bufferedReader.readLine(); 
			while(line != null){
				if(line.startsWith("#") | line.equals("")){
					line = bufferedReader.readLine();
					continue;
				}
				
				
				String[] parts = line.split("=");
				translations.put(parts[0], parts[1]);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		readTranslation();
	}

	public static String getTranslation(ValidationCheck check) {
		
		String key = check.getKey() +  "." + check.getStatus().toString().toLowerCase();
		String result = translations.get(key);
		
		if(result != null & check.getParams().length != 0){
			result = MessageFormat.format(result, check.getParams());
		}
		result = result == null ? key : result;
		return result;
	}
	

}
