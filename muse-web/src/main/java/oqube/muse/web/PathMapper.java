/**
 * Copyright (C) 2007 - OQube / Arnaud Bailly This library is free software; you
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
 * Created on Thu Jul 5 2007
 */
package oqube.muse.web;

import java.io.File;

import oqube.muse.html.HTMLPublisher;

/**
 * A mapper from paths to paths. Implemented as a chain of responsibility
 * pattern.
 * 
 * @author abailly@oqube.com
 * @version $Id$
 */
public class PathMapper {

  public static final PathMapper DEFAULT = new PathMapper(null) {

    @Override
    protected Page doMap(String path) {
      File f = new File(path);
      if (f.exists() && f.canRead())
        return new DefaultPage(f);
      else
        return null;
    }

  };

  private PathMapper next = null;

  private File root;

  private HTMLPublisher publisher;

  public PathMapper(PathMapper next) {
    this.setNext(next);
  }

  public final Page map(String path) {
    Page ret = null;
    try {
      ret = doMap(path);
    } catch (Exception e) {
    }
    if (ret == null && getNext() != null)
      ret = getNext().map(path);
    return ret != null ? ret : DEFAULT.doMap(new File(root,
        stripLeadingSlash(path)).getAbsolutePath());
  }

  public String stripLeadingSlash(String s) {
    int pref = 0;
    int ln = s.length();
    for (; pref < ln && s.charAt(pref) == '/'; pref++)
      ;
    if (pref != 0)
      s = s.substring(pref);
    return s;
  }

  protected Page doMap(String path) {
    return null;
  }

  public File getRoot() {
    return root;
  }

  public void setRoot(File root) {
    this.root = root;
    if (getNext() != null)
      getNext().setRoot(root);
  }

  public void setPublisher(HTMLPublisher publisher) {
    this.publisher = publisher;
    if (getNext() != null)
      getNext().setPublisher(publisher);
  }

  public HTMLPublisher getPublisher() {
    return publisher;
  }

  public void setNext(PathMapper next) {
    this.next = next;
  }

  public PathMapper getNext() {
    return next;
  }
}
