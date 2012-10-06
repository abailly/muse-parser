/*
 * ______________________________________________________________________________
 * 
 * Copyright (C) 2006 Arnaud Bailly / OQube
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
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
 * email: contact@oqube.com creation: Thu Aug 24 2006
 */
package oqube.muse;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.lifl.utils.CommandLine;
import fr.lifl.utils.Pipe;

/**
 * Simple publishing command-line utility class. This application recursively
 * traverses a directory structure and publishes all Muse files it finds in
 * XHTML format. Other files are copied as-is. Command-line accepts the
 * following options: <table>
 * <tr>
 * <th>character</th>
 * <th>arguments</th>
 * <th>Meaning</th>
 * <th>Default</th>
 * </tr>
 * <tr>
 * <td>E</td>
 * <td>charset encoding name</td>
 * <td>sets the <b>output</b> character set to use</td>
 * <td>platform dependent</td>
 * </tr>
 * <tr>
 * <td>e</td>
 * <td>charset encoding name</td>
 * <td>sets the <b>input</b> character set to use</td>
 * <td>platform dependent</td>
 * </tr>
 * </table> The rest of the command line arguments are assumed to be files and
 * directories to process.
 * 
 * @author abailly@oqube.com
 * @version $Id: /local/muse-parser/oqube/src/main/java/oqube/muse/Publish.java
 *          968 2006-09-20T12:25:14.437654Z nono $
 */
public class Publish implements Runnable {

  /*
   * Pattern of files and directories to ignore in publishing
   */
  private Pattern exclude = Pattern
      .compile("(.*\\.svn.*)|(.*~)|(.*\\#.*)|(.*/\\..*)");

  private String inputEncoding;

  public String getInputEncoding() {
    return inputEncoding;
  }

  public void setInputEncoding(String inputEncoding) {
    this.inputEncoding = inputEncoding;
  }

  private String outputEncoding;

  public String getOutputEncoding() {
    return outputEncoding;
  }

  public void setOutputEncoding(String outputEncoding) {
    this.outputEncoding = outputEncoding;
  }

  private List files = new ArrayList();

  public void setFiles(List dirs) {
    this.files = dirs;
  }

  public List getFiles() {
    return files;
  }

  private File outputDirectory;

  public void addFile(File f) throws IOException {
    if (!f.exists() || !f.canRead()) {
      throw new IOException("Non existent or non readable file " + f);
    }
    files.add(f);
  }

  /*
   * configured publisher object.
   */
  private Publisher publisher;

  private String sessionId;

  private Log log = LogFactory.getLog(Publish.class);

  private boolean force = false;

  public boolean isForce() {
    return force;
  }

  public void setForce(boolean force) {
    this.force = force;
  }

  public static void main(String[] argv) {
    /* instantiate publisher */
    Publish pub = new Publish();
    configurePublish(argv, pub);
    /* run it */
    pub.run();
  }

  /**
   * @param argv
   * @param pub
   */
  public static void configurePublish(String[] argv, Publish pub) {
    CommandLine cl = new CommandLine();
    cl.addOptionSingle('e');
    cl.addOptionSingle('E');
    cl.addOptionSingle('t');
    cl.addOptionSingle('T');
    cl.addOptionSingle('b');
    cl.addOptionSingle('B');
    cl.addOptionSingle('o'); // output dir
    cl.addOptionSingle('f'); // format
    cl.addOptionMultiple('x'); // patterns to exclude
    cl.addOption('F'); // force publishing
    cl.addOption('h');
    cl.addOption('?');
    cl.parseOptions(argv);
    if (cl.isSet('?') || cl.isSet('h') || argv.length == 0) {
      usage(null);
      System.exit(1);
    }
    if (cl.isSet('e'))
      pub.setInputEncoding((String) cl.getOption('e').getArgument());
    if (cl.isSet('E'))
      pub.setOutputEncoding((String) cl.getOption('E').getArgument());
    if (cl.isSet('o'))
      pub
          .setOutputDirectory(new File((String) cl.getOption('o').getArgument()));
    if (cl.isSet('f')) {
      final Publisher pubinstance = PublisherFactory.instance((String) cl
          .getOption('f').getArgument());
      if (pubinstance == null)
        throw new IllegalArgumentException("Incorrect format for publisher: "
            + (String) cl.getOption('f').getArgument());
      pub.setPublisher(pubinstance);
    } else
      pub.setPublisher(PublisherFactory.instance("xhtml"));
    assert pub.publisher != null;
    configureFooter(pub, cl);
    configureHeader(pub, cl);
    if (cl.isSet('x'))
      pub.excludePatterns((List) cl.getOption('x').getArgument());
    pub.setForce(cl.isSet('F'));
    /* add files to process */
    for (Iterator i = cl.getArguments().iterator(); i.hasNext();) {
      File f = new File((String) i.next());
      try {
        pub.addFile(f);
      } catch (IOException e) {
        usage(e);
        System.exit(1);
      }
    }
  }

  /**
   * @param pub
   * @param cl
   */
  private static void configureHeader(Publish pub, CommandLine cl) {
    if (cl.isSet('b'))
      pub.setHeader((String) cl.getOption('b').getArgument());
    if (cl.isSet('B')) {
      File file = new File((String) cl.getOption('B').getArgument());
      if (!file.exists())
        System.err.println("File " + file + " does not exist, ignoring header");
      else {
        String ft;
        try {
          ft = readFile(file);
          pub.setHeader(ft);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * @param pub
   * @param cl
   */
  private static void configureFooter(Publish pub, CommandLine cl) {
    if (cl.isSet('t'))
      pub.setFooter((String) cl.getOption('t').getArgument());
    if (cl.isSet('T')) {
      File file = new File((String) cl.getOption('T').getArgument());
      if (!file.exists())
        System.err.println("File " + file + " does not exist, ignoring footer");
      else {
        String ft;
        try {
          ft = readFile(file);
          pub.setFooter(ft);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void setHeader(String string) {
    publisher.setHeader(string);
  }

  public void setFooter(String string) {
    publisher.setFooter(string);
  }

  private static String readFile(File file) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    FileInputStream fis = new FileInputStream(file);
    new Pipe(bos, fis).pump();
    return new String(bos.toByteArray());
  }

  /**
   * Sets the list of file patterns to excludes.
   * 
   * @param list
   *          a List<String> of patterns denoting paths to exclude from
   *          processing.
   */
  public void excludePatterns(List list) {
    if (list == null)
      return;
    StringBuffer sb = new StringBuffer();
    for (Iterator i = list.iterator(); i.hasNext();) {
      String s = (String) i.next();
      sb.append(s);
      if (i.hasNext())
        sb.append('|');
    }
    this.exclude = Pattern.compile(sb.toString());
  }

  private static void usage(Throwable object) {
    System.err
        .println("Usage: java -cp <classpath> oqube.muse.Publish [options] <files>");
    System.err.println("       Format <files> recursively into documents.");
    System.err.println();
    System.err.println("Options:");
    System.err
        .println(" -o <dir>    : output directory for generated documents. If non existent");
    System.err.println("               will be created (default: .)");
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
        .println(" -F          : force publishing even for non modified files.");
    System.err
        .println(" -T <file>   : set content of <file> as footer for all published files.");
    System.err
        .println(" -t <string> : set <string> as footer for all published files.");
    System.err
        .println(" -B <file>   : set content of <file> as header for all published files.");
    System.err
        .println(" -b <string> : set <string> as header for all published files.");
    System.err.println(" -?|-h       : this documentation.");
    if (object != null)
      object.printStackTrace();
  }

  /**
   * Sets default values in publish class. The default values for input and
   * output encoding are set to the value of the system property
   * <code>file.encoding</code>.
   */
  public Publish() {
    String defaultEnc = System.getProperty("file.encoding");
    this.inputEncoding = this.outputEncoding = defaultEnc;
  }

  public void run() {
    this.sessionId = "publish" + Thread.currentThread().getId();
    log.info("Starting session " + sessionId);
    publisher.startSession(sessionId);
    /*
     * iterate over files if directories: recurse and create equivalent output
     * dir if files: publish using current settings
     */
    for (Iterator i = files.iterator(); i.hasNext();) {
      File f = (File) i.next();
      // check exclusions
      if (exclude.matcher(f.getPath()).matches())
        continue;
      if (f.isDirectory())
        processDirectory(f);
      else
        try {
          publishOrCopyFile(f, outputDirectory);
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    publisher.endSession(sessionId);
    log.info("Ending session " + sessionId);
  }

  public void publishOrCopyFile(File f, File outputDirectory2)
      throws IOException {
    if (f.getName().endsWith(".muse"))
      publishFile(f, outputDirectory2);
    else
      copyFile(f, outputDirectory2);
  }

  private void copyFile(File f, File outputDirectory2) throws IOException {
    if (outputDirectory2 != null) {
      String name = publisher.getTargetName(f);
      if (!outputDirectory2.exists() && !outputDirectory2.mkdirs())
        throw new IOException("Unable to create non-existent output directory "
            + outputDirectory2);
      // publish if modification time is newer for input file
      File outf = new File(outputDirectory2, name);
      if (dontPublish(f, outf)) {
        log.info("Output file " + outf + " is newer than input file " + f
            + ", ignoring");
        return;
      }
      OutputStream os = new FileOutputStream(outf);
      log.info("copying to file " + outf);
      new Pipe(os, new FileInputStream(f)).pump();
    }

  }

  /**
   * @param f
   * @param outf
   * @return
   */
  private boolean dontPublish(File f, File outf) {
    return !force && outf.exists() && outf.lastModified() >= f.lastModified();
  }

  /**
   * Publish a file to a directory.
   * 
   * @param f
   *          a file in muse format to publish.
   * @param outputDirectory2
   *          the directory where this file should be created in.
   * @throws IOException
   */
  public void publishFile(File f, File outputDirectory2) throws IOException {
    // construct input stream
    InputStream is = new FileInputStream(f);
    BufferedReader br = new BufferedReader(new InputStreamReader(is,
        inputEncoding));
    PrintWriter pw = null;
    // construct output stream if directory is not null
    if (outputDirectory2 != null) {
      String name = publisher.getTargetName(f);
      if (!outputDirectory2.exists() && !outputDirectory2.mkdirs())
        throw new IOException("Unable to create non-existent output directory "
            + outputDirectory2);
      // publish if modification time is newer for input file
      File outf = new File(outputDirectory2, name);
      if (dontPublish(f, outf)) {
        log.info("Output file " + outf + " is newer than input file " + f
            + ", ignoring");
        return;
      }
      OutputStream os = new FileOutputStream(outf);
      // make writers for encoding
      pw = new PrintWriter(new OutputStreamWriter(os, outputEncoding));
      publisher.setOutputEncoding(outputEncoding);
      log.info("publishing to file " + outf);
    }
    publisher.process(sessionId, br, pw);
    // flush and close streams
    is.close();
    if (pw != null)
      pw.close();
    log.info("Done publishing input file " + f);
  }

  /**
   * Processes a whole directory. The complete content of the directory is
   * looked up, its subdirectories are mirrored in the output directory and
   * files matching file filter are generated.
   * 
   * @param base
   *          a directory to process.
   */
  public void processDirectory(File base) {
    String root = base.getAbsolutePath();
    List todo = new LinkedList(Arrays.asList(base.listFiles()));
    while (!todo.isEmpty()) {
      File f = (File) todo.remove(0);
      // check exclusions
      if (exclude.matcher(f.getPath()).matches())
        continue;
      if (f.isDirectory()) {
        // append content
        todo.addAll(Arrays.asList(f.listFiles()));
      } else {
        // compute output directory relative to base
        String path = f.getAbsolutePath().substring(root.length() + 1);
        // compute dir and file parts
        int i = path.lastIndexOf(File.separatorChar);
        File dir = (i == -1) ? outputDirectory
            : (outputDirectory == null ? null : new File(outputDirectory, path
                .substring(0, i)));
        try {
          publishOrCopyFile(f, dir);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * @return Returns the outputDirectory.
   */
  public File getOutputDirectory() {
    return outputDirectory;
  }

  /**
   * @param outputDirectory
   *          The outputDirectory to set.
   */
  public void setOutputDirectory(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  /**
   * @return Returns the publisher.
   */
  public Publisher getPublisher() {
    return publisher;
  }

  /**
   * @param publisher
   *          The publisher to set.
   */
  public void setPublisher(Publisher publisher) {
    this.publisher = publisher;
  }

  /**
   * @return Returns the sessionId.
   */
  public String getSessionId() {
    return sessionId;
  }

  public void setLog(Log log) {
    this.log = log;
    publisher.setLog(log);
  }

}
