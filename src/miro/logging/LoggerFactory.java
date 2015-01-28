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
package miro.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class LoggerFactory {

	
	private static String logDir = "/var/data/MIRO/MIRO.Browser/logs/miro.log";
	private static FileHandler filehandler;
	
	
	public static Logger getLogger(Class clazz, Level lev) {
		Logger log = Logger.getLogger( clazz.getName() );
		
		log.setUseParentHandlers(false);
		log.setLevel(lev);
		removeHandlers(log);
		log.addHandler(getConsoleHandler(lev));
		log.addHandler(getFileHandler(lev, logDir));
		return log;
	}
	
	private static void removeHandlers(Logger log){
		for(Handler h : log.getHandlers()){
			log.removeHandler(h);
		}
	}
	
	
	private static ConsoleHandler getConsoleHandler(Level lev){
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(lev);
		return ch;
	}
	
	private static FileHandler getFileHandler(Level lev, String filepath) {
		
		if(filehandler == null){
			try {
				filehandler = new FileHandler(filepath);
				filehandler.setLevel(lev);
				filehandler.setFormatter(new SimpleFormatter());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return filehandler;
	}
	
}
