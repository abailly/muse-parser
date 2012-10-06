/*______________________________________________________________________________
 * 
 * Copyright 2006  Arnaud Bailly - 
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 * (2) Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 * (3) The name of the author may not be used to endorse or promote
 *     products derived from this software without specific prior
 *     written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on Mon Aug 21 2006
 *
 */
package oqube.muse.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oqube.muse.AbstractMuseLinker;
import oqube.muse.MuseSink;

/**
 * A basic linker that generates links for muse pages. This linker output an
 * XHTML anchor tag with the =.muse= suffix replaced by a given suffix (by
 * default, this is =.html=).
 * 
 * @author abailly@oqube.muse.com
 * @version $Id$
 * @plexus.component role="oqube.muse.MuseLinker" role-hint="xhtml"
 */
public class MuseAnchorLinker extends AbstractMuseLinker {

  /**
   * @plexus.configuration default-value="muse"
   */
  private String sourceSuffix = "muse";

  /**
   * @plexus.configuration default-value="html"
   */
  private String targetSuffix = "html";

  /**
   * A pattern denoting images.
   * 
   * @plexus.configuration default-value=".*(\\.jpeg|\\.png|\\.gif|\\.svg)"
   */
  private String imagePattern = ".*(\\.jpeg|\\.png|\\.gif|\\.svg)";

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseLinker#link(oqube.muse.MuseSink, java.lang.String,
   *      java.lang.String)
   */
  public void link(MuseSink sink, String link, String text) {
    Pattern imgs = Pattern.compile(imagePattern);
    Matcher m = imgs.matcher(link);
    if (m.matches()) // an image
      sink.rawText(new StringBuffer().append("<img src=\"").append(link)
          .append("\" alt=\"").append(text).append("\" />").toString());
    Pattern muse = Pattern.compile("([^.]*)(\\." + sourceSuffix + ")?");
    m = muse.matcher(link);
    if (m.matches()) // a muse link
      sink.rawText(new StringBuffer().append("<a href=\"").append(m.group(1))
          .append(".").append(targetSuffix).append('"').append(" />").append(
              text).append("</a>").toString());
    else 
      super.link(sink,link,text);
  }

}
