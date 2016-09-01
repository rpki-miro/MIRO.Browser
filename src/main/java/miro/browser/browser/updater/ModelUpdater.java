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
package main.java.miro.browser.browser.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.miro.browser.logging.LoggerFactory;
import main.java.miro.browser.util.DirectoryFilter;
import main.java.miro.browser.util.FileExtensionFilter;
import main.java.miro.validator.ResourceCertificateTreeValidator;
import main.java.miro.validator.export.json.JsonExporter;
import main.java.miro.validator.export.rtr.RTRExporter;
import main.java.miro.validator.fetcher.ObjectFetcher;
import main.java.miro.validator.fetcher.RsyncFetcher;
import main.java.miro.validator.stats.ResultExtractor;
import main.java.miro.validator.stats.types.RPKIRepositoryStats;
import main.java.miro.validator.stats.types.Result;
import main.java.miro.validator.types.ResourceCertificateTree;
import main.java.miro.validator.types.TrustAnchorLocator;

import org.apache.commons.io.FileUtils;
import org.eclipse.rap.rwt.service.ApplicationContext;
import org.joda.time.DateTime;

public class ModelUpdater implements Runnable {

	private static Logger log = LoggerFactory.getLogger(ModelUpdater.class,
			Level.FINEST);

	private static final List<ModelObserver> modelObervers = new CopyOnWriteArrayList<ModelObserver>();

	private static final List<StatsObserver> statsObservers = new CopyOnWriteArrayList<StatsObserver>();

	public static final String MODEL_NAMES_KEY = "Model.names";

	public static final String STATS_NAMES_KEY = "Stats.names";

	public final static String STATS_NAME_PREFIX = "Stats.";

	final String CONFIG_FILE_LOCATION = "/var/data/MIRO/Browser/miro.browser.conf";
	
	final String BASE_DIRECTORY = "/var/data/MIRO/Browser/repositories/";

	final String PREFETCH_FILE_DIRECTORY = "/var/data/MIRO/Browser/prefetching/";

	public static final String EXPORT_DIRECTORY = "/var/data/MIRO/Browser/export/";

	final String STATS_ARCHIVE_DIRECTORY = "/var/data/MIRO/MIRO.Stats/repo/";

	private int UPDATE_PORT;

	private String inputPath;

	private String TALDirectory;


	private volatile boolean run;

	private ApplicationContext context;

	public ModelUpdater(ApplicationContext con) {
		run = true;
		context = con;
	}

	@Override
	public void run() {
		try {
			readConfig(CONFIG_FILE_LOCATION);
			ServerSocket triggerSocket = new ServerSocket(UPDATE_PORT);
			update();
			while(run) {
				waitForUpdateTrigger(triggerSocket);
				update();
			}
			triggerSocket.close();
		} catch (IOException e) {
			log.log(Level.SEVERE, e.toString(), e);
			log.log(Level.SEVERE, "Update failed");
		}
	}

	public void waitForUpdateTrigger(ServerSocket triggerSocket) {
		try {
			boolean updateTriggered = false;
			Socket clientSocket;
			while (!updateTriggered) {
				clientSocket = triggerSocket.accept();
				if (clientSocket.getInetAddress().isLoopbackAddress())
					updateTriggered = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		addModels();
		notifyObservers();
	}

	public void addModels() {
		log.log(Level.INFO, "Getting models");
		List<String> modelKeys = new ArrayList<String>();
		List<String> statsKeys = new ArrayList<String>();
		for (File talGroupDirectory : getTALGroupDirectories()) {
			addModelsFromTALGroup(talGroupDirectory, modelKeys, statsKeys);
		}
		addStatsToContext(getTotalStats(statsKeys), statsKeys);
		archiveStats(statsKeys);
		context.setAttribute(MODEL_NAMES_KEY, modelKeys);
		context.setAttribute(STATS_NAMES_KEY, statsKeys);
	}
	
	public File[] getTALGroupDirectories() {
		File talMainDir = new File(TALDirectory);
		File[] talGroupDirectories = talMainDir.listFiles(new DirectoryFilter());
		return talGroupDirectories;
	}
	
	public void addModelsFromTALGroup(File talDirectory, List<String> modelKeys, List<String> statsKeys) {
		ResourceCertificateTreeValidator validator;
		ObjectFetcher fetcher;
		TrustAnchorLocator tal;
		ResourceCertificateTree tree;
		try {
			fetcher = new RsyncFetcher(BASE_DIRECTORY, PREFETCH_FILE_DIRECTORY
					+ talDirectory.getName());
			validator = new ResourceCertificateTreeValidator(fetcher);

			for (String filename : talDirectory.list(new FileExtensionFilter("tal"))) {
				tal = new TrustAnchorLocator(talDirectory.getAbsolutePath() + "/" + filename);
				tree = validator.withTAL(tal);

				if (tree == null)
					continue;

				addModelToContext(tree, modelKeys);
				addStatsToContext(getRPKIRepositoryStats(tree), statsKeys);
				exportTreeAsJson(tree);
				exportRTR(tree);
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, "Could not process " + talDirectory.getName(), e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void addModelToContext(ResourceCertificateTree tree, List<String> modelKeys) {
		String key = getModelKey(tree);
		context.setAttribute(key, tree);
		modelKeys.add(key);
	}
	
	public void addStatsToContext(RPKIRepositoryStats stats, List<String> statsKeys) {
		String key = getStatsKey(stats);
		context.setAttribute(key, stats);
		statsKeys.add(key);
	}
	
	public void exportTreeAsJson(ResourceCertificateTree tree) {
		JsonExporter exporter = new JsonExporter(EXPORT_DIRECTORY + tree.getName());
		exporter.export(tree);
	}
	
	public void exportRTR(ResourceCertificateTree tree){
		RTRExporter exporter = new RTRExporter(EXPORT_DIRECTORY + "/roas/roas_" + tree.getName() + "_" + tree.getTimeStamp());
		exporter.export(tree);
	}
	
	public void archiveStats(List<String> statsKeys) {
		RPKIRepositoryStats stats;
		for(String statsKey : statsKeys) {
			stats = (RPKIRepositoryStats) context.getAttribute(statsKey);
			ResultExtractor.archiveStats(stats, STATS_ARCHIVE_DIRECTORY + stats.getName());
		}
	}
	
	public static String getModelKey(ResourceCertificateTree tree) {
		return tree.getName();
	}
	
	public static String getStatsKey(RPKIRepositoryStats stats) {
		return STATS_NAME_PREFIX + stats.getName();
	}

	public RPKIRepositoryStats getRPKIRepositoryStats(
			ResourceCertificateTree certTree) {
		ResultExtractor extractor = new ResultExtractor(certTree);
		extractor.count();
		return extractor.getRPKIRepositoryStats();
	}

	private RPKIRepositoryStats getTotalStats(List<String> statsKeys) {

		RPKIRepositoryStats totalStats = new RPKIRepositoryStats("Global RPKI", new DateTime(), "All", new Result("Total"),
				new ArrayList<Result>());
		for (String statsKey : statsKeys) {
			RPKIRepositoryStats stats = (RPKIRepositoryStats) context
					.getAttribute(statsKey);
			totalStats.addStats(stats);
		}
		return totalStats;
	}

	public void readConfig(String path) throws IOException {
		Properties prop = new Properties();

		InputStream is = new FileInputStream(path);

		prop.load(is);

		log.log(Level.FINE, "Reading config file at: {0}", path);
		String key;
		for (Object s : prop.keySet()) {
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
			default:
				break;
			}
		}
	}

	public void notifyObservers() {
		notifyModelObervers();
		notifyStatsObservers();
	}

	private static void notifyModelObervers() {
		log.log(Level.FINE, "Notifying {0} model observers",
				modelObervers.size());
		for (ModelObserver obs : modelObervers) {
			obs.notifyModelChange();
			log.log(Level.FINEST, "Notified observer: {0}", obs.getClass()
					.getName());
		}
	}
	

	private static void notifyStatsObservers() {
		log.log(Level.FINE, "Notifying {0} stats observers",
				statsObservers.size());
		for (StatsObserver obs : statsObservers) {
			obs.notifyStatsChange();
			log.log(Level.FINEST, "Notified observer: {0}", obs.getClass()
					.getName());
		}
	}

	public static boolean addObserver(Object obs, ObserverType type) {
		switch (type) {
		case MODEL:
			return modelObervers.add((ModelObserver) obs);
		case STATS:
			return statsObservers.add((StatsObserver) obs);
		default:
			return false;
		}
	}

	public static boolean removeObserver(Object obs, ObserverType type) {
		switch (type) {
		case MODEL:
			return modelObervers.remove((ModelObserver) obs);
		case STATS:
			return statsObservers.remove((StatsObserver) obs);
		default:
			return false;
		}
	}

	private void setInputdir(String key) {
		inputPath = key;
		log.log(Level.FINE, "Set inputDir: {0}", key);
	}

	private void setPort(String port2) {
		UPDATE_PORT = Integer.parseInt(port2);
		log.log(Level.FINE, "Set port: {0}", port2);

	}

	private void setTALDir(String key) {
		TALDirectory = key;
		log.log(Level.FINE, "Set TALDirectory: {0}", key);
	}
}
