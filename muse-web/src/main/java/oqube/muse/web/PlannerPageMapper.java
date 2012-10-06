/*
 *  Copyright (c) 2007 - OQube / Arnaud Bailly
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  Created 2 nov. 07
 */
package oqube.muse.web;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Maps planner page references.
 * 
 * @author nono
 *
 */
public class PlannerPageMapper extends PathMapper {
  private static final Pattern pat = Pattern.compile(".*(\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d)");

  public PlannerPageMapper(PathMapper next) {
    super(next);
  }

  public PlannerPageMapper() {
    super(null);
  }

  @Override
  protected Page doMap(String s) {
    Matcher m = pat.matcher(s);
    if (m.matches()) {
      s = stripLeadingSlash(s);
      return new MusePage(new File(getRoot(), s + ".muse"),getPublisher());
    }
    return null;
  }


}
