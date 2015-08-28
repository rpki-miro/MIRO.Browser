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
package miro.browser.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import miro.logging.LoggerFactory;
import miro.validator.ResourceCertificateTreeValidator;
import miro.validator.stats.ResultExtractor;
import miro.validator.stats.types.RPKIRepositoryStats;
import miro.validator.stats.types.Result;
import miro.validator.types.ResourceCertificateTree;

import org.apache.commons.io.FileUtils;
import org.eclipse.rap.rwt.service.ApplicationContext;

import types.RepositoryTree;
import exported.repository.reader.RepositoryTreeReader;

public class ModelUpdater implements Runnable {

	private static Logger log = LoggerFactory.getLogger(ModelUpdater.class, Level.FINEST);
	
	private static final List<ModelObserver> modelObervers = new CopyOnWriteArrayList<ModelObserver>();
	
	private static final List<StatsObserver> statsObservers = new CopyOnWriteArrayList<StatsObserver>();
	
	public static final String MODEL_NAMES_KEY = "Model.names";
	
	public static final String STATS_NAMES_KEY = "Stats.names";
	
	final String CONFIG_FILE_LOCATION = "/var/data/MIRO/MIRO.Browser/miro.browser_config";

	final String PREFETCH_URI_FILE_LOCATION = "/var/data/MIRO/MIRO.Browser/prefetchURIs";
	
	final String STATS_NAME_PREFIX = "Stats.";
	
	final String STATS_ARCHIVE_DIRECTORY = "/var/data/MIRO/MIRO.Stats/repo/"; 
	
	private static HashMap<String, String> modelNames;
	
	private int UPDATE_PORT; 
	
	private String inputPath;
	
	private String TALDirectory;
	
	private String exportPath;
	
	private volatile boolean run;
	
	private ApplicationContext context;
	
	public static boolean addObserver(Object obs, ObserverType type){
		switch (type) {
		case MODEL:
			return modelObervers.add((ModelObserver) obs);
		case STATS:
			return statsObservers.add((StatsObserver) obs);
		default:
			return false;
		}
	}
	
	public static boolean removeObserver(Object obs, ObserverType type){
		switch (type) {
		case MODEL:
			return modelObervers.remove((ModelObserver) obs);
		case STATS:
			return statsObservers.remove((StatsObserver) obs);
		default:
			return false;
		}
	}
	
	private static void notifyModelObervers() {
		log.log(Level.FINE,"Notifying {0} model observers",modelObervers.size());
		for(ModelObserver obs : modelObervers){
			obs.notifyModelChange();
			log.log(Level.FINEST,"Notified observer: {0}",obs.getClass().getName());
		}
	}
	
	private static void notifyStatsObservers() {
		log.log(Level.FINE, "Notifying {0} stats observers", statsObservers.size());
		for(StatsObserver obs : statsObservers){
			obs.notifyStatsChange();
			log.log(Level.FINEST,"Notified observer: {0}",obs.getClass().getName());
		}
	}
	
	public ModelUpdater(ApplicationContext con) {
		run = true;
		context = con;
	}
	
	@Override
	public void run() {
		log.log(Level.INFO, "Thread started");
		ServerSocket triggerSocket;
		Socket clientSocket;
		URI[] prefetchURIs;
		try {
			readConfig(CONFIG_FILE_LOCATION);
			triggerSocket = new ServerSocket(UPDATE_PORT);
			prefetchURIs = readPrefetchURIs(PREFETCH_URI_FILE_LOCATION);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.toString(),e);
			throw new RuntimeException(e);
		}
		
		update(prefetchURIs);
		while(run){
			try {
				clientSocket = triggerSocket.accept();
				if(clientSocket.getInetAddress().isLoopbackAddress()){
					update(prefetchURIs);
				} 
			} catch (IOException e) {
				log.log(Level.SEVERE, e.toString(),e);
				log.log(Level.SEVERE,"Update failed");
				continue;
			}
		}
		
		try {
			triggerSocket.close();
		} catch (IOException e) {
			log.log(Level.SEVERE,e.toString(),e);
		}
		log.log(Level.INFO,"Quitting thread");
	}
	
	public void notifyObservers() {
		notifyModelObervers();
		notifyStatsObservers();
	}
	
	public void update(URI[] prefetchURIs) {
		getModels(prefetchURIs);
		notifyObservers();
	}
	

	public void readModels() {
		
		File inputDirFile = new File(exportPath);
		String[] importFiles = inputDirFile.list();
		String[] modelNames = new String[importFiles.length];
		
		
		int nameIndex = 0;
		RepositoryTree repoTree = null;
		for (String filename : importFiles) {
			try {
				log.log(Level.FINE, "Reading json file: {0}",exportPath + filename);
				repoTree = RepositoryTreeReader.readJson(exportPath + filename);
				
				context.setAttribute(repoTree.getName(), repoTree);
				modelNames[nameIndex] = repoTree.getName();
				nameIndex++;
			} catch (Throwable e) {
				log.log(Level.SEVERE, e.toString(), e);
				continue;
			} 
		}
		context.setAttribute(MODEL_NAMES_KEY, modelNames);
	}
	
	
	public URI[] readPrefetchURIs(String path) {
 		BufferedReader br;
 		File prefetchFile = new File(path); 
 		List<URI> result = new ArrayList<URI>();
 		try {
			br = new BufferedReader(new FileReader(prefetchFile));
			String line = br.readLine();
			while(line != null){
				result.add(URI.create(line));
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			log.log(Level.SEVERE, e.toString(), e);
		}
 		return result.toArray(new URI[result.size()]);
	}
	
	public void cleanInputPath(){
		try {
			FileUtils.cleanDirectory(new File(inputPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getModels(URI[] uris){
		log.log(Level.INFO, "Getting models");

		RPKIRepositoryStats stats;
		ResourceCertificateTree certTree;
		int index = 0;
		String name = "";
		
		File[] talFiles = new File(TALDirectory).listFiles();
		String[] statsKeys = new String[talFiles.length];
		String[] modelKeys = new String[talFiles.length];
		String key;
		cleanInputPath();
		ResourceCertificateTreeValidator treeValidator = new ResourceCertificateTreeValidator(inputPath);
		treeValidator.preFetch(uris);
		for(File talFile : talFiles){
			
			/* Get repo name from TAL file, get ResourceCertificateTree, save it to application context and remember the key */
			name = getRepositoryName(talFile.getName());
			certTree = treeValidator.getTreeWithTAL(talFile.toString(), name);
			key = certTree.getName();
			context.setAttribute(key, certTree);
			modelKeys[index] = key;			
			
			/* Get stats about the tree, save them to disk, save them to context and remember the key */
			stats = getRPKIRepositoryStats(certTree);
			ResultExtractor.archiveStats(stats, STATS_ARCHIVE_DIRECTORY + name);
			key = STATS_NAME_PREFIX + name;
			context.setAttribute(key, stats);
			statsKeys[index] = key;

			index++;
		}
		
		/* Get global RPKI stats over all processed repositories */
		RPKIRepositoryStats totalStats = getTotalStats(statsKeys);
		key = STATS_NAME_PREFIX + totalStats.getName();
		context.setAttribute(key, totalStats);

		String[] allStatsKeys = prependToStringArray(statsKeys,key);
		Arrays.sort(statsKeys);
		
		context.setAttribute(MODEL_NAMES_KEY, modelKeys);
		context.setAttribute(STATS_NAMES_KEY, allStatsKeys);
	}
	
	public String[] prependToStringArray(String[] arr, String str) {
		String[] resultArr = new String[arr.length + 1];
		resultArr[0] = str;
		for(int i = 1; i< resultArr.length;i++){
			resultArr[i] = arr[i-1];
		}
		return resultArr;
	}
	
	public RPKIRepositoryStats getRPKIRepositoryStats(ResourceCertificateTree certTree){
		ResultExtractor extractor = new ResultExtractor(certTree);
		extractor.count();
		return extractor.getRPKIRepositoryStats();
	}
	
	private RPKIRepositoryStats getTotalStats(String[] statsKeys) {
		
		RPKIRepositoryStats totalStats = new RPKIRepositoryStats("All repositories", "-", "-", new Result("Total"), new ArrayList<Result>());
		for(String statsKey : statsKeys) {
			RPKIRepositoryStats stats = (RPKIRepositoryStats) context.getAttribute(statsKey);
			totalStats.addStats(stats);
		}
		return totalStats;
		
	}

	private String getRepositoryName(String name) {
		
		/* Use the filename without its extension suffix */
		if(name.indexOf(".") > 0){
			return name.substring(0,name.lastIndexOf("."));
		}
		return name;
	}

	public void readConfig(String path) throws IOException{
		Properties prop = new Properties();
		
		InputStream	is = new FileInputStream(path);
		
		prop.load(is);
		
		log.log(Level.FINE,"Reading config file at: {0}",path);
		String key;
		for(Object s : prop.keySet()){
			key = (String) s;
			switch (key) {
			case "port":
				setPort(prop.getProperty(key));
				break;
			case "inputDir":
				setInputdir(prop.getProperty(key));
				break;
			case "TALDir":
				setTALDir(prop.getProperty(key));
				break;
			case "exportDir":
				setExportPath(prop.getProperty(key));
				break;
			default:
				break;
			}
		}
	}
	
	public void readNames(String path) {
		modelNames = new HashMap<String, String>();
		Properties prop = new Properties();
		
		try {
			InputStream is = new FileInputStream(path);
			prop.load(is);
			log.log(Level.FINE,"Reading names file at: {0}",path);
			String key;
			for(Object s : prop.keySet()){
				key = (String) s;
				modelNames.put(key, prop.getProperty(key));
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, e.toString(), e);
		}
		
	}

	private void setInputdir(String key) {
		inputPath = key;
		log.log(Level.FINE,"Set inputDir: {0}",key);
	}

	private void setPort(String port2) {
		UPDATE_PORT = Integer.parseInt(port2);
		log.log(Level.FINE,"Set port: {0}",port2);
		
	}
	
	private void setExportPath(String key) {
		exportPath = key;
		log.log(Level.FINE, "Set exportPath: {0}", key);
	}
	
	private void setTALDir(String key) {
		TALDirectory = key;
		log.log(Level.FINE, "Set TALDirectory: {0}", key);
	}
}
