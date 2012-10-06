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

Created 1 oct. 2006
 */
package oqube.muse;

import java.util.regex.Pattern;

/**
 * Base linker class imlpementing Chain of responsibility pattern.
 * 
 * @author nono
 *
 */
public class AbstractMuseLinker implements MuseLinker {

  /* handle different subcases */
  public static final Pattern imageUrl = Pattern.compile(".*(\\.jpeg|\\.png|\\.gif|\\.svg)");
  public static final Pattern externalUrl = Pattern.compile("(http|ftp)://(\\w+\\.?)+(/(.+))*");
  public static final Pattern museUrl = Pattern.compile("([^#]*)(\\.muse)(:?#(\\w+))?");
  private MuseLinker next;

  /**
   * @return Returns the next.
   */
  public MuseLinker getNext() {
    return next;
  }

  /**
   * @param next The next to set.
   */
  public void setNext(MuseLinker next) {
    this.next = next;
  }

  /* (non-Javadoc)
   * @see oqube.muse.MuseLinker#link(oqube.muse.MuseSink, java.lang.String, java.lang.String)
   */
  public void link(MuseSink sink, String link, String text) {
    if (next != null) {
      next.link(sink, link, text);
    }
  }

  /* (non-Javadoc)
   * @see oqube.muse.MuseLinker#anchor(oqube.muse.MuseSink, java.lang.String)
   */
  public void anchor(MuseSink sink, String anchor) {
    if (next != null) {
      next.anchor(sink, anchor);
    }
  }
}
