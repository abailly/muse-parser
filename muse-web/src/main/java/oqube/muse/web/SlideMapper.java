package oqube.muse.web;

import java.io.File;

import oqube.muse.html.HTMLPublisher;
import oqube.muse.html.SlidePublisher;

public class SlideMapper extends PathMapper {

  public SlideMapper(PathMapper next) {
    super(next);
  }

  public SlideMapper() {
    super(null);
  }

  @Override
  protected Page doMap(String s) {
    File f = new File(getRoot(), stripLeadingSlash(s) + ".muse");
    if (f.exists() && f.canRead())
      return new SlidePage(f, getPublisher());
    else
      return getNext().doMap(s);
  }
}
