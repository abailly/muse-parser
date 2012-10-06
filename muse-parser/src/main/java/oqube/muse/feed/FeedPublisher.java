/**
 * Copyright (C) 2008 - OQube / Arnaud Bailly Licensed under MIT open-source
 * license: see http://www.opensource.org/licenses/mit-license.php Created 29
 * sept. 08
 */
package oqube.muse.feed;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import oqube.muse.AbstractPublisher;
import oqube.muse.DefaultSink;
import oqube.muse.html.DefaultHTMLLinker;
import oqube.muse.html.LinkingHTMLSourceTag;
import oqube.muse.html.MuseHTMLTagHandler;

/**
 * @author nono
 * 
 */
public class FeedPublisher extends AbstractPublisher {

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#endSession(java.lang.String)
   */
  public void endSession(String id) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#getExtension()
   */
  public String getExtension() {
    return ".xml";
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#process(java.lang.String, java.io.Reader,
   *      java.io.PrintWriter)
   */
  public void process(String session, Reader br, PrintWriter pw)
      throws IOException {
    if (!th.get().equals(session))
      return;
    sink.setOut(pw);
    parser.setSink(sink);
    parser.setReader(br);
    sink.start("document", null);
    parser.start();
    sink.end("document");
    pw.flush();
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#startSession(java.lang.String)
   */
  public void startSession(String id) {
    th.set(id);
    DefaultSink sink = new FeedSink();
    MuseHTMLTagHandler tagh = new MuseHTMLTagHandler();
    LinkingHTMLSourceTag src = new LinkingHTMLSourceTag();
    tagh.setNext(src);
    sink.setTagHandler(tagh);
    sink.setLinker(new DefaultHTMLLinker());
    this.sink = sink;
  }

}
