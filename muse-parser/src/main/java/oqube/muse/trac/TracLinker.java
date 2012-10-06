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
 * Created 18 juin 07
 */
package oqube.muse.trac;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oqube.muse.AbstractMuseLinker;
import oqube.muse.MuseSink;

/**
 * A class for generating links when publishing to trac. T
 * 
 * @author nono
 * 
 */
public class TracLinker extends AbstractMuseLinker {

  final static Pattern imgs = Pattern.compile(".*(\\.jpeg|\\.png|\\.gif)");

  final static Pattern muse = Pattern
      .compile("([\\./]+)?+([^#]*)(\\.muse)(:?#(\\w+))?");

  AbstractMuseLinker imageLinker = new AbstractMuseLinker() {

    public void link(MuseSink sink, String link, String text) {
      Matcher m = imgs.matcher(link);
      if (m.matches()) {
        sink.rawText("[[Image(" + link + ", alt=\"" + text + "\")]]");
      } else {
        getNext().link(sink, link, text);
      }
    }
  };

  class Path {

    Path next = null;

    String path = null;

    Path(String uri) {
      int slash = uri.indexOf('/');
      if (slash >= 0) {
        next = new Path(uri.substring(slash + 1));
        path = uri.substring(0, slash);
      } else
        path = uri;
    }

    public String toString() {
      if (next != null)
        return Character.toUpperCase(path.charAt(0)) + path.substring(1)
            + next.toString();
      else
        return path;
    }

  }

  AbstractMuseLinker museLinker = new AbstractMuseLinker() {

    public void link(MuseSink sink, String link, String text) {
      Matcher m = muse.matcher(link);
      if (m.matches()) {
        // a muse link - output a wiki: link
        String uri = m.group(2);
        // transform paths to camel case
        sink.rawText("[wiki:" + new Path(uri).toString() + " " + text + "]");
      } else
        getNext().link(sink, link, text);
    }

  };

  AbstractMuseLinker defaultLinker = new AbstractMuseLinker() {
    public void link(MuseSink sink, String link, String text) {
      sink.rawText("[" + link + " " + text + "]");
    }
  };

  public void link(MuseSink sink, String link, String text) {
    // delegate
    getNext().link(sink, link, text);
  }

  public TracLinker() {
    setNext(imageLinker);
    imageLinker.setNext(museLinker);
    museLinker.setNext(defaultLinker);
  }
}
