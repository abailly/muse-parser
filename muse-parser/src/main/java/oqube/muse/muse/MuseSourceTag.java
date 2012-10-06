/**
 *  Copyright (C) 2006 - OQube / Arnaud Bailly
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

 Created 19 sept. 2006
 */
package oqube.muse.muse;

import java.util.HashMap;
import java.util.Map;

import oqube.muse.AbstractTagHandler;
import oqube.muse.MuseSink;

/**
 * A class that generates an muse fragment from <source> tags encountered in a
 * muse literate file. This class uses a
 * {@link oqube.muse.literate.DefaultSourceRepository} object if it exists so that
 * fragments can later be processed for source code generation. Fragments 
 * are output as is enclosed in a <source> tag and with a leading anchor 
 * identifying the fragment. Inline references to other fragments are kept
 * as is for further processing.
 * 
 * @author nono
 * @see oqube.muse.html.LinkingHTMLSourceTag 
 */
public class MuseSourceTag extends AbstractTagHandler {

  /*
   * map from fragment names to references count.
   */
  private Map ids = new HashMap();

  private static final String EOL = System.getProperty("line.separator");
  
  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseTagHandler#block(oqube.muse.MuseSink, java.lang.String,
   *      java.lang.String[][], java.lang.String)
   */
  public boolean block(MuseSink sink, String tag, String[][] at, String content) {
    // Check tag and parameters
    if (!"source".equals(tag))
      return false;
    StringBuffer ats = new StringBuffer();
    ats.append(EOL);
    ats.append("<source");
    for (int i = 0; i < at.length; i++) {
      if("hidden".equals(at[i][0]) && "true".equalsIgnoreCase(at[i][1]))
        return true;
      ats.append(' ').append(at[i][0]).append('=').append('"').append(at[i][1]).append('"');
    }
    ats.append('>');
    sink.rawText(ats.toString());
    sink.rawText(EOL);
    sink.rawText(content);
    sink.rawText(EOL);
    sink.rawText("</source>");
    sink.rawText(EOL);
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseTagHandler#flow(oqube.muse.MuseSink, java.lang.String,
   *      java.lang.String[][], java.lang.String)
   */
  public boolean flow(MuseSink sink, String tag, String[][] at, String content) {
    return block(sink, tag, at, content);
  }

}
