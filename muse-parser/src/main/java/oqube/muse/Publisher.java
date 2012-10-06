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

    Created 19 sept. 2006
 */
package oqube.muse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.logging.Log;

/**
 * An facade interface for configuring a set of objcets
 * for publishing muse files.
 * 
 * @author nono
 *
 */
public interface Publisher {
  
  /**
   * The file extension associated with this publisher.
   * 
   * @return a string (with leading dot) for extending file names 
   * published with this instance. 
   */
  String getExtension();

  /**
   * Sets the output encoding used for publishing.
   * This is for informative purpose only and to allow publisher to produce
   * data according to publishing format. Publisher should not attempt
   * to change encoding of output writer.
   * 
   * @param outputEncoding a valid encoding.
   */
  void setOutputEncoding(String outputEncoding);

  /**
   * Called to process a stream of data.
   * 
   * @param session identifier for this processes session.
   * @param br the input character stream.
   * @param pw the output character stream. May be null.
   * @throws IOException
   */
  void process(String session,Reader br, PrintWriter pw) throws IOException;

  
  /**
   * Called when starting a publishing session.
   * Should be called before any call to {@link #process(Reader, Writer)} to
   * give publisher a chance to initialize its state.
   * 
   * @param id a session identifier. Format depends on the caller,
   * may be used for messages.
   */
  void startSession(String id);
  
  /**
   * Called when session end.
   * No more calls to {@link #process(Reader, Writer)} should be made 
   * before another session is being initiated with {@link #startSession(String)}. 
   * This method allows publisher to do some postprocessing of data.
   * 
   * @param a session identfier.
   */
  void endSession(String id);

  /**
   * Returns a {@link MuseSink} object associated with given session.
   * 
   * @param id session id
   * @return a sink used in this session. There is no guarantee that this sink will be used
   * in more than one process call.
   */
  MuseSink getSink(String id);
  
  /**
   * Sets the sink to be used for the rest of hte given session.
   * 
   * @param id session id.
   * @param sink sink to use.
   */
  void setSink(String id,MuseSink sink);

  /**
   * Defines the log channel to use.
   * 
   * @param log a log channel.
   */
  void setLog(Log log);

  /**
   * Compute the target file name from the source file name.
   * 
   * @param f the input file to publish.
   * @return the name of the output file.
   */
  String getTargetName(File f);

  /**
   * Set the default header to add at start of every published file.
   * @param string
   */
  void setHeader(String string);

  /**
   * Set the default footer to add at end of every published file.
   * @param string
   */
  void setFooter(String string);
}
