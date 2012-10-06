/*
 * Copyright (c) 2007 - OQube / Arnaud Bailly This library is free software; you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * Created 4 nov. 07
 */
package oqube.muse.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

import fr.lifl.utils.CommandLine;

/**
 * Main application class for creating a server configured from a configuration
 * script (see {@link ServerConfigBuilder}.
 * 
 * @author nono
 * 
 */
public class Start {

  /**
   * @param args
   */
  public static void main(String[] args) {
    InputStream is = System.in;
    CommandLine cli = new CommandLine();
    cli.addOptionSingle('c'); // configuration file
    cli.parseOptions(args);
    if (cli.isSet('c'))
      try {
        is = new FileInputStream((String) cli.getOption('c')
                                             .getArgument());
      } catch (FileNotFoundException e) {
        System.err.println("cannot find file :" + e);
      }
    ServerConfigBuilder builder = new ServerConfigBuilder();
    try {
      builder.buildFromReader(new InputStreamReader(is));
    } catch (IOException e) {
      System.err.println("Error while configuring server " + e);
    }
    // create Server
    Server server = new Server();
    SocketConnector connector = new SocketConnector();
    connector.setPort(4444);
    server.setConnectors(new Connector[] { connector });
    ServletHandler hdl = new ServletHandler();
    int i = 0;
    for (Map.Entry<String, ServerConfig> e : builder.getConfigurations()
                                                    .entrySet()) {
      MuseRawServlet raw = new MuseRawServlet();
      MuseHTMLServer srv = new MuseHTMLServer();
      e.getValue()
       .configure(srv);
      raw.setServer(srv);
      ServletHolder hold = new ServletHolder(raw);
      hold.setName("muse" + i++);
      hdl.addServletWithMapping(hold, e.getKey());
      System.err.println("started servlet at " + e.getKey());
    }
    server.setHandler(hdl);
    try {
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

  }

}
