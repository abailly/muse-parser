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
package oqube.muse;

/**
 * An interface for handling custom tags in muse source files.
 * This interface can be required by {@see MuseSink} implementations 
 * for delegating the handling of custom block level and flow level tags
 * in muse.
 * 
 * @author abailly@norsys.fr
 * @version $Id$
 */
public interface MuseTagHandler {

    /*
   * Role string for plexus container.
   */
  String ROLE = MuseTagHandler.class.getName();


  /**
   * Called for handling of a block tag.
   * By convention, if this tag is handled by thi handler, teh
   * method should return true.
   * 
   * @param sink the MuseSink where data (if any) should be output
   * @param tag the string representation of tag
   * @param at the tag's arguments as an array of strings.
   * @param content the entire content of tag
   * @return true if handler understands tag, false otherwise
   */
  boolean block(MuseSink sink,String tag, String[][] at, String content);
  

  /**
   * Called for handling of a flow tag.
   * By convention, if this tag is handled by thi handler, teh
   * method should return true.
   * 
   * @param sink the MuseSink where data (if any) should be output
   * @param tag the string representation of tag
   * @param at the tag's arguments as an array of strings.
   * @param content the entire content of tag
   * @return true if handler understands tag, false otherwise
   */
  boolean flow(MuseSink sink,String tag, String[][]at,String content);
  
}
