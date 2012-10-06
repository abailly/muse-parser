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

import java.io.Reader;

import oqube.muse.parser.MuseParser;
import oqube.muse.MuseSink;

import org.apache.maven.doxia.parser.AbstractParser;
import org.apache.maven.doxia.parser.ParseException;
import org.apache.maven.doxia.sink.Sink;

/**
 * Parse the muse file format.
 * 
 * @author oqube
 * @plexus.component role="org.apache.maven.doxia.parser.Parser" role-hint="muse"
 */
public class MuseDoxiaParser extends AbstractParser
{

    /**
     * @plexus.requirement role="oqube.muse.MuseSink" role-hint="muse-doxia"
     */
    private MuseSink museSink;

    private MuseParser parser = new MuseParser();

    /**
     * @see org.apache.maven.doxia.parser.Parser#parse(java.io.Reader, org.apache.maven.doxia.sink.Sink)
     */
    public final synchronized void parse( final Reader reader, final Sink sink ) throws ParseException
    {
        try
        {
            // instantiate wrapper
            ( (SinkWrapper) museSink ).setSink( sink );
            museSink.start("document",null);
            parser.setSink( museSink );
            parser.setReader( reader );
            parser.start();
            museSink.end("document");
        }
        catch ( final Exception e )
        {
            throw new ParseException( e );
        }
    }
}
