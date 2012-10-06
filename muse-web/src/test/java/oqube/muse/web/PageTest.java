package oqube.muse.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import org.junit.Before;

import oqube.muse.Publisher;
import oqube.muse.feed.FeedPublisher;
import junit.framework.TestCase;

public abstract class PageTest extends TestCase {

  protected TemporaryFS fs;

  protected File file;

  protected File journal;

  private String sessionId;

  @Before
  public void setUp() throws Exception {
    fs = new TemporaryFS(new File(
        new File(System.getProperty("java.io.tmpdir")), "tests"));
    file = fs.copy("test1/metrics.muse");
    journal = fs.copy("test1/journal.muse");
  }

  protected abstract Publisher getPublisher();

  protected String getPageContent(Page page) throws IOException {
    StringWriter bos = new StringWriter();
    Reader r = new InputStreamReader(page.content("id"));
    char[] buf = new char[1024];
    int ln = 0;
    while ((ln = r.read(buf)) != -1)
      bos.write(buf, 0, ln);
    // find xhtml
    String s = bos.toString();
    return s;
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    fs.clean();
  }

}
