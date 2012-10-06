package oqube.muse.html;

import oqube.muse.AbstractTagHandler;
import oqube.muse.MuseSink;
import oqube.muse.MuseTagHandler;

public class HTMLSourceTagHandler extends AbstractTagHandler {

  public HTMLSourceTagHandler() {
    super();
  }

  protected void doOutput(MuseSink sink, SourceFragment sourceFragment) {
    sink.rawText("<pre class=\"" + sourceFragment.getLanguage() + "\" id=\""
        + sourceFragment.getName() + "\">");
    sink.rawText(sourceFragment.getContent());
    sink.rawText("</pre>");
  }

  protected SourceFragment makeFragmentFrom(String[][] at, String content) {
    SourceFragment fragment = new SourceFragment();
    for (int i = 0; i < at.length; i++) {
      if ("name".equals(at[i][0]))
        fragment.setName(at[i][1]);
      else if ("language".equals(at[i][0]) || "lang".equals(at[i][0]))
        fragment.setLanguage(at[i][1]);
      else if ("hidden".equals(at[i][0]) && "true".equalsIgnoreCase(at[i][1]))
        fragment.setHidden(true);
    }
    fragment.setContent(content);
    return fragment;
  }

  protected boolean skipToNextTag(MuseSink sink, String tag, String[][] at,
      String content) {
        if (getNext() != null)
          return getNext().block(sink, tag, at, content);
        else
          return false;
      }

  public boolean flow(MuseSink sink, String tag, String[][] at, String content) {
    return block(sink, tag, at, content);
  }

}