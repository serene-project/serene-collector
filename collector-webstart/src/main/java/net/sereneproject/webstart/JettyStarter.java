/**
 * Copyright (c) 2012, Serene Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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

/**
 * Starts a Jetty server with our application.
 * 
 * @author gehel
 */
public class JettyStarter {

	/** Port used by the Jetty server. */
	private static final int SERVER_PORT = 8080;
	/** Name of the WAR file to deploy. */
	private static final String WAR_NAME = "collector-webapp-0.1.0.BUILD-SNAPSHOT.war";
	/** Buffer size used to copy files. */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * Starts the server.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {

		Server server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(SERVER_PORT);
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
			InputStream warIn = cl.getResourceAsStream(WAR_NAME);
			byte[] buf = new byte[BUFFER_SIZE];
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
