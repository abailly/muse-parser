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
package oqube.muse.literate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import fr.lifl.utils.SubstitutableString;

import oqube.muse.AbstractTagHandler;
import oqube.muse.MuseSink;
import oqube.muse.MuseTagHandler;


/**
 * Base class for handling literate source fragments.
 * This class collects fragments from its tags (given through block method)
 * and produces a set of source files according to the tags structure. Syntax
 * follows roughly the <em>noweb</em> package syntax:
 * <ul> 
 * <li>Each block is named with an arbitrary string given in the 
 * standard XML syntax as a <code>name</code> attribute,</li>
 * <li>Within blocks, references to other (named) blocks can be made 
 * using <code>&lt;&lt;name&gt;&gt;</code> syntax,</li>
 * <li>Content of identically named blocks is concatenated,</li>
 * <li>Name of toplevel blocks (ie. blocks not included by another block) 
 * is used as filename for final output.</li>
 * </ul>
 * This class alone does not produce data to be included into documents, this
 * is the responsibility of format aware tag handlers. This class just 
 * collate fragments that are part of the same file.
 * 
 * @author nono
 * @version $Id: /local/muse-parser/oqube/src/main/java/oqube/muse/literate/LiterateSourceHandler.java 968 2006-09-20T12:25:14.437654Z nono  $
 */
public class DefaultSourceRepository implements SourceRepository {

  /*
   * A map from names to substituable strings.
   */
  private Map /* < String, SubstitutableString >*/ fragments = new HashMap();
  
  private String pattern = "(<<([^>]+)>>)";
  
  /* 
   * set of referenced names.
   */
  private Set refs = new HashSet();

  /*
   * if true, unresolved references are ignored.
   */
  private boolean lenient;
  
  /* (non-Javadoc)
   * @see oqube.muse.literate.SourceRepository#collectSources()
   */
  public Map /* < String, String > */ collectSources() {
    Map ret = new HashMap();
    Set top = new HashSet(fragments.keySet());
    top.removeAll(refs);
    for(Iterator i=top.iterator();i.hasNext();) {
      String name = (String)i.next();
      SubstitutableString val = (SubstitutableString)fragments.get(name);
      ret.put(name,val.instance(fragments));
    }
    return ret;
  }

  /* (non-Javadoc)
   * @see oqube.muse.literate.SourceRepository#addFragment(java.lang.String, java.lang.String)
   */
  public void addFragment(String name, String content) {
    SubstitutableString st = new SubstitutableString(content,pattern);
    st.setRecurse(true);
    st.setIgnoreMissing(lenient);
    refs.addAll(st.variables());
    SubstitutableString s = (SubstitutableString) fragments.get(name);
    if(s != null)
      fragments.put(name,s.append(st));
    else 
      fragments.put(name,st);
  }

  /* (non-Javadoc)
   * @see oqube.muse.literate.SourceRepository#isLenient()
   */
  public boolean isLenient() {
    return lenient;
  }

  /* (non-Javadoc)
   * @see oqube.muse.literate.SourceRepository#setLenient(boolean)
   */
  public void setLenient(boolean lenient) {
    this.lenient = lenient;
  }

  public void reset() {
    fragments = new HashMap();
    refs =  new HashSet();
  }
  
}
