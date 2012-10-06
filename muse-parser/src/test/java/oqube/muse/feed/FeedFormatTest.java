package oqube.muse.feed;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import oqube.muse.html.DefaultHTMLLinker;
import oqube.muse.html.MuseHTMLTagHandler;
import oqube.muse.parser.MuseParser;

import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

public class FeedFormatTest extends TestCase {

  private FeedSink sink;
  private MuseParser parser;
  private ByteArrayOutputStream bos;

  protected void setUp() throws Exception {
    super.setUp();
    sink = new FeedSink();
    sink.setTagHandler(new MuseHTMLTagHandler());
    sink.setLinker(new DefaultHTMLLinker());
    sink.setOut(bos = new ByteArrayOutputStream());
    parser = new MuseParser();
    parser.setSink(sink);
  }

  public void testCanProduceFeedEntriesFromADatedMuseFile()
          throws IllegalArgumentException, FeedException {
    parser.setStream(getClass().getResourceAsStream("/journal.muse"));
    runParser();
    SyndFeed feed = readFeed(bos);
    assertEquals(4, feed.getEntries().size());
  }

  public void testFeedContainsMetadataFromTheMuseFile()
          throws IllegalArgumentException, FeedException {
    parser.setStream(getClass().getResourceAsStream("/journal2.muse"));
    runParser();
    SyndFeed feed = readFeed(bos);
    assertEquals("OQube weblog", feed.getTitle());
    assertEquals("Arnaud Bailly", feed.getAuthor());
    assertEquals("Some feed by oqube", feed.getDescription());
  }

  public void testFeedEntryContainsDateFromMuseFile()
          throws IllegalArgumentException, FeedException {
    parser.setStream(getClass().getResourceAsStream("/journal.muse"));
    runParser();
    System.err.println(new String(bos.toByteArray()));
    SyndFeed feed = readFeed(bos);
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(2008, 8, 17);
    Date date = calendar.getTime();
    assertEquals(date, ((List<SyndEntry>) feed.getEntries()).get(1).getPublishedDate());
  }

  public void testFeedEntryGuidDependsOnTitleAndDate()
          throws IllegalArgumentException, FeedException {
    parser.setStream(getClass().getResourceAsStream("/journal3.muse"));
    runParser();
    System.err.println(new String(bos.toByteArray()));
    SyndFeed feed = readFeed(bos);
    List<SyndEntry> entries = ((List<SyndEntry>) feed.getEntries());
    for (SyndEntry entry : entries) {
      for (SyndEntry other : entries) {
        if (entry != other) {
          assertTrue(!entry.getUri().equals(other.getUri()));
        }
      }
    }
  }

  public void testLastFeedEntryContainsDescription()
          throws IllegalArgumentException, FeedException {
    parser.setStream(getClass().getResourceAsStream("/journal.muse"));
    runParser();
    System.err.println(new String(bos.toByteArray()));
    SyndFeed feed = readFeed(bos);
    assertTrue(((List<SyndEntry>) feed.getEntries()).get(3).getDescription().getValue().contains("wrapping JUnitCore executor"));
  }

  public void testLinkCanBeSetOnFeed() throws IllegalArgumentException,
          FeedException {
    parser.setStream(getClass().getResourceAsStream("/journal.muse"));
    runParser();
    SyndFeed feed = readFeed(bos);
    assertEquals("http://www.oqube.net/", feed.getLink());
  }

  public void testFeedThrowsExceptionOnIncorrectTitleFormat()
          throws IllegalArgumentException, FeedException {
    parser.setStream(getClass().getResourceAsStream("/journal4.muse"));
    try {
      runParser();
      fail("Expected runtime exception to be thrown");
    } catch (Exception e) {
      // OK
    }
  }

  public void testFeedThrowsExceptionOnIncorrectDateFormat()
          throws IllegalArgumentException, FeedException {
    parser.setStream(getClass().getResourceAsStream("/journal5.muse"));
    try {
      runParser();
      fail("Expected runtime exception to be thrown");
    } catch (Exception e) {
      // OK
    }
  }

  public void testFeedContentIsHTMLFormatted() throws IllegalArgumentException,
          FeedException {
    parser.setStream(getClass().getResourceAsStream("/journal.muse"));
    runParser();
    System.err.println(new String(bos.toByteArray()));
    SyndFeed feed = readFeed(bos);
    assertEquals("text/html", ((List<SyndEntry>) feed.getEntries()).get(3).getDescription().getType());
    assertTrue(((List<SyndEntry>) feed.getEntries()).get(3).getDescription().getValue().contains("<ul><li>tests are automatically"));
  }

  public void testRuntimeErrorOnTitle() {
    parser.setStream(getClass().getResourceAsStream("/journal6.muse"));
    runParser();
  }

//  public void testImageLinksGotEnclosed() throws IllegalArgumentException, FeedException {
//    parser.setStream(getClass().getResourceAsStream("/journal7.muse"));
//    runParser();
//    System.err.println(new String(bos.toByteArray()));
//    SyndFeed feed = readFeed(bos);
//    assertEquals("http://localhost:4444/journal/hpc.png", ((List<Enclosure>) ((List<SyndEntry>) feed.getEntries()).get(0).getEnclosures()).get(0).getUrl());
//  }

  private void runParser() {
    sink.start("document", null);
    parser.start();
    sink.end("document");
    sink.getOut().flush();
  }

  private SyndFeed readFeed(ByteArrayOutputStream bos2)
          throws IllegalArgumentException, FeedException {
    SyndFeedInput input = new SyndFeedInput();
    return input.build(new InputSource(new StringReader(new String(bos.toByteArray()))));
  }
}
