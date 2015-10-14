package main.java.miro.browser.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	public boolean sendDownload(RepositoryObject obj) {
		try {
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();
			builder.registerTypeAdapter(RepositoryObject.class, new RepositoryObjectSerializer());
			builder.registerTypeAdapter(ResourceHoldingObject.class, new ResourceHoldingObjectSerializer());
			builder.registerTypeAdapter(CertificateObject.class,
					new CertificateObjectJsonSerializer());
			builder.registerTypeAdapter(ManifestObject.class, new ManifestSerializer());
			builder.registerTypeAdapter(CRLObject.class, new CRLSerializer());
			builder.registerTypeAdapter(RoaObject.class, new RoaSerializer());
			builder.registerTypeAdapter(ValidationResults.class, new ValidationResultsSerializer());
			builder.registerTypeAdapter(IpResourceSet.class, new IpResourceSetSerializer());
			builder.registerTypeAdapter(byte[].class, new ByteArrayToHexSerializer());
			Gson gson = builder.create();
			String text = gson.toJson(obj, RepositoryObject.class);
			StringBuffer sb = new StringBuffer(text);
			ByteArrayInputStream in;
			in = new ByteArrayInputStream(sb.toString().getBytes("ASCII"));
			DownloadService service = new DownloadService(in, obj.getFilename());
			runDownloadService(service);
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
    }
	
	public boolean sendDownload(String filepath) {
		try {
			File file = new File(filepath);
			FileInputStream stream = new FileInputStream(file);
			DownloadService service = new DownloadService(stream, file.getName());
			runDownloadService(service);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		/*
		 * TODO:
		 * DownloadService should take a byte stream (the data that is to be sent to the user) and a filename
		 * Then here we open the filestream and derive filename from filepath, open a DownloadService and execute the js code
		 */
		return false;
	}
	
	public void runDownloadService(DownloadService service) {
        service.register();
        RWT.getClient().getService(JavaScriptExecutor.class).execute("window.location=\"" + service.getURL() +  "\";" );
	}

    private static final class DownloadService implements ServiceHandler {

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
