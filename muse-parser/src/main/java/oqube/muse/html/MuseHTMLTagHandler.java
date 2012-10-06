/*______________________________________________________________________________
 * 
 * Copyright (C) 2006 Arnaud Bailly / OQube 
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *  
 * email: contact@oqube.com
 * creation: Wed Aug 23 2006
 */
package oqube.muse.html;

import oqube.muse.AbstractTagHandler;
import oqube.muse.MuseSink;
import oqube.muse.MuseTagHandler;
import oqube.muse.SimpleTagHandler;

/**
 * A tag handler that output XHTML code when encountering known tags. Tags that
 * are understood and XTHML result are listed in following table. The behavior
 * is the same as starndard muse publisher under emacs : <table>
 * <tr>
 * <th>Tag</th>
 * <th>Action</th>
 * </tr>
 * <tr>
 * <td>example</td>
 * <td>Output content as &lt;pre&gt; block with <code>example</code> as class</td>
 * </tr>
 * <tr>
 * <td>code</td>
 * <td>Output content &lt;pre&gt; block with <code>code</code> as class</td>
 * </tr>
 * <tr>
 * <td>literal</td>
 * <td>Output content in tag as-is</td>
 * </tr>
 * <tr>
 * <td>comment</td>
 * <td>Output content as a comment in html</td>
 * </tr>
 * <table>
 * 
 * @author abailly@norsys.fr
 * @version $Id$
 * @plexus.component role="oqube.muse.MuseTagHandler" role-hint="xhtml"
 */
public class MuseHTMLTagHandler extends AbstractTagHandler {

  class Example extends SimpleTagHandler {

    public void doHandle(MuseSink sink, String content) {
      sink.rawText("<pre class=\"example\">");
      sink.text(content);
      sink.rawText("</pre>");
    }
  }

  class Code extends SimpleTagHandler {

    public void doHandle(MuseSink sink, String content) {
      sink.rawText("<code>");
      sink.text(content);
      sink.rawText("</code>");
    }

  }

  class Literal extends SimpleTagHandler {

    public void doHandle(MuseSink sink, String content) {
      sink.rawText(content);
    }
  }

  class Comment extends SimpleTagHandler {

    public void doHandle(MuseSink sink, String content) {
      sink.rawText("<!-- ");
      sink.rawText(content);
      sink.rawText(" -->");
    }
  }

  public MuseHTMLTagHandler() {
    addTag("example", new Example());
    addTag("code", new Code());
    addTag("literal", new Literal());
    addTag("comment", new Comment());
    setNext(new MuseTagHandler() {

      public boolean flow(MuseSink sink, String tag, String[][] at,
          String content) {
        sink.rawText("<span class=\"" + tag + "\"");
        if (at != null)
          for (int i = 0; i < at.length; i++)
            sink.rawText(at[0] + "=" + at[1]);
        sink.rawText(">");
        sink.text(content);
        sink.rawText("</span>");
        return true;
      }

      public boolean block(MuseSink sink, String tag, String[][] at,
          String content) {
        sink.rawText("<div class=\"" + tag + "\"");
        if (at != null)
          for (int i = 0; i < at.length; i++)
            sink.rawText(at[i][0] + "=" + at[i][1]);
        sink.rawText(">");
        sink.text(content);
        sink.rawText("</div>");
        return true;
      }

    });
  }
}
