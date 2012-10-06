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

 Created 18 sept. 2006
 */
package oqube.muse;

import java.util.HashMap;
import java.util.Map;


/**
 * Abstract for tag handlers working along the chain of responsibility pattern.
 * 
 * @author nono
 * 
 */
public class AbstractTagHandler implements MuseTagHandler {

  /*
   * next in chain.
   */
  private MuseTagHandler next;

  /*
   * map from tag names to handlers.
   */
  private Map tags = new HashMap();

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseTagHandler#block(oqube.muse.MuseSink, java.lang.String,
   *      java.lang.String)
   */
  public boolean block(MuseSink sink, String tag, String[][]at,String content) {
    MuseTagHandler tg = (MuseTagHandler) tags.get(tag);
    if (tg != null && tg.block(sink, tag, at,content)) {
      return true;
    } else if (next != null)
      return next.block(sink, tag, at,content);

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseTagHandler#flow(oqube.muse.MuseSink, java.lang.String,
   *      java.lang.String)
   */
  public boolean flow(MuseSink sink, String tag, String[][]at,String content) {
    MuseTagHandler tg = (MuseTagHandler) tags.get(tag);
    if (tg != null && tg.flow(sink, tag, at,content)) {
      return true;
    } else if (next != null)
      return next.flow(sink, tag, at,content);
    return false;
  }

  /**
   * @return Returns the next.
   */
  public MuseTagHandler getNext() {
    return next;
  }

  /**
   * @param next
   *          The next to set.
   */
  public void setNext(MuseTagHandler next) {
    this.next = next;
  }

  /**
   * For use by subclass that wish to override part of the behavior.
   */
  public Map getTags() {
    return tags;
  }

  public void addTag(String name, MuseTagHandler hdl) {
    this.tags.put(name, hdl);
  }

}
