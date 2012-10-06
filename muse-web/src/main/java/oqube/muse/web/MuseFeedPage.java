/**
 * Copyright (C) 2008 - OQube / Arnaud Bailly Licensed under MIT open-source
 * license: see http://www.opensource.org/licenses/mit-license.php Created 30
 * sept. 08
 */
package oqube.muse.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import oqube.muse.MuseSink;
import oqube.muse.feed.FeedUtils;
import oqube.muse.filter.FilterSink;
import oqube.muse.html.HTMLPublisher;

/**
 * A virtual page for feed links. Content is extracted according to the request
 * from one or more files, and formatted using muse publisher.
 * 
 * @author nono
 * 
 */
public class MuseFeedPage extends MusePage {

  private String date;

  private String title;

  public MuseFeedPage(File f, String date, String title, HTMLPublisher publisher) {
    super(f, publisher);
    this.date = date;
    this.title = title;
  }

  @Override
  public InputStream content(String sessionId) {
    MuseSink oldSink = getPub().getSink(sessionId);
    FeedEntrySelector selector = new FeedEntrySelector(date, title);
    selector.setSink(oldSink);
    getPub().setSink(sessionId, selector.getSink());
    InputStream content = super.content(sessionId);
    getPub().setSink(sessionId, oldSink);
    return content;
  }
}
