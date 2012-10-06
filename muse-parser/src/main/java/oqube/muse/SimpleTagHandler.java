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
package oqube.muse;


/**
 * A class for simple tags.
 * This class can be used for handlers that treats identically flow
 * and block elements and that handles only one tag. Tag attributes are ignored.
 *  
 * @author nono
 *
 */
public abstract class SimpleTagHandler implements MuseTagHandler {

  /*
   * name of this tag. This should be the string as used by the 
   * tag to produce its content. If null, then it is expected 
   * this handler handles everything.
   */
  private String name;
  
  /**
   * Construct a named handler.
   * 
   * @param name
   */
  public SimpleTagHandler(String name) {
    this.name = name;
  }
  
  /**
   * Construct an anonymous handler.
   *
   */
  public SimpleTagHandler() {
    
  }
  
  /**
   * @return Returns the name.
   */
  public String getName() {
    return name;
  }

  /**
   * @param name The name to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /* (non-Javadoc)
   * @see oqube.muse.MuseTagHandler#block(oqube.muse.MuseSink, java.lang.String, java.lang.String)
   */
  public boolean block(MuseSink sink, String tag, String[][]at,String content) {
    if(this.name == null || this.name.equals(tag))
      doHandle(sink,content);
    return false;
  }

  /* (non-Javadoc)
   * @see oqube.muse.MuseTagHandler#flow(oqube.muse.MuseSink, java.lang.String, java.lang.String)
   */
  public boolean flow(MuseSink sink, String tag, String[][]at,String content) {
    return block(sink,tag,at,content);
  }

  /**
   * Method to override for formatting tag content by concrete subclasses.
   *  
   * @param sink the data sink used.
   * @param content the raw content.
   */
  protected abstract void doHandle(MuseSink sink, String content);
}
