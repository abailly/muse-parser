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
 * Created 19 sept. 2006
 */
package oqube.muse.html;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oqube.muse.AbstractTagHandler;
import oqube.muse.MuseSink;

/**
 * A class that generates an XHTML fragment from <source> tags encountered in a
 * muse literate file. Highlighting is delegated to client-side container. This
 * handler generates links between the various elements.
 * 
 * @author nono
 */
public class LinkingHTMLSourceTag extends HTMLSourceTagHandler {

  private HTMLFragmentLinker fragmentLinker = new HTMLFragmentLinker();

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
    SourceFragment sourceFragment = makeFragmentFrom(at, content);
    handleLinking(sink, sourceFragment);
    doOutput(sink, sourceFragment);
    return true;
  }

  private void handleLinking(MuseSink sink, SourceFragment sourceFragment) {
    fragmentLinker.add(sourceFragment);
    fragmentLinker.setSink(sink);
    fragmentLinker.outputLinksFor(sourceFragment);
    fragmentLinker.substituteContent(sourceFragment);
  }

}
