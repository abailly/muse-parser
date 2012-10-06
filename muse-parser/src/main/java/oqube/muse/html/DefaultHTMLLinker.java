/**
 * Copyright (C) 2006 - OQube / Arnaud Bailly This library is free software; you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * Created 9 oct. 2006
 */
package oqube.muse.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oqube.muse.AbstractMuseLinker;
import oqube.muse.MuseSink;

/**
 * @author nono
 * 
 */
public class DefaultHTMLLinker extends AbstractMuseLinker {

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseLinker#anchor(oqube.muse.MuseSink, java.lang.String)
   */
  public void anchor(MuseSink sink, String anchor) {
    sink.rawText(new StringBuffer().append("<a name=\"").append(anchor).append(
            '"').append("></a>").toString());
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.AbstractMuseLinker#link(oqube.muse.MuseSink,
   *      java.lang.String, java.lang.String)
   */
  public void link(MuseSink sink, String s, String t) {
    if (t == null || "".equals(t)) {
      t = s;
    }
    if (imageUrl.matcher(s).matches()) { // an image
      sink.rawText("<img src=\"" + s + "\" alt=\"" + t + "\" />");
    } else if (externalUrl.matcher(s).matches()) {
      sink.rawText("<a href=\"" + s + "\" >");
      sink.text(t);
      sink.rawText("</a>");
    } else {
      Matcher m = museUrl.matcher(s);
      if (m.matches()) {
        // a muse link - always output an html link which is bad
        // if uri does not start with a slash, add it
        String uri = m.group(1);
        if (uri == null || uri.length() == 0) {
          assert m.group(4) != null;
          sink.rawText("<a href=\"#" + m.group(4) + "\">");
        } else if (m.group(3) != null) // link with an anchor
        {
          sink.rawText("<a href=\"" + uri + ".html#" + m.group(4) + "\">");
        } else {
          sink.rawText("<a href=\"" + uri + ".html\" >");
        }
        sink.text(t);
        sink.rawText("</a>");
      } else {
        sink.rawText("<a href=\"" + s + "\">");
        sink.text(t);
        sink.rawText("</a>");
      }
    }

  }
}