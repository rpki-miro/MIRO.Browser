package miro.browser.download;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import miro.validator.export.json.ByteArrayToHexSerializer;
import miro.validator.export.json.CRLSerializer;
import miro.validator.export.json.CertificateObjectJsonSerializer;
import miro.validator.export.json.IpResourceSetSerializer;
import miro.validator.export.json.ManifestSerializer;
import miro.validator.export.json.RepositoryObjectSerializer;
import miro.validator.export.json.RoaSerializer;
import miro.validator.export.json.ValidationResultsSerializer;
import miro.validator.types.CRLObject;
import miro.validator.types.CertificateObject;
import miro.validator.types.ManifestObject;
import miro.validator.types.RepositoryObject;
import miro.validator.types.RoaObject;
import miro.validator.types.ValidationResults;
import net.ripe.ipresource.IpResourceSet;

import org.apache.commons.io.IOUtils;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.UrlLauncher;
import org.eclipse.rap.rwt.service.ServiceHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DownloadHandler {
	
	public boolean sendDownload(RepositoryObject obj) {

        DownloadService service = new DownloadService(obj);
        service.register();

        UrlLauncher launcher = RWT.getClient().getService(UrlLauncher.class);
        launcher.openURL(service.getURL());
        return true;
    }

    private static final class DownloadService implements ServiceHandler {

        private final RepositoryObject obj;

        private final String filename;
        private String id;
        private String text;

        public DownloadService(RepositoryObject obj) {
            this.obj = obj;
            this.filename = obj.getFilename() + ".json";
            this.id = calculateId();
            
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();
			builder.registerTypeAdapter(RepositoryObject.class, new RepositoryObjectSerializer());
			builder.registerTypeAdapter(CertificateObject.class, new CertificateObjectJsonSerializer());
			builder.registerTypeAdapter(ManifestObject.class, new ManifestSerializer());
			builder.registerTypeAdapter(CRLObject.class, new CRLSerializer());
			builder.registerTypeAdapter(RoaObject.class, new RoaSerializer());
			builder.registerTypeAdapter(ValidationResults.class, new ValidationResultsSerializer());
			builder.registerTypeAdapter(IpResourceSet.class, new IpResourceSetSerializer());
			builder.registerTypeAdapter(byte[].class, new ByteArrayToHexSerializer());
			Gson gson = builder.create();
			text = gson.toJson(obj, RepositoryObject.class);
        }

        public String getURL() {
            return RWT.getServiceManager().getServiceHandlerUrl(getId());
        }

        private String getId() {
            return id;
        }

        private String calculateId() {
            return String.valueOf(System.currentTimeMillis()) + obj.getFilename().length();
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
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + filename
                        + "\"");
                StringBuffer sb = new StringBuffer(text);
                ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes("ASCII"));
                ServletOutputStream out = response.getOutputStream();
                
                IOUtils.copy(in, out);
                
                in.close();
                out.flush();
                out.close();
                
            } catch (Exception e) {
            } finally {
                unregister();
            }
        }

    }
}
