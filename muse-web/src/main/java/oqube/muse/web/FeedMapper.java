/**
 *  Copyright (C) 2008 - OQube / Arnaud Bailly
    Licensed under MIT open-source license: 
    see http://www.opensource.org/licenses/mit-license.php
    Created 29 sept. 08
 */
package oqube.muse.web;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nono
 * 
 */
public class FeedMapper extends PathMapper {

  public FeedMapper(PathMapper next) {
    super(next);
  }

  public FeedMapper() {
    super(null);
  }

  @Override
  protected Page doMap(String s) {
    File f = new File(getRoot(), stripLeadingSlash(s) + ".muse");
    if (f.exists() && f.canRead())
      return new FeedPage(f);
    else
      return getNext().doMap(s);
  }

}
