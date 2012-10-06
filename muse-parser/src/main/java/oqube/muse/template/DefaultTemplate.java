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

import fr.lifl.utils.SubstitutableString;

/**
 * Simple templates. Default templates are very simple and implement
 * straightforward variable substitution. A variable <code>var</code> in the
 * template is denoted as string <code>${var}</code>. The string value (as
 * given by toString()) of the variable is extracted from the environment and
 * replaced in the output. If the variable is undefined, the empty string is
 * output.
 * 
 * @author nono
 * 
 */
public class DefaultTemplate implements Template {

  private SubstitutableString data;

  public DefaultTemplate(String string) {
    this.data = new SubstitutableString(string);
    this.data.setIgnoreMissing(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.template.Template#content(java.util.Map)
   */
  public String content(Map<Object, Object> env) {
    return data.instance(env);
  }

}
