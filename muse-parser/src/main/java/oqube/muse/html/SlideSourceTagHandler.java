package oqube.muse.html;

import oqube.muse.MuseSink;
import oqube.muse.MuseTagHandler;

public class SlideSourceTagHandler extends HTMLSourceTagHandler {

  public SlideSourceTagHandler(MuseTagHandler tagHandler) {
    setNext(tagHandler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseTagHandler#block(oqube.muse.MuseSink, java.lang.String,
   *      java.lang.String[][], java.lang.String)
   */
  public boolean block(MuseSink sink, String tag, String[][] at, String content) {
    // Check tag and parameters
    if (!"source".equals(tag) && !"src".equals(tag))
      return skipToNextTag(sink, tag, at, content);
    doOutput(sink, makeFragmentFrom(at, content));
    return true;
  }

}
