/*
 * Copyright (c) 2007 - OQube / Arnaud Bailly This library is free software; you
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
 * Created 2 nov. 07
 */
package oqube.muse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A formatter delegate handles storing of {@link ElementHandler} instances and
 * delegation of formatting to these instances.
 * 
 * Basic property of a delegate is handling of mutiple handlers per tag.
 * 
 * @author nono
 * 
 */
public class FormatterDelegate {

  private Map<String, List<ElementHandler>> handlers = new HashMap<String, List<ElementHandler>>();

  public void end(String element) {
    List<ElementHandler> el = handlers.get(element);
    if (el != null)
      for (ElementHandler h : el)
        h.end();
  }

  public void start(String element, Map<String, String> parameters) {
    List<ElementHandler> el = handlers.get(element);
    if (el != null)
      for (ElementHandler h : el)
        h.start(parameters);
  }

  public void prepend(String string, ElementHandler elementHandler) {
    List<ElementHandler> el = handlers.get(string);
    if (el == null) {
      el = new ArrayList<ElementHandler>();
      handlers.put(string, el);
    }
    el.add(0, elementHandler);
  }

  public void append(String string, ElementHandler elementHandler) {
    List<ElementHandler> el = handlers.get(string);
    if (el == null) {
      el = new ArrayList<ElementHandler>();
      handlers.put(string, el);
    }
    el.add(elementHandler);
  }

  public void clear(String element) {
    handlers.remove(element);
  }

}
