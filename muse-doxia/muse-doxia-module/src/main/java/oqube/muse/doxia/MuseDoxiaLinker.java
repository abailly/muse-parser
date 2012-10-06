/*
 *  Copyright 2006 OQube contact at oqube dot com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package oqube.muse.doxia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oqube.muse.MuseLinker;
import oqube.muse.MuseSink;

import org.apache.maven.doxia.sink.Sink;

/**
 * Basic linker for Doxia-muse integration. This linker is very crude: It transforms URL likely to point to images as
 * inline images, URLs for muse pages in relative links and output all other URLS as is.
 * 
 * @author abailly@oqube.com
 * @version $Id$
 * @plexus.component role="oqube.muse.MuseLinker" role-hint="muse-linker"
 */
public class MuseDoxiaLinker implements MuseLinker
{


    /*
     *  (non-Javadoc)
     * @see oqube.muse.MuseLinker#link(oqube.muse.MuseSink, java.lang.String, java.lang.String)
     */
    public void link( MuseSink sk, String s, String t ){
        /* extract real Sink */
      Sink sink = ( (SinkWrapper) sk ).getSink();
      /* handle different subcases */
      /* handle different subcases */
      Pattern imgs = Pattern.compile(".*(\\.jpeg|\\.png|\\.gif)");
      Pattern uris = Pattern.compile("(http|ftp)://(\\w+\\.?)+(/(.+))*");
      Pattern muse = Pattern.compile("([^#]*)(\\.muse)(:?#(\\w+))?");
      Matcher m = imgs.matcher(s);
      if (m.matches()) { // an image
	sink.figure();	  
	sink.figureGraphics( s );
	sink.figureCaption( );
	sink.text( t);
	sink.figureCaption_( );
	sink.figure_();
	return;
      }
    // t may be empty
      if (t == null || "".equals(t))
	t = s;
      // check uris
      m = uris.matcher(s);
      if (m.matches()) {
	sink.link( s);
	sink.text(t);
	sink.link_();
	return;
      }
      m = muse.matcher(s);
      if (m.matches()) {
	// a muse link - always output an html link which is bad
	// if uri does not start with a slash, add it
	String uri = m.group(1);
	if (uri == null || uri.length() == 0) {
	  assert m.group(4) != null;
	  sink.link("#" + m.group(4));
	} else if (m.group(3) != null) // link with an anchor
	  sink.link(uri + ".html#" + m.group(4));
	else
	  sink.link(uri + ".html");
	sink.text(t);
	sink.link_();
      } else {
	sink.link(s);
	sink.text(t);
	sink.link_();
      }      
    }

    /*
     *  (non-Javadoc)
     * @see oqube.muse.MuseLinker#anchor(oqube.muse.MuseSink, java.lang.String)
     */
    public void anchor( MuseSink sk, String s )
    {
        /* extract real Sink */
        Sink sink = ( (SinkWrapper) sk ).getSink();
        sink.anchor( s );
        sink.anchor_();
    }
}
