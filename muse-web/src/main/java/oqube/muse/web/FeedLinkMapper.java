/**
 * Copyright (C) 2008 - OQube / Arnaud Bailly Licensed under MIT open-source
 * license: see http://www.opensource.org/licenses/mit-license.php Created 30
 * sept. 08
 */
package oqube.muse.web;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A mapper for feed links that outputs a feed entry content.
 * 
 * @author nono
 * 
 */
public class FeedLinkMapper extends PathMapper {

  private static final Pattern uriPattern = Pattern
      .compile("(.*)/([0-9]{4})/([0-9]{2})/([0-9]{2})/(.*)");

  public FeedLinkMapper() {
    super(null);
  }
  
  public FeedLinkMapper(PathMapper next) {
    super(next);
  }

  @Override
  protected Page doMap(String path) {
    Matcher m = uriPattern.matcher(path);
    if (m.matches()) {
      File f = new File(getRoot(), m.group(1) + ".muse");
      return new MuseFeedPage(f, m.group(2) + m.group(3) + m.group(4), m
          .group(5),getPublisher());
    } else
      return null;
  }

}
