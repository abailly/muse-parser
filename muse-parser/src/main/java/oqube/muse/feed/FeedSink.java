/**
 * Copyright (C) 2008 - OQube / Arnaud Bailly Licensed under MIT open-source
 * license: see http://www.opensource.org/licenses/mit-license.php Created 29
 * sept. 08
 */
package oqube.muse.feed;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oqube.muse.DefaultSink;
import oqube.muse.ElementHandler;
import oqube.muse.MuseSink;
import oqube.muse.MuseTagHandler;
import oqube.muse.html.MuseHTMLSink;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * A sink for producing RSS 2.0 Feeds from a muse file. The muse file is
 * expected to contain entries made of <em>dated sections</em>. A dated
 * section is a section with a title to which is prepended a date, as in <code>
 * * 20080927: Some title
 * </code>
 * 
 * @author nonofeed/
 * 
 */
public class FeedSink extends DefaultSink {

  private static final Pattern LOGTITLE = Pattern
      .compile("\\s*([0-9]{8}):\\s+(.*)\\s*");

  protected SyndFeedImpl feed;

  private SyndEntryImpl entry;

  protected List<SyndEntry> entries = new ArrayList<SyndEntry>();

  private TextAccumulator accumulator = new TextAccumulator();

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
      "yyyyMMdd");

  private MuseHTMLSink sink = new MuseHTMLSink();

  private StringWriter htmlOut = new StringWriter();

  class ProxySinkHandler extends ElementHandler {

    private MuseSink sink;

    protected ProxySinkHandler(String name, MuseSink sink) {
      super(name);
      this.sink = sink;
    }

    @Override
    public void end() {
      sink.end(name);
    }

    @Override
    public void start(Map<String, String> parameters) {
      sink.start(name, parameters);
    }

  }

  {
    sink.setEncoding(getEncoding());
    sink.setOut(new PrintWriter(htmlOut));

    delegate.append("document", new ElementHandler("document") {

      @Override
      public void start(Map<String, String> parameters) {
        feed = new SyndFeedImpl();
      }

      @Override
      public void end() {
        SyndFeedOutput output = new SyndFeedOutput();
        try {
          closeCurrentEntry();
          feed.setFeedType("rss_2.0");
          feed.setDescription(getProperty("description"));
          feed.setEntries(entries);
          feed.setLink(getProperty("link"));
          feed.setTitle(getProperty("title"));
          feed.setAuthor(getProperty("author"));
          output.output(feed, out);
        } catch (IOException e) {
          e.printStackTrace();
        } catch (FeedException e) {
          e.printStackTrace();
        }
      }
    });

    delegate.append("title1", new ElementHandler("title1") {

      @Override
      public void start(Map<String, String> parameters) {
        closeCurrentEntry();
        entry = new SyndEntryImpl();
        entries.add(entry);
      }

      @Override
      public void end() {
        parseEntryTitle();
        accumulator.reset();
      }
    });

    for (String element : new String[] { "title2", "title3", "title4", "para",
        "list", "item", "quote", "center", "enums", "emph", "strong", "verb",
        "uline", "table", "tableData", "tableHeader", "tableRow", "math" })
      delegate.append(element, new ProxySinkHandler(element, sink));
  }

  @Override
  public void text(String txt) {
    accumulator.accumulate(txt);
    sink.text(txt);
  }

  protected SyndContent makeDescription() {
    SyndContent content = new SyndContentImpl();
    content.setType("text/html");
    content.setValue(htmlOut.getBuffer().toString());
    htmlOut.flush();
    this.htmlOut = new StringWriter();
    this.sink.setOut(new PrintWriter(htmlOut));
    accumulator.reset();
    return content;
  }

  protected void parseEntryTitle() {
    String dateAndTitle = accumulator.content();
    Pattern pat = LOGTITLE;
    Matcher match = pat.matcher(dateAndTitle);
    try {
      if (match.matches()) {
        String title = match.group(2).trim();
        this.entry.setTitle(title);
        SIMPLE_DATE_FORMAT.setLenient(false);
        this.entry.setPublishedDate(SIMPLE_DATE_FORMAT.parse(match.group(1)));
        final String linkToEntry = getProperty("link") + "/"
            + splitDate(match.group(1)) + "/" + FeedUtils.escapeTitle(title);
        this.entry.setUri(linkToEntry);
        this.entry.setLink(linkToEntry);
      } else
        throw new RuntimeException("Invalid title date format '" + dateAndTitle
            + "', should respect pattern '\\s*([0-9]{8}): (.*)\\s*'");
    } catch (ParseException e) {
      throw new RuntimeException(
          "Invalid title date format, should be 'yyyyMMdd'", e);
    }
  }

  private String splitDate(String group) {
    return group.substring(0, 4) + "/" + group.substring(4, 6) + "/"
        + group.substring(6);
  }

  private void closeCurrentEntry() {
    if (entry != null)
      entry.setDescription(makeDescription());
  }

  public void anchor(String a) {
    sink.anchor(a);
  }

  public void block(String tag, String[][] at, String content) {
    sink.block(tag, at, content);
  }

  public void flow(String tag, String[][] at, String content) {
    sink.flow(tag, at, content);
  }

  public void link(String s, String t) {
    sink.link(s, t);
  }

  public void setTagHandler(MuseTagHandler tagHandler) {
    sink.setTagHandler(tagHandler);
  }

}
