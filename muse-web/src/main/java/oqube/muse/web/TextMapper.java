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
 * Created 7 juil. 07
 */
package oqube.muse.web;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Map common text file types.
 * 
 * @author nono
 * 
 */
public class TextMapper extends PathMapper {

  private static final Pattern pat = Pattern.compile("(.*)\\.(css|xml|js)");

  public TextMapper(PathMapper next) {
    super(next);
  }

  public TextMapper() {
    super(null);
  }

  @Override
  protected Page doMap(String path) {
    Matcher m = pat.matcher(path);
    if (m.matches()) {
      path = m.group(1) + '.' + m.group(2);
      return new DefaultPage("text/" + m.group(2), new File(getRoot(), path));
    }
    return null;
  }

}
