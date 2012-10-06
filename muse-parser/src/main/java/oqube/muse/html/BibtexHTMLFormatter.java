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

 Created 9 oct. 2006
 */
package oqube.muse.html;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexPerson;
import bibtex.dom.BibtexPersonList;

/**
 * This utility class is used to format bibtex entries extracted by a
 * {@see oqube.muse.html.BibtexLinker}. Entries are output as as divs, with
 * each entry field output as a span with class name as bibtex_xxx.
 * BibtexpersonList are handled to generate correctly formatted lists. A
 * property file is used to localize various text elements (eg. "and"). The
 * whole set of entries is enclosed in another div with a title as bibliography.
 * 
 * @author nono
 * 
 */
public class BibtexHTMLFormatter {

  public static final String AND_KEY = "bibtex.and";

  public static final String BIBTITLE_KEY = "bibtex.title";

  private PropertyResourceBundle props = (PropertyResourceBundle) PropertyResourceBundle
      .getBundle("oqube.muse.html.bibtexformat");

  public void format(PrintWriter pw, List entries) {
    if(entries == null || entries.isEmpty())
      return;
    pw.println("<div name=\"bibliography\" id=\"bibliography\">");
    pw.print("<span class=\"bibtex.title\">" + props.getString(BIBTITLE_KEY) + "</span>");
    int idx = 1;
    for (Iterator i = entries.iterator(); i.hasNext();) {
      BibtexEntry entry = (BibtexEntry) i.next();
      pw.println("<div id=\"" + entry.getEntryKey() + "\" name=\""
          + entry.getEntryKey() + "\" >");
      pw.print("<span class=\"label\">[");
      pw.print(idx++);
      pw.print("]</span>");          
      // output all fields as spans
      for (Iterator j = entry.getFields().entrySet().iterator(); j.hasNext();) {
        Map.Entry me = (Map.Entry) j.next();
        String k = (String) me.getKey();
        Object v = me.getValue();
        pw.print("<span class=\"" + k + "\">");
        if (v instanceof BibtexPersonList) {
          formatList(pw, k, (BibtexPersonList) v);
        } else
          format(pw, k, (BibtexAbstractValue) v);
        pw.print("</span>");
      }
      pw.println("</div>");
    }
    pw.println("</div>");
  }

  private void format(PrintWriter pw, String k, BibtexAbstractValue value) {
    pw.print(unbracket(evenspace(value.toString())));
  }

  private void formatList(PrintWriter pw, String k, BibtexPersonList list) {
    int ln = list.getList().size();
    int i = ln;
    for (Iterator it = list.getList().iterator(); it.hasNext();) {
      BibtexPerson p = (BibtexPerson) it.next();
      pw.print(p.getFirst());
      pw.print(' ');
      if (p.getPreLast() != null) {
        pw.print(p.getPreLast());
        pw.print(' ');
      }
      pw.print(p.getLast());
      if (i == 2)
        pw.print(" " + props.getString(AND_KEY) + " ");
      else if (i > 1)
        pw.print(", ");
      i--;
    }
  }

  /**
   * Removes bracketed parts of expressions.
   * 
   * @param string
   * @return
   */
  private String unbracket(String string) {
    StringBuffer sb = new StringBuffer();
    Pattern pat = Pattern.compile("\\{([^{}]+)\\}");
    Matcher m = pat.matcher(string);
    while (m.find()) {
      String vn = m.group(1);
      /* replace in string */
      m.appendReplacement(sb, vn);
    }
    m.appendTail(sb);
    return sb.toString();
  }

  /**
   * Replace all kind of space by one space.
   * 
   * @param string
   * @return
   */
  private String evenspace(String string) {
    StringBuffer sb = new StringBuffer();
    Pattern pat = Pattern.compile("\\s+");
    Matcher m = pat.matcher(string);
    while (m.find()) {
      /* replace in string */
      m.appendReplacement(sb, " ");
    }
    m.appendTail(sb);
    return sb.toString();
  }

}
