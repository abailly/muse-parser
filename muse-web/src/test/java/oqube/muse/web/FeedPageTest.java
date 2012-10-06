package oqube.muse.web;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import oqube.muse.MuseSink;
import oqube.muse.Publisher;
import oqube.muse.feed.FeedPublisher;
import oqube.muse.html.HTMLPublisher;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;

public class FeedPageTest extends PageTest {

  public void testMuseFileProducesRssFeed() throws IOException {
    FeedPage page = new FeedPage(journal);
    String s = getPageContent(page);
    Assert.assertEquals("application/rss+xml", page.contentType());
    Assert.assertTrue("header not found", s.matches("(?s:.*<\\?xml.*\\?>.*)"));
    Assert.assertTrue(
        "incorrect content",
        s.matches("(?s:.*&lt;li&gt;things are integrated into the graphical monitor &lt;/li&gt;.*)"));
  }

  public void testFeedMapperProducesFeedPageOnRequest() {
    FeedMapper h = new FeedMapper();
    final File dir = new File(fs.root(), "test1/");
    h.setRoot(dir);
    FeedPage p = (FeedPage) h.map("/journal");
    assertNotNull(p);
    assertEquals(new File(dir, "journal.muse"), p.getFile());
  }

  public void testFeedLinkMapperShowsFeedEntryAsHTML() throws IOException {
    FeedLinkMapper m = new FeedLinkMapper(null);
    final File dir = new File(fs.root(), "test1/");
    m.setRoot(dir);
    MuseFeedPage p = (MuseFeedPage) m.map("/journal/2008/09/09/Dojo-de-lundi");
    HTMLPublisher publisher = new HTMLPublisher();
    publisher.startSession("id");
    p.setPub(publisher);
    assertNotNull(p);
    String s = getPageContent(p);
    assertTrue(
        "page does not contain expected data",
        s.contains("http://blog.tmorris.net/haskell-scala-java-7-functional-java-java"));
  }

  public void testCanExtractsSectionFromFeedPage() {
    FeedEntrySelector selector = new FeedEntrySelector("20080909",
        "Dojo-de-lundi");
    Mockery mockery = new Mockery();
    final MuseSink mock = mockery.mock(MuseSink.class);
    selector.setSink(mock);
    selector.setInput(getClass().getResourceAsStream("/test1/journalmock.muse"));
    mockery.checking(new Expectations() {
      {
        one(mock).start("header", null);
        one(mock).end("header");
        one(mock).start("footer", null);
        one(mock).end("footer");
        one(mock).start("body", null);
        one(mock).end("body");
        one(mock).start("title1", null);
        one(mock).end("title1");
        one(mock).start("para", null);
        one(mock).end("para");
        one(mock).text("20080909: Dojo de lundi");
        one(mock).text("Un dojo inspiré de  ");
      }
    });
    selector.select();
    mockery.assertIsSatisfied();
  }

  public void testFeedLinkMapperOnlyShowsSelectedFeedEntry() throws IOException {
    FeedLinkMapper m = new FeedLinkMapper(null);
    final File dir = new File(fs.root(), "test1/");
    m.setRoot(dir);
    MuseFeedPage p = (MuseFeedPage) m.map("/journal/2008/09/09/Dojo-de-lundi");
    HTMLPublisher publisher = new HTMLPublisher();
    publisher.startSession("id");
    p.setPub(publisher);
    assertNotNull(p);
    String s = getPageContent(p);
    assertTrue("page contains unexpected data",
        !s.contains("infrastructure for executing JUnit tests"));
  }

  @Override
  protected Publisher getPublisher() {
    // TODO Auto-generated method stub
    return null;
  }


}
