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
 *  Created 3 nov. 07
 */
package oqube.muse.web;

import java.io.File;

/**
 * A muse server that can format files from a directory.
 * 
 * @author nono
 *
 */
public interface MuseServer {

  void setInputEncoding(String string);

  void setHeader(Header header);

  void setOutputEncoding(String string);

  void setFooter(Header header);

  void setRootDirectory(File file);

  void setMapper(PathMapper mapper);

  void setPath(String path);

}
