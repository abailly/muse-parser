/**
 * 
 */
package oqube.muse.muse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import oqube.muse.AbstractPublisher;

/**
 * Class for publishing muse files to muse files. This class may seem useless
 * but it is in fact interesting for preprocessing part of a muse file.
 * 
 * @author nono
 * 
 */
public class MusePublisher extends AbstractPublisher {

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.AbstractPublisher#getExtension()
   */
  public String getExtension() {
    return ".muse";
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
    parser.start();
    pw.flush();
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#startSession(java.lang.String)
   */
  public void startSession(String id) {
    th.set(id);
    MuseSourceTag src = new MuseSourceTag();
    MuseMuseSink sink = new MuseMuseSink();
    sink.setTagHandler(src);
    this.sink = sink;
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#endSession(java.lang.String)
   */
  public void endSession(String id) {
  }
}
