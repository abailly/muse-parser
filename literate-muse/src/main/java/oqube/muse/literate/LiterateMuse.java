/**
 * Copyright (C) 2006 - OQube / Arnaud Bailly This library is free software; you
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
 * Created 21 sept. 2006
 */
package oqube.muse.literate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;

import oqube.muse.Publish;
import oqube.muse.PublisherFactory;
import fr.lifl.utils.CommandLine;

/**
 * Application for handling literate source files. This class is mainly a
 * user-friendly and command-line interface wrapper for
 * {@link oqube.muse.literate.LiteratePublisher} and {@link oqube.muse.Publish}
 * classes.
 * 
 * @author nono
 * 
 */
public class LiterateMuse implements Runnable {

  private Publish publish;

  private LiteratePublisher literate;

  private String postProcessor;

  public LiterateMuse() {
    this.publish = new Publish();
    this.literate = new LiteratePublisher();
    this.publish.setPublisher(literate);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    CommandLine cli = new CommandLine();
    cli.addOptionSingle('s'); // sources output directory
    cli.addOptionSingle('d');// documentation output directory
    cli.addOptionSingle('H');// header file to prepend in all outputs
    cli.addOptionSingle('F');// footer file to append in all outputs
    cli.addOptionSingle('B');// header file to prepend in all document
    cli.addOptionSingle('T');// footer file to append in all document
    cli.addOption('m'); // multifile option
    cli.addOption('l'); // lenient option
    // options from Publish
    cli.addOptionMultiple('x'); // exclude file patterns
    cli.addOptionSingle('e'); // input encoding
    cli.addOptionSingle('f'); // documentation format
    cli.addOption('F'); // force publishing
    cli.addOptionSingle('E'); // output encoding
    cli.addOptionSingle('p'); // post process output
    cli.addOption('h');
    cli.addOption('?');
    cli.parseOptions(args);
    /* instantiate publisher */
    LiterateMuse muse = new LiterateMuse();
    if (cli.isSet('?') || cli.isSet('h') || args.length == 0) {
      usage(null);
      return;
    }
    /* configure publish part */
    if (cli.isSet('F'))
      muse.forcePublishing();
    if (cli.isSet('e'))
      muse.publish.setInputEncoding((String) cli.getOption('e').getArgument());
    if (cli.isSet('E'))
      muse.publish.setOutputEncoding((String) cli.getOption('E').getArgument());
    if (cli.isSet('d'))
      muse.publish.setOutputDirectory(new File((String) cli.getOption('d')
          .getArgument()));
    else
      muse.publish.setOutputDirectory(new File("."));
    if (cli.isSet('f'))
      muse.literate.setPublisher(PublisherFactory.instance((String) cli
          .getOption('f').getArgument()));
    else
      muse.literate.setPublisher(PublisherFactory.instance("xhtml"));
    if (cli.isSet('x'))
      muse.publish.excludePatterns((List) cli.getOption('x').getArgument());
    /* add files to process */
    for (Iterator i = cli.getArguments().iterator(); i.hasNext();) {
      File f = new File((String) i.next());
      try {
        muse.publish.addFile(f);
      } catch (IOException e) {
        usage(e);
        System.exit(1);
      }
    }
    /* configure literate part */
    if (cli.isSet('s'))
      muse.literate.setOutputDirectory(new File((String) cli.getOption('s')
          .getArgument()));
    else
      muse.literate.setOutputDirectory(new File("."));
    if (cli.isSet('B'))
      muse.literate.setHeader(muse.read((String) cli.getOption('B')
          .getArgument()));
    if (cli.isSet('T'))
      muse.literate.setFooter(muse.read((String) cli.getOption('T')
          .getArgument()));
    if (cli.isSet('H'))
      muse.literate.setSourceHeader(muse.read((String) cli.getOption('H')
          .getArgument()));
    if (cli.isSet('F'))
      muse.literate.setSourceFooter(muse.read((String) cli.getOption('H')
          .getArgument()));
    if (cli.isSet('m'))
      muse.literate.setMultifile(true);
    if (cli.isSet('l'))
      muse.literate.setLenient(true);
    if (cli.isSet('p'))
      muse.setPostProcessor((String) cli.getOption('p').getArgument());
    /* run */
    muse.run();
  }

  public void setPostProcessor(String processor) {
    this.postProcessor = processor;
  }

  /**
   * Read the content of the file named <code>string</code>, if it exists and
   * return it as a String.
   * 
   * @param string
   *          name of file. Maybe null.
   * @return content of file or null if it cannot be read.
   */
  public String read(String string) {
    if (string == null)
      return null;
    File f = new File(string);
    if (!f.exists() || !f.canRead())
      return null;
    try {
      InputStreamReader r = new InputStreamReader(new FileInputStream(f),
          publish.getInputEncoding());
      StringWriter w = new StringWriter();
      char[] buf = new char[1024];
      int ln = 0;
      while ((ln = r.read(buf)) > -1) {
        w.write(buf, 0, ln);
      }
      r.close();
      return w.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static void usage(Throwable object) {
    System.err
        .println("Usage: java -cp <classpath> oqube.muse.literate.LiterateMuse [options] <files>");
    System.err
        .println("       Processes <files> and generate sources and optionally formatted ");
    System.err.println("       documents from these files and directories. ");
    System.err.println();
    System.err.println("Options:");
    System.err
        .println(" -s <dir>    : output directory for generated sources. If non existent");
    System.err.println("               will be created (default: .)");
    System.err
        .println(" -d <dir>    : output directory for generated documents. If non existent");
    System.err.println("               will be created (default: .)");
    System.err.println(" -H <file>   : header prepended to generated sources ");
    System.err.println(" -F <file>   : footer appended to generated sources ");
    System.err
        .println(" -B <file>   : header prepended to generated documents ");
    System.err
        .println(" -T <file>   : footer appended to generated documents ");
    System.err
        .println(" -m          : multifile option. Sources fragments are accumulated for");
    System.err
        .println("               for all processed files before generation.");
    System.err.println(" -x <pat1>(:<pat>)*  ");
    System.err
        .println("             : list of pattern for excluding matching paths for processing. ");
    System.err
        .println("               (default: \"(.*\\.svn.*):(.*~):(.*\\#.*):(.*/\\..*)\")");
    System.err
        .println(" -e <code>   : encoding of input files (default: platform dependent)");
    System.err
        .println(" -E <code>   : encoding of output files (default: platform dependent)");
    System.err
        .println(" -f <format> : format name for generating documentation.");
    System.err
        .println(" -p <command> : post-processing command to run after publishing finishes.");
    System.err.println(" -?|-h       : this documentation.");
    if (object != null)
      object.printStackTrace();
  }

  public void run() {
    /* link everything */
    publish.run();
    postProcess();
  }

  private void postProcess() {
    if (postProcessor == null)
      return;
    ProcessBuilder builder = new ProcessBuilder();
    final String[] args = (postProcessor + " \""
        + literate.getOutputDirectory().getAbsolutePath() + "\"")
        .split("[ \"]+");
    List<String> commandLine = new ArrayList<String>();
    for (String arg : args)
      commandLine.add(arg.trim());
    builder.command(commandLine);
    builder.directory(new File(System.getProperty("user.dir")));
    System.err.println("Running post-processor " + builder.command());
    builder.redirectErrorStream(true);
    try {
      Process start = builder.start();
      InputStream inputStream = start.getInputStream();
      byte[] buf = new byte[1024];
      int ln = 0;
      while ((ln = inputStream.read(buf)) >= 0) {
        String s = new String(buf, 0, ln);
        System.out.print(s);
      }
      start.waitFor();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public void forcePublishing() {
    this.publish.setForce(true);
  }

  /**
   * @param publish
   *          The publish to set.
   */
  public void setPublish(Publish publish) {
    this.publish = publish;
  }

  public void setHeader(String header) {
    publish.setHeader(read(header));
  }

  public void setFooter(String footer) {
    publish.setFooter(read(footer));
  }

  public void setDocHeader(String header) {
    publish.setHeader(read(header));
  }

  public void setDocFooter(String footer) {
    publish.setFooter(read(footer));
  }

  public void setMultifile(boolean multifile) {
    literate.setMultifile(multifile);
  }

  public void setSourceOutputDirectory(File sourceOutput) {
    literate.setOutputDirectory(sourceOutput);
  }

  public void setOutputEncoding(String outputEncoding) {
    if (outputEncoding != null)
      publish.setOutputEncoding(outputEncoding);
  }

  public void setDocOutputDirectory(File docOutput) {
    publish.setOutputDirectory(docOutput);
  }

  public void setInputEncoding(String inputEncoding) {
    if (inputEncoding != null)
      publish.setInputEncoding(inputEncoding);
  }

  public void setFormat(String format) {
    if (format == null)
      literate.setPublisher(null);
    else
      literate.setPublisher(PublisherFactory.instance(format));
  }

  public void setExcludes(List excludes) {
    publish.excludePatterns(excludes);
  }

  public void setLog(Log log) {
    publish.setLog(log);
  }

  public void setFiles(List l) {
    publish.setFiles(l);
  }

  public void setInclude(Pattern pattern) {
    literate.setInclude(pattern);
  }

  public void setExclude(Pattern pattern) {
    literate.setExclude(pattern);
  }

  public void setLenient(boolean lenient) {
    literate.setLenient(lenient);
  }

}
