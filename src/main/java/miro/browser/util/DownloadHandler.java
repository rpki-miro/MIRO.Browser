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
package main.java.miro.browser.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.miro.browser.logging.LoggerFactory;
import main.java.miro.validator.export.json.ByteArrayToHexSerializer;
import main.java.miro.validator.export.json.CRLSerializer;
import main.java.miro.validator.export.json.CertificateObjectJsonSerializer;
import main.java.miro.validator.export.json.IpResourceSetSerializer;
import main.java.miro.validator.export.json.ManifestSerializer;
import main.java.miro.validator.export.json.RepositoryObjectSerializer;
import main.java.miro.validator.export.json.ResourceHoldingObjectSerializer;
import main.java.miro.validator.export.json.RoaSerializer;
import main.java.miro.validator.export.json.ValidationResultsSerializer;
import main.java.miro.validator.types.CRLObject;
import main.java.miro.validator.types.CertificateObject;
import main.java.miro.validator.types.ManifestObject;
import main.java.miro.validator.types.RepositoryObject;
import main.java.miro.validator.types.ResourceHoldingObject;
import main.java.miro.validator.types.RoaObject;
import main.java.miro.validator.types.ValidationResults;
import net.ripe.ipresource.IpResourceSet;

import org.apache.commons.io.IOUtils;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.service.ServiceHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DownloadHandler {

	private static Logger log = LoggerFactory.getLogger(DownloadHandler.class,
			Level.FINEST);
	
	
	public void sendDownload(RepositoryObject obj, boolean subtree) {
		InputStream in = objectToStream(obj,subtree);
		runDownloadService(in,obj.getFilename());
    }

	public void sendDownload(List<RepositoryObject> objects) {
		List<InputStream> stream_collection = new ArrayList<InputStream>();
		for(RepositoryObject obj : objects) {
			stream_collection.add(objectToStream(obj, false));
		}
		SequenceInputStream seq_stream = new SequenceInputStream(java.util.Collections.enumeration(stream_collection));
		runDownloadService(seq_stream, "object_list");
	}

	public void sendDownload(String filepath) {
		try {
			File file = new File(filepath);
			InputStream stream = new FileInputStream(file);
			runDownloadService(stream,file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.log(Level.WARNING, e.toString());
		}
	}
	
	private InputStream objectToStream(RepositoryObject obj, boolean subtree) {
		try {
			String text = objectToJson(obj,subtree);
			StringBuffer sb = new StringBuffer(text);
			return new ByteArrayInputStream(sb.toString().getBytes("ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ByteArrayInputStream("ERROR: Unsupported Encoding".getBytes()); 
		}
	}
	
	private String objectToJson(RepositoryObject obj, boolean subtree) {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.registerTypeAdapter(RepositoryObject.class, new RepositoryObjectSerializer());
		builder.registerTypeAdapter(ResourceHoldingObject.class,
				new ResourceHoldingObjectSerializer());
		builder.registerTypeAdapter(CertificateObject.class, new CertificateObjectJsonSerializer(subtree));
		builder.registerTypeAdapter(ManifestObject.class, new ManifestSerializer());
		builder.registerTypeAdapter(CRLObject.class, new CRLSerializer());
		builder.registerTypeAdapter(RoaObject.class, new RoaSerializer());
		builder.registerTypeAdapter(ValidationResults.class, new ValidationResultsSerializer());
		builder.registerTypeAdapter(IpResourceSet.class, new IpResourceSetSerializer());
		builder.registerTypeAdapter(byte[].class, new ByteArrayToHexSerializer());
		Gson gson = builder.create();
		return gson.toJson(obj, RepositoryObject.class);
	}
	
	private void runDownloadService(InputStream s, String filename) {
		DownloadService service = new DownloadService(s, filename);
        service.register();
        RWT.getClient().getService(JavaScriptExecutor.class).execute("window.location=\"" + service.getURL() +  "\";" );
	}
	

    private final class DownloadService implements ServiceHandler {

        private final String filename;

        private String id;
        
        private InputStream stream;

        public DownloadService(InputStream strm, String filename) {
        	this.filename = filename;
            this.id = calculateId();
            this.stream = strm;
        }

        public String getURL() {
            return RWT.getServiceManager().getServiceHandlerUrl(getId());
        }

        private String getId() {
            return id;
        }

        private String calculateId() {
            return String.valueOf(System.currentTimeMillis()) + filename.length();
        }

        public boolean register() {
            try {
                RWT.getServiceManager().registerServiceHandler(getId(), this);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        private boolean unregister() {
            try {
                RWT.getServiceManager().unregisterServiceHandler(getId());
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void service(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            try {
            	setResponseMetadata(response);
            	writeDataToStream(response.getOutputStream());
            } catch (Exception e) {
            } finally {
                unregister();
            }
        }
        
        public void setResponseMetadata(HttpServletResponse response) {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + filename
                        + "\"");
        }
        
        public void writeDataToStream(ServletOutputStream out) throws IOException {
                IOUtils.copy(stream, out);
                stream.close();
                out.flush();
                out.close();
        }

    }

}
