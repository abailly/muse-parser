package oqube.muse.web;

import java.io.File;
import java.io.IOException;

import oqube.muse.html.HTMLPublisher;
import oqube.muse.html.SlidePublisher;

import org.junit.Assert;
import org.junit.Test;

public class SlidePageTest extends PageTest {

  @Test
  public void testMuseFileProducesSlideContent() throws IOException {
    SlidePage page = new SlidePage(file, getPublisher());
    String s = getPageContent(page);
    System.err.println(s);
    Assert.assertTrue("expected slide div not found",
        s.contains("class=\"slide\""));
  }

  public void testSlideMapperProduceSlidePageOnRequest() throws IOException {
    SlideMapper h = new SlideMapper();
    h.setPublisher(getPublisher());
    final File dir = new File(fs.root(), "test1/");
    h.setRoot(dir);
    MusePage p = (MusePage) h.map("/metrics");
    assertNotNull(p);
    assertEquals(new File(dir, "metrics.muse"), p.getFile());
    Assert.assertTrue(
        "expected slide div not found",
        getPageContent(p).contains(
                             "class=\"slide\""));
  }

  protected HTMLPublisher getPublisher() {
    HTMLPublisher pub = new HTMLPublisher();
    String sessionId = "id";
    pub.startSession(sessionId);
    return pub;
  }

}
