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
package miro.browser.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import miro.browser.updater.ModelObserver;
import miro.logging.LoggerFactory;
import types.Certificate;
import types.ResourceHolder;


/*CLASS IS OBSOLETE, DO NOT USE ANYMORE*/
public class CertificateChainModel extends Observable{
	
	private static final Logger log = LoggerFactory.getLogger(CertificateChainModel.class, Level.FINEST);
	
	private static ArrayList<CertificateChainModel> MODELS = new ArrayList<CertificateChainModel>();
	private static String LAST_UPDATE_TIME = "";
	private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static ArrayList<ModelObserver> observers = new ArrayList<ModelObserver>();
	
	
	private Certificate trustAnchor;
	private String name;

	public CertificateChainModel(String name, Certificate trustAnchor){
		this.trustAnchor = trustAnchor;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	//Add an empty fake root on top, because treeviewers do not display the top element
	public Certificate createTreeModel() {
		Certificate fake_root = new Certificate();
		ArrayList<ResourceHolder> fake_children = new ArrayList<ResourceHolder>();
		fake_children.add(trustAnchor);
		fake_root.setChildren(fake_children);
		return fake_root;
	}
	
	//Write lock, update models, notify observers
	public static void setModels(ArrayList<CertificateChainModel> newModels){
		readWriteLock.writeLock().lock();
		MODELS = newModels;
		readWriteLock.writeLock().unlock();
		log.log(Level.FINE,"Models changed");
		notifyModelObservers();
	}
	
	public static void addObserver(ModelObserver obj){
		observers.add(obj);
	}
	
	public static String getLastUpdateTime(){
		readWriteLock.readLock().lock();
		String timestamp = new String(LAST_UPDATE_TIME);
		readWriteLock.readLock().unlock();
		return timestamp;
	}
	
	public static void setLastUpdateTime(String s){
		readWriteLock.writeLock().lock();
		LAST_UPDATE_TIME = s;
		readWriteLock.writeLock().unlock();
		log.log(Level.FINE, "Set LastUpdateTime: {0}",s);
	}
	
	public static void notifyModelObservers(){
		log.log(Level.FINE,"Notifying {0} observers",observers.size());
		for(ModelObserver obs : observers){
			obs.notifyModelChange();
			log.log(Level.FINEST,"Notified observer: {0}",obs.getClass().getName());
		}
	}

	
	public static CertificateChainModel getModelByName(String name){
		readWriteLock.readLock().lock();
		for(CertificateChainModel m : MODELS){
			if(m.name.equals(name)){
				readWriteLock.readLock().unlock();
				return m;
			}
		}
		readWriteLock.readLock().unlock();
		return null;
	}
	
	public static String[] getModelNames(){
		readWriteLock.readLock().lock();
		String[] names = new String[MODELS.size()];
		for (int j = 0; j < names.length; j++) {
			names[j] = MODELS.get(j).name;
		}
		readWriteLock.readLock().unlock();
		return names;
	}
}
