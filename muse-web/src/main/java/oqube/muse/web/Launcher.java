package oqube.muse.web;

import java.util.List;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

import fr.lifl.utils.CommandLine;

public class Launcher {

  /**
   * Launches the current lab
   */
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    String dir = ".";
    String header = "";
    String headerFile = "";
    String footer = "";
    String footerFile = "";
    String outputEncoding = System.getProperty("file.encoding");
    String inputEncoding = outputEncoding;
    CommandLine cli = new CommandLine();
    cli.addOptionSingle('h'); // header
    cli.addOptionSingle('H'); // header file
    cli.addOptionSingle('f'); // footer
    cli.addOptionSingle('F'); // footer file
    cli.addOptionSingle('E'); // output encoding
    cli.addOptionSingle('e'); // input encoding
    cli.parseOptions(args);
    if (cli.isSet('h')) {
      header = (String) cli.getOption('h').getArgument();
    }
    if (cli.isSet('H')) {
      headerFile = (String) cli.getOption('H').getArgument();
    }
    if (cli.isSet('f')) {
      footer = (String) cli.getOption('f').getArgument();
    }
    if (cli.isSet('F')) {
      footerFile = (String) cli.getOption('F').getArgument();
    }
    if (cli.isSet('E'))
      outputEncoding = (String) cli.getOption('E').getArgument();
    if (cli.isSet('e'))
      inputEncoding = (String) cli.getOption('e').getArgument();
    List<String> dirs = cli.getArguments();
    if (!dirs.isEmpty()) {
      dir = "";
      for (String d : dirs) {
        dir = dir + d + ",";
      }
      // remove last comma
      dir = dir.substring(0, dir.length() - 1);
    }
    Server server = new Server();
    SocketConnector connector = new SocketConnector();
    connector.setPort(4444);
    server.setConnectors(new Connector[] { connector });
    ServletHandler hdl = new ServletHandler();
    ServletHolder hold = new ServletHolder(new MuseServlet());
    hold.setInitParameter("muse.directory", dir);
    hold.setInitParameter("muse.header", header);
    hold.setInitParameter("muse.header.file", headerFile);
    hold.setInitParameter("muse.footer", footer);
    hold.setInitParameter("muse.footer.file", footerFile);
    hold.setInitParameter("muse.output.encoding", outputEncoding);
    hold.setInitParameter("muse.input.encoding", inputEncoding);
    hold.setName("muse");
    hdl.addServletWithMapping(hold, "/");
    server.setHandler(hdl);
    try {
      server.start();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}
