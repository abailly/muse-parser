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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Maps <code>.muse</code> extension request into muse files.
 * 
 * @author abailly@oqube.com
 * @version $Id$
 */
public class MuseMapper extends PathMapper {

  private static final Pattern pat = Pattern.compile(".*\\.muse");

  public MuseMapper(PathMapper next) {
    super(next);
  }

  public MuseMapper() {
    super(null);
  }

  @Override
  protected Page doMap(String s) {
    Matcher m = pat.matcher(s);
    if (m.matches()) {
      File f = new File(getRoot(), stripLeadingSlash(s));
      return new MusePage(f,getPublisher());
    }
    return null;
  }

}
