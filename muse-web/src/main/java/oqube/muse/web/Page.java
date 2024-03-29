/**
 *  Copyright (C) 2007 - OQube / Arnaud Bailly
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
 * Created on Fri Jul  6 2007
 */
package oqube.muse.web;

import java.io.InputStream;



/**
 * An interface for accessing the content of a page.
 *
 * @author abailly@oqube.com
 * @version $Id$
 */
public interface Page {

  /**
   * 
   * @return the length (in 8-bit bytes) of this page.
   */
  int length();
  
  /**
   * @return a stream containing the content the page. This content will
   *  written asis to the client.
   */
  InputStream content(String sessionId);

  /**
   * 
   * @return the MIME type of this page.
   */
  String contentType();
}
