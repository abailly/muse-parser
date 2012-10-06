/*
 *  Copyright (c) 2007 - OQube / Arnaud Bailly
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  Created 2 nov. 07
 */
package oqube.muse;

import java.util.Map;

/**
 * Handles formatting for given element.
 * These handlers are called by sinks with which they are registered to handle formatting of tag elements.
 * 
 * @author nono
 *
 */
public class ElementHandler {

  protected String name;

  protected ElementHandler(String name) {
    this.name = name;
  }

  public void end() {
  }

  public void start(Map<String, String> parameters) {
  }

}