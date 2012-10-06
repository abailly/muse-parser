package oqube.muse.feed;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.TestCase;

public class FeedPublisherTest extends TestCase {

  public void testPublisherOutputsFeedFormatFromInput() throws IOException {
    FeedPublisher publisher = new FeedPublisher();
    publisher.startSession("1");
    StringWriter stringOut = new StringWriter();
    publisher.process("1", new InputStreamReader(getClass()
        .getResourceAsStream("/journal.muse")), new PrintWriter(stringOut));
    publisher.endSession("1");
    assertTrue(stringOut
        .toString()
        .contains(
            "&lt;li&gt;things are integrated into the graphical monitor &lt;/li&gt;"));
  }
  
  public void testFeedPublisherHandlesSourceTag() throws IOException {
    FeedPublisher publisher = new FeedPublisher();
    publisher.startSession("1");
    StringWriter stringOut = new StringWriter();
    publisher.process("1", new InputStreamReader(getClass()
        .getResourceAsStream("/journal6.muse")), new PrintWriter(stringOut));
    publisher.endSession("1");
    assertTrue(stringOut
        .toString()
        .contains(
            "pre class=\"haskell\""));
  }
  
  
}
