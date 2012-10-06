/**
 *  Copyright (C) 2008 - OQube / Arnaud Bailly
    Licensed under MIT open-source license: 
    see http://www.opensource.org/licenses/mit-license.php
    Created 29 sept. 08
 */
package oqube.muse.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import oqube.muse.feed.FeedPublisher;
import oqube.muse.feed.FeedSink;
import oqube.muse.html.DefaultHTMLLinker;
import oqube.muse.html.MuseHTMLTagHandler;
import oqube.muse.parser.MuseParser;

/**
 * A page that produces RSS 2.0 feed from muse content.
 * 
 * @author nono
 * 
 */
public class FeedPage implements Page {

  private File file;

  private FeedPublisher publisher;

  private int length;

  public FeedPage(File file) {
    this.file = file;
    this.publisher = new FeedPublisher();
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.web.Page#content(java.lang.String)
   */
  public InputStream content(String sessionId) {
    try {
      StringWriter stringOutput = new StringWriter();
      PrintWriter writer = new PrintWriter(stringOutput);
      publisher.startSession(sessionId);
      BufferedReader reader = new BufferedReader(new FileReader(file));
      publisher.process(sessionId, reader, writer);
      reader.close();
      writer.close();
      publisher.endSession(sessionId);
      byte[] bytes = stringOutput.toString().getBytes();
      this.length = bytes.length;
      return new ByteArrayInputStream(bytes);
    } catch (IOException e) {
      return new ByteArrayInputStream(("Error reading feed page "
          + file.getPath() + ": " + e.getMessage()).getBytes());
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.web.Page#contentType()
   */
  public String contentType() {
    return "application/rss+xml";
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.web.Page#length()
   */
  public int length() {
    return length;
  }

  public File getFile() {
    return file;
  }

}
