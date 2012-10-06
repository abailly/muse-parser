/**
 * 
 */
package oqube.muse.literate;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import oqube.muse.DefaultSink;
import oqube.muse.MuseSink;
import oqube.muse.Publisher;
import oqube.muse.parser.MuseParser;
import oqube.muse.template.Template;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A composite publisher that can produce source files and/or comment files from
 * literate muse sources. This publisher creates source files from a
 * {@link oqube.muse.literate.SourceRepository} that is feeded with literate
 * muse input files. It needs another publisher for parsing input files and
 * formatting comment files.
 * 
 * @author nono
 * @version $Id$
 */
public class LiteratePublisher implements Publisher {

  private Log log = LogFactory.getLog(LiteratePublisher.class);
  private Publisher publisher;
  private SourceRepository repository;
  private LiterateSourceTagHandler handler;

  /*
   * pattern for filtering toplevel fragments to publish
   */
  private Pattern include = Pattern.compile(".*");

  /*
   * fragments must included and not excluded for publication
   */
  private Pattern exclude = Pattern.compile("^$");

  public LiteratePublisher() {
    repository = new DefaultSourceRepository();
    handler = new LiterateSourceTagHandler();
    handler.setRepository(repository);
  }

  public void setSourceFooter(String read) {
    this.sourceFooter = read;
  }

  public void setSourceHeader(String read) {
    this.sourceHeader = read;
  }

  class WrapSink implements MuseSink {

    MuseSink sink;

    WrapSink(MuseSink sink, LiterateSourceTagHandler hdl) {
      this.sink = sink;
    }

    public void setFooter(Template footer) {
      sink.setFooter(footer);
    }

    public void setHeader(Template header) {
      sink.setHeader(header);
    }

    public void addMetadata(String s, String t) {
      sink.addMetadata(s, t);
    }

    public void anchor(String a) {
      sink.anchor(a);
    }

    public void block(String tag, String[][] currentArgs, String content) {
      handler.block(sink, tag, currentArgs, content);
      sink.block(tag, currentArgs, content);
    }

    public void flow(String tag, String[][] currentArgs, String content) {
      sink.flow(tag, currentArgs, content);
    }

    public void link(String s, String t) {
      sink.link(s, t);
    }

    public void rawText(String text) {
      sink.rawText(text);
    }

    public void separator() {
      sink.separator();
    }

    public void setLineWidth(int lw) {
      sink.setLineWidth(lw);
    }

    public void text(String txt) {
      sink.text(txt);
    }

    public void setOut(PrintWriter pw) {
      sink.setOut(pw);
    }

    public void setEncoding(String outputEncoding) {
      sink.setEncoding(outputEncoding);
    }

    public String getEncoding() {
      return sink.getEncoding();
    }

    public void end(String element) {
      sink.end(element);
    }

    public void start(String element, Map<String, String> parameters) {
      sink.start(element, parameters);
    }

    public void flush() {
      sink.flush();
    }
  }

  /*
   * whether or not to accumulate files accross a whole session.
   */
  private boolean multifile;

  /*
   * where files are generated.
   */
  private File outputDirectory;

  /*
   * optional header prepended to all files
   */
  private String sourceHeader;

  /*
   * optional footer appended to all sources.
   */
  private String sourceFooter;
  private String header;

  /*
   * optional footer appended to all sources.
   */
  private String footer;

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#endSession(java.lang.String)
   */
  public void endSession(String id) {
    if (publisher != null) {
      publisher.endSession(id);
    }
    if (multifile) {
      try {
        generateSources();
      } catch (IOException e) {
        log.error("Error while generating sources from repository", e);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#getExtension()
   */
  public String getExtension() {
    if (publisher != null) {
      return publisher.getExtension();
    } else {
      return "";
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#process(java.lang.String, java.io.Reader,
   *      java.io.PrintWriter)
   */
  public void process(String session, Reader br, PrintWriter pw)
          throws IOException {
    assert pw != null;
    // setup repository
    if (!multifile) {
      repository.reset();
    }
    if (publisher != null) {
      // intercept tag blocks and delegate parsing and publishing
      MuseSink sink = publisher.getSink(session);
      publisher.setSink(session, new WrapSink(sink, handler));
      publisher.process(session, br, pw);
    } else {
      // parse
      MuseParser parser = new MuseParser();
      parser.setSink(new WrapSink(new DefaultSink(), handler));
      parser.setReader(br);
      parser.getSink().start("document", null);
      parser.start();
      parser.getSink().end("document");
    }
    // process sources
    if (!multifile) {
      generateSources();
    }
  }

  /**
   * Generate all sources accumulatd in the repository.
   * 
   * @throws IOException
   */
  private void generateSources() throws IOException {
    if (log.isDebugEnabled()) {
      log.debug("Start generating collected sources");
    }
    Map m = repository.collectSources();
    for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      String fn = (String) me.getKey();
      String data = (String) me.getValue();
      // check filter
      if (!include.matcher(fn).matches() || exclude.matcher(fn).matches()) {
        if (log.isInfoEnabled()) {
          log.info("Filtering out file " + fn);
        }
        continue;
      }
      // create file
      File out = makeFile(fn);
      if (out == null) {
        throw new IOException("Cannot make output file " + out + " from " + fn);
      }
      PrintWriter sw = new PrintWriter(out);
      if (sourceHeader != null) {
        sw.println(sourceHeader);
      }
      sw.println(data);
      if (sourceFooter != null) {
        sw.println(sourceFooter);
      }
      sw.flush();
      sw.close();
      if (log.isDebugEnabled()) {
        log.debug("Done generating source file " + fn);
      }
    }
  }

  /*
   * create output file and intermediate directories
   */
  private File makeFile(String fn) throws IOException {
    File f = new File(outputDirectory, fn);
    // base case
    if (f.exists() && f.canRead()) {
      return f;
    }
    String[] parts = fn.split(File.separator);
    File dir = outputDirectory;
    int i = 0;
    if (!dir.exists() && !dir.mkdirs()) {
      throw new IOException("Cannot create directory " + dir);
    // create intermediary directories
    }
    for (; i < parts.length - 1; i++) {
      dir = new File(dir, parts[i]);
      if (!dir.exists() && !dir.mkdir()) {
        throw new IOException("Cannot create directory " + dir);
      }
    }
    // last part is file
    assert i == parts.length - 1;
    return new File(dir, parts[i]);
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#setOutputEncoding(java.lang.String)
   */
  public void setOutputEncoding(String outputEncoding) {
    if (publisher != null) {
      publisher.setOutputEncoding(outputEncoding);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#startSession(java.lang.String)
   */
  public void startSession(String id) {
    if (multifile) {
      repository.reset();
    }
    if (publisher != null) {
      if(header != null)
        publisher.setHeader(header);
      if(footer != null)
        publisher.setFooter(footer);
      publisher.startSession(id);
    }
  }

  public MuseSink getSink(String id) {
    if (publisher != null) {
      return publisher.getSink(id);
    }
    return null;
  }

  public void setSink(String id, MuseSink sink) {
    if (publisher != null) {
      publisher.setSink(id, sink);
    }
  }

  /**
   * @param footer
   *          The footer to set.
   */
  public void setFooter(String footer) {
    this.footer = footer;
  }

  /**
   * @return Returns the handler.
   */
  public LiterateSourceTagHandler getHandler() {
    return handler;
  }

  /**
   * @param handler
   *          The handler to set.
   */
  public void setHandler(LiterateSourceTagHandler handler) {
    this.handler = handler;
  }

  /**
   * @param header
   *          The header to set.
   */
  public void setHeader(String header) {
    this.header = header;
  }

  /**
   * @return Returns the multifile.
   */
  public boolean isMultifile() {
    return multifile;
  }

  /**
   * @param multifile
   *          The multifile to set.
   */
  public void setMultifile(boolean multifile) {
    this.multifile = multifile;
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
   * @return Returns the repository.
   */
  public SourceRepository getRepository() {
    return repository;
  }

  /**
   * @param repository
   *          The repository to set.
   */
  public void setRepository(SourceRepository repository) {
    this.repository = repository;
  }

  public void setLog(Log log) {
    this.log = log;
  }

  /**
   * @return Returns the filter.
   */
  public Pattern getInclude() {
    return include;
  }

  /**
   * @param filter
   *          The filter to set.
   */
  public void setInclude(Pattern filter) {
    if (filter != null) {
      this.include = filter;
    } else {
      this.include = Pattern.compile(".*");
    }
  }

  /**
   * Set lenient property.
   * 
   * @param b
   *          if true, missing fragments will be ignored.
   * @see SourceRepository
   */
  public void setLenient(boolean b) {
    repository.setLenient(b);
  }

  /**
   * @return Returns the exclude.
   */
  public Pattern getExclude() {
    return exclude;
  }

  /**
   * @param exclude
   *          The exclude to set.
   */
  public void setExclude(Pattern exclude) {
    if (exclude != null) {
      this.exclude = exclude;
    } else {
      this.exclude = Pattern.compile("^$");
    }
  }

  /**
   * @return Returns the log.
   */
  public Log getLog() {
    return log;
  }

  public String getTargetName(File f) {
    if (publisher != null) {
      return publisher.getTargetName(f);
    } else {
      return null;
    }
  }
}
