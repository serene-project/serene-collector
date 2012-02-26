package net.sereneproject.webstart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sereneproject.collector.web.utils.ResourceAnchor;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyStarter {

	public static void main(String[] args) throws Exception {

		int port = 8080;

		Server server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		server.setConnectors(new Connector[] { connector });

		System.setSecurityManager(null);

		ClassLoader cl = ResourceAnchor.class.getClassLoader();
		String warFileName = null;
		try {
			File tmpFile = File.createTempFile("app-web", ".war");
			warFileName = tmpFile.getAbsolutePath();
			OutputStream warOut = new FileOutputStream(tmpFile);
			// TODO: War file name must be retrieved from somewhere (to get
			// correct version)
			InputStream warIn = cl.getResourceAsStream("collector-webapp-0.1.0.BUILD-SNAPSHOT.war");
			byte[] buf = new byte[1024];
			int len;
					while ((len = warIn.read(buf)) > 0) {
				warOut.write(buf, 0, len);
			}
			warIn.close();
			warOut.close();
		} catch (FileNotFoundException ex) {
			// TODO: Handle exception properly
			System.err
					.println(ex.getMessage() + " in the specified directory.");
		} catch (IOException e) {
			// TODO: Handle exception properly
			System.err.println(e.getMessage());
		}

		WebAppContext webapp = new WebAppContext();
		webapp.setWar(warFileName);
		webapp.setContextPath(URIUtil.SLASH);
		WebAppClassLoader loader = new WebAppClassLoader(
				JettyStarter.class.getClassLoader(), webapp);
		webapp.setClassLoader(loader);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.addHandler(webapp);
		server.setHandler(contexts);

		server.start();

	}

}
