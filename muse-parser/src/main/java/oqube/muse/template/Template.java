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
 * Created 22 juil. 07
 */
package oqube.muse.template;

import java.util.Map;

/**
 * Abstract representation of some template to insert in output files.
 * 
 * @author nono
 * 
 */
public interface Template {

  /**
   * Constant template. Always output the empty string.
   */
  Template DEFAULT = new Template() {

    public String content(Map<Object, Object> env) {
      return "";
    }

  };

  /**
   * Get this template's evaluation in given environment.
   * 
   * @param environment
   *          the environment to use. May not be null.
   * @return a possibly empty string representing template's content.
   */
  String content(Map<Object, Object> environment);
}
