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

import oqube.muse.AbstractTagHandler;
import oqube.muse.MuseSink;
import oqube.muse.SimpleTagHandler;
import oqube.muse.html.HTMLSourceTag;

import org.apache.maven.doxia.sink.Sink;

/**
 * Tag handler for doxia sinks. This class adds support for some metadata information: title, author and date and
 * modifies the behavior of example tag.
 * 
 * @author abailly@oqube.com
 * @version $Id$
 * @plexus.component role="oqube.muse.MuseTagHandler" role-hint="muse-xhtml"
 */
public class DefaultTagHandler extends AbstractTagHandler
{

    public DefaultTagHandler()
    {
        addTag( "example", new SimpleTagHandler()
        {
            public void doHandle( MuseSink sink, String content )
            {
                Sink sk = ( (SinkWrapper) sink ).getSink();
                sk.verbatim( true );
                sk.text( content );
                sk.verbatim_();
            }
        } );

        addTag( "title", new SimpleTagHandler()
        {
            public void doHandle( MuseSink sink, String content )
            {
                Sink sk = ( (SinkWrapper) sink ).getSink();
                sk.title();
                sk.text( content );
                sk.title_();
            }
        } );

        addTag( "author", new SimpleTagHandler()
        {
            public void doHandle( MuseSink sink, String content )
            {
                Sink sk = ( (SinkWrapper) sink ).getSink();
                sk.author();
                sk.text( content );
                sk.author_();
            }
        } );

        addTag( "date", new SimpleTagHandler()
        {
            public void doHandle( MuseSink sink, String content )
            {
                Sink sk = ( (SinkWrapper) sink ).getSink();
                sk.date();
                sk.text( content );
                sk.date_();
            }
        } );
        setNext( new HTMLSourceTag() );
    }

}
