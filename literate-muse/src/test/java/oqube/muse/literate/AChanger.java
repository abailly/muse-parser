/*
 *  Copyright (c) 2009 - OQube / Arnaud Bailly
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
 *  Created 17 févr. 09
 */
package oqube.muse.literate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class AChanger {
  public static void main(String[] args) throws IOException {
    File srcFile = new File(new File(args[0]), "Test.hs");
    String content = readContent(srcFile);
    content = content.replace('a', 'o');
    writeContent(content, srcFile);
  }

  private static void writeContent(String content, File srcFile)
      throws IOException {
    FileWriter fw = new FileWriter(srcFile);
    fw.write(content);
    fw.flush();
    fw.close();
  }

  public static String readContent(File f) {
    try {
      InputStreamReader r = new InputStreamReader(new FileInputStream(f));
      StringWriter w = new StringWriter();
      char[] buf = new char[1024];
      int ln = 0;
      while ((ln = r.read(buf)) > -1) {
        w.write(buf, 0, ln);
      }
      r.close();
      return w.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

  }
}